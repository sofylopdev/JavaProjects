package wormgame;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import wormgame.domain.Worm;
import wormgame.gui.UserInterface;
import wormgame.game.WormGame;

public class Main {

    public static void main(String[] args) {
        // write test code here
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        int calcWidth = (int)(width/20);
        int calcHeight = (int)(height/20);
        
        WormGame game = new WormGame(calcWidth, calcHeight);

        UserInterface ui = new UserInterface(game, 20);
        SwingUtilities.invokeLater(ui);

        while (ui.getUpdatable() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println("The drawing board hasn't been created yet.");
            }
        }

        game.setUpdatable(ui.getUpdatable());
        game.start();
    }
}
