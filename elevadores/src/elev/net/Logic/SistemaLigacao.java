/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.Logic;

import elev.net.UserInterface.UserInterface;
import java.util.Random;

/**
 *
 * @author Sofia
 */
public class SistemaLigacao implements Runnable {// serve como ligacao entre ui e elevadores e actualiza o ui conforme o estado dos elevadores

    private UserInterface ui;
    private ElevatorDeck deck1;

    public SistemaLigacao(UserInterface ui) {

        this.ui = ui;
        this.deck1 = ui.deck;
        this.ui.setSistema(this);
    }

    @Override
    public void run() {
        while (true) {

            try {
                Thread.sleep(100);//faz update de 100 em 100 milisegundos
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            this.ui.updateElevators(this.deck1.obterAndares());
        }
    }
}
