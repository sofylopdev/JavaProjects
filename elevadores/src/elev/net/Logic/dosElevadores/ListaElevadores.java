/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.Logic.dosElevadores;

import elev.net.Logic.AvisosOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Sofia
 */
public class ListaElevadores extends ArrayList<Elevator> {//aqui cria se os elevadores

    private List<String> myList;

    public ListaElevadores(int max, int min, String str, AvisosOut aout) {

        myList = new ArrayList<String>(Arrays.asList(str.split(",")));

        for (String each : myList) {
            if (each.equals("normal")) {
                Elevator normal = new Normal(max, min, aout);
                this.add(normal);
            } else if (each.equals("largo")) {
                Elevator largo = new Largo(max, min, aout);
                this.add(largo);
            } else if (each.equals("carga")) {
                Elevator carga = new Carga(max, min, aout);
                this.add(carga);
            } else if (each.equals("expresso")) {
                Elevator expresso = new Expresso(max, min, aout);
                this.add(expresso);
            } else if (each.equals("restrito")) {
                Elevator restrito = new Restrito(max, min, aout);
                this.add(restrito);
            }
        }
    }
}
