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
public class Restrito extends Elevator {

    private int code;

    public Restrito(int max,int min,AvisosOut aout) {
        super("restrito",4,max,min,aout);
        code = 1234;
    }
    public int getCode(){
        return code;
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
