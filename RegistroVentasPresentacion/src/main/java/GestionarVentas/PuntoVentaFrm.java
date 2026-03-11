/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GestionarVentas;

import Componentes.TarjetaCategoria;
import Componentes.TarjetaProducto;
import javax.swing.BorderFactory;

/**
 *
 * @author rramirez
 */
public class PuntoVentaFrm extends javax.swing.JFrame {

    /**
     * Creates new form MenuFrm
     */
    public PuntoVentaFrm() {
        initComponents();
        configurarEstiloFrm();
        setExtendedState(PuntoVentaFrm.MAXIMIZED_BOTH);
    }

    public void configurarEstiloFrm() {
        aplicarEstilosPanelDerecho();
        configurarCategorias();
        configurarCatalogo();
    }

    public void configurarCategorias() {
        PnlCategorias.setOpaque(false);
        PnlCategorias.setPreferredSize(new java.awt.Dimension(0, 150));

        PnlCategorias.setBorder(new javax.swing.border.EmptyBorder(0, 0, 20, 0));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // T1: CALDOS Y PLATOS
        gbc.weightx = 0.40;
        gbc.insets = new java.awt.Insets(0, 0, 0, 20);
        PnlCategorias.add(new TarjetaCategoria("CALDOS Y PLATOS", "9", new java.awt.Color(255, 51, 51)), gbc);

        // T2: TACOS
        gbc.weightx = 0.20;
        gbc.insets = new java.awt.Insets(0, 0, 0, 20);
        PnlCategorias.add(new TarjetaCategoria("TACOS", "8", new java.awt.Color(255, 128, 0)), gbc);

        // T3: COMBOS
        gbc.weightx = 0.20;
        gbc.insets = new java.awt.Insets(0, 0, 0, 20);
        PnlCategorias.add(new TarjetaCategoria("COMBOS", "1", new java.awt.Color(156, 39, 176)), gbc);

        // T4: BEBIDAS
        gbc.weightx = 0.20;
        gbc.insets = new java.awt.Insets(0, 0, 0, 0);
        PnlCategorias.add(new TarjetaCategoria("BEBIDAS", "4", new java.awt.Color(0, 51, 255)), gbc);

        PnlCategorias.revalidate();
        PnlCategorias.repaint();
    }

    public void configurarCatalogo() {

        ScrollCatalogo.setBorder(null);

        ScrollCatalogo.setViewportBorder(null);

        ScrollCatalogo.setBackground(java.awt.Color.WHITE);
        ScrollCatalogo.getViewport().setBackground(java.awt.Color.WHITE);

        ScrollCatalogo.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        ScrollCatalogo.getViewport().setBackground(java.awt.Color.WHITE);
        ScrollCatalogo.getViewport().setOpaque(false);

        ScrollCatalogo.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        ScrollCatalogo.getVerticalScrollBar().setUnitIncrement(16);

        PnlCatalogo.setOpaque(false);
        PnlCatalogo.removeAll();

        PnlCatalogo.setLayout(new java.awt.GridLayout(0, 3, 20, 20));

        // Dummys hacer un llenarCatalogo()
        PnlCatalogo.add(new TarjetaProducto("Taco de Cahuamanta", 35.0, "TacoCahuamanta.png"));
        PnlCatalogo.add(new TarjetaProducto("Taco de Cahuamanta con Camarón", 50.0, "TacoCahuamanta.png"));
        PnlCatalogo.add(new TarjetaProducto("Taco de Moronga", 35.0, "TacoCahuamanta.png"));
        PnlCatalogo.add(new TarjetaProducto("Taco de Aleta", 90.0, "TacoCahuamanta.png"));
        PnlCatalogo.add(new TarjetaProducto("Taco de Camarón Empanizado", 55.0, "TacoCahuamanta.png"));
        PnlCatalogo.add(new TarjetaProducto("Taco de Camarón Cocido", 80.0, "TacoCahuamanta.png"));
        PnlCatalogo.add(new TarjetaProducto("Taco de Aleta y Camarón", 110.0, "TacoCahuamanta.png"));
        PnlCatalogo.add(new TarjetaProducto("Taco de Pescado Frito", 50.0, "TacoCahuamanta.png"));

        javax.swing.JPanel wrapper = new javax.swing.JPanel(new java.awt.BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(PnlCatalogo, java.awt.BorderLayout.NORTH);

        ScrollCatalogo.setViewportView(wrapper);

        ScrollCatalogo.revalidate();
        ScrollCatalogo.repaint();
    }

    private void aplicarEstilosPanelDerecho() {
        PnlCardSeleccionado.setBackground(new java.awt.Color(245, 245, 245));

        PnlAgrupadorCantidad.setOpaque(false);

        BoxCantidad.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        BoxCantidad.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        BoxCantidad.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        BtnAgregar.setFocusPainted(false);
        BtnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnAgregar.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //Estilo panel resumen
        PnlCardResumen.setBackground(new java.awt.Color(245, 245, 245));

        ScrollResumen.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        ScrollResumen.setOpaque(false);
        ScrollResumen.getViewport().setOpaque(false);

        TblResumen.setShowGrid(false);
        TblResumen.setIntercellSpacing(new java.awt.Dimension(0, 0));
        TblResumen.setRowHeight(40);
        TblResumen.setBackground(new java.awt.Color(245, 245, 245));

        TblResumen.setTableHeader(null);
        ScrollResumen.setBorder(BorderFactory.createEmptyBorder());

        //Estilos panel totales
        PnlCardTotales.setBackground(new java.awt.Color(245, 245, 245));
        BtnCobrar.setFocusPainted(false);
        BtnCobrar.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 0, 15, 0));

        //Scroll bordes
        ScrollResumen.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        ScrollResumen.setOpaque(false);
        ScrollResumen.getViewport().setOpaque(false);
        TblResumen.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        //Tabla resuemen detalles
        javax.swing.table.DefaultTableCellRenderer alineacionDerecha = new javax.swing.table.DefaultTableCellRenderer();
        alineacionDerecha.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TblResumen.getColumnModel().getColumn(1).setCellRenderer(alineacionDerecha);
        alineacionDerecha.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));

        BtnAgregar.setContentAreaFilled(false);
        BtnAgregar.setOpaque(true);
        BtnAgregar.setBackground(new java.awt.Color(0, 51, 255));
        BtnAgregar.repaint();
        BtnCobrar.setContentAreaFilled(false);
        BtnCobrar.setOpaque(true);
        BtnCobrar.setBackground(new java.awt.Color(0, 51, 255));
        BtnCobrar.repaint();
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

        PnlContenido = new javax.swing.JPanel();
        PnlIzquierdo = new javax.swing.JPanel();
        PnlCategorias = new javax.swing.JPanel();
        ScrollCatalogo = new javax.swing.JScrollPane();
        PnlWrraper = new javax.swing.JPanel();
        PnlCatalogo = new javax.swing.JPanel();
        PnlDerecho = new javax.swing.JPanel();
        LblTituloSeleccionado = new javax.swing.JLabel();
        PnlCardSeleccionado = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Color colorFondo = getBackground();
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();

                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                int radius = 25; 

                g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                g2.setColor(new java.awt.Color(230, 230, 230)); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                g2.dispose();
            }
        };
        LblNombreProducto = new javax.swing.JLabel();
        BtnAgregar = new javax.swing.JButton();
        PnlAgrupadorCantidad = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Color colorFondo = getBackground();
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();

                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                int radius = 25; 

                g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                g2.setColor(new java.awt.Color(230, 230, 230)); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                g2.dispose();
            }
        };
        LblCantidad = new javax.swing.JLabel();
        BoxCantidad = new javax.swing.JSpinner();
        LblTituloResumen = new javax.swing.JLabel();
        PnlCardResumen = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Color colorFondo = getBackground();
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();

                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                int radius = 25; 

                g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                g2.setColor(new java.awt.Color(230, 230, 230)); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                g2.dispose();
            }
        }
        ;
        ScrollResumen = new javax.swing.JScrollPane();
        TblResumen = new javax.swing.JTable();
        TblResumen.setTableHeader(null);
        PnlCardTotales = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Color colorFondo = getBackground();
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();

                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                int radius = 25; 

                g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                g2.setColor(new java.awt.Color(230, 230, 230)); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                g2.dispose();
            }
        };
        LblSubtotalTexto = new javax.swing.JLabel();
        LblSubtotalMonto = new javax.swing.JLabel();
        LblSeparador = new javax.swing.JLabel();
        LblTotalTexto = new javax.swing.JLabel();
        LblTotalMonto = new javax.swing.JLabel();
        BtnCobrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        PnlContenido.setBackground(new java.awt.Color(252, 252, 252));
        PnlContenido.setForeground(new java.awt.Color(255, 255, 255));
        PnlContenido.setLayout(new java.awt.GridBagLayout());

        PnlIzquierdo.setBackground(new java.awt.Color(204, 204, 204));
        PnlIzquierdo.setOpaque(false);
        PnlIzquierdo.setLayout(new java.awt.BorderLayout());

        PnlCategorias.setLayout(new java.awt.GridBagLayout());
        PnlIzquierdo.add(PnlCategorias, java.awt.BorderLayout.NORTH);

        ScrollCatalogo.setBorder(null);
        ScrollCatalogo.setOpaque(false);

        PnlWrraper.setLayout(new java.awt.BorderLayout());

        PnlCatalogo.setLayout(new java.awt.GridLayout());
        PnlWrraper.add(PnlCatalogo, java.awt.BorderLayout.NORTH);

        ScrollCatalogo.setViewportView(PnlWrraper);

        PnlIzquierdo.add(ScrollCatalogo, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 10);
        PnlContenido.add(PnlIzquierdo, gridBagConstraints);

        PnlDerecho.setBackground(new java.awt.Color(255, 255, 255));
        PnlDerecho.setLayout(new java.awt.GridBagLayout());

        LblTituloSeleccionado.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LblTituloSeleccionado.setForeground(new java.awt.Color(0, 0, 0));
        LblTituloSeleccionado.setText("Seleccionado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 5, 20);
        PnlDerecho.add(LblTituloSeleccionado, gridBagConstraints);

        PnlCardSeleccionado.setForeground(new java.awt.Color(229, 229, 229));
        PnlCardSeleccionado.setOpaque(false);
        PnlCardSeleccionado.setLayout(new java.awt.GridBagLayout());

        LblNombreProducto.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LblNombreProducto.setForeground(new java.awt.Color(0, 0, 0));
        LblNombreProducto.setText("Taco de Cahuamanta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 5, 0);
        PnlCardSeleccionado.add(LblNombreProducto, gridBagConstraints);

        BtnAgregar.setBackground(new java.awt.Color(0, 17, 255));
        BtnAgregar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        BtnAgregar.setText("Agregar");
        BtnAgregar.setBorder(null);
        BtnAgregar.setPreferredSize(new java.awt.Dimension(100, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 15, 15);
        PnlCardSeleccionado.add(BtnAgregar, gridBagConstraints);

        PnlAgrupadorCantidad.setForeground(new java.awt.Color(229, 229, 229));
        PnlAgrupadorCantidad.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        LblCantidad.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LblCantidad.setForeground(new java.awt.Color(0, 0, 0));
        LblCantidad.setText("Cantidad: ");
        PnlAgrupadorCantidad.add(LblCantidad);

        BoxCantidad.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BoxCantidad.setBorder(null);
        PnlAgrupadorCantidad.add(BoxCantidad);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 15, 10);
        PnlCardSeleccionado.add(PnlAgrupadorCantidad, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
        PnlDerecho.add(PnlCardSeleccionado, gridBagConstraints);

        LblTituloResumen.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LblTituloResumen.setForeground(new java.awt.Color(0, 0, 0));
        LblTituloResumen.setText("Resumen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 5, 20);
        PnlDerecho.add(LblTituloResumen, gridBagConstraints);

        PnlCardResumen.setForeground(new java.awt.Color(229, 229, 229));
        PnlCardResumen.setLayout(new java.awt.BorderLayout());

        ScrollResumen.setBorder(null);
        ScrollResumen.setForeground(new java.awt.Color(229, 229, 229));

        TblResumen.setBackground(new java.awt.Color(229, 229, 229));
        TblResumen.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TblResumen.setForeground(new java.awt.Color(0, 0, 0));
        TblResumen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Taco de Cahuamanta", "$35"},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Producto", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ScrollResumen.setViewportView(TblResumen);

        PnlCardResumen.add(ScrollResumen, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 15, 20);
        PnlDerecho.add(PnlCardResumen, gridBagConstraints);

        PnlCardTotales.setForeground(new java.awt.Color(229, 229, 229));
        PnlCardTotales.setLayout(new java.awt.GridBagLayout());

        LblSubtotalTexto.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        LblSubtotalTexto.setText("Subtotal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 5, 5);
        PnlCardTotales.add(LblSubtotalTexto, gridBagConstraints);

        LblSubtotalMonto.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        LblSubtotalMonto.setText("$35");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 15);
        PnlCardTotales.add(LblSubtotalMonto, gridBagConstraints);

        LblSeparador.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LblSeparador.setText("...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 0);
        PnlCardTotales.add(LblSeparador, gridBagConstraints);

        LblTotalTexto.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LblTotalTexto.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 15, 5);
        PnlCardTotales.add(LblTotalTexto, gridBagConstraints);

        LblTotalMonto.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LblTotalMonto.setText("$35");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 15, 15);
        PnlCardTotales.add(LblTotalMonto, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 15, 20);
        PnlDerecho.add(PnlCardTotales, gridBagConstraints);

        BtnCobrar.setBackground(new java.awt.Color(0, 17, 255));
        BtnCobrar.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        BtnCobrar.setForeground(new java.awt.Color(255, 255, 255));
        BtnCobrar.setText("Cobrar");
        BtnCobrar.setBorder(null);
        BtnCobrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnCobrar.setPreferredSize(new java.awt.Dimension(100, 50));
        BtnCobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCobrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 50, 10, 50);
        PnlDerecho.add(BtnCobrar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 20, 20);
        PnlContenido.add(PnlDerecho, gridBagConstraints);

        getContentPane().add(PnlContenido, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCobrarActionPerformed
        // TODO add your handling code here:
        this.dispose();
        MetodoPagoFrm mpf = new MetodoPagoFrm();
        mpf.setVisible(true);
    }//GEN-LAST:event_BtnCobrarActionPerformed

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
            java.util.logging.Logger.getLogger(PuntoVentaFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(PuntoVentaFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(PuntoVentaFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(PuntoVentaFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PuntoVentaFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner BoxCantidad;
    private javax.swing.JButton BtnAgregar;
    private javax.swing.JButton BtnCobrar;
    private javax.swing.JLabel LblCantidad;
    private javax.swing.JLabel LblNombreProducto;
    private javax.swing.JLabel LblSeparador;
    private javax.swing.JLabel LblSubtotalMonto;
    private javax.swing.JLabel LblSubtotalTexto;
    private javax.swing.JLabel LblTituloResumen;
    private javax.swing.JLabel LblTituloSeleccionado;
    private javax.swing.JLabel LblTotalMonto;
    private javax.swing.JLabel LblTotalTexto;
    private javax.swing.JPanel PnlAgrupadorCantidad;
    private javax.swing.JPanel PnlCardResumen;
    private javax.swing.JPanel PnlCardSeleccionado;
    private javax.swing.JPanel PnlCardTotales;
    private javax.swing.JPanel PnlCatalogo;
    private javax.swing.JPanel PnlCategorias;
    private javax.swing.JPanel PnlContenido;
    private javax.swing.JPanel PnlDerecho;
    private javax.swing.JPanel PnlIzquierdo;
    private javax.swing.JPanel PnlWrraper;
    private javax.swing.JScrollPane ScrollCatalogo;
    private javax.swing.JScrollPane ScrollResumen;
    private javax.swing.JTable TblResumen;
    // End of variables declaration//GEN-END:variables
}
