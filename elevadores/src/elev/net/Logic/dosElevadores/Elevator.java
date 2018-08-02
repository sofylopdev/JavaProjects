/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.Logic.dosElevadores;

import elev.net.Logic.AvisosOut;
import elev.net.UserInterface.PainelCriarElevadores;
import elev.net.UserInterface.UserInterface;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sofia
 */
public class Elevator implements Runnable {

    private static int CONTADOR = 0;//permite dar novo numero a cada elevador, quando criado
    private int idElevator = 0;//numero do elevador
    private int maxFloor;//ultimo andar do arranha-ceus
    private int minFloor;//"primeiro" andar do arranha-ceus

    private String tipoDeElevador; // nome do elevador para facilitar "saber" qual esta a ser usado
    private Direction direction;// direccao em que elevador esta a ir
    private int currentFloor;// andar em q elevador esta (parado) ou por onde esta a passar
    private int floorChoosen;// andar para o qual esta a ir (no momento)

    private Map<Direction, List<Integer>> requests; //pedidos feitos a este elevador, "memoria do elevador", para cima e para baixo 
    //ele corre todos de uma lista e depois vai a outra lista

    private boolean moving;// o elevador esta em movimento(true) ou nao(false)
    private int timeBetweenFloors;// tempo q elevador demora entre andares
    private int timeOpeningAndClosingDoor;// tempo q elevador demora a abrir portas = tempo q demora a fechar
    private int timeOpenDoor; // tempo que elevador fica c porta aberta
    private boolean doorsOpen; // portas estao abertas(true) ou fechadas(false)

    private int capacity;// quantas pessoas (ou carga <=> o seu equivalente em pessoas) "cabem" no elevador
    private int personsInsideElevator;// quantas pessoas estao dentro do elevador

    private volatile boolean isBroken;//elevador esta avariado(true) ou nao(false); e volatile pq thread tem de o verificar c frequencia

    private AvisosOut aout;//permite por avisos no painel avisos
    private Object lock;//para permitir a sincronia entre o elevador estar parado e o metodo q o activa novamente

    public Elevator(String tipo, int capacity, int maxFloor, int minFloor, AvisosOut avisosout) {
        this.idElevator = CONTADOR;
        this.capacity = capacity;
        this.personsInsideElevator = 0;

        this.tipoDeElevador = tipo;
        this.aout = avisosout;

        this.direction = Direction.DOWN;// por convencao, ele vai para baixo primeiro
        this.currentFloor = 0; // comeca no andar zero (carga comeca no -1)
        this.floorChoosen = 0; // no inicio esta parado, logo, o andar para onde vai e igual ao andar onde esta

        requests = new HashMap<Direction, List<Integer>>();
        requests.put(Direction.UP, new ArrayList<Integer>());//lista para pedidos "a subir"
        requests.put(Direction.DOWN, new ArrayList<Integer>());// lista para pedidos "a descer"

        this.moving = false; // ao inicio esta parado
        this.timeBetweenFloors = 1000;
        this.doorsOpen = false;// ao inicio as portas estao fechadas
        this.timeOpeningAndClosingDoor = 500;
        this.timeOpenDoor = 500;

        this.CONTADOR++;//vai aumentando o contador, sempre q 1 elevador e criado

        this.maxFloor = maxFloor;
        this.minFloor = minFloor;

        this.isBroken = false;//de inicio elevador esta a funcionar
        lock = new Object();// cada elevador tem o seu proprio objecto lock pois nao dependem uns dos outros para funcionar

    }


    public void addRequest(int floorRequest, String elevatorType) {
        if (this.isBroken == false) {// se elevador nao avariado:
            System.out.println("dentro do elevador:");
            System.out.println("requestedFloor: " + floorRequest + " tipo de elevador: " + elevatorType);
            //quando o pedido de mudanca de andar e feito dentro do painel de cada elevador precisa de "excepcoes" nos tipos carga e expresso, pq nao vao a todos os andares:
            if (elevatorType.equals("carga")) {
                if (floorRequest == -1 || floorRequest == -2 || floorRequest == maxFloor - 1) {
                    addingRequest(floorRequest);
                } else {
                    System.out.println("Os elevadores de carga n tem acesso a esse piso. Pisos disponiveis: -2, -1, " + (maxFloor - 1));
                    aout.printAvisos("Os elevadores de carga n tem acesso a esse piso. Pisos disponiveis: -2, -1, " + (maxFloor - 1));
                }
            } else if (elevatorType.equals("expresso")) {
                if (floorRequest == 0 || floorRequest == 10 || floorRequest == 20) {
                    addingRequest(floorRequest);
                } else {
                    System.out.println("Os elevadores expresso n tem acesso a esse piso. Pisos disponiveis: 0, 10, 20.");
                    aout.printAvisos("Os elevadores expresso n tem acesso a esse piso. Pisos disponiveis: 0, 10, 20");
                }
            } else {//largo, normal e restrito podem ir a qq andar:
                addingRequest(floorRequest);
            }
        } else {//se elevador avariado:
            System.out.println("Elevator " + (this.idElevator + 1) + " (" + this.getName() + ") is broken.");
        }
    }

    public void addingRequest(int floorRequest) { // Adiciona pedido a lista respectiva:
        if (currentFloor > floorRequest) {           // - "para baixo" se o andar pedido e inferior ao actual
            requests.get(Direction.DOWN).add(floorRequest);
            Collections.sort(requests.get(Direction.DOWN), Collections.reverseOrder());//poe os andares por ordem(do maior para menor)
        } else if (currentFloor < floorRequest) {    // - "para cima" se o andar pedido e superior ao actual
            requests.get(Direction.UP).add(floorRequest);
            Collections.sort(requests.get(Direction.UP));//poe os andares por ordem(do menor para maior)
        } else {
            openDoors();// se ja esta no andar, abre as portas
        }
    }

    public void openDoors() {
        System.out.println("A abrir a porta");
        try {
            Thread.sleep(timeOpeningAndClosingDoor);//a abrir a porta
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("A porta esta aberta");
        try {
            Thread.sleep(timeOpenDoor);//a porta esta aberta
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("A fechar a porta");
        try {
            Thread.sleep(timeOpeningAndClosingDoor);// a fechar a porta
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                if (isBroken) {
                    try {
                        lock.wait();
                        while ((!requests.get(Direction.DOWN).isEmpty()) || (!requests.get(Direction.UP).isEmpty())) {//se o elevador tem pedido na lista para cima OU para baixo:
                            whichDirectionToGo(); //ve qual a direccao a seguir
                            movingToOneFloor();   //"anda" um andar
                            sleepBetweenFloors(); //demora um tempo a andar entre andares
                        }
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else {
                    while ((!requests.get(Direction.DOWN).isEmpty()) || (!requests.get(Direction.UP).isEmpty())) {//se o elevador tem pedido na lista para cima OU para baixo:
                        whichDirectionToGo(); //ve qual a direccao a seguir
                        movingToOneFloor();   //"anda" um andar
                        sleepBetweenFloors(); //demora um tempo a andar entre andares
                    }
                }
            }
        }
    }

    public void whichDirectionToGo() {
        if (direction == Direction.DOWN) {//se a direccao e para baixo, o andar para o qual vai (pedido) e o andar que esta no inicio da lista "para baixo" 
            if (requests.get(Direction.DOWN).isEmpty()) {//mas se a lista esta vazia, troca a direccao para cima
                direction = Direction.UP;
            } else {
                floorChoosen = requests.get(Direction.DOWN).get(0);
            }
        }
        if (direction == Direction.UP) {//se a direccao e para cima, o andar para o qual vai (pedido) e o andar que esta no inicio da lista "para cima" 
            if (requests.get(Direction.UP).isEmpty()) {//mas se a lista esta vazia, troca a direccao para baixo
                direction = Direction.DOWN;
            } else {
                floorChoosen = requests.get(Direction.UP).get(0);
            }
        }
    }

    public void movingToOneFloor() {
        if (!moving) {
            System.out.println("Eu, " + this.tipoDeElevador + " vou por me a caminho de " + floorChoosen);
        }

        if (floorChoosen == currentFloor) {                    //Quando chega ao andar:
            System.out.println("Cheguei a " + currentFloor);
            moving = false;                                   //-para de "andar"
            openDoors();                                      // - abre as portas
            //- remove o pedido da lista(respectiva):
            if (direction == Direction.UP) {                  //("lista para cima" - se estiver em modo subida)
                try {
                    requests.get(Direction.UP).remove(requests.get(Direction.UP).get(0));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("...........................................................................elevador foi interrompido");
                    System.out.println(e.getMessage());
                }
            } else if (direction == Direction.DOWN) {        //("lista para baixo" - se estiver em modo descida)
                try {
                    requests.get(Direction.DOWN).remove(requests.get(Direction.DOWN).get(0));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("...........................................................................elevador foi interrompido");
                    System.out.println(e.getMessage());
                }
            }

        } else {  // se nao esta no andar pedido :                                         
            moving = true;//comeca a andar
            if (floorChoosen > currentFloor) {//"anda" para cima se andar estiver acima
                currentFloor++;
            } else if (floorChoosen < currentFloor) {//"anda" para baixo se andar estiver abaixo
                currentFloor--;
            }
            System.out.println("Eu, " + this.tipoDeElevador + " estou em " + currentFloor);
        }
    }

    public void sleepBetweenFloors() { // tempo de deslocacao entre andares
        try {
            Thread.sleep(timeBetweenFloors);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPersonToElevator() {// pessoa "entra" no elevador
        if (this.IsMoving() == false) {
            this.personsInsideElevator++;
        }
    }

    public void removePerson() {// pessoa "sai" do elevador
        if (personsInsideElevator > 0 && this.IsMoving() == false) {
            personsInsideElevator--;
        }
    }

    public int getHowManyInElevator() {//quantas pessoas estao, neste momento, no elevador
        return this.personsInsideElevator;
    }

    protected int getCapacity() {//a usar pelos "filhos"
        return capacity;
    }

    protected void setTimeBetweenFloors(int time) {// este metodo serve para mudar o tempo q elevador demora a andar entre elevadores(por causa do elevador "carga", demora mais tempo)
        this.timeBetweenFloors = time;//a usar pelos "filhos"
    }

    protected void setCurrentFloor(int floor) {//modificar valor onde elevador comeca (carga usa este metodo) //a usar pelos "filhos"
        this.currentFloor = floor;
    }

    public boolean IsMoving() {// o elevador esta a mover-se?
        return this.moving;
    }

    public boolean doorsOpenOrClosed() {//portas abertas ou fechadas?
        return this.doorsOpen;
    }

    public int getCurrentFloor() {// metodo necessario p ElevatorDeck saber onde elevador esta(p ajudar na decisao:dar pedido a elevador mais proximo)
        return this.currentFloor;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public static void setContador(int contador) {
        CONTADOR = contador;
    }

    public int getIDElevator() {
        return this.idElevator;
    }

    public String getName() {
        return this.tipoDeElevador;
    }

    public boolean isBroken() {//o elevador esta avariado?
        return this.isBroken;
    }

    public void interrupt() {//elevador e interrompido devido a avaria ou falha de energia; memorias do elevador sao apagadas
        aout.printAvarias("Elevador " + (this.getIDElevator() + 1) + " (" + this.getName() + ") " + " nao esta a funcionar.");
        System.out.println("(elevador) elevator " + (this.getIDElevator() + 1) + " (" + this.getName() + ") " + " is waiting");
        this.isBroken = true;
        this.requests.get(Direction.DOWN).clear();
        this.requests.get(Direction.UP).clear();
    }

    public void activate() {// elevador e reactivado
        synchronized (lock) {
            this.isBroken = false;
            this.moving = false;
            System.out.println("(elevador) elevator " + (this.getIDElevator() + 1) + " (" + this.getName() + ") " + " STOPED  waiting\"); ");
            lock.notify();
        }
    }

}
