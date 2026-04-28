package GestionarVentas;

import com.github.lgooddatepicker.components.DatePicker;
import dtos.UsuarioDTO;
import dtos.VentaDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
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
import javax.swing.table.TableCellRenderer;

public class GestionarVenta extends JFrame {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final Color COLOR_BANNER = new Color(43, 90, 140);
    private final Color COLOR_FONDO_APP = new Color(245, 247, 250);
    private final Color COLOR_TITULO = new Color(30, 40, 60);
    private final Color COLOR_BTN_AGREGAR = new Color(40, 199, 111);
    private final Color COLOR_BTN_NARANJA = new Color(243, 156, 18);

    private final Runnable onAgregarVenta;
    private final Runnable onBack;
    private final Consumer<VentaDTO> onVerDetalles;
    private final Function<LocalDate, List<VentaDTO>> onFiltrarVentas;
    private final boolean mostrarBotonAgregar;
    private final String tituloPantalla;
    private final LocalDate fechaInicialFiltro;

    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    private DatePicker datePickerFiltro;

    public GestionarVenta(
            Runnable onAgregarVenta,
            Consumer<VentaDTO> onVerDetalles,
            Runnable onBack,
            List<VentaDTO> ventasDelDia
    ) {
        this("Gestionar Venta", true, onAgregarVenta, onVerDetalles, onBack, ventasDelDia, null, LocalDate.now());
    }

    public GestionarVenta(
            String tituloPantalla,
            boolean mostrarBotonAgregar,
            Runnable onAgregarVenta,
            Consumer<VentaDTO> onVerDetalles,
            Runnable onBack,
            List<VentaDTO> ventasIniciales,
            Function<LocalDate, List<VentaDTO>> onFiltrarVentas,
            LocalDate fechaInicialFiltro
    ) {
        this.tituloPantalla = tituloPantalla;
        this.mostrarBotonAgregar = mostrarBotonAgregar;
        this.onAgregarVenta = onAgregarVenta;
        this.onVerDetalles = onVerDetalles;
        this.onBack = onBack;
        this.onFiltrarVentas = onFiltrarVentas;
        this.fechaInicialFiltro = fechaInicialFiltro;

        setTitle(tituloPantalla);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(COLOR_BANNER);
        panelNorte.setPreferredSize(new Dimension(0, 60));
        add(panelNorte, BorderLayout.NORTH);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelSur.setBackground(COLOR_BANNER);
        panelSur.setPreferredSize(new Dimension(0, 60));
        panelSur.add(crearBotonRegresar());
        add(panelSur, BorderLayout.SOUTH);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBackground(COLOR_FONDO_APP);
        panelCentro.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel lblTitulo = new JLabel(tituloPantalla);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitulo.setForeground(COLOR_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentro.add(lblTitulo);
        panelCentro.add(Box.createVerticalStrut(20));

        JPanel panelFiltros = crearPanelFiltros();
        if (panelFiltros != null) {
            panelCentro.add(panelFiltros);
            panelCentro.add(Box.createVerticalStrut(20));
        }

        String[] columnas = {"Id", "Fecha", "Total", "Metodo Pago", "Usuario", ""};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        tablaVentas = new JTable(modeloTabla);
        configurarEsteticaTabla();
        llenarTabla(ventasIniciales);

        JScrollPane scrollTabla = new JScrollPane(tablaVentas);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());
        scrollTabla.getViewport().setBackground(Color.WHITE);

        JPanel panelCuadroVentas = new JPanel(new BorderLayout());
        panelCuadroVentas.setBackground(Color.WHITE);
        panelCuadroVentas.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1));
        panelCuadroVentas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 560));
        panelCuadroVentas.add(scrollTabla, BorderLayout.CENTER);

        if (mostrarBotonAgregar) {
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
                if (this.onAgregarVenta != null) {
                    this.onAgregarVenta.run();
                }
            });
            panelCuadroVentas.add(btnAgregar, BorderLayout.SOUTH);
        }

        panelCentro.add(panelCuadroVentas);
        add(panelCentro, BorderLayout.CENTER);
    }

    private JPanel crearPanelFiltros() {
        if (onFiltrarVentas == null) {
            return null;
        }

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelFiltros.setBackground(COLOR_FONDO_APP);
        panelFiltros.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFiltros.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFecha.setForeground(COLOR_TITULO);

        datePickerFiltro = new DatePicker();
        datePickerFiltro.setDate(fechaInicialFiltro == null ? LocalDate.now() : fechaInicialFiltro);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> cargarVentasFiltradas(datePickerFiltro.getDate(), true));

        JButton btnHoy = new JButton("Hoy");
        btnHoy.addActionListener(e -> {
            LocalDate hoy = LocalDate.now();
            datePickerFiltro.setDate(hoy);
            cargarVentasFiltradas(hoy, false);
        });

        panelFiltros.add(lblFecha);
        panelFiltros.add(datePickerFiltro);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnHoy);
        return panelFiltros;
    }

    private void cargarVentasFiltradas(LocalDate fecha, boolean mostrarMensajeSinDatos) {
        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una fecha para filtrar las ventas.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<VentaDTO> ventas = onFiltrarVentas.apply(fecha);
        llenarTabla(ventas);
        if (mostrarMensajeSinDatos && (ventas == null || ventas.isEmpty())) {
            JOptionPane.showMessageDialog(this, "No hay ventas registradas para la fecha seleccionada.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void llenarTabla(List<VentaDTO> ventas) {
        modeloTabla.setRowCount(0);
        if (ventas == null) {
            return;
        }

        for (VentaDTO venta : ventas) {
            Object[] fila = {
                venta.getIdVenta(),
                venta.getFechaHora() == null ? "Sin fecha" : venta.getFechaHora().format(FORMATO_FECHA),
                "$" + (venta.getTotal() == null ? "0.00" : venta.getTotal()),
                venta.getMetodoPago() == null ? "Sin metodo" : venta.getMetodoPago(),
                obtenerNombreUsuario(venta.getUsuario()),
                venta
            };
            modeloTabla.addRow(fila);
        }
    }

    private void configurarEsteticaTabla() {
        tablaVentas.setRowHeight(45);
        tablaVentas.setShowVerticalLines(false);
        tablaVentas.setGridColor(new Color(235, 235, 235));
        tablaVentas.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tablaVentas.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

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
        btn.addActionListener(e -> onBack.run());
        return btn;
    }

    private String obtenerNombreUsuario(UsuarioDTO usuarioDTO) {
        return usuarioDTO == null || usuarioDTO.getNombre() == null ? "Sin usuario" : usuarioDTO.getNombre();
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setForeground(Color.WHITE);
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            g.setColor(COLOR_BTN_NARANJA);
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Ver Detalle");
            setBackground(COLOR_BTN_NARANJA);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private VentaDTO dtoSeleccionado;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
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
            button.addActionListener(e -> {
                if (dtoSeleccionado != null) {
                    onVerDetalles.accept(dtoSeleccionado);
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value instanceof VentaDTO ventaDTO) {
                this.dtoSeleccionado = ventaDTO;
            }
            button.setText("Ver Detalle");
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return dtoSeleccionado;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new GestionarVenta(
                    "Historial de Ventas",
                    false,
                    null,
                    venta -> {},
                    () -> {},
                    new ArrayList<>(),
                    fecha -> new ArrayList<>(),
                    LocalDate.now()
            ).setVisible(true);
        });
    }
}
