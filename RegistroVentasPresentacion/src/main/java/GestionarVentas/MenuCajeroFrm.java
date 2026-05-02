/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GestionarVentas;

import utils.EstiloUI;

/**
 *
 * @author rramirez
 */
public class MenuCajeroFrm extends javax.swing.JFrame {

    private Runnable onGestionarVentas;
    private Runnable onBack;

    /**
     * Creates new form MenuFrm
     */
    public MenuCajeroFrm(
            Runnable onGestionarVentas,
            Runnable onBack
    ) {
        initComponents();
        setExtendedState(MenuCajeroFrm.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setTitle("El Chuta — Caja");

        this.onGestionarVentas = onGestionarVentas;
        this.onBack = onBack;

        aplicarEstilo();
    }

    private void aplicarEstilo() {
        getContentPane().setBackground(EstiloUI.COLOR_FONDO);

        // Reemplazar el header NetBeans por el header con wordmark
        PnlHeader.removeAll();
        PnlHeader.setLayout(new java.awt.BorderLayout());
        PnlHeader.setBackground(EstiloUI.COLOR_BARRA);
        PnlHeader.setPreferredSize(new java.awt.Dimension(0, 78));
        PnlHeader.add(EstiloUI.crearHeader("Punto de venta", "Sesión: Cajero"),
                java.awt.BorderLayout.CENTER);

        // Footer con botón regresar usando el helper estandar
        PnlFooter.removeAll();
        PnlFooter.setLayout(new java.awt.BorderLayout());
        PnlFooter.setBackground(EstiloUI.COLOR_BARRA_OSCURA);
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
        PnlFooter.setBorder(javax.swing.BorderFactory.createMatteBorder(
                2, 0, 0, 0, EstiloUI.COLOR_BARRA_ACENTO));
        PnlFooter.setPreferredSize(new java.awt.Dimension(0, 64));

        // Contenido editorial: kicker + título serif + subtítulo + CTA único
        PnlContenido.removeAll();
        PnlContenido.setBackground(EstiloUI.COLOR_FONDO);
        PnlContenido.setLayout(new java.awt.GridBagLayout());

        javax.swing.JPanel tarjeta = new javax.swing.JPanel();
        tarjeta.setLayout(new javax.swing.BoxLayout(tarjeta, javax.swing.BoxLayout.Y_AXIS));
        tarjeta.setBackground(EstiloUI.COLOR_TARJETA);
        tarjeta.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, EstiloUI.COLOR_ACCION),
                javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, EstiloUI.COLOR_BORDE),
                        new javax.swing.border.EmptyBorder(36, 40, 36, 40))));
        tarjeta.setPreferredSize(new java.awt.Dimension(640, 360));
        tarjeta.setMaximumSize(new java.awt.Dimension(640, 360));

        javax.swing.JLabel kicker = EstiloUI.crearKicker("Turno abierto");
        kicker.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        LblTitulo.setText("Buen turno");
        LblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LblTitulo.setFont(new java.awt.Font(
                EstiloUI.FUENTE_TITULO_PANTALLA.getFamily(),
                java.awt.Font.BOLD, 44));
        LblTitulo.setForeground(EstiloUI.COLOR_TEXTO);
        LblTitulo.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        javax.swing.JLabel sub = EstiloUI.crearSubtitulo(
                "Inicia una nueva venta cuando el cliente esté listo para ordenar.");
        sub.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        BtnGestionarVentas.setText("Abrir nueva venta  →");
        BtnGestionarVentas.setFont(EstiloUI.FUENTE_BOTON_GRANDE);
        BtnGestionarVentas.setBackground(EstiloUI.COLOR_ACCION);
        BtnGestionarVentas.setForeground(java.awt.Color.WHITE);
        BtnGestionarVentas.setOpaque(true);
        BtnGestionarVentas.setFocusPainted(false);
        BtnGestionarVentas.setBorderPainted(false);
        BtnGestionarVentas.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 32, 16, 32));
        BtnGestionarVentas.setPreferredSize(new java.awt.Dimension(280, 56));
        BtnGestionarVentas.setMaximumSize(new java.awt.Dimension(280, 56));
        BtnGestionarVentas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BtnGestionarVentas.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        tarjeta.add(kicker);
        tarjeta.add(javax.swing.Box.createVerticalStrut(10));
        tarjeta.add(LblTitulo);
        tarjeta.add(javax.swing.Box.createVerticalStrut(10));
        tarjeta.add(sub);
        tarjeta.add(javax.swing.Box.createVerticalStrut(28));
        tarjeta.add(BtnGestionarVentas);

        PnlContenido.add(tarjeta);
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
        BtnGestionarVentas = new javax.swing.JButton();

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
        LblTitulo.setText("Menú de Opciones");
        LblTitulo.setAlignmentY(0.0F);
        LblTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 55.0;
        PnlContenido.add(LblTitulo, gridBagConstraints);

        BtnGestionarVentas.setBackground(new java.awt.Color(0, 119, 182));
        BtnGestionarVentas.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        BtnGestionarVentas.setForeground(new java.awt.Color(255, 255, 255));
        BtnGestionarVentas.setText("Gestionar Ventas");
        BtnGestionarVentas.setAlignmentY(0.0F);
        BtnGestionarVentas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BtnGestionarVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnGestionarVentas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BtnGestionarVentas.setPreferredSize(new java.awt.Dimension(400, 125));
        BtnGestionarVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGestionarVentasActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PnlContenido.add(BtnGestionarVentas, gridBagConstraints);

        getContentPane().add(PnlContenido, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBackActionPerformed
        onBack.run();
    }//GEN-LAST:event_BtnBackActionPerformed

    private void BtnGestionarVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGestionarVentasActionPerformed
        // TODO add your handling code here:
        onGestionarVentas.run();
    }//GEN-LAST:event_BtnGestionarVentasActionPerformed

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
            java.util.logging.Logger.getLogger(MenuCajeroFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(MenuCajeroFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(MenuCajeroFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(MenuCajeroFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuCajeroFrm(
                        () -> {},
                        () -> {}
                ).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnBack;
    private javax.swing.JButton BtnGestionarVentas;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JPanel PnlContenido;
    private javax.swing.JPanel PnlFooter;
    private javax.swing.JPanel PnlHeader;
    // End of variables declaration//GEN-END:variables
}
