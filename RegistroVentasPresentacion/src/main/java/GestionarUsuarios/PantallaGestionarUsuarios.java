package GestionarUsuarios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import utils.EstiloUI;

/**
 *
 * @author Daniel
 */
public class PantallaGestionarUsuarios extends JFrame {

    public PantallaGestionarUsuarios(
            Runnable onBack,
            Consumer<String> onOpcion
    ) {
        setTitle("Gestionar usuarios");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(EstiloUI.crearBarraSuperior("Gestionar usuarios"), BorderLayout.NORTH);
        add(crearContenido(onOpcion), BorderLayout.CENTER);
        add(EstiloUI.crearBarraInferior(EstiloUI.crearBotonRegresar(onBack)), BorderLayout.SOUTH);
    }

    private JPanel crearContenido(Consumer<String> onOpcion) {
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(EstiloUI.COLOR_FONDO);
        fondo.setBorder(new EmptyBorder(32, 40, 32, 40));

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(EstiloUI.COLOR_TARJETA);
        tarjeta.setBorder(EstiloUI.bordeTarjeta());

        JLabel lblTitulo = new JLabel("Gestionar usuarios");
        lblTitulo.setFont(EstiloUI.FUENTE_TITULO_PANTALLA);
        lblTitulo.setForeground(EstiloUI.COLOR_TEXTO);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Administra el personal del negocio");
        lblSubtitulo.setFont(EstiloUI.FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(EstiloUI.COLOR_MUTED);
        lblSubtitulo.setAlignmentX(LEFT_ALIGNMENT);

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createVerticalStrut(28));

        JPanel grid = new JPanel(new GridLayout(1, 3, 18, 0));
        grid.setBackground(EstiloUI.COLOR_TARJETA);
        grid.add(crearTarjetaOpcion(
                "Agregar usuario",
                "Da de alta un nuevo cajero o dueno.",
                EstiloUI.COLOR_ACCION,
                () -> onOpcion.accept("AGREGAR")
        ));
        grid.add(crearTarjetaOpcion(
                "Editar usuario",
                "Actualiza los datos de un usuario existente.",
                EstiloUI.COLOR_SECUNDARIO,
                () -> onOpcion.accept("EDITAR")
        ));
        grid.add(crearTarjetaOpcion(
                "Eliminar usuario",
                "Da de baja un usuario del sistema.",
                EstiloUI.COLOR_DANGER,
                () -> onOpcion.accept("ELIMINAR")
        ));

        tarjeta.add(grid);
        fondo.add(tarjeta, BorderLayout.CENTER);
        return fondo;
    }

    private JPanel crearTarjetaOpcion(String titulo, String descripcion, Color colorBoton, Runnable accion) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(249, 250, 248));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE, 1, true),
                new EmptyBorder(22, 20, 22, 20)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        lblTitulo.setForeground(EstiloUI.COLOR_TEXTO);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("<html><div style='width:220px'>" + descripcion + "</div></html>");
        lblDescripcion.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        lblDescripcion.setForeground(EstiloUI.COLOR_MUTED);
        lblDescripcion.setAlignmentX(LEFT_ALIGNMENT);

        JButton boton = new JButton("Abrir");
        boton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaGestionarUsuarios ventana = new PantallaGestionarUsuarios(
                    () -> {},
                    e -> {}
            );
            ventana.setVisible(true);
        });
    }
}
