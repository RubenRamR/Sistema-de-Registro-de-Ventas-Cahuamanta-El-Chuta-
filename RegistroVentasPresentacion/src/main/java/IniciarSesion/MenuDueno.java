package IniciarSesion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
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

public class MenuDueno extends JFrame {

    private static final Color COLOR_BARRA = new Color(18, 63, 94);
    private static final Color COLOR_FONDO = new Color(244, 247, 243);
    private static final Color COLOR_TARJETA = new Color(255, 255, 255);
    private static final Color COLOR_TEXTO = new Color(25, 41, 56);
    private static final Color COLOR_MUTED = new Color(92, 105, 117);
    private static final Color COLOR_BORDE = new Color(214, 222, 230);
    private static final Color COLOR_ACCION = new Color(28, 143, 124);
    private static final Color COLOR_SECUNDARIO = new Color(214, 124, 37);

    public MenuDueno(
            Runnable onBack,
            Runnable onGestionarUsuarios,
            Runnable onHistorialVentas,
            Runnable onGenerarReportes
    ) {
        setTitle("Menu Dueno");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearContenido(onGestionarUsuarios, onHistorialVentas, onGenerarReportes), BorderLayout.CENTER);
        add(crearBarraInferior(onBack), BorderLayout.SOUTH);
    }

    private JPanel crearBarraSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BARRA);
        panel.setPreferredSize(new Dimension(0, 74));

        JLabel lbl = new JLabel("Panel del dueno");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 0));
        panel.add(lbl, BorderLayout.WEST);
        return panel;
    }

    private JPanel crearContenido(
            Runnable onGestionarUsuarios,
            Runnable onHistorialVentas,
            Runnable onGenerarReportes
    ) {
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(COLOR_FONDO);
        fondo.setBorder(new EmptyBorder(32, 40, 32, 40));

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(34, 36, 34, 36)
        ));

        JLabel lblTitulo = new JLabel("Menu de opciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Accede rapido a las operaciones mas importantes del sistema");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblSubtitulo.setForeground(COLOR_MUTED);
        lblSubtitulo.setAlignmentX(LEFT_ALIGNMENT);

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createVerticalStrut(26));

        JPanel grid = new JPanel(new GridLayout(1, 3, 18, 0));
        grid.setBackground(COLOR_TARJETA);
        grid.add(crearTarjetaAcceso(
                "Gestionar usuarios",
                "Altas, ediciones y bajas del personal.",
                COLOR_ACCION,
                onGestionarUsuarios
        ));
        grid.add(crearTarjetaAcceso(
                "Historial de ventas",
                "Consulta ventas registradas y revisa sus detalles.",
                COLOR_SECUNDARIO,
                onHistorialVentas
        ));
        grid.add(crearTarjetaAcceso(
                "Generar reportes",
                "Resumenes por periodo y exportacion en PDF.",
                COLOR_BARRA,
                onGenerarReportes
        ));

        tarjeta.add(grid);
        fondo.add(tarjeta, BorderLayout.CENTER);
        return fondo;
    }

    private JPanel crearTarjetaAcceso(String titulo, String descripcion, Color colorBoton, Runnable accion) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(249, 250, 248));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(22, 20, 22, 20)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("<html><div style='width:220px'>" + descripcion + "</div></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblDescripcion.setForeground(COLOR_MUTED);
        lblDescripcion.setAlignmentX(LEFT_ALIGNMENT);

        JButton boton = new JButton("Abrir");
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorBoton);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(140, 42));
        boton.setMaximumSize(new Dimension(140, 42));
        boton.setAlignmentX(LEFT_ALIGNMENT);
        boton.addActionListener(e -> accion.run());

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblDescripcion);
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalStrut(18));
        panel.add(boton);
        return panel;
    }

    private JPanel crearBarraInferior(Runnable onBack) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 10));
        panel.setBackground(COLOR_BARRA);
        panel.setPreferredSize(new Dimension(0, 72));

        JButton btnRegresar = new JButton("Cerrar sesion");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setBackground(new Color(12, 38, 57));
        btnRegresar.setOpaque(true);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.setPreferredSize(new Dimension(150, 46));
        btnRegresar.addActionListener(e -> onBack.run());
        panel.add(btnRegresar);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MenuDueno(
                    () -> {
                    },
                    () -> {
                    },
                    () -> {
                    },
                    () -> {
                    }
            ).setVisible(true);
        });
    }
}
