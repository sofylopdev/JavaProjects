/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.Logic.dosElevadores;

import elev.net.Logic.AvisosOut;
import elev.net.UserInterface.PainelCriarElevadores;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Sofia
 */
public class Expresso extends Elevator {

    public Expresso(int max,int min,AvisosOut aout) {
        super("expresso",10,max,min,aout);
    }

 @Override
    public void addPersonToElevator() {
         if (getHowManyInElevator() < super.getCapacity()) {
            super.addPersonToElevator();
        }else{
             System.out.println("Este elevador nao pode levar mais do que "+super.getCapacity() +" pessoas!!!");
         }
    }
}
