/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.ConfigFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Sofia
 */
public class CriarConfigFile {

    public static Properties prop = new Properties();

    public void saveProperties(String maxFloorValue,
            String minFloorValue,
            String elevators) {
        try {
            prop.setProperty("maxFloor", maxFloorValue);
            prop.setProperty("minFloor", minFloorValue);
            prop.setProperty("ListaElevators", elevators);
            prop.store(new FileOutputStream("config.ArranhaCeus"), null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
