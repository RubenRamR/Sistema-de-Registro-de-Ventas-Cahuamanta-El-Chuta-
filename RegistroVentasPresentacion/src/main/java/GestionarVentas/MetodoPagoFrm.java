/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gestionarventas;

import componentes.TarjetaPago;
import utils.EstiloUI;

/**
 *
 * @author rramirez
 */
public class MetodoPagoFrm extends javax.swing.JFrame {

    private Runnable onPagoEfectivo;
    private Runnable onPagoTarjeta;
    private Runnable onPagoTransferencia;
    private Runnable onBack;

    /**
     * Creates new form MenuFrm
     */
    public MetodoPagoFrm(
            Runnable onPagoEfectivo,
            Runnable onPagoTarjeta,
            Runnable onPagoTransferencia,
            Runnable onBack
    ) {
        this.onPagoEfectivo = onPagoEfectivo;
        this.onPagoTarjeta = onPagoTarjeta;
        this.onPagoTransferencia = onPagoTransferencia;
        this.onBack = onBack;

        initComponents();
        utils.EstiloUI.aplicarTamanioMinimo(this);
        setExtendedState(MetodoPagoFrm.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setTitle("El Chuta — Método de pago");
        aplicarEstilo();
        configurarTarjetas();
    }

    private void aplicarEstilo() {
        getContentPane().setBackground(EstiloUI.COLOR_FONDO);

        // Header con wordmark
        PnlHeader.removeAll();
        PnlHeader.setLayout(new java.awt.BorderLayout());
        PnlHeader.setBackground(EstiloUI.COLOR_BARRA);
        PnlHeader.setPreferredSize(new java.awt.Dimension(0, 78));
        PnlHeader.add(EstiloUI.crearHeader("Cobro", "Paso 2 de 3"),
                java.awt.BorderLayout.CENTER);

        // Footer con regla de acento
        PnlFooter.removeAll();
        PnlFooter.setLayout(new java.awt.BorderLayout());
        PnlFooter.setBackground(EstiloUI.COLOR_BARRA_OSCURA);
        PnlFooter.setBorder(javax.swing.BorderFactory.createMatteBorder(
                2, 0, 0, 0, EstiloUI.COLOR_BARRA_ACENTO));
        PnlFooter.setPreferredSize(new java.awt.Dimension(0, 64));

        BtnBack.setText("←  Regresar");
        BtnBack.setFont(EstiloUI.FUENTE_BOTON);
        BtnBack.setBackground(EstiloUI.COLOR_BARRA);
        BtnBack.setForeground(java.awt.Color.WHITE);
        BtnBack.setOpaque(true);
        BtnBack.setBorderPainted(false);
        BtnBack.setFocusPainted(false);
        BtnBack.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
        BtnBack.setPreferredSize(new java.awt.Dimension(160, 42));
        javax.swing.JPanel wrap = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 14, 12));
        wrap.setOpaque(false);
        wrap.add(BtnBack);
        PnlFooter.add(wrap, java.awt.BorderLayout.WEST);

        // Contenido: encabezado editorial + tarjetas
        PnlContenido.removeAll();
        PnlContenido.setBackground(EstiloUI.COLOR_FONDO);
        PnlContenido.setLayout(new java.awt.BorderLayout());
        PnlContenido.setBorder(new javax.swing.border.EmptyBorder(36, 56, 36, 56));

        javax.swing.JPanel encabezado = new javax.swing.JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new javax.swing.BoxLayout(encabezado, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.JLabel kicker = EstiloUI.crearKicker("Selecciona método");
        kicker.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        LblTitulo.setText("¿Cómo va a pagar?");
        LblTitulo.setFont(EstiloUI.FUENTE_TITULO_PANTALLA);
        LblTitulo.setForeground(EstiloUI.COLOR_TEXTO);
        LblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LblTitulo.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        javax.swing.JLabel sub = EstiloUI.crearSubtitulo(
                "Toca el método para registrar el cobro de la venta.");
        sub.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        encabezado.add(kicker);
        encabezado.add(javax.swing.Box.createVerticalStrut(8));
        encabezado.add(LblTitulo);
        encabezado.add(javax.swing.Box.createVerticalStrut(6));
        encabezado.add(sub);

        PnlContenido.add(encabezado, java.awt.BorderLayout.NORTH);
    }

    private void configurarTarjetas() {
        // Grid 1x3 horizontal con un acento de color por método
        javax.swing.JPanel grid = new javax.swing.JPanel(new java.awt.GridLayout(1, 3, 24, 0));
        grid.setOpaque(false);
        grid.setBorder(new javax.swing.border.EmptyBorder(28, 0, 0, 0));

        grid.add(new TarjetaPago("Efectivo", "efectivo.png", onPagoEfectivo,
                EstiloUI.COLOR_CATEGORIA_NARANJA));
        grid.add(new TarjetaPago("Tarjeta", "tarjeta.png", onPagoTarjeta,
                EstiloUI.COLOR_SECUNDARIO));
        grid.add(new TarjetaPago("Transferencia", "transferencia.png", onPagoTransferencia,
                EstiloUI.COLOR_CATEGORIA_MORADO));

        PnlContenido.add(grid, java.awt.BorderLayout.CENTER);
        PnlContenido.revalidate();
        PnlContenido.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        PnlHeader = new javax.swing.JPanel();
        PnlFooter = new javax.swing.JPanel();
        BtnBack = new javax.swing.JButton();
        PnlContenido = new javax.swing.JPanel();
        LblTitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        PnlHeader.setBackground(new java.awt.Color(47, 102, 144));
        PnlHeader.setPreferredSize(new java.awt.Dimension(1024, 60));

        javax.swing.GroupLayout PnlHeaderLayout = new javax.swing.GroupLayout(PnlHeader);
        PnlHeader.setLayout(PnlHeaderLayout);
        PnlHeaderLayout.setHorizontalGroup(
            PnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PnlHeaderLayout.setVerticalGroup(
            PnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(PnlHeader, java.awt.BorderLayout.NORTH);

        PnlFooter.setBackground(new java.awt.Color(47, 102, 144));
        PnlFooter.setPreferredSize(new java.awt.Dimension(1024, 60));
        PnlFooter.setLayout(new java.awt.GridBagLayout());

        BtnBack.setBackground(new java.awt.Color(3, 4, 94));
        BtnBack.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        BtnBack.setForeground(new java.awt.Color(255, 255, 255));
        BtnBack.setText("←");
        BtnBack.setAlignmentY(0.0F);
        BtnBack.setBorder(null);
        BtnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnBack.setFocusPainted(false);
        BtnBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BtnBack.setInheritsPopupMenu(true);
        BtnBack.setMargin(new java.awt.Insets(2, 14, 0, 14));
        BtnBack.setMaximumSize(new java.awt.Dimension(32, 64));
        BtnBack.setPreferredSize(new java.awt.Dimension(60, 10));
        BtnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBackActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 2.0;
        PnlFooter.add(BtnBack, gridBagConstraints);

        getContentPane().add(PnlFooter, java.awt.BorderLayout.SOUTH);

        PnlContenido.setBackground(new java.awt.Color(255, 255, 255));
        PnlContenido.setLayout(new java.awt.GridBagLayout());

        LblTitulo.setBackground(new java.awt.Color(255, 255, 255));
        LblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 80)); // NOI18N
        LblTitulo.setForeground(new java.awt.Color(3, 4, 94));
        LblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LblTitulo.setText("Método de pago");
        LblTitulo.setAlignmentY(0.0F);
        LblTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        PnlContenido.add(LblTitulo, gridBagConstraints);

        getContentPane().add(PnlContenido, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBackActionPerformed
        onBack.run();
    }//GEN-LAST:event_BtnBackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(MetodoPagoFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(MetodoPagoFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(MetodoPagoFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(MetodoPagoFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MetodoPagoFrm(
                        () -> {},
                        () -> {},
                        () -> {},
                        () -> {}
                ).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnBack;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JPanel PnlContenido;
    private javax.swing.JPanel PnlFooter;
    private javax.swing.JPanel PnlHeader;
    // End of variables declaration//GEN-END:variables
}
