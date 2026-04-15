package GestionarVentas;

import dtos.DetalleVentaDTO; // Asegúrate de importar tus DTOs
import dtos.VentaDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class DetalleVenta extends JFrame {

    private final Color COLOR_BANNER = new Color(43, 90, 140);
    private final Color COLOR_FONDO_APP = new Color(235, 240, 245); 
    private final Color COLOR_TEXTO_OSCURO = new Color(50, 50, 50);
    private final Color COLOR_BTN_ROJO = new Color(220, 53, 69);
    
    private final Font FUENTE_TITULOS = new Font("Segoe UI", Font.BOLD, 15);
    private final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 15);

    // NUEVO: Constructor recibe el DTO
    public DetalleVenta(
            VentaDTO venta,
            Runnable onBack,
            Runnable onCerrarDetalle
    ) {
        setTitle("Detalle de Venta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==========================================
        // 1. BANNERS
        // ==========================================
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panelNorte.setBackground(COLOR_BANNER);
        panelNorte.setPreferredSize(new Dimension(0, 60));
        
        JLabel lblTituloNorte = new JLabel("Consulta de Venta");
        lblTituloNorte.setForeground(Color.WHITE);
        lblTituloNorte.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panelNorte.add(lblTituloNorte);
        add(panelNorte, BorderLayout.NORTH);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelSur.setBackground(COLOR_BANNER);
        panelSur.setPreferredSize(new Dimension(0, 60));

        JButton btnRegresar = new JButton("<");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setBackground(new Color(25, 60, 100)); 
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.setOpaque(true);
        btnRegresar.setContentAreaFilled(true);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setPreferredSize(new Dimension(70, 60));
        btnRegresar.addActionListener(e -> {
            onBack.run();
        });
        panelSur.add(btnRegresar);
        add(panelSur, BorderLayout.SOUTH);

        // ==========================================
        // 2. CONTENIDO CENTRAL
        // ==========================================
        JPanel panelCentro = new JPanel(new GridBagLayout()); 
        panelCentro.setBackground(COLOR_FONDO_APP);

        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(new BorderLayout(0, 20)); 
        panelTarjeta.setBackground(Color.WHITE);
        panelTarjeta.setPreferredSize(new Dimension(800, 600)); 
        panelTarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 210), 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        // --- CABECERA DE LA TARJETA (Info extraída del DTO) ---
        JPanel panelInfo = new JPanel(new GridLayout(2, 2, 10, 15));
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(240, 240, 240))); 

        JLabel lblFechaTxt = new JLabel("Fecha y Hora:");
        lblFechaTxt.setFont(FUENTE_TITULOS);
        lblFechaTxt.setForeground(Color.GRAY);
        
        // EXTRACCIÓN DE DATOS: Fecha
        String fechaStr = (venta != null) ? venta.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm")) : "N/A";
        JLabel lblFechaVal = new JLabel(fechaStr);
        lblFechaVal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFechaVal.setForeground(COLOR_TEXTO_OSCURO);

        JLabel lblMetodoTxt = new JLabel("Método de Pago:");
        lblMetodoTxt.setFont(FUENTE_TITULOS);
        lblMetodoTxt.setForeground(Color.GRAY);
        
        // EXTRACCIÓN DE DATOS: Método de Pago
        String metodoStr = (venta != null) ? venta.getMetodoPago() : "N/A";
        JLabel lblMetodoVal = new JLabel(metodoStr);
        lblMetodoVal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMetodoVal.setForeground(new Color(40, 167, 69)); 

        panelInfo.add(lblFechaTxt);
        panelInfo.add(lblMetodoTxt);
        panelInfo.add(lblFechaVal);
        panelInfo.add(lblMetodoVal);
        
        JPanel wrapInfo = new JPanel(new BorderLayout());
        wrapInfo.setBackground(Color.WHITE);
        wrapInfo.add(panelInfo, BorderLayout.CENTER);
        wrapInfo.setBorder(new EmptyBorder(0, 0, 20, 0));

        // --- TABLA DE PRODUCTOS (Llenada desde el DTO) ---
        String[] columnas = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        
        // Usamos un modelo sin datos iniciales
        DefaultTableModel modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // EXTRACCIÓN DE DATOS: Llenar tabla con los detalles
        System.out.println("Detalles Ventas que hay: " + venta.getDetallesVenta());
        if (venta != null && venta.getDetallesVenta() != null) {
            for (DetalleVentaDTO detalle : venta.getDetallesVenta()) {
                Object[] fila = {
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad(),
                    "$" + detalle.getPrecioUnitario().toString(), // Asegúrate de usar el método correcto (getPrecio o getPrecioUnitario)
                    "$" + detalle.getSubtotal().toString()
                };
                modeloTabla.addRow(fila);
            }
        }

        JTable tablaDetalles = new JTable(modeloTabla) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 252));
                }
                return c;
            }
        };

        tablaDetalles.setRowHeight(45);
        tablaDetalles.setFont(FUENTE_NORMAL);
        tablaDetalles.setShowVerticalLines(false);
        tablaDetalles.setGridColor(new Color(230, 230, 230));

        JTableHeader header = tablaDetalles.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.GRAY);
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaDetalles.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaDetalles.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaDetalles.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollTabla = new JScrollPane(tablaDetalles);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(230, 235, 240)));
        scrollTabla.getViewport().setBackground(Color.WHITE);

        // --- PIE DE TARJETA ---
        JPanel panelPie = new JPanel(new BorderLayout());
        panelPie.setBackground(Color.WHITE);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelTotal.setBackground(Color.WHITE);
        JLabel lblTotalTxt = new JLabel("Total Cobrado:");
        lblTotalTxt.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        // EXTRACCIÓN DE DATOS: Total
        String totalStr = (venta != null && venta.getTotal() != null) ? "$" + venta.getTotal().toString() : "$0.00";
        JLabel lblTotalMonto = new JLabel(totalStr);
        lblTotalMonto.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTotalMonto.setForeground(COLOR_TEXTO_OSCURO);
        
        panelTotal.add(lblTotalTxt);
        panelTotal.add(lblTotalMonto);

        JPanel wrapBoton = new JPanel();
        wrapBoton.setBackground(Color.WHITE);
        wrapBoton.setBorder(new EmptyBorder(20, 0, 0, 0)); 
        
        JButton btnCerrar = new JButton("Cerrar Detalle") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                g.setColor(COLOR_BTN_ROJO);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12); 
                super.paintComponent(g);
            }
        };
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setPreferredSize(new Dimension(250, 45));
        btnCerrar.addActionListener(e -> {
            onCerrarDetalle.run();
        });
        
        wrapBoton.add(btnCerrar);

        panelPie.add(panelTotal, BorderLayout.NORTH);
        panelPie.add(wrapBoton, BorderLayout.CENTER);

        panelTarjeta.add(wrapInfo, BorderLayout.NORTH);
        panelTarjeta.add(scrollTabla, BorderLayout.CENTER);
        panelTarjeta.add(panelPie, BorderLayout.SOUTH);

        panelCentro.add(panelTarjeta); 
        add(panelCentro, BorderLayout.CENTER);
    }
}