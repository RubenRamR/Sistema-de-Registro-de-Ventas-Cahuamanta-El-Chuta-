package gestionarventas;

import dtos.DetalleVentaDTO;
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

public class PantallaDetalleVenta extends JFrame {

    private final Color COLOR_BANNER       = utils.EstiloUI.COLOR_BARRA;
    private final Color COLOR_FONDO_APP    = utils.EstiloUI.COLOR_FONDO;
    private final Color COLOR_TEXTO_OSCURO = utils.EstiloUI.COLOR_TEXTO;
    private final Color COLOR_BTN_ROJO     = utils.EstiloUI.COLOR_DANGER;

    private final Font FUENTE_TITULOS = utils.EstiloUI.FUENTE_KICKER;
    private final Font FUENTE_NORMAL  = utils.EstiloUI.FUENTE_TABLA;

    // NUEVO: Constructor recibe el DTO
    public PantallaDetalleVenta(
            VentaDTO venta,
            Runnable onBack,
            Runnable onCerrarDetalle
    ) {
        setTitle("Detalle de Venta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        utils.EstiloUI.aplicarTamanioMinimo(this);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(utils.EstiloUI.crearHeader("Detalle de venta", null), BorderLayout.NORTH);

        JButton btnRegresar = utils.EstiloUI.crearBoton("←  Regresar", utils.EstiloUI.COLOR_BARRA);
        btnRegresar.setPreferredSize(new Dimension(160, 42));
        btnRegresar.addActionListener(e -> onBack.run());
        add(utils.EstiloUI.crearBarraInferior(btnRegresar), BorderLayout.SOUTH);

   
        JPanel panelCentro = new JPanel(new GridBagLayout()); 
        panelCentro.setBackground(COLOR_FONDO_APP);

        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(new BorderLayout(0, 20));
        panelTarjeta.setBackground(utils.EstiloUI.COLOR_TARJETA);
        panelTarjeta.setPreferredSize(new Dimension(820, 600));
        panelTarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(utils.EstiloUI.COLOR_BORDE, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JPanel panelInfo = new JPanel(new GridLayout(2, 2, 10, 15));
        panelInfo.setBackground(utils.EstiloUI.COLOR_TARJETA);
        panelInfo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, utils.EstiloUI.COLOR_BORDE));

        JLabel lblFechaTxt = new JLabel("FECHA Y HORA");
        lblFechaTxt.setFont(FUENTE_TITULOS);
        lblFechaTxt.setForeground(utils.EstiloUI.COLOR_MUTED);

        String fechaStr = (venta != null) ? venta.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm")) : "N/A";
        JLabel lblFechaVal = new JLabel(fechaStr);
        lblFechaVal.setFont(new Font(utils.EstiloUI.FUENTE_SECCION.getFamily(), Font.BOLD, 18));
        lblFechaVal.setForeground(COLOR_TEXTO_OSCURO);

        JLabel lblMetodoTxt = new JLabel("MÉTODO DE PAGO");
        lblMetodoTxt.setFont(FUENTE_TITULOS);
        lblMetodoTxt.setForeground(utils.EstiloUI.COLOR_MUTED);

        String metodoStr = (venta != null) ? venta.getMetodoPago() : "N/A";
        JLabel lblMetodoVal = new JLabel(metodoStr);
        lblMetodoVal.setFont(new Font(utils.EstiloUI.FUENTE_SECCION.getFamily(), Font.BOLD, 18));
        lblMetodoVal.setForeground(utils.EstiloUI.COLOR_ACCION);

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
                    c.setBackground(row % 2 == 0 ? utils.EstiloUI.COLOR_TARJETA : utils.EstiloUI.COLOR_TARJETA_BEIGE);
                }
                return c;
            }
        };

        tablaDetalles.setRowHeight(42);
        tablaDetalles.setFont(FUENTE_NORMAL);
        tablaDetalles.setShowVerticalLines(false);
        tablaDetalles.setGridColor(utils.EstiloUI.COLOR_BORDE);

        JTableHeader header = tablaDetalles.getTableHeader();
        header.setFont(utils.EstiloUI.FUENTE_TABLA_HEADER);
        header.setBackground(utils.EstiloUI.COLOR_BARRA);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 38));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, utils.EstiloUI.COLOR_BORDE));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaDetalles.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaDetalles.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaDetalles.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollTabla = new JScrollPane(tablaDetalles);
        scrollTabla.setBorder(BorderFactory.createLineBorder(utils.EstiloUI.COLOR_BORDE));
        scrollTabla.getViewport().setBackground(utils.EstiloUI.COLOR_TARJETA);

        // --- PIE DE TARJETA ---
        JPanel panelPie = new JPanel(new BorderLayout());
        panelPie.setBackground(utils.EstiloUI.COLOR_TARJETA);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelTotal.setBackground(utils.EstiloUI.COLOR_AUXILIAR);
        panelTotal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(utils.EstiloUI.COLOR_BORDE, 1, true),
                new EmptyBorder(8, 18, 8, 18)));
        JLabel lblTotalTxt = new JLabel("TOTAL COBRADO");
        lblTotalTxt.setFont(utils.EstiloUI.FUENTE_KICKER);
        lblTotalTxt.setForeground(utils.EstiloUI.COLOR_MUTED);

        String totalStr = (venta != null && venta.getTotal() != null) ? "$" + venta.getTotal().toString() : "$0.00";
        JLabel lblTotalMonto = new JLabel(totalStr);
        lblTotalMonto.setFont(utils.EstiloUI.FUENTE_TOTAL);
        lblTotalMonto.setForeground(COLOR_TEXTO_OSCURO);

        panelTotal.add(lblTotalTxt);
        panelTotal.add(lblTotalMonto);

        JPanel wrapBoton = new JPanel();
        wrapBoton.setBackground(utils.EstiloUI.COLOR_TARJETA);
        wrapBoton.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnCerrar = utils.EstiloUI.crearBoton("Cerrar detalle", COLOR_BTN_ROJO);
        btnCerrar.setPreferredSize(new Dimension(220, 44));
        btnCerrar.addActionListener(e -> onCerrarDetalle.run());
        
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