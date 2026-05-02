package IniciarSesion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import utils.EstiloUI;

public class MenuDueno extends JFrame {

    public MenuDueno(
            Runnable onBack,
            Runnable onGestionarUsuarios,
            Runnable onHistorialVentas,
            Runnable onGenerarReportes
    ) {
        setTitle("El Chuta — Panel del dueño");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(EstiloUI.crearHeader("Panel del dueño", "Sesión: Dueño"), BorderLayout.NORTH);
        add(crearContenido(onGestionarUsuarios, onHistorialVentas, onGenerarReportes), BorderLayout.CENTER);
        add(crearBarraInferior(onBack), BorderLayout.SOUTH);
    }

    private JPanel crearContenido(
            Runnable onGestionarUsuarios,
            Runnable onHistorialVentas,
            Runnable onGenerarReportes
    ) {
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(EstiloUI.COLOR_FONDO);
        fondo.setBorder(new EmptyBorder(36, 56, 36, 56));

        // Encabezado de sección (kicker + título + subtítulo)
        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));

        JLabel kicker = EstiloUI.crearKicker("Centro de control");
        kicker.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = EstiloUI.crearTituloPantalla("Buenas tardes");
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = EstiloUI.crearSubtitulo(
                "Gestiona al personal, revisa el historial de ventas y genera reportes del negocio.");
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        encabezado.add(kicker);
        encabezado.add(Box.createVerticalStrut(8));
        encabezado.add(lblTitulo);
        encabezado.add(Box.createVerticalStrut(8));
        encabezado.add(lblSubtitulo);

        // Grid 1x3 con 3 tarjetas de acceso
        JPanel grid = new JPanel(new GridLayout(1, 3, 22, 0));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(28, 0, 0, 0));

        grid.add(crearTarjetaAcceso(
                "01",
                "Gestionar usuarios",
                "Altas, ediciones y bajas del personal del local.",
                "Abrir gestión",
                EstiloUI.COLOR_ACCION,
                onGestionarUsuarios));
        grid.add(crearTarjetaAcceso(
                "02",
                "Historial de ventas",
                "Consulta las ventas registradas y revisa cada detalle.",
                "Ver historial",
                EstiloUI.COLOR_SECUNDARIO,
                onHistorialVentas));
        grid.add(crearTarjetaAcceso(
                "03",
                "Generar reportes",
                "Resúmenes por periodo y exportación en PDF para el negocio.",
                "Generar reporte",
                EstiloUI.COLOR_BARRA,
                onGenerarReportes));

        fondo.add(encabezado, BorderLayout.NORTH);
        fondo.add(grid, BorderLayout.CENTER);
        return fondo;
    }

    private JPanel crearTarjetaAcceso(String numero, String titulo, String descripcion,
                                      String etiquetaBoton, Color colorBoton, Runnable accion) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(EstiloUI.COLOR_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, colorBoton),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 0, 1, 1, EstiloUI.COLOR_BORDE),
                        new EmptyBorder(28, 26, 26, 26))));

        JLabel lblNum = new JLabel(numero);
        lblNum.setFont(new Font(EstiloUI.FUENTE_PRECIO.getFamily(), Font.BOLD, 32));
        lblNum.setForeground(colorBoton);
        lblNum.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font(EstiloUI.FUENTE_SECCION.getFamily(), Font.BOLD, 22));
        lblTitulo.setForeground(EstiloUI.COLOR_TEXTO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel(
                "<html><div style='width:240px; line-height:140%;'>" + descripcion + "</div></html>");
        lblDescripcion.setFont(EstiloUI.FUENTE_SUBTITULO);
        lblDescripcion.setForeground(EstiloUI.COLOR_MUTED);
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton boton = EstiloUI.crearBoton(etiquetaBoton, colorBoton);
        boton.setPreferredSize(new Dimension(170, 42));
        boton.setMaximumSize(new Dimension(200, 42));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.addActionListener(e -> accion.run());

        panel.add(lblNum);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblDescripcion);
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalStrut(20));
        panel.add(boton);
        return panel;
    }

    private JPanel crearBarraInferior(Runnable onBack) {
        JButton btn = EstiloUI.crearBoton("Cerrar sesión", EstiloUI.COLOR_BARRA);
        btn.setPreferredSize(new Dimension(170, 42));
        btn.addActionListener(e -> onBack.run());
        return EstiloUI.crearBarraInferior(btn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MenuDueno(() -> {}, () -> {}, () -> {}, () -> {}).setVisible(true);
        });
    }
}
