package IniciarSesion;

import controladores.LoginController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PantallaLogin extends JFrame {

    private final JTextField txtUsuario = new JTextField(20);
    private final JPasswordField txtContrasena = new JPasswordField(20);
    private final JButton btnIniciarSesion = new JButton("Iniciar sesion");

    public PantallaLogin() {
        setTitle("Inicio de sesion");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        construirVista();
    }

    private void construirVista() {
        Color azul = Color.decode("#336690");
        Color azulOscuro = Color.decode("#090060");
        Color fondo = Color.decode("#F5F4EE");

        JPanel header = new JPanel();
        header.setBackground(azul);
        header.setPreferredSize(new Dimension(0, 50));
        add(header, BorderLayout.NORTH);

        JPanel footer = new JPanel();
        footer.setBackground(azul);
        footer.setPreferredSize(new Dimension(0, 50));
        add(footer, BorderLayout.SOUTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titulo = new JLabel("Inicio de sesion");
        titulo.setFont(new Font("Arial", Font.BOLD, 38));
        titulo.setForeground(azulOscuro);
        gbc.gridy = 0;
        center.add(titulo, gbc);

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        formulario.setBackground(fondo);
        formulario.setPreferredSize(new Dimension(420, 220));

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 18));
        formulario.add(lblUsuario);
        formulario.add(txtUsuario);
        formulario.add(Box.createVerticalStrut(15));

        JLabel lblContrasena = new JLabel("Contrasenia");
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 18));
        formulario.add(lblContrasena);
        formulario.add(txtContrasena);
        formulario.add(Box.createVerticalStrut(20));

        btnIniciarSesion.setBackground(Color.decode("#007ACC"));
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setFont(new Font("Arial", Font.BOLD, 16));
        btnIniciarSesion.setAlignmentX(CENTER_ALIGNMENT);
        formulario.add(btnIniciarSesion);

        gbc.gridy = 1;
        center.add(formulario, gbc);
        add(center, BorderLayout.CENTER);
    }

    public String getNombreUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getContrasenia() {
        return new String(txtContrasena.getPassword()).trim();
    }

    public void addLoginListener(ActionListener listener) {
        btnIniciarSesion.addActionListener(listener);
        txtContrasena.addActionListener(listener);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaLogin vista = new PantallaLogin();
            new LoginController(vista);
            vista.setVisible(true);
        });
    }
}
