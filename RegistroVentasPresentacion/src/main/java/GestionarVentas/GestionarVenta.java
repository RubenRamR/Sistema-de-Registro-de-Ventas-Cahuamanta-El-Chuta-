package GestionarVentas;

import dtos.VentaDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class GestionarVenta extends JFrame {
private final Color COLOR_BANNER = new Color(43, 90, 140);
    private final Color COLOR_FONDO_APP = new Color(245, 247, 250);
    private final Color COLOR_TITULO = new Color(30, 40, 60);
    private final Color COLOR_BTN_AGREGAR = new Color(40, 199, 111);
    private final Color COLOR_BTN_NARANJA = new Color(243, 156, 18);
    
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    
    private Runnable onBack;
    private Consumer<VentaDTO> onVerDetalles;

    // EL CONSTRUCTOR RECIBE LA LISTA
    public GestionarVenta(
            Runnable onAgregarVenta,
            Consumer<VentaDTO> onVerDetalles,
            Runnable onBack,
            List<VentaDTO> ventasDelDia
    ) {
        this.onBack = onBack;
        this.onVerDetalles = onVerDetalles;
        
        setTitle("Gestionar Venta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // --- Banners ---
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(COLOR_BANNER);
        panelNorte.setPreferredSize(new Dimension(0, 60));
        add(panelNorte, BorderLayout.NORTH);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelSur.setBackground(COLOR_BANNER);
        panelSur.setPreferredSize(new Dimension(0, 60));
        JButton btnRegresar = crearBotonRegresar();
        panelSur.add(btnRegresar);
        add(panelSur, BorderLayout.SOUTH);

        // --- Contenido ---
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBackground(COLOR_FONDO_APP);
        panelCentro.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel lblTitulo = new JLabel("Gestionar Venta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitulo.setForeground(COLOR_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Configuración de Tabla y Modelo ---
        String[] columnas = {"Id", "Fecha", "Total", "Metodo Pago", "Usuario", ""};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la celda del botón es editable
            }
        };

        tablaVentas = new JTable(modeloTabla);
        configurarEsteticaTabla();

        // Llenamos la tabla con la lista recibida
        llenarTabla(ventasDelDia);

        JScrollPane scrollTabla = new JScrollPane(tablaVentas);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());
        scrollTabla.getViewport().setBackground(Color.WHITE);

        // --- Botón Agregar ---
        JButton btnAgregar = new JButton("Agregar Nueva Venta");
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAgregar.setBackground(COLOR_BTN_AGREGAR);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setOpaque(true);
        btnAgregar.setContentAreaFilled(true);
        btnAgregar.setBorderPainted(false);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setPreferredSize(new Dimension(0, 55));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> {
            onAgregarVenta.run();
        });

        // --- Panel Marco ---
        JPanel panelCuadroVentas = new JPanel(new BorderLayout());
        panelCuadroVentas.setBackground(Color.WHITE);
        panelCuadroVentas.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1));
        panelCuadroVentas.setMaximumSize(new Dimension(900, 500));
        
        panelCuadroVentas.add(scrollTabla, BorderLayout.CENTER);
        panelCuadroVentas.add(btnAgregar, BorderLayout.SOUTH);

        panelCentro.add(lblTitulo);
        panelCentro.add(Box.createVerticalStrut(30));
        panelCentro.add(panelCuadroVentas);

        add(panelCentro, BorderLayout.CENTER);
    }

    // MÉTODO CLAVE: LLENAR LA TABLA DESDE LA LISTA
    private void llenarTabla(List<VentaDTO> ventas) {
        // 1. Limpiamos cualquier dato previo
        modeloTabla.setRowCount(0);

        // 2. Si la lista no es nula, la recorremos
        if (ventas != null) {
            for (VentaDTO venta : ventas) {
                // Creamos la fila. 
                // IMPORTANTE: El último elemento es el objeto 'venta' completo.
                Object[] fila = {
                    venta.getIdVenta(),
                    venta.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:")),//venta.getFechaFormateada(), // Supongamos que tienes este método en tu DTO
                    "$" + venta.getTotal(),
                    venta.getMetodoPago(),
                    venta.getUsuario().getNombre(),//venta.getUsuario().getNombre(),
                    venta // <-- Aquí metemos el DTO para el botón "Ver Detalle"
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    private void configurarEsteticaTabla() {
        tablaVentas.setRowHeight(45);
        tablaVentas.setShowVerticalLines(false);
        tablaVentas.setGridColor(new Color(235, 235, 235));
        
        // Render de botones naranja (usando el truco de paintComponent para evitar opacidad)
        tablaVentas.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tablaVentas.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), tablaVentas));

        // Centrar columnas de texto
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < 5; i++) {
            tablaVentas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JButton crearBotonRegresar() {
        JButton btn = new JButton("<");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(25, 60, 100));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(70, 60));
        btn.addActionListener(e -> {
            onBack.run();
        });
        return btn;
    }
    // ==========================================
    // CLASES PARA EL BOTÓN "VER DETALLE" (NARANJA)
    // ==========================================

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            // 1. Apagamos el pintado nativo del sistema operativo
            setContentAreaFilled(false); 
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setForeground(Color.WHITE);
        }

        // 2. Forzamos a Java a pintar nuestro color exacto
        @Override
        protected void paintComponent(java.awt.Graphics g) {
            g.setColor(COLOR_BTN_NARANJA);
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g); // Esto pinta el texto encima
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            // AQUÍ ESTABA EL ERROR: Antes decía value.toString()
            // Lo cambiamos para que SIEMPRE diga "Ver Detalle"
            setText("Ver Detalle");

            setBackground(COLOR_BTN_NARANJA);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean isPushed;
        private JTable tabla;

        // NUEVO: Variable para guardar el DTO de la fila que estamos clickeando
        private VentaDTO dtoSeleccionado;

        public ButtonEditor(JCheckBox checkBox, JTable tabla) {
            super(checkBox);
            this.tabla = tabla;

            button = new JButton() {
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    g.setColor(COLOR_BTN_NARANJA);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    super.paintComponent(g);
                }
            };

            button.setOpaque(true);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // NUEVO: La acción del botón ahora usa el DTO directamente
            button.addActionListener(e -> {
                if (dtoSeleccionado != null) {
                    onVerDetalles.accept(dtoSeleccionado);
                    // Aquí ya tienes todo el objeto. ¡Puedes pasarlo a tu siguiente pantalla!
                    //JOptionPane.showMessageDialog(button, "Abrir detalles del objeto con ID: " + dtoSeleccionado.getIdVenta());

                    // Ejemplo: new PantallaResumen(dtoSeleccionado).setVisible(true);
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {

            // Guardamos el DTO de forma invisible
            if (value instanceof VentaDTO) {
                this.dtoSeleccionado = (VentaDTO) value;
            }

            // Y forzamos el texto visual del botón
            button.setText("Ver Detalle");

            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return dtoSeleccionado; // Devolvemos el DTO en lugar del texto
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> {
            new GestionarVenta(
                    () -> {},
                    e -> {},
                    () -> {},
                    new ArrayList<>()
            ).setVisible(true);
        });
    }
}