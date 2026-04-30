package IniciarSesion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.BiConsumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import utils.EstiloUI;

/**
 *
 * @author Daniel
 */
public class PantallaLogin extends JFrame {

    public PantallaLogin(BiConsumer<String, String> onIniciarSesion) {
        setTitle("Iniciar Sesion");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(EstiloUI.crearBarraSuperior("Cahuamanta El Chuta"), BorderLayout.NORTH);
        add(crearContenido(onIniciarSesion), BorderLayout.CENTER);
        add(crearBarraInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearContenido(BiConsumer<String, String> onIniciarSesion) {
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBackground(EstiloUI.COLOR_FONDO);

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(EstiloUI.COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(40, 56, 40, 56)
        ));

        JLabel lblTitulo = new JLabel("Iniciar sesion");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(EstiloUI.COLOR_TEXTO);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Accede con tu usuario y contrasena");
        lblSubtitulo.setFont(EstiloUI.FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(EstiloUI.COLOR_MUTED);
        lblSubtitulo.setAlignmentX(CENTER_ALIGNMENT);

        JTextField txtUsuario = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createVerticalStrut(28));
        tarjeta.add(crearCampo("Usuario", txtUsuario));
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(crearCampo("Contrasena", txtContrasena));
        tarjeta.add(Box.createVerticalStrut(28));

        JButton btnIniciar = EstiloUI.crearBoton("Iniciar sesion", EstiloUI.COLOR_ACCION);
        btnIniciar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnIniciar.setPreferredSize(new Dimension(0, 46));
        btnIniciar.setAlignmentX(CENTER_ALIGNMENT);
        btnIniciar.addActionListener(e -> onIniciarSesion.accept(
                txtUsuario.getText(),
                new String(txtContrasena.getPassword())
        ));
        tarjeta.add(btnIniciar);

        tarjeta.setMaximumSize(new Dimension(520, 460));
        tarjeta.setPreferredSize(new Dimension(520, 460));

        fondo.add(tarjeta);
        return fondo;
    }

    private JPanel crearCampo(String etiqueta, JTextField campo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(EstiloUI.FUENTE_ETIQUETA);
        lbl.setForeground(EstiloUI.COLOR_TEXTO);
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        campo.setFont(EstiloUI.FUENTE_CAMPO);
        campo.setBorder(EstiloUI.bordeInput());
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campo.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        panel.add(campo);
        return panel;
    }

    private JPanel crearBarraInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(EstiloUI.COLOR_BARRA);
        panel.setPreferredSize(new Dimension(0, 56));

        JLabel pie = new JLabel("Sistema de registro de ventas");
        pie.setForeground(new Color(220, 230, 240));
        pie.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pie.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 0));
        panel.add(pie, BorderLayout.WEST);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaLogin ventana = new PantallaLogin((a, b) -> {});
            ventana.setVisible(true);
        });
    }
}
