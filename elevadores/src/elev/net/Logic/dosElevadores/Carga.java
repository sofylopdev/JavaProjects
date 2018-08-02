/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.Logic.dosElevadores;

import elev.net.Logic.AvisosOut;
import elev.net.UserInterface.PainelCriarElevadores;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Sofia
 */
public class Carga extends Elevator {

    public Carga(int max,int min,AvisosOut aout) {
        super("carga", 20,max,min,aout);
        super.setTimeBetweenFloors(2000);
        super.setCurrentFloor(-1);
    }

 @Override
    public void addPersonToElevator() {
         if (getHowManyInElevator() < super.getCapacity()) {
            super.addPersonToElevator();
        }else{
             System.out.println("Este elevador nao pode levar mais do que peso de "+super.getCapacity() +" pessoas!!!");
         }
    }
}
