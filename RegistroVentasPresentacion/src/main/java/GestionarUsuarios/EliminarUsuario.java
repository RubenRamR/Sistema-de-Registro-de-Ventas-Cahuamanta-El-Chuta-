package GestionarUsuarios;

import dtos.UsuarioDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Daniel
 */
public class EliminarUsuario extends JFrame {

    // --- Paleta de Colores ---
    private final Color COLOR_HEADER_FOOTER = new Color(65, 114, 159);
    private final Color COLOR_OSCURO = new Color(11, 19, 84);
    private final Color COLOR_BEIGE = new Color(248, 246, 240);
    private final Color COLOR_BORDE = new Color(220, 220, 215);
    private final Color COLOR_ROJO = new Color(190, 50, 50); // Rojo para el botón eliminar

    private UsuarioDTO usuario;
    
    public EliminarUsuario(
            UsuarioDTO usuario,
            Runnable onBack
    ) {
        this.usuario = usuario;
        
        setTitle("Eliminar Usuario");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- ENCABEZADO ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_HEADER_FOOTER);
        headerPanel.setPreferredSize(new Dimension(800, 50));
        add(headerPanel, BorderLayout.NORTH);

        // --- CONTENIDO CENTRAL ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.anchor = GridBagConstraints.CENTER;

        // 1. Título
        JLabel lblTitulo = new JLabel("Eliminar Usuario");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblTitulo.setForeground(COLOR_OSCURO);
        
        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(0, 0, 40, 0); // Espacio debajo del título
        centerPanel.add(lblTitulo, gbcMain);

        // 2. Tarjeta de Información (Recuadro Beige)
        JPanel cardPanel = crearTarjetaInformacion("Usuario1", "Cajero");
        
        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(cardPanel, gbcMain);

        add(centerPanel, BorderLayout.CENTER);

        // --- PIE DE PÁGINA ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(COLOR_HEADER_FOOTER);
        footerPanel.setPreferredSize(new Dimension(800, 60));
        footerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JButton btnRegresar = new JButton("<");
        btnRegresar.setFont(new Font("SansSerif", Font.BOLD, 24));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setBackground(COLOR_OSCURO);
        btnRegresar.setPreferredSize(new Dimension(60, 60));
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setOpaque(true);
        btnRegresar.addActionListener(e -> {
            onBack.run();
        });
        footerPanel.add(btnRegresar);

        add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Construye el panel beige con la información del usuario y el botón
     */
    private JPanel crearTarjetaInformacion(String nombre, String rol) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BEIGE);
        
        // Borde sutil y padding interno amplio
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(50, 80, 50, 80)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Espacio entre los elementos de la cuadrícula
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Nombre de Usuario
        JLabel lblNombreLabel = new JLabel("Nombre de Usuario:");
        lblNombreLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblNombreLabel.setForeground(Color.BLACK);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; // Alinear a la derecha
        panel.add(lblNombreLabel, gbc);

        JLabel lblNombreValor = new JLabel(nombre);
        lblNombreValor.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblNombreValor.setForeground(Color.DARK_GRAY);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // Alinear a la izquierda
        panel.add(lblNombreValor, gbc);

        // Fila 2: Rol
        JLabel lblRolLabel = new JLabel("Rol:");
        lblRolLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblRolLabel.setForeground(Color.BLACK);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(lblRolLabel, gbc);

        JLabel lblRolValor = new JLabel(rol);
        lblRolValor.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblRolValor.setForeground(Color.DARK_GRAY);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblRolValor, gbc);

        // Fila 3: Botón Eliminar
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setBackground(COLOR_ROJO);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setOpaque(true);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_ROJO.darker(), 1, true),
                new EmptyBorder(10, 40, 10, 40) // Botón ancho
        ));

        // Acción al hacer clic (Cuadro de confirmación)
        btnEliminar.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que deseas eliminar al usuario " + nombre + "?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.");
                // Aquí iría la lógica para volver a la pantalla anterior
            }
        });

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; // Ocupa las dos columnas para centrarse
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(40, 0, 0, 0); // Empujar el botón más abajo
        panel.add(btnEliminar, gbc);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EliminarUsuario(
                    null,
                    () -> {}
            ).setVisible(true);
        });
    }
}
