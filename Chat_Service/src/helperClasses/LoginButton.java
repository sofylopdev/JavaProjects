package helperClasses;

import javax.swing.*;
import java.awt.*;

public class LoginButton extends JButton {

    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;

    public LoginButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
        hoverBackgroundColor = Colors.darkBlue;
        pressedBackgroundColor = Colors.darkBlue;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(pressedBackgroundColor);
        } else if (getModel().isRollover()) {
            g.setColor(hoverBackgroundColor);
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    @Override
    public void setContentAreaFilled(boolean b) {
    }

    public Insets getInsets() {
        return new Insets(10, 20, 10, 20);
    }
}