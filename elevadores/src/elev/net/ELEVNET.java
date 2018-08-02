/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net;

import elev.net.ConfigFile.AcederConfigFile;
import elev.net.Logic.Azar;
import elev.net.Logic.ElevatorDeck;
import elev.net.Logic.dosElevadores.ListaElevadores;
import elev.net.Logic.dosElevadores.Direction;
import elev.net.Logic.dosElevadores.Elevator;
import elev.net.Logic.dosElevadores.Normal;
import elev.net.UserInterface.MenuCriarArranhaCeus;
import elev.net.UserInterface.UserInterface;
import elev.net.Logic.SistemaLigacao;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.SwingUtilities;

/**
 *
 * @author Sofia
 */
public class ELEVNET {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {

        MenuCriarArranhaCeus menu = new MenuCriarArranhaCeus();
        menu.setVisible(true);
    }

}
