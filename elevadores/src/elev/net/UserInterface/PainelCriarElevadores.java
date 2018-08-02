/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.UserInterface;

import elev.net.Logic.ElevatorDeck;
import elev.net.Logic.dosElevadores.ListaElevadores;
import elev.net.Logic.dosElevadores.Elevator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author Sofia
 */
public class PainelCriarElevadores extends JPanel {

    private ListaElevadores listaElevadores;
    private int minFloor;
    private int maxFloor;
    private ArrayList<JLabel> paragens;

    public PainelCriarElevadores(ListaElevadores list, int minFloor, int maxFloor) {
        this.listaElevadores = list;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        paragens = new ArrayList<JLabel>();

        try {
            criarPainelElevadores();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void criarPainelElevadores() {
        BorderLayout layoutPainel2 = new BorderLayout();
        setLayout(layoutPainel2);

        JPanel elevadores = new JPanel();
        GridLayout layoutElevadores = new GridLayout(0, listaElevadores.size());
        layoutElevadores.setHgap(10);
        elevadores.setLayout(layoutElevadores);
        Border borderCaixaElevadores = BorderFactory.createLineBorder(Color.BLACK);
        elevadores.setBorder(borderCaixaElevadores);

        for (int i = maxFloor; i >= minFloor; i--) {
            for (Elevator each : listaElevadores) {

                JLabel cadaParagem = new JLabel();

                cadaParagem.setName("elev " + each.getIDElevator() + " andar " + i);
                Border borderCadaParagem = BorderFactory.createLineBorder(Color.white);
                cadaParagem.setBorder(borderCadaParagem);
                cadaParagem.setBackground(Color.darkGray);
                cadaParagem.setOpaque(true);

                paragens.add(cadaParagem);
            }
        }
        for (JLabel each : paragens) {
            elevadores.add(each);
        }

        JPanel tipoElevador = new JPanel();
        tipoElevador.setLayout(layoutElevadores);
        for (Elevator each : listaElevadores) {
            JLabel tipos = new JLabel(each.getName());
            tipoElevador.add(tipos);
        }
        
        this.add(elevadores);
        this.add(tipoElevador, BorderLayout.SOUTH);
    }

    public int getMaxFloor() {
        return this.maxFloor;
    }

    public int getMinFloor() {
        return this.minFloor;
    }

    public JLabel getLabelByName(String nomeLabel) {
        for (JLabel each : paragens) {
            if (each.getName().equals(nomeLabel)) {
                return each;
            }
        }
        return null;
    }
}
