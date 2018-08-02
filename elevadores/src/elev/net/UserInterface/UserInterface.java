/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.UserInterface;

import elev.net.ConfigFile.AcederConfigFile;
import elev.net.Logic.AvisosOut;
import elev.net.Logic.SistemaLigacao;
import elev.net.Logic.dosElevadores.EstadoDosElevadores;
import elev.net.Logic.ElevatorDeck;
import elev.net.Logic.dosElevadores.ListaElevadores;
import elev.net.Logic.dosElevadores.Direction;
import elev.net.Logic.dosElevadores.Elevator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Component.TOP_ALIGNMENT;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import static javax.swing.BoxLayout.PAGE_AXIS;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Sofia
 */
public class UserInterface implements Runnable {

    private JFrame frame;
    private PainelCriarElevadores painelElevadores;
    private ListaElevadores listaElevadores;

    public ElevatorDeck deck;//public para poder ser acedido pelo SistemaLigacao

    private int maxFloor;
    private int minFloor;
    private String strList;

    private SistemaLigacao sistema;

    private JComboBox pickElevator;
    private JLabel pessoas;
    private JLabel numero;
    private String elevatorType;

    private JTextArea avisos;
    private JTextArea avarias;
    private AvisosOut aout;

    private AcederConfigFile dados;

    public UserInterface() {
        elevatorType = "pessoa";//por definicao comeca como pessoa
        
        dados = new AcederConfigFile();//acede ao config
        maxFloor = dados.getMaxFloor();//do config tira o max floor,
        minFloor = dados.getMinFloor();// o min floor
        strList = dados.getListElevators();// e a lista de tipos de elevadores selecionados

        this.aout = new AvisosOut();//inicia a classe q permite por os avisos no painel dos avisos

        listaElevadores = new ListaElevadores(maxFloor, minFloor, strList, aout);//cria a lista
        deck = new ElevatorDeck(listaElevadores, maxFloor, minFloor, aout);//cria o deck
        painelElevadores = new PainelCriarElevadores(listaElevadores, minFloor, maxFloor);//cria o painel dos elevadores

    }

    @Override
    public void run() {
        frame = new JFrame("Arranha-ceus");
        frame.setPreferredSize(new Dimension(1000, 1000));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(Color.white);

        createComponents(frame.getContentPane());
        frame.pack();

        frame.setVisible(true);
        deck.run();//poe em funcionamento as threads (elevadores) aqui

    }

    public void setSistema(SistemaLigacao novoSistema) {
        this.sistema = novoSistema;
    }

    private void createComponents(Container container) {
        container.setBackground(Color.white);

        JPanel contemTodos = new JPanel();
        GridLayout layout = new GridLayout(1, 4);
        layout.setHgap(2);
        contemTodos.setLayout(layout);
        contemTodos.setBorder(new EmptyBorder(10, 10, 10, 10));

        contemTodos.add(leftPanel());
        contemTodos.add(elevators());
        contemTodos.add(elevatorButtonsUPDown());
        contemTodos.add(painelDireito());

        container.add(contemTodos);
    }

    public void updateElevators(EstadoDosElevadores[] estados) {//muda a cor das label conforme os elevadores estao avariados, em movimento, ou parado nesse andar OU se nao ha elevador nesse andar
       
        for (int elevador = 0; elevador < estados.length; elevador++) {
            for (int andar = minFloor; andar <= maxFloor; andar++) {
                if (andar == estados[elevador].andar) {
                    if (estados[elevador].isBroken) {
                        painelElevadores.getLabelByName("elev " + (elevador) + " andar " + andar).setBackground(Color.yellow);
                    } else {
                        if (estados[elevador].aMover) {
                            painelElevadores.getLabelByName("elev " + (elevador) + " andar " + andar).setBackground(Color.red);
                        } else {
                            painelElevadores.getLabelByName("elev " + (elevador) + " andar " + andar).setBackground(Color.GREEN);
                        }
                    }
                } else {
                    try {
                        painelElevadores.getLabelByName("elev " + (elevador) + " andar " + andar).setBackground(Color.darkGray);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

    }

    private JPanel leftPanel() {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(3, 0);
        panel.setLayout(layout);

        panel.add(escolherElevador());
        panel.add(painelControloCadaElevador());

        return panel;
    }

    private JPanel escolherElevador() {
        JPanel panel = new JPanel();
        String[] elevadores = new String[listaElevadores.size()];

        for (int i = 0; i < listaElevadores.size(); i++) {
            elevadores[i] = "elevador " + (i + 1) + " (" + listaElevadores.get(i).getName() + ")";//para elevador comecar no 1
        }

        pessoas = new JLabel("Número de Pessoas: ");
        numero = new JLabel("0");//indica o numero de pessoas ou equivalente em carga q esta dentro do elevador seleccionado

        pickElevator = new JComboBox(elevadores);//permite seleccionar o elevador onde "estamos"
        pickElevator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String opcaoSeleccionada = (String) pickElevator.getSelectedItem();
                for (Elevator each : deck.getElevators()) {
                    if (("elevador " + (each.getIDElevator() + 1 + " (" + each.getName() + ")")).equals(opcaoSeleccionada)) {
                        if (each.getName().equals("carga")) {
                            pessoas.setText("Quantidade de carga: ");
                        } else {
                            pessoas.setText("Número de Pessoas: ");
                        }
                        numero.setText("" + each.getHowManyInElevator());
                    }
                }
            }
        });

        JButton plus = new JButton("+");//faz entrar pessoa no elevador seleccionado
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String opcaoSeleccionada = (String) pickElevator.getSelectedItem();
                for (Elevator each : deck.getElevators()) {
                    if (("elevador " + (each.getIDElevator() + 1 + " (" + each.getName() + ")")).equals(opcaoSeleccionada)) {
                        each.addPersonToElevator();
                        numero.setText("" + each.getHowManyInElevator());
                    }
                }
            }
        });

        JButton minus = new JButton("-");//faz sair pessoa no elevador seleccionado
        minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String opcaoSeleccionada = (String) pickElevator.getSelectedItem();
                for (Elevator each : deck.getElevators()) {
                    if (("elevador " + (each.getIDElevator() + 1 + " (" + each.getName() + ")")).equals(opcaoSeleccionada)) {
                        each.removePerson();
                        numero.setText("" + each.getHowManyInElevator());
                    }
                }
            }
        });

        panel.add(pickElevator);

        //c jpanel labels e buttons conseguimos q as labels e os botoes, respectivamente, 
        //fiquem sempre juntos mesmo que se altere o tamanho da janela
        JPanel labels = new JPanel();
        labels.add(pessoas);
        labels.add(numero);
        panel.add(labels);

        JPanel buttons = new JPanel();
        buttons.add(plus);
        buttons.add(minus);
        panel.add(buttons);
        return panel;

    }

    private JPanel painelControloCadaElevador() {//este painel e como se estivessemos dentro do elevador(seleccionado no JComboBox)
        int quantidadeDeBotoes = (maxFloor + (Math.abs(minFloor))) + 1 / 2;
        GridLayout layout = new GridLayout(quantidadeDeBotoes, 3);
        JPanel panel = new JPanel(layout);
        panel.setLayout(layout);

        for (int i = maxFloor; i >= minFloor; i--) {
            JButton floor = new JButton();
            floor.setName("floor" + i);
            if (i > 9) {
                floor.setText("" + i);

            } else {
                floor.setText(" " + i);
            }

            floor.addActionListener(new ActionListener() {//elevador anda para andar selecionado
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String opcaoSeleccionada = (String) pickElevator.getSelectedItem();
                    for (Elevator each : deck.getElevators()) {
                        if (("elevador " + (each.getIDElevator() + 1 + " (" + each.getName() + ")")).equals(opcaoSeleccionada)) {
                            each.addRequest(Integer.parseInt(floor.getName().substring(5)), each.getName());
                        }
                    }
                }
            });
            panel.add(floor);
        }
        JButton holdDoors = new JButton("◄►");//◄► de: character map// se portas fechadas, abre as portas do elevador
        holdDoors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String opcaoSeleccionada = (String) pickElevator.getSelectedItem();
                for (Elevator each : deck.getElevators()) {
                    if (("elevador " + (each.getIDElevator() + 1 + " (" + each.getName() + ")")).equals(opcaoSeleccionada)) {
                        if (each.doorsOpenOrClosed() == false && each.IsMoving() == false) {
                            each.openDoors();
                        }
                    }
                }
            }
        });
        panel.add(holdDoors);

        return panel;
    }

    private JPanel elevators() {
        JPanel elevators = painelElevadores;

        return elevators;
    }

    private JPanel elevatorButtonsUPDown() {//botoes de chamada de elevador e numero do andar,em cada andar
        int quantidadeDeBotoes = maxFloor + (Math.abs(minFloor)) + 1;//+1 p incluir o andar zero
        GridLayout betweenButtons = new GridLayout(quantidadeDeBotoes, 0);
        betweenButtons.setVgap(5);
        JPanel panelUpAndDown = new JPanel(betweenButtons);
        List<JButton> listButtonsUpDown = new ArrayList<JButton>();

        for (int i = maxFloor; i >= minFloor; i--) {
            JButton up = new JButton("▲");
            up.setName("up" + i);

            JButton down = new JButton("▼");
            down.setName("down" + i);

            JLabel andar = new JLabel((i) + "  ");

            up.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String nameUp = up.getName();
                    int andarUp = Integer.parseInt(nameUp.substring(2));
                    deck.addRequestToElevator(andarUp, Direction.UP, elevatorType);
                }
            });

            down.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String nameDown = down.getName();
                    int andarDown = Integer.parseInt(nameDown.substring(4));
                    deck.addRequestToElevator(andarDown, Direction.DOWN, elevatorType);
                }
            });

            listButtonsUpDown.add(down);
            listButtonsUpDown.add(up);

            panelUpAndDown.add(andar);
            panelUpAndDown.add(up);
            panelUpAndDown.add(down);

            if (i == maxFloor) {
                up.setVisible(false);
            }
            if (i == minFloor) {
                down.setVisible(false);
            }
        }

        return panelUpAndDown;
    }

    private JPanel warningPanel() {//painel de avisos
        JPanel warningsPanel = new JPanel(new GridLayout(3, 1));

        warningsPanel.add(new JLabel("AVISOS"));
        avisos = new JTextArea("");
        avisos.setLineWrap(true);

        avarias = new JTextArea("");//especifico das avarias
        avarias.setLineWrap(true);

        warningsPanel.add(avisos);
        warningsPanel.add(avarias);
        this.aout.setAvisos(avisos);
        this.aout.setAvarias(avarias);
        return warningsPanel;
    }

    private JPanel requests() {//onde se escolhe o tipo de elevador que queremos utilizar
        GridLayout layout = new GridLayout(4, 1);
        layout.setVgap(10);

        JPanel requests = new JPanel(layout);

        JButton carga = new JButton("Carga");
        carga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                for (Elevator each : listaElevadores) {
                    if (!each.getName().equals("carga")) {
                        carga.setEnabled(false);
                        avisos.setText("Nao existem elevadores de carga.");
                    } else {
                        carga.setEnabled(true);

                        avisos.setText("So elevadores de CARGA" + "\n" + " podem ser utilizados");
                        System.out.println("So elevadores de carga podem ser utilizados");
                        elevatorType = "carga";

                        return;

                    }
                }
            }
        });
        requests.add(carga);

        JButton pessoa = new JButton("Pessoa");
        pessoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                for (Elevator each : listaElevadores) {
                    if (each.getName().equals("normal") || (each.getName().equals("largo"))) {
                        pessoa.setEnabled(true);
                        avisos.setText("So elevadores de PESSOAS" + "\n" + "(tipo normal e largo)" + "\n" + " podem ser utilizados");
                        System.out.println("So elevadores de pessoas podem ser utilizados");
                        elevatorType = "pessoa";

                        return;

                    } else {
                        pessoa.setEnabled(false);
                        avisos.setText("Nao existem elevadores largos ou normais.");
                    }
                }
            }
        });
        requests.add(pessoa);

        JButton restrito = new JButton("Restrito");
        restrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                for (Elevator each : listaElevadores) {
                    if (!each.getName().equals("restrito")) {
                        restrito.setEnabled(false);
                        avisos.setText("Nao existem elevadores restritos.");
                    } else {
                        restrito.setEnabled(true);
                        avisos.setText("So RESTRITOS podem ser utilizados");
                        System.out.println("So restritos podem ser utilizados");
                        elevatorType = "restrito";
                        return;

                    }
                }
            }
        });
        requests.add(restrito);

        JButton expresso = new JButton("Expresso");
        expresso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                for (Elevator each : listaElevadores) {
                    if (!each.getName().equals("expresso")) {
                        expresso.setEnabled(false);
                        avisos.setText("Nao existem elevadores espressos.");
                    } else {
                        expresso.setEnabled(true);
                        avisos.setText("So EXPRESSOS podem ser utilizados");
                        System.out.println("So expressos podem ser utilizados");
                        elevatorType = "expresso";
                        return;
                    }
                }
            }
        });
        requests.add(expresso);

        return requests;
    }

    private JPanel fixElevators() {//tem os botoes q permitem por elevadores a funcionar de novo
        GridLayout layout = new GridLayout(4, 0);
        layout.setVgap(10);
        JPanel panel = new JPanel(layout);

        JLabel accoes = new JLabel("Accoes possiveis do Seguranca: ");

        JButton fixBroken = new JButton("Reparar avariado");
        fixBroken.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                deck.activateBroken();
            }
        });

        JButton fixAll = new JButton("Accionar gerador");
        fixAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                deck.activateThemAll();
            }
        });

        JLabel opcoes = new JLabel("Selecionar tipo de elevador:");//foi colocado aqui pois este painel tinha menos coisas

        panel.add(accoes);
        panel.add(fixBroken);
        panel.add(fixAll);
        panel.add(opcoes);

        return panel;
    }

    private JPanel painelDireito() {
        JPanel panel = new JPanel(new GridLayout(3, 0));

        panel.add(warningPanel());
        panel.add(fixElevators());
        panel.add(requests());
        return panel;
    }

    public JTextArea getAvisos() {
        return this.avisos;
    }
}
