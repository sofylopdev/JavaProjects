/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.Logic;

import java.util.Random;

/**
 *
 * @author Sofia
 */
public class Azar implements Runnable {

    private ElevatorDeck deck;
    private int number;

    public Azar(ElevatorDeck deck) {
        this.deck = deck;

    }

    @Override
    public void run() {
        System.out.println("dentro do azar");
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            Random generator = new Random();
            number = generator.nextInt(50);
            System.out.println(number);
            if (number > 45) {               
                System.out.println("(sistema:).........................................................falha electricidade!!!!");
                deck.interruptAll();
                
            } else if (number < 10) {              
                System.out.println("(sistema:).........................................................avaria!!!!!!!!");
                deck.interruptOne();                
            } else {
                System.out.println("(sistema:).........................................................nao ha problemas");

            }
        }
    }

}
