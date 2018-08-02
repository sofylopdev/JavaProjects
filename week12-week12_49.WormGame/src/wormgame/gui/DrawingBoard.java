/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wormgame.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import wormgame.game.WormGame;

/**
 *
 * @author Sofia
 */
public class DrawingBoard extends JPanel implements Updatable{

    private WormGame game;
    private int pieceLength;

    public DrawingBoard(WormGame game,int pieceSideLength) {
        super.setBackground(Color.LIGHT_GRAY);
        this.game = game;
        pieceLength = pieceSideLength;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        game.getApple().draw(g,pieceLength);
        game.getWorm().draw(g,pieceLength);
    }

    @Override
    public void update() {
      repaint();
    }
}
