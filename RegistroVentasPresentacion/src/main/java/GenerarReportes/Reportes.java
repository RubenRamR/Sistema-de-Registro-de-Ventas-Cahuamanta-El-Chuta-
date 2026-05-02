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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Reportes extends JFrame {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Color COLOR_BARRA    = utils.EstiloUI.COLOR_BARRA;
    private static final Color COLOR_FONDO    = utils.EstiloUI.COLOR_FONDO;
    private static final Color COLOR_TARJETA  = utils.EstiloUI.COLOR_TARJETA;
    private static final Color COLOR_TEXTO    = utils.EstiloUI.COLOR_TEXTO;
    private static final Color COLOR_MUTED    = utils.EstiloUI.COLOR_MUTED;
    private static final Color COLOR_BORDE    = utils.EstiloUI.COLOR_BORDE;
    private static final Color COLOR_ACCION   = utils.EstiloUI.COLOR_ACCION;
    private static final Color COLOR_DANGER   = utils.EstiloUI.COLOR_DANGER;
    private static final Color COLOR_AUXILIAR = utils.EstiloUI.COLOR_AUXILIAR;

    private final ReporteConsultaOperacion onConsultar;
    private final ReporteExportacionOperacion onExportar;
    private final DatePicker datePickerInicio = new DatePicker();
    private final DatePicker datePickerFin = new DatePicker();
    private DefaultTableModel modeloTabla;
    private final JLabel lblPeriodoValor = new JLabel("-");
    private final JLabel lblCantidadValor = new JLabel("0");
    private final JLabel lblTotalValor = new JLabel("$0.00");
    private final JLabel lblPromedioValor = new JLabel("$0.00");
    private JButton btnPdf;

    public Reportes(
            Runnable onBack,
            ReporteConsultaOperacion onConsultar,
            ReporteExportacionOperacion onExportar,
            ReporteVentasDTO reporteInicial
    ) {
        this.onConsultar = onConsultar;
        this.onExportar = onExportar;

        setTitle("Reportes de ventas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
        add(crearBarraInferior(onBack), BorderLayout.SOUTH);

        ReporteVentasDTO reporte = reporteInicial == null ? new ReporteVentasDTO() : reporteInicial;
        LocalDate hoy = LocalDate.now();
        datePickerInicio.setDate(reporte.getFechaInicio() == null ? hoy : reporte.getFechaInicio());
        datePickerFin.setDate(reporte.getFechaFin() == null ? hoy : reporte.getFechaFin());
        renderizarReporte(reporte);
    }

    private JPanel crearBarraSuperior() {
        return utils.EstiloUI.crearHeader("Panel de reportes", "Sesión: Dueño");
    }

    private JPanel crearContenido() {
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

        JLabel lblKicker = utils.EstiloUI.crearKicker("Reporte de ventas");
        lblKicker.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Reportes");
        lblTitulo.setFont(utils.EstiloUI.FUENTE_TITULO_PANTALLA);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Consulta, resume y exporta las ventas reales del periodo seleccionado.");
        lblSubtitulo.setFont(utils.EstiloUI.FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(COLOR_MUTED);
        lblSubtitulo.setAlignmentX(LEFT_ALIGNMENT);

        tarjeta.add(lblKicker);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createVerticalStrut(22));
        tarjeta.add(crearPanelFiltros());
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(crearPanelResumen());
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(crearTablaVentas());
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(crearPanelExportacion());

        fondo.add(tarjeta, BorderLayout.CENTER);
        return fondo;
    }

    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        panel.setBackground(COLOR_AUXILIAR);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JButton btnHoy = crearBoton("Hoy", COLOR_BARRA);
        btnHoy.addActionListener(e -> aplicarPreset(LocalDate.now(), LocalDate.now(), false));

        JButton btnSemana = crearBoton("Semana", COLOR_BARRA);
        btnSemana.addActionListener(e -> {
            LocalDate fin = LocalDate.now();
            aplicarPreset(fin.minusDays(6), fin, false);
        });

        JButton btnMes = crearBoton("Mes", COLOR_BARRA);
        btnMes.addActionListener(e -> {
            LocalDate fin = LocalDate.now();
            aplicarPreset(fin.withDayOfMonth(1), fin, false);
        });

        JButton btnConsultar = crearBoton("Consultar", COLOR_ACCION);
        btnConsultar.addActionListener(e -> consultarReporte(true));

        panel.add(crearEtiquetaSecundaria("Inicio"));
        panel.add(datePickerInicio);
        panel.add(crearEtiquetaSecundaria("Fin"));
        panel.add(datePickerFin);
        panel.add(btnHoy);
        panel.add(btnSemana);
        panel.add(btnMes);
        panel.add(btnConsultar);
        return panel;
    }

    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 16, 0));
        panel.setBackground(COLOR_TARJETA);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 118));

        panel.add(crearTarjetaResumen("Periodo", lblPeriodoValor));
        panel.add(crearTarjetaResumen("Ventas", lblCantidadValor));
        panel.add(crearTarjetaResumen("Monto total", lblTotalValor));
        panel.add(crearTarjetaResumen("Promedio", lblPromedioValor));
        return panel;
    }

    private JPanel crearTarjetaResumen(String titulo, JLabel valor) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(utils.EstiloUI.COLOR_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, utils.EstiloUI.COLOR_ACCION),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 0, 1, 1, COLOR_BORDE),
                        new EmptyBorder(16, 16, 16, 16))));

        JLabel lblTitulo = new JLabel(titulo.toUpperCase());
        lblTitulo.setFont(utils.EstiloUI.FUENTE_KICKER);
        lblTitulo.setForeground(COLOR_MUTED);

        valor.setFont(new Font(utils.EstiloUI.FUENTE_PRECIO.getFamily(), Font.BOLD, 24));
        valor.setForeground(COLOR_TEXTO);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(valor, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane crearTablaVentas() {
        String[] columnas = {"Fecha", "Venta", "Usuario", "Metodo", "Total"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(34);
        tabla.setFont(utils.EstiloUI.FUENTE_TABLA);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setSelectionBackground(COLOR_AUXILIAR);
        tabla.setSelectionForeground(COLOR_TEXTO);
        tabla.getTableHeader().setBackground(COLOR_BARRA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(utils.EstiloUI.FUENTE_TABLA_HEADER);
        tabla.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(900, 360));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));
        return scrollPane;
    }

    private JPanel crearPanelExportacion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.setBackground(COLOR_TARJETA);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        btnPdf = crearBoton("Descargar PDF", COLOR_DANGER);
        btnPdf.setPreferredSize(new Dimension(190, 46));
        btnPdf.addActionListener(e -> exportarPdf());
        
        btnPdf.setEnabled(false); 
        
        panel.add(btnPdf);
        return panel;
    }
    private JPanel crearBarraInferior(Runnable onBack) {
        JButton btnRegresar = utils.EstiloUI.crearBoton("←  Regresar", COLOR_BARRA);
        btnRegresar.setPreferredSize(new Dimension(160, 42));
        btnRegresar.addActionListener(e -> onBack.run());
        return utils.EstiloUI.crearBarraInferior(btnRegresar);
    }

    private JButton crearBoton(String texto, Color color) {
        return utils.EstiloUI.crearBoton(texto, color);
    }

    private JLabel crearEtiquetaSecundaria(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(utils.EstiloUI.FUENTE_ETIQUETA);
        label.setForeground(COLOR_TEXTO);
        return label;
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

        // --- LÓGICA DEL BOTÓN ---
        if (reporte.getVentas() == null || reporte.getVentas().isEmpty()) {
            if (btnPdf != null) {
                btnPdf.setEnabled(false);
            }
            return;
        }

        if (btnPdf != null) {
            btnPdf.setEnabled(true);
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
                    () -> {
                    },
                    (inicio, fin) -> reporte,
                    (inicio, fin, destino) -> {
                    },
                    reporte
            ).setVisible(true);
        });
    }
}
