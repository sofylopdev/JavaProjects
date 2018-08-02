/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wormgame.domain;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import wormgame.Direction;

/**
 *
 * @author Sofia
 */
public class Worm {

    private int originalX;
    private int originalY;
    private Direction originalDirection;
    private Direction startDirection;
    private List<Piece> wormPieces;
    private int check;
    private int check2;

    public Worm(int originalX, int originalY, Direction originalDirection) {
        this.originalX = originalX;
        this.originalY = originalY;
        this.originalDirection = originalDirection;
        this.startDirection = originalDirection;
        wormPieces = new ArrayList<Piece>();
        wormPieces.add(new Piece(this.originalX, this.originalY));
        check = 0;
        check2 = 0;
    }
    
    public void Reset()
    {
        this.originalDirection = this.startDirection;
        this.wormPieces = new ArrayList<Piece>();
        this.wormPieces.add(new Piece(this.originalX, this.originalY));
        this.check = 0;
        this.check2 = 0;
    }

    public Direction getDirection() {
        return originalDirection;
    }

    public void setDirection(Direction dir) {
        originalDirection = dir;
    }

    public int getLength() {
        return wormPieces.size();
    }

    public List<Piece> getPieces() {
        return wormPieces;
    }

    public void move() {
        check = 1;
        grow();
        check = 0;
        check2 = 0;
    }
//worm grows only when it moves!

    public void grow() {

        if (check == 1) {
            if (wormPieces.size() < 3) {
                addPiece();

            } else {
                if (check2 == 1) {
                    addPiece();
                } else {
                    addPiece();
                    wormPieces.remove(wormPieces.get(0));
                }
            }
        } else {
            wormPieces.add(0, new Piece(wormPieces.get(0).getX(),
                    wormPieces.get(wormPieces.size() - 1).getY()));
            wormPieces.remove(wormPieces.get(0));

        }
        check2 = 1;
    }

    public void addPiece() {
        if (originalDirection == Direction.DOWN) {
            wormPieces.add(new Piece(wormPieces.get(wormPieces.size() - 1).getX(),
                    wormPieces.get(wormPieces.size() - 1).getY() + 1));
        } else if (originalDirection == Direction.UP) {
            wormPieces.add(new Piece(wormPieces.get(wormPieces.size() - 1).getX(),
                    wormPieces.get(wormPieces.size() - 1).getY() - 1));
        } else if (originalDirection == Direction.LEFT) {
            wormPieces.add(new Piece(wormPieces.get(wormPieces.size() - 1).getX() - 1,
                    wormPieces.get(wormPieces.size() - 1).getY()));
        } else if (originalDirection == Direction.RIGHT) {
            wormPieces.add(new Piece(wormPieces.get(wormPieces.size() - 1).getX() + 1,
                    wormPieces.get(wormPieces.size() - 1).getY()));
        }
    }

    public boolean runsInto(Piece piece) {
        for (Piece each : wormPieces) {
            if (each.getX() == piece.getX() && each.getY() == piece.getY()) {
                return true;
            }
        }
        return false;
    }

    public boolean runsIntoItself() {
        for (int i = 0; i < wormPieces.size(); i++) {
            for (int j = 0; j < wormPieces.size(); j++) {
                if (i == j) {
                    continue;
                } else {
                    if (wormPieces.get(i).compareTo(wormPieces.get(j)) == 0) {                       
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public  void draw(Graphics graphics,int size){
        for(Piece each:getPieces()){
        graphics.setColor(Color.BLACK);
        graphics.fill3DRect(size*each.getX(),size*each.getY(),size,size,true);
        
        
        } 
    }   
}
