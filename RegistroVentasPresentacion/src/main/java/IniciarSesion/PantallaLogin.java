package IniciarSesion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author MiCuenta
 */
public class PantallaLogin extends JFrame {

    public PantallaLogin() {
        setTitle("Iniciar Sesión");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla al abrir
        setLayout(new BorderLayout());

        // Colores
        Color colorAzulFranja = Color.decode("#336690");
        Color colorAzulOscuroTitulo = Color.decode("#090060");
        Color colorFondoBeige = Color.decode("#F5F4EE");
        Color colorBotonIniciar = Color.decode("#007ACC");
        Color colorBotonAtras = Color.decode("#00005C");

        // HEADER
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(colorAzulFranja);
        panelSuperior.setPreferredSize(new Dimension(0, 50)); // El ancho 0 hace que se adapte al contenedor
        add(panelSuperior, BorderLayout.NORTH);

        // FOOTER
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelInferior.setBackground(colorAzulFranja);
        panelInferior.setPreferredSize(new Dimension(0, 60));

        // Boton retroceso
        JButton btnAtras = new JButton("<");
        btnAtras.setPreferredSize(new Dimension(60, 60));
        btnAtras.setBackground(colorBotonAtras);
        btnAtras.setForeground(Color.WHITE);
        btnAtras.setFont(new Font("Arial", Font.BOLD, 24));
        btnAtras.setFocusPainted(false);
        btnAtras.setBorder(null);
        panelInferior.add(btnAtras);
        
        add(panelInferior, BorderLayout.SOUTH);

        // CENTRO
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Columna 0
        gbc.anchor = GridBagConstraints.CENTER; // Anclado al centro

        // Título "Iniciar Sesión"
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 46));
        lblTitulo.setForeground(colorAzulOscuroTitulo);
        
        gbc.gridy = 0; // Fila 0
        gbc.insets = new Insets(0, 0, 40, 0); 
        panelCentral.add(lblTitulo, gbc);

        // Recuadro Beige del Formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(null);
        panelFormulario.setBackground(colorFondoBeige);
        panelFormulario.setPreferredSize(new Dimension(560, 300));

        // Etiqueta Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 18));
        lblUsuario.setBounds(150, 40, 260, 25);
        panelFormulario.add(lblUsuario);

        // Campo de texto Usuario
        JTextField txtUsuario = new JTextField(); 
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsuario.setBounds(150, 70, 260, 35);
        panelFormulario.add(txtUsuario);

        // Etiqueta Contraseña
        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 18));
        lblContrasena.setBounds(150, 130, 260, 25);
        panelFormulario.add(lblContrasena);

        // Campo de Contraseña
        JPasswordField txtContrasena = new JPasswordField(); 
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 16));
        txtContrasena.setBounds(150, 160, 260, 35);
        panelFormulario.add(txtContrasena);

        // Botón Iniciar Sesión
        JButton btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setBackground(colorBotonIniciar);
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setFont(new Font("Arial", Font.PLAIN, 14));
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setBounds(180, 230, 200, 40);
        btnIniciarSesion.setBorder(BorderFactory.createEmptyBorder()); 
        panelFormulario.add(btnIniciarSesion);

        // Añadir el recuadro beige al panel central en la fila 1
        gbc.gridy = 1; 
        gbc.insets = new Insets(0, 0, 0, 0);
        panelCentral.add(panelFormulario, gbc);

        // Añadir el panel central a la ventana principal
        add(panelCentral, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaLogin ventana = new PantallaLogin();
            ventana.setVisible(true);
        });
    }
}
