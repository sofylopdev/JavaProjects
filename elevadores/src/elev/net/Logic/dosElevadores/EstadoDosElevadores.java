/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.Logic.dosElevadores;

/**
 *
 * @author Sofia
 */
public class EstadoDosElevadores {
     public int andar;
     public boolean aMover;
     public boolean isBroken;

    public EstadoDosElevadores(int andar, boolean aMover, boolean isBroken) {
        this.andar = andar;
        this.aMover = aMover;
        this.isBroken = isBroken;
    }
     @Override
    public String toString(){
        return "andar: "+this.andar+" a mover: "+ this.aMover;
    }  
}
