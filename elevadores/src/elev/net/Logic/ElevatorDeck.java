/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.Logic;

import elev.net.Logic.dosElevadores.EstadoDosElevadores;
import elev.net.Logic.dosElevadores.Carga;
import elev.net.Logic.dosElevadores.Direction;
import elev.net.Logic.dosElevadores.Elevator;
import elev.net.Logic.dosElevadores.Expresso;
import elev.net.Logic.dosElevadores.ListaElevadores;
import elev.net.Logic.dosElevadores.Restrito;
import elev.net.UserInterface.PainelCriarElevadores;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 *
 * @author Sofia
 */
public class ElevatorDeck {//controla os elevadores do arranha ceus

    private List<Elevator> elevators;
    private int maxFloor;
    private int minFloor;

    private List<Elevator> elevadoresCarga = new ArrayList<Elevator>();
    private List<Elevator> elevadoresPessoas = new ArrayList<Elevator>();
    private List<Elevator> elevadoresRestritos = new ArrayList<Elevator>();
    private List<Elevator> elevadoresExpresso = new ArrayList<Elevator>();

    private AvisosOut aout;
   
    
    public ElevatorDeck(ListaElevadores list, int maxFloor, int minFloor, AvisosOut avisosout) {
        elevators = list;
        this.maxFloor = maxFloor;
        this.minFloor = minFloor;

        for (Elevator each : elevators) {
            if (each.getClass() == Carga.class) {
                elevadoresCarga.add(each);
            } else if (each.getClass() == Restrito.class) {
                elevadoresRestritos.add(each);
            } else if (each.getClass() == Expresso.class) {
                elevadoresExpresso.add(each);
            } else {
                elevadoresPessoas.add(each);
            }
        }
        this.aout = avisosout;
    }


    public void run() {// poe os elevadores a funcionar
        ExecutorService executor = Executors.newFixedThreadPool(elevators.size());
        for (Elevator each : elevators) {
            executor.submit(each);
        }
    }

    public void addRequestToElevator(int request, Direction requestedDirection, String allowedElevator) {// "verifica" qual dos elevadores e o mais "adequado" a receber o pedido; e transmite esse pedido ao escolhido
        //verifica quais os elevadores vao na mesma direccao do pedido:
        // List<Elevator> sameDirElevators = whichElevatorsGoInTheDirectionRequested(requestedDirection);
       // System.out.println("recebeu request");

        if (allowedElevator.equals("carga")) {
            if (request == -1 || request == -2 || request == (maxFloor - 1)) {
                addingRequest(request, elevadoresCarga, allowedElevator);
            } else {
                System.out.println("Os elevadores de carga n tem acesso a esse piso. Pisos disponiveis: -2, -1, " + (maxFloor - 1));
                aout.printAvisos("Os elevadores de carga n tem acesso a esse piso. Pisos disponiveis: -2, -1, " + (maxFloor - 1));
            }
            System.out.println("adicionou " + allowedElevator + " elevadoresDeCarga");
        } else if (allowedElevator.equals("restrito")) {
            addingRequest(request, elevadoresRestritos, allowedElevator);
        } else if (allowedElevator.equals("expresso")) {
            if (request == 0 || request == 10 || request == 20) {
                addingRequest(request, elevadoresExpresso, allowedElevator);
            } else {
                System.out.println("Os elevadores expresso n tem acesso a esse piso. Pisos disponiveis: 0, 10, 20");
                aout.printAvisos("Os elevadores expresso n tem acesso a esse piso. Pisos disponiveis: 0, 10, 20");
            }
        } else {
            if (!elevadoresPessoas.isEmpty()) {//isto foi adicionado pq comecamos, por defeito, c elevatorType=pessoas
                addingRequest(request, elevadoresPessoas, allowedElevator);
            } else {//se nao existir nenhum elevador do tipo pessoa(largo ou normal) n adiciona request e lanca aviso p seleccionar outro tipo de elevador
                System.out.println("Nao ha nenhum elevador normal nem largo. Por favor, carregar no botao carga, restrito ou expresso.");
                aout.printAvisos("Nao ha nenhum elevador normal nem largo. Por favor, carregar no botao carga, restrito ou expresso.");
            }
        }
    }

    public void addingRequest(int request, List<Elevator> list, String allowedElevator) {
        if (selectIdleElevator(list) != null) {//se elevador esta parado, escolhe esse
            selectIdleElevator(list).addRequest(request, allowedElevator);
        } else {
            // Caso estejam todos em movimento; selecciona elevador mais proximo: //sameDirElevators
            if (selectClosestElevator(request, list) != null) {
                selectClosestElevator(request, list).addRequest(request, allowedElevator);
            } else {
                for (Elevator each : list) {
                    if (each.isBroken() == false) {
                        each.addRequest(request, allowedElevator);
                    }
                }
            }
        }
    }

    public Elevator selectIdleElevator(List<Elevator> elevadores) {
        Elevator chosen = null;
        for (Elevator each : elevadores) {
            if (each.IsMoving() == false && each.isBroken() == false) {//se elevador esta parado, escolhe esse
                chosen = each;
            }
        }
        return chosen;
    }

    public Elevator selectClosestElevator(int floor, List<Elevator> elevadores) {//qual e o elevador mais proximo?
        //dos elevadores q vao para a mesma direccao, qual o mais proximo?:
//        if (!sameDirectionElevators.isEmpty()) {
//            Elevator chosen = sameDirectionElevators.get(0);
//            for (Elevator each : sameDirectionElevators) {
//                if (Math.abs(each.getCurrentFloor() - floor) < Math.abs(chosen.getCurrentFloor() - floor)) {
//                    chosen = each;
//                    return chosen;
//                }
//            }
//        } else {
        //Se nao ha elevadores a ir na mesma direccao: qual o elevador mais proximo(de todos os disponiveis dentro do elevators):
        Elevator chosen = elevadores.get(0);
        for (Elevator each : elevadores) {
            if (each.isBroken() == false) {
                if (Math.abs(each.getCurrentFloor() - floor) < Math.abs(chosen.getCurrentFloor() - floor)) {//O CHOSEN NAO TEM CURRENT FLOOR!!!!!!!!PQ E NULL
                    chosen = each;
                    System.out.println("VEIO AOS Q ESTAO MAIS PERTO: " + each.getIDElevator() + " " + each.getName());
                    break;
                }
            }
        }
        return chosen;
    }

//    public List<Elevator> whichElevatorsGoInTheDirectionRequested(Direction floorRequestedDirection) {
//        List<Elevator> sameDirElevators = new ArrayList<Elevator>();
//        for (Elevator each : elevators) {
//            if (each.getDirection() == floorRequestedDirection) {
//                sameDirElevators.add(each);
//            }
//        }
//        return sameDirElevators;
//    }
// public List<Elevator> checkElevatorOnOff() {// verificacao do tipo de elevador
// List<Elevator> list = new ArrayList<Elevator>();
//  for (Elevator each : elevators) {
//  if (each.getStatus()) {
//list.add(each);
// }
//  }
//     return list;
// }
    public List<Elevator> getElevators() {
        return this.elevators;
    }

    public EstadoDosElevadores[] obterAndares() {//permite obter o estado de cada elevador a um dado momento
        EstadoDosElevadores[] andares = new EstadoDosElevadores[elevators.size()];
        for (int i = 0; i < elevators.size(); i++) {
            andares[i] = new EstadoDosElevadores(elevators.get(i).getCurrentFloor(), elevators.get(i).IsMoving(), elevators.get(i).isBroken());
        }
        return andares;
    }

    public void interruptAll() {//interrompe todos os elevadores
        aout.printAvarias("Falha de energia!");
        System.out.println("Falha de energia!");
        for (Elevator each : elevators) {
            each.interrupt();
        }
    }

    public void interruptOne() {//"avaria" um elevador random
        Random generator = new Random();
        int number = generator.nextInt(elevators.size());//tem de ser um numero entre 0 e tamanho da array list(para poder escolher um elevador da lista)
        for (Elevator each : elevators) {
            if (each.getIDElevator() == number) {
                System.out.println("(Deck:)Avaria no elevador " + (each.getIDElevator() + 1) + " (" + each.getName() + ")");
                each.interrupt();
            }
        }
    }

    public void activateThemAll() {//activa os elevadores todos se todos estao "interrompidos"
        List<Elevator> avariados = new ArrayList<>();
        for (Elevator each : elevators) {
            if (each.isBroken()) {
                avariados.add(each);
            }
        }
        if (avariados.size() == elevators.size()) {
            for (Elevator each : elevators) {
                each.activate();
            }
            aout.printAvarias("A energia voltou.");
        } else {
            System.out.println("Nao ha falha de energia.\n O gerador nao e necessario.");
            aout.printAvarias("Nao ha falha de energia.\n O gerador nao e necessario.");
        }
    }

    public void activateBroken() {//activa o elevador parado
        List<Elevator> avariados = new ArrayList<>();
        for (Elevator each : elevators) {
            if (each.isBroken()) {
                avariados.add(each);
            }
        }
        if (avariados.size() == elevators.size()) {
            System.out.println("Nao ha nenhum elevador avariado.");
            aout.printAvarias("Nao ha nenhum elevador avariado.");
        } else {
            for (Elevator each : elevators) {
                if (each.isBroken()) {
                    each.activate();
                    aout.printAvarias("Elevador " + (each.getIDElevator() + 1) + " (" + each.getName() + ") " + " voltou a funcionar.");
                    return;
                }
            }
        }
    }

}
