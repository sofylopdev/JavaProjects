/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elev.net.UserInterface;

import elev.net.ConfigFile.AcederConfigFile;
import elev.net.Logic.Azar;
import elev.net.ConfigFile.CriarConfigFile;
import elev.net.Logic.SistemaLigacao;
import elev.net.UserInterface.Exceptions.CamposPorPreencherException;
import elev.net.UserInterface.Exceptions.ValoresSemSentidoException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Sofia
 */
public class MenuCriarArranhaCeus extends javax.swing.JFrame {

    private CriarConfigFile config = new CriarConfigFile();
    private List<String> elevadores = new ArrayList<String>();

    private UserInterface ui;
    private SistemaLigacao classe;
    private Azar azar;

    /**
     * Creates new form MenuCriarArranhaCeus
     */
    public MenuCriarArranhaCeus() {
        initComponents();
    }

    public void IniciarArranhaCeus() {
        this.setVisible(false);

        ui = new UserInterface();//deck, list//cria ui
        SwingUtilities.invokeLater(ui);//da start a ui

        classe = new SistemaLigacao(ui);
        Thread t1 = new Thread(classe);//cria thread SistemaLigacao

        azar = new Azar(ui.deck);
        Thread t2 = new Thread(azar);//cria thread azar

        t1.start();//da start a SistemaLigacao
        t2.start();//da start a azar

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        minFloor = new javax.swing.JTextField();
        maxFloor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tiposDeElevadores = new javax.swing.JComboBox<>();
        AdicionarElevadores = new javax.swing.JButton();
        Criar = new javax.swing.JToggleButton();
        Salvar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaNova = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Criar Arranha-ceus");

        jLabel2.setText("Andar minimo:");

        jLabel3.setText("Andar maximo:");

        minFloor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minFloorActionPerformed(evt);
            }
        });

        maxFloor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxFloorActionPerformed(evt);
            }
        });

        jLabel5.setText("Adicionar tipo de elevadores:");

        tiposDeElevadores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "normal", "largo", "carga", "expresso", "restrito" }));
        tiposDeElevadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tiposDeElevadoresActionPerformed(evt);
            }
        });

        AdicionarElevadores.setText("Adicionar");
        AdicionarElevadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdicionarElevadoresActionPerformed(evt);
            }
        });

        Criar.setText("Criar");
        Criar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CriarActionPerformed(evt);
            }
        });

        Salvar.setText("Salvar");
        Salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalvarActionPerformed(evt);
            }
        });

        jLabel4.setText("Elevadores");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        listaNova.setColumns(20);
        listaNova.setRows(5);
        jScrollPane1.setViewportView(listaNova);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Salvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Criar)
                .addGap(19, 19, 19))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tiposDeElevadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(113, 113, 113)
                                .addComponent(AdicionarElevadores))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(16, 16, 16)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(minFloor, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                    .addComponent(maxFloor)))
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(257, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(minFloor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maxFloor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(52, 52, 52)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tiposDeElevadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AdicionarElevadores))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Criar)
                    .addComponent(Salvar))
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AdicionarElevadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdicionarElevadoresActionPerformed
        // verifica qual elevador esta seleccionado:
        String opcaoSeleccionada = (String) tiposDeElevadores.getSelectedItem();
        elevadores.add(opcaoSeleccionada);//adiciona a lista de elevadores

        String newList;
        StringBuilder stgB = new StringBuilder();
        for (String each : elevadores) {
            stgB.append(each);
            stgB.append("\n");

        }
        newList = stgB.toString();

        listaNova.setText(newList);//adicionar elevador(es) escolhido ao JTextArea


    }//GEN-LAST:event_AdicionarElevadoresActionPerformed

    private void tiposDeElevadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tiposDeElevadoresActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_tiposDeElevadoresActionPerformed

    private void maxFloorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxFloorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maxFloorActionPerformed

    private void minFloorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minFloorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_minFloorActionPerformed

    private void CriarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CriarActionPerformed

        if ((maxFloor.getText().isEmpty()) && (minFloor.getText().isEmpty()) && (elevadores.isEmpty())) {//se todos os campos vazios

            int message = JOptionPane.showConfirmDialog(null, "Nao criou novo Arranha-Ceus! Deseja continuar?", "", JOptionPane.OK_CANCEL_OPTION);
            if (message == JOptionPane.CANCEL_OPTION) {
                return;
            } else {
                this.IniciarArranhaCeus();//cria arranha ceus se ok selecionado
            }
        } else {
            this.IniciarArranhaCeus();//se todos os campos preenchidos, cria arranha ceus
        }
    }//GEN-LAST:event_CriarActionPerformed

    private void SalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalvarActionPerformed
        // TODO add your handling code here:
        StringBuilder strBuild = new StringBuilder();
        for (int i = 0; i < elevadores.size(); i++) {
            strBuild.append(elevadores.get(i));
            if (i != (elevadores.size() - 1)) {
                strBuild.append(",");
            }
        }
        String lista = strBuild.toString();// lista de elevadores em string para adicionar ao config file

        try {
            //Excepcoes no caso de valores nao fazerem sentido para criar o arranha ceus:
            if (Integer.parseInt(maxFloor.getText()) <= Integer.parseInt(minFloor.getText())) {
                throw new ValoresSemSentidoException("Andar Minimo e INFERIOR ou IGUAL ao Andar Maximo!");
            }
            if (Integer.parseInt(maxFloor.getText()) < 0 || Integer.parseInt(minFloor.getText()) > -1) {
                throw new ValoresSemSentidoException("Tem de existir piso -1!");
            }
            if (elevadores.size() > 10) {
                throw new ValoresSemSentidoException("Numero de Elevadores tem de ser inferior a 10!");////DPS COMO FAZER QNDO JA ADICIONOU?
            }
            if (Integer.parseInt(maxFloor.getText()) > 20) {
                throw new ValoresSemSentidoException("Maximo andar permitido: 20!");
            }
            if (Integer.parseInt(minFloor.getText()) < -4) {
                throw new ValoresSemSentidoException("Minimo andar permitido: -4!");
            }

            //Excepcoes para campos vazios:
            if (maxFloor.getText().isEmpty()) {
                throw new CamposPorPreencherException("Falta preencher o campo Andar Maximo!");
            } else if (minFloor.getText().isEmpty()) {
                throw new CamposPorPreencherException("Falta preencher o campo do Andar Minimo!");
            } else if (elevadores.isEmpty()) {
                throw new CamposPorPreencherException("Nao adicionou nenhum elevador!");
            }
            //caso nao haja erros ou falhas:
            config.saveProperties(maxFloor.getText(), minFloor.getText(), lista);               //guardar ficheiro config novo

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Os campos Andar Minimo e Andar Maximo so aceitam numeros inteiros!");
        } catch (ValoresSemSentidoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (CamposPorPreencherException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
// menuPrincipal.RefreshLista();


    }//GEN-LAST:event_SalvarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuCriarArranhaCeus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuCriarArranhaCeus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuCriarArranhaCeus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuCriarArranhaCeus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuCriarArranhaCeus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AdicionarElevadores;
    private javax.swing.JToggleButton Criar;
    private javax.swing.JButton Salvar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea listaNova;
    private javax.swing.JTextField maxFloor;
    private javax.swing.JTextField minFloor;
    private javax.swing.JComboBox<String> tiposDeElevadores;
    // End of variables declaration//GEN-END:variables
}
