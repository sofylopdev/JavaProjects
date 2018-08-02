/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.Logic;

import javax.swing.JTextArea;

/**
 *
 * @author Sofia
 */
public class AvisosOut {
    
    private JTextArea avisos = null;
    private JTextArea avarias = null;


    public void setAvisos(JTextArea avisos) {
        this.avisos = avisos;
    }
    public void setAvarias(JTextArea avarias) {
        this.avarias = avarias;
    }

    public void printAvisos(String texto) {
        if (this.avisos != null) {
            this.avisos.setText(texto);
        }
    }

    public void printAvarias(String texto) {
        if (this.avarias != null) {
            this.avarias.setText(texto);
        }
    }
}
