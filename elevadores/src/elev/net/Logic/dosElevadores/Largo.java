/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.Logic.dosElevadores;

import elev.net.Logic.AvisosOut;
import elev.net.Logic.dosElevadores.Elevator;
import elev.net.UserInterface.PainelCriarElevadores;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Sofia
 */
public class Largo extends Elevator {

    public Largo(int max,int min, AvisosOut aout) {
        super("largo",10,max,min,aout);
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
