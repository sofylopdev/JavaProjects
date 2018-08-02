package wormgame.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import wormgame.game.WormGame;

public class UserInterface implements Runnable {

    private JFrame frame;
    private WormGame game;
    private DrawingBoard board;
    private int sideLength;

    public UserInterface(WormGame game, int sideLength) {
        this.game = game;
        this.sideLength = sideLength;
    }

    @Override
    public void run() {
        frame = new JFrame("Worm Game");
        int width = (game.getWidth() + 1) * sideLength + 10;
        int height = (game.getHeight() + 2) * sideLength + 10;

        // frame.setPreferredSize(new Dimension(width, height));
        frame.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    public void createComponents(Container container) {
        // Create drawing board first which then is added into container-object.
        // After this, create keyboard listener which is added into frame-object

        board = new DrawingBoard(game, sideLength);
        JLabel score = new JLabel("");
        this.game.setLabel(score);

        board.add(score, BorderLayout.NORTH);
        container.add(board);
        KeyboardListener listener = new KeyboardListener(game.getWorm());
        frame.addKeyListener(listener);

    }

    public Updatable getUpdatable() {
        return board;
    }

    public JFrame getFrame() {
        return frame;
    }
}
