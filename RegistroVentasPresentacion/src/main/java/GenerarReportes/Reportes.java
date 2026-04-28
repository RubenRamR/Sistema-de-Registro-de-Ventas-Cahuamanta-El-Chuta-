package GenerarReportes;

import aplicacion.ReporteConsultaOperacion;
import aplicacion.ReporteExportacionOperacion;
import com.github.lgooddatepicker.components.DatePicker;
import dtos.ReporteVentasDTO;
import dtos.UsuarioDTO;
import dtos.VentaDTO;
import excepciones.NegocioException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel
 */
public class Reportes extends JFrame {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Color COLOR_HEADER_FOOTER = new Color(65, 114, 159);
    private final Color COLOR_OSCURO = new Color(11, 19, 84);
    private final Color COLOR_BEIGE = new Color(248, 246, 240);
    private final Color COLOR_BORDE = new Color(220, 220, 215);
    private final Color COLOR_ROJO = new Color(178, 34, 34);

    private final ReporteConsultaOperacion onConsultar;
    private final ReporteExportacionOperacion onExportar;
    private final DatePicker datePickerInicio = new DatePicker();
    private final DatePicker datePickerFin = new DatePicker();
    private final DefaultTableModel modeloTabla;
    private final JLabel lblPeriodoValor = new JLabel("-");
    private final JLabel lblCantidadValor = new JLabel("0");
    private final JLabel lblTotalValor = new JLabel("$0.00");
    private final JLabel lblPromedioValor = new JLabel("$0.00");

    public Reportes(
            Runnable onBack,
            ReporteConsultaOperacion onConsultar,
            ReporteExportacionOperacion onExportar,
            ReporteVentasDTO reporteInicial
    ) {
        this.onConsultar = onConsultar;
        this.onExportar = onExportar;

        setTitle("Reportes");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_HEADER_FOOTER);
        headerPanel.setPreferredSize(new Dimension(800, 50));
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel lblTitulo = new JLabel("Reportes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblTitulo.setForeground(COLOR_OSCURO);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);

        centerPanel.add(lblTitulo);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(crearPanelFiltros());
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(crearPanelResumen());
        centerPanel.add(Box.createVerticalStrut(15));

        String[] columnas = {"Fecha", "Venta", "Usuario", "Metodo", "Total"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(32);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(900, 350));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 380));

        centerPanel.add(scrollPane);
        centerPanel.add(Box.createVerticalStrut(15));

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(Color.WHITE);
        JButton btnPdf = new JButton("Descargar PDF");
        btnPdf.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnPdf.setForeground(Color.WHITE);
        btnPdf.setBackground(COLOR_ROJO);
        btnPdf.setFocusPainted(false);
        btnPdf.setOpaque(true);
        btnPdf.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPdf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_ROJO.darker(), 1, true),
                new EmptyBorder(10, 30, 10, 30)
        ));
        btnPdf.addActionListener(e -> exportarPdf());
        panelBoton.add(btnPdf);
        centerPanel.add(panelBoton);

        add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        footerPanel.setBackground(COLOR_HEADER_FOOTER);
        footerPanel.setPreferredSize(new Dimension(800, 60));

        JButton btnRegresar = new JButton("<");
        btnRegresar.setFont(new Font("SansSerif", Font.BOLD, 24));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setBackground(COLOR_OSCURO);
        btnRegresar.setPreferredSize(new Dimension(60, 60));
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setOpaque(true);
        btnRegresar.addActionListener(e -> onBack.run());
        footerPanel.add(btnRegresar);
        add(footerPanel, BorderLayout.SOUTH);

        ReporteVentasDTO reporte = reporteInicial == null ? new ReporteVentasDTO() : reporteInicial;
        LocalDate hoy = LocalDate.now();
        datePickerInicio.setDate(reporte.getFechaInicio() == null ? hoy : reporte.getFechaInicio());
        datePickerFin.setDate(reporte.getFechaFin() == null ? hoy : reporte.getFechaFin());
        renderizarReporte(reporte);
    }

    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JButton btnHoy = new JButton("Hoy");
        btnHoy.addActionListener(e -> aplicarPreset(LocalDate.now(), LocalDate.now(), false));

        JButton btnSemana = new JButton("Semana");
        btnSemana.addActionListener(e -> {
            LocalDate fin = LocalDate.now();
            aplicarPreset(fin.minusDays(6), fin, false);
        });

        JButton btnMes = new JButton("Mes");
        btnMes.addActionListener(e -> {
            LocalDate fin = LocalDate.now();
            aplicarPreset(fin.withDayOfMonth(1), fin, false);
        });

        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(e -> consultarReporte(true));

        panel.add(new JLabel("Inicio:"));
        panel.add(datePickerInicio);
        panel.add(new JLabel("Fin:"));
        panel.add(datePickerFin);
        panel.add(btnHoy);
        panel.add(btnSemana);
        panel.add(btnMes);
        panel.add(btnConsultar);
        return panel;
    }

    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        panel.add(crearTarjetaResumen("Periodo", lblPeriodoValor));
        panel.add(crearTarjetaResumen("Ventas", lblCantidadValor));
        panel.add(crearTarjetaResumen("Monto Total", lblTotalValor));
        panel.add(crearTarjetaResumen("Promedio", lblPromedioValor));
        return panel;
    }

    private JPanel crearTarjetaResumen(String titulo, JLabel valor) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_OSCURO);

        valor.setFont(new Font("SansSerif", Font.PLAIN, 16));
        valor.setForeground(Color.DARK_GRAY);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(valor, BorderLayout.CENTER);
        return panel;
    }

    private void aplicarPreset(LocalDate inicio, LocalDate fin, boolean mostrarMensajeSinDatos) {
        datePickerInicio.setDate(inicio);
        datePickerFin.setDate(fin);
        consultarReporte(mostrarMensajeSinDatos);
    }

    private void consultarReporte(boolean mostrarMensajeSinDatos) {
        LocalDate fechaInicio = datePickerInicio.getDate();
        LocalDate fechaFin = datePickerFin.getDate();
        if (fechaInicio == null || fechaFin == null) {
            JOptionPane.showMessageDialog(this, "Selecciona la fecha inicial y final.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ReporteVentasDTO reporte = onConsultar.ejecutar(fechaInicio, fechaFin);
            renderizarReporte(reporte);
            if (mostrarMensajeSinDatos && (reporte.getVentas() == null || reporte.getVentas().isEmpty())) {
                JOptionPane.showMessageDialog(this, "No hay registros disponibles en el periodo seleccionado.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderizarReporte(ReporteVentasDTO reporte) {
        modeloTabla.setRowCount(0);

        LocalDate inicio = reporte.getFechaInicio();
        LocalDate fin = reporte.getFechaFin();
        lblPeriodoValor.setText(inicio == null || fin == null ? "-" : inicio.format(FORMATO_FECHA) + " - " + fin.format(FORMATO_FECHA));
        lblCantidadValor.setText(String.valueOf(reporte.getCantidadVentas()));
        lblTotalValor.setText("$" + formatearMonto(reporte.getMontoTotal()));
        lblPromedioValor.setText("$" + formatearMonto(reporte.getPromedioVenta()));

        if (reporte.getVentas() == null) {
            return;
        }

        for (VentaDTO venta : reporte.getVentas()) {
            modeloTabla.addRow(new Object[]{
                venta.getFechaHora() == null ? "Sin fecha" : venta.getFechaHora().format(FORMATO_FECHA_HORA),
                venta.getFolio() == null || venta.getFolio().isBlank() ? "#" + venta.getIdVenta() : venta.getFolio(),
                obtenerNombreUsuario(venta.getUsuario()),
                venta.getMetodoPago() == null ? "Sin metodo" : venta.getMetodoPago(),
                "$" + formatearMonto(venta.getTotal())
            });
        }
    }

    private void exportarPdf() {
        LocalDate fechaInicio = datePickerInicio.getDate();
        LocalDate fechaFin = datePickerFin.getDate();
        if (fechaInicio == null || fechaFin == null) {
            JOptionPane.showMessageDialog(this, "Selecciona la fecha inicial y final.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte PDF");
        chooser.setSelectedFile(new File("reporte-ventas-" + fechaInicio + "-" + fechaFin + ".pdf"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));

        int resultado = chooser.showSaveDialog(this);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File destino = chooser.getSelectedFile();
        if (!destino.getName().toLowerCase().endsWith(".pdf")) {
            destino = new File(destino.getParentFile(), destino.getName() + ".pdf");
        }

        try {
            onExportar.ejecutar(fechaInicio, fechaFin, destino);
            JOptionPane.showMessageDialog(this, "Reporte generado correctamente en:\n" + destino.getAbsolutePath(), "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerNombreUsuario(UsuarioDTO usuarioDTO) {
        return usuarioDTO == null || usuarioDTO.getNombre() == null ? "Sin usuario" : usuarioDTO.getNombre();
    }

    private String formatearMonto(BigDecimal monto) {
        return monto == null ? "0.00" : monto.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ReporteVentasDTO reporte = new ReporteVentasDTO();
            reporte.setFechaInicio(LocalDate.now());
            reporte.setFechaFin(LocalDate.now());
            new Reportes(
                    () -> {},
                    (inicio, fin) -> reporte,
                    (inicio, fin, destino) -> {},
                    reporte
            ).setVisible(true);
        });
    }
}
