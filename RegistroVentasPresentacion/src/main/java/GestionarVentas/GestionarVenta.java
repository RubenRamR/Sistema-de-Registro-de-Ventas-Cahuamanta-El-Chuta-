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
    private static final Color COLOR_BARRA = new Color(18, 63, 94);
    private static final Color COLOR_FONDO = new Color(244, 247, 243);
    private static final Color COLOR_TARJETA = new Color(255, 255, 255);
    private static final Color COLOR_TEXTO = new Color(25, 41, 56);
    private static final Color COLOR_MUTED = new Color(92, 105, 117);
    private static final Color COLOR_BORDE = new Color(214, 222, 230);
    private static final Color COLOR_AGREGAR = new Color(28, 143, 124);
    private static final Color COLOR_DETALLE = new Color(214, 124, 37);
    private static final Color COLOR_BLOQUE = new Color(223, 235, 229);

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
    private JLabel lblResumen;

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

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearContenido(ventasIniciales), BorderLayout.CENTER);
        add(crearBarraInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearBarraSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BARRA);
        panel.setPreferredSize(new Dimension(0, 74));

        JLabel lbl = new JLabel(mostrarBotonAgregar ? "Ventas activas" : "Consulta de ventas");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 0));
        panel.add(lbl, BorderLayout.WEST);
        return panel;
    }

    private JPanel crearContenido(List<VentaDTO> ventasIniciales) {
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(COLOR_FONDO);
        fondo.setBorder(new EmptyBorder(28, 36, 28, 36));

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(30, 32, 30, 32)
        ));

        JLabel lblTitulo = new JLabel(tituloPantalla);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel(mostrarBotonAgregar
                ? "Consulta ventas del dia o registra una nueva desde el mismo flujo"
                : "Revisa el historial del dia seleccionado y abre el detalle cuando lo necesites");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblSubtitulo.setForeground(COLOR_MUTED);
        lblSubtitulo.setAlignmentX(LEFT_ALIGNMENT);

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createVerticalStrut(18));

        JPanel filtros = crearPanelFiltros();
        if (filtros != null) {
            tarjeta.add(filtros);
            tarjeta.add(Box.createVerticalStrut(16));
        }

        lblResumen = new JLabel();
        lblResumen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblResumen.setForeground(COLOR_TEXTO);
        lblResumen.setOpaque(true);
        lblResumen.setBackground(COLOR_BLOQUE);
        lblResumen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        lblResumen.setAlignmentX(LEFT_ALIGNMENT);
        tarjeta.add(lblResumen);
        tarjeta.add(Box.createVerticalStrut(16));

        crearTabla();
        llenarTabla(ventasIniciales);

        JScrollPane scrollTabla = new JScrollPane(tablaVentas);
        scrollTabla.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1, true));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setAlignmentX(LEFT_ALIGNMENT);
        scrollTabla.setPreferredSize(new Dimension(900, 420));
        scrollTabla.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        tarjeta.add(scrollTabla);

        if (mostrarBotonAgregar) {
            tarjeta.add(Box.createVerticalStrut(18));
            JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            acciones.setBackground(COLOR_TARJETA);
            acciones.setAlignmentX(LEFT_ALIGNMENT);

            JButton btnAgregar = crearBoton("Agregar nueva venta", COLOR_AGREGAR);
            btnAgregar.setPreferredSize(new Dimension(220, 46));
            btnAgregar.addActionListener(e -> {
                if (this.onAgregarVenta != null) {
                    this.onAgregarVenta.run();
                }
            });
            acciones.add(btnAgregar);
            tarjeta.add(acciones);
        }

        fondo.add(tarjeta, BorderLayout.CENTER);
        return fondo;
    }

    private JPanel crearPanelFiltros() {
        if (onFiltrarVentas == null) {
            return null;
        }

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        panelFiltros.setBackground(COLOR_BLOQUE);
        panelFiltros.setAlignmentX(LEFT_ALIGNMENT);
        panelFiltros.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
        panelFiltros.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel lblFecha = new JLabel("Fecha");
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFecha.setForeground(COLOR_TEXTO);

        datePickerFiltro = new DatePicker();
        datePickerFiltro.setDate(fechaInicialFiltro == null ? LocalDate.now() : fechaInicialFiltro);

        JButton btnFiltrar = crearBoton("Filtrar", COLOR_BARRA);
        btnFiltrar.addActionListener(e -> cargarVentasFiltradas(datePickerFiltro.getDate(), true));

        JButton btnHoy = crearBoton("Hoy", COLOR_AGREGAR);
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

    private void crearTabla() {
        String[] columnas = {"Id", "Fecha", "Total", "Metodo Pago", "Usuario", ""};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        tablaVentas = new JTable(modeloTabla);
        tablaVentas.setRowHeight(38);
        tablaVentas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaVentas.setForeground(COLOR_TEXTO);
        tablaVentas.setShowHorizontalLines(true);
        tablaVentas.setGridColor(new Color(234, 238, 242));
        tablaVentas.setSelectionBackground(new Color(236, 243, 248));
        tablaVentas.setSelectionForeground(COLOR_TEXTO);
        tablaVentas.getTableHeader().setBackground(COLOR_BARRA);
        tablaVentas.getTableHeader().setForeground(Color.WHITE);
        tablaVentas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaVentas.getTableHeader().setReorderingAllowed(false);
        tablaVentas.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tablaVentas.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < 5; i++) {
            tablaVentas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JPanel crearBarraInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 10));
        panel.setBackground(COLOR_BARRA);
        panel.setPreferredSize(new Dimension(0, 72));

        JButton btn = crearBoton("Regresar", new Color(12, 38, 57));
        btn.setPreferredSize(new Dimension(150, 46));
        btn.addActionListener(e -> onBack.run());
        panel.add(btn);
        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
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
        List<VentaDTO> ventasSeguras = ventas == null ? List.of() : ventas;

        for (VentaDTO venta : ventasSeguras) {
            modeloTabla.addRow(new Object[]{
                venta.getIdVenta(),
                venta.getFechaHora() == null ? "Sin fecha" : venta.getFechaHora().format(FORMATO_FECHA),
                "$" + (venta.getTotal() == null ? "0.00" : venta.getTotal()),
                venta.getMetodoPago() == null ? "Sin metodo" : venta.getMetodoPago(),
                obtenerNombreUsuario(venta.getUsuario()),
                venta
            });
        }

        String fechaResumen = datePickerFiltro == null || datePickerFiltro.getDate() == null
                ? "la fecha seleccionada"
                : datePickerFiltro.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        lblResumen.setText("Ventas encontradas: " + ventasSeguras.size() + " para " + fechaResumen);
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
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setForeground(Color.WHITE);
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            g.setColor(COLOR_DETALLE);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            super.paintComponent(g);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Ver detalle");
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
                    g.setColor(COLOR_DETALLE);
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    super.paintComponent(g);
                }
            };
            button.setOpaque(true);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
            button.setText("Ver detalle");
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
                    venta -> {
                    },
                    () -> {
                    },
                    new ArrayList<>(),
                    fecha -> new ArrayList<>(),
                    LocalDate.now()
            ).setVisible(true);
        });
    }
}
