package IniciarSesion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PantallaGestionarUsuarios extends JFrame {

    private final JLabel lblBienvenida = new JLabel("Gestion de usuarios", JLabel.CENTER);
    private final JButton btnAdministrar = new JButton("Gestionar usuarios");
    private final JButton btnPuntoVenta = new JButton("Abrir punto de venta");
    private final JButton btnCerrarSesion = new JButton("Cerrar sesion");

    public PantallaGestionarUsuarios() {
        setTitle("Panel del dueno");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        construirVista();
    }

    private void construirVista() {
        JPanel header = new JPanel();
        header.setBackground(Color.decode("#336690"));
        header.setPreferredSize(new Dimension(0, 50));
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 34));
        lblBienvenida.setForeground(Color.decode("#090060"));
        gbc.gridy = 0;
        center.add(lblBienvenida, gbc);

        btnAdministrar.setPreferredSize(new Dimension(280, 60));
        btnPuntoVenta.setPreferredSize(new Dimension(280, 60));
        btnCerrarSesion.setPreferredSize(new Dimension(280, 60));

        gbc.gridy = 1;
        center.add(btnAdministrar, gbc);
        gbc.gridy = 2;
        center.add(btnPuntoVenta, gbc);
        gbc.gridy = 3;
        center.add(btnCerrarSesion, gbc);

        add(center, BorderLayout.CENTER);
    }

    public void setNombreUsuario(String nombre) {
        lblBienvenida.setText("Bienvenido, " + nombre + " (dueno)");
    }

    public void addAccionUsuariosListener(ActionListener listener) {
        btnAdministrar.addActionListener(listener);
    }

    public void addAbrirPuntoVentaListener(ActionListener listener) {
        btnPuntoVenta.addActionListener(listener);
    }

    public void addCerrarSesionListener(ActionListener listener) {
        btnCerrarSesion.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
