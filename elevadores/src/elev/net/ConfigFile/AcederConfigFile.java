/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.ConfigFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Sofia
 */
public class AcederConfigFile {

    private Properties prop = new Properties();
    private InputStream input = null;

    public int getMaxFloor() {
        int maxFloor = 0;
        try {

            input = new FileInputStream("config.ArranhaCeus");

            // load a properties file
            prop.load(input);
            maxFloor = Integer.parseInt(prop.getProperty("maxFloor"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return maxFloor;
    }

    public int getMinFloor() {
        int minFloor = 0;
        try {

            input = new FileInputStream("config.ArranhaCeus");

            // load a properties file
            prop.load(input);
            minFloor = Integer.parseInt(prop.getProperty("minFloor"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return minFloor;
    }

    public String getListElevators() {
        String list = "";
        try {

            input = new FileInputStream("config.ArranhaCeus");

            // load a properties file
            prop.load(input);
            list = prop.getProperty("ListaElevators");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return list;
    }
}
