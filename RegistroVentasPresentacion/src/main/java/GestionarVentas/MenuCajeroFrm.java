package GestionarVentas;

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
import javax.swing.JPanel;

public class MenuCajeroFrm extends JFrame {

    private final JLabel lblTitulo = new JLabel("Menu del cajero", JLabel.CENTER);
    private final JButton btnGestionarVentas = new JButton("Abrir punto de venta");
    private final JButton btnCerrarSesion = new JButton("Cerrar sesion");

    public MenuCajeroFrm() {
        setTitle("Menu cajero");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        construirVista();
    }

    private void construirVista() {
        JPanel header = new JPanel();
        header.setBackground(Color.decode("#2F6690"));
        header.setPreferredSize(new Dimension(0, 50));
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 15, 15, 15);

        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(Color.decode("#03045E"));
        gbc.gridy = 0;
        center.add(lblTitulo, gbc);

        btnGestionarVentas.setPreferredSize(new Dimension(280, 65));
        btnCerrarSesion.setPreferredSize(new Dimension(280, 65));
        gbc.gridy = 1;
        center.add(btnGestionarVentas, gbc);
        gbc.gridy = 2;
        center.add(btnCerrarSesion, gbc);

        add(center, BorderLayout.CENTER);
    }

    public void setNombreUsuario(String nombre) {
        lblTitulo.setText("Bienvenido, " + nombre);
    }

    public void addGestionarVentasListener(ActionListener listener) {
        btnGestionarVentas.addActionListener(listener);
    }

    public void addCerrarSesionListener(ActionListener listener) {
        btnCerrarSesion.addActionListener(listener);
    }
}
