package wormgame.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.Timer;
import wormgame.Direction;
import wormgame.domain.Apple;
import wormgame.domain.Worm;
import wormgame.gui.Updatable;

public class WormGame extends Timer implements ActionListener {

    private int width;
    private int height;
    private boolean continues;
    private Updatable updatable;
    private Worm worm;
    private Apple apple;
    private int score;
    private int lastScore;
    private JLabel text;

    public WormGame(int width, int height) {
        super(1000, null);

        this.width = width;
        this.height = height;
        this.continues = true;

        addActionListener(this);
        setInitialDelay(2000);

        worm = new Worm(width / 2, height / 2, Direction.DOWN);
        this.apple = createApple();
        
        score=0;
        lastScore=0;

    }
    public void setLabel(JLabel label){
        this.text = label;
    }
    
    public String getScore(){
        if(lastScore>0){
        return "Score: " +score + " Last Score: " +lastScore;
        }else{
            return "Score: " +score;
        }
    }

    private Apple createApple() {
        Random random = new Random();
        Apple newApple;
        int x;
        int y;
        while (true) {
            x = random.nextInt(width);
            y = random.nextInt(height);
            if ((x != (width / 2)) || y != height / 2) {
                newApple = new Apple(x, y);
                break;
            }
        }
        return newApple;
    }

    public boolean continues() {
        return continues;
    }

    public void setUpdatable(Updatable updatable) {
        this.updatable = updatable;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Worm getWorm() {
        return this.worm;
    }

    public void setWorm(Worm worm) {
        this.worm = worm;
    }

    public Apple getApple() {
        return this.apple;
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (!continues) {
            continues = true;
        this.worm.Reset();
        lastScore=score;
        score=0;
        
          //  return;
        }
        
        worm.move();
        if (worm.runsInto(apple)) {
            worm.grow();
            this.score++;
            apple = createApple();
        } else if (worm.runsIntoItself()) {
            this.continues = false;
        } else if (worm.getPieces().get(worm.getPieces().size() - 1).getX() == this.width
                || worm.getPieces().get(worm.getPieces().size() - 1).getY() == this.height) {
            this.continues = false;
        } else if (worm.getPieces().get(worm.getPieces().size() - 1).getX() < 0
                || worm.getPieces().get(worm.getPieces().size() - 1).getY() < 0) {
            this.continues = false;
        }
        
        this.text.setText(getScore());
        updatable.update();
        setDelay(1000 / worm.getLength());
        
    }
}
