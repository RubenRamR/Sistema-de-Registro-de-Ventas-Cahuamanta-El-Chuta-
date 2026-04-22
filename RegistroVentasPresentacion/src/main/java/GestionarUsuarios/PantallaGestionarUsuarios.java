package GestionarUsuarios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Daniel
 */
public class PantallaGestionarUsuarios extends JFrame {

    public PantallaGestionarUsuarios(
            Runnable onBack
    ) {
        setTitle("Gestionar Usuarios");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Colores
        Color colorAzulFranja = Color.decode("#336690");
        Color colorAzulOscuroTitulo = Color.decode("#090060");
        Color colorFondoBeige = Color.decode("#F5F4EE");
        Color colorBotonAccion = Color.decode("#007ACC");
        Color colorBotonAtras = Color.decode("#00005C");

        // HEADER
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(colorAzulFranja);
        panelSuperior.setPreferredSize(new Dimension(0, 50));
        add(panelSuperior, BorderLayout.NORTH);

        // FOOTER
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelInferior.setBackground(colorAzulFranja);
        panelInferior.setPreferredSize(new Dimension(0, 60));

        // Botón de retroceso
        JButton btnAtras = new JButton("<");
        btnAtras.setPreferredSize(new Dimension(60, 60));
        btnAtras.setBackground(colorBotonAtras);
        btnAtras.setForeground(Color.WHITE);
        btnAtras.setFont(new Font("Arial", Font.BOLD, 24));
        btnAtras.setFocusPainted(false);
        btnAtras.setBorder(null);
        btnAtras.addActionListener(e -> {
            onBack.run();
        });
        panelInferior.add(btnAtras);
        
        add(panelInferior, BorderLayout.SOUTH);

        // CENTRO
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Título "Gestionar Usuarios"
        JLabel lblTitulo = new JLabel("Gestionar Usuarios");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 46));
        lblTitulo.setForeground(colorAzulOscuroTitulo);
        
        gbc.gridy = 0; 
        gbc.insets = new Insets(0, 0, 40, 0); 
        panelCentral.add(lblTitulo, gbc);

        // Recuadro Beige de las Opciones
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(null);
        panelOpciones.setBackground(colorFondoBeige);
        panelOpciones.setPreferredSize(new Dimension(640, 320)); 

        // Configuración de fuente general para los botones
        Font fuenteBotones = new Font("Arial", Font.PLAIN, 18);

        // Botón: Agregar Usuario
        JButton btnAgregar = new JButton("Agregar Usuario");
        btnAgregar.setBackground(colorBotonAccion);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(fuenteBotones);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBounds(60, 60, 240, 80); // (x, y, ancho, alto)
        panelOpciones.add(btnAgregar);

        // Botón: Eliminar Usuario
        JButton btnEliminar = new JButton("Eliminar Usuario");
        btnEliminar.setBackground(colorBotonAccion);
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(fuenteBotones);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBounds(340, 60, 240, 80);
        panelOpciones.add(btnEliminar);

        // Botón: Editar Usuario
        JButton btnEditar = new JButton("Editar Usuario");
        btnEditar.setBackground(colorBotonAccion);
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(fuenteBotones);
        btnEditar.setFocusPainted(false);
        btnEditar.setBounds(200, 180, 240, 80);
        panelOpciones.add(btnEditar);

        // Añadir recuadro beige al panel central principal
        gbc.gridy = 1; 
        gbc.insets = new Insets(0, 0, 0, 0); 
        panelCentral.add(panelOpciones, gbc);

        // Añadir la sección central a la ventana principal
        add(panelCentral, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaGestionarUsuarios ventana = new PantallaGestionarUsuarios(() -> {});
            ventana.setVisible(true);
        });
    }
}