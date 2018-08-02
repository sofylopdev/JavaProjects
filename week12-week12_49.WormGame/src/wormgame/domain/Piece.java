/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wormgame.domain;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Sofia
 */
public class Piece implements Comparable<Piece> {

    private int x;
    private int y;

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean runsInto(Piece piece) {
        return (this.x == piece.getX() && this.y == piece.getY());
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public int compareTo(Piece t) {
        return Math.abs(this.x - t.getX()) + Math.abs(this.y - t.getY());
    }

    public void draw(Graphics graphics,int size) {
        graphics.setColor(Color.red);
        graphics.fillOval(size*getX(), size*getY(), size, size);
        
    }
}
