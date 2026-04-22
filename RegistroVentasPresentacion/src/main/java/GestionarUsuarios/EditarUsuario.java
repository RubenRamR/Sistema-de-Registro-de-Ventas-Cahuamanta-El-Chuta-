package GestionarUsuarios;

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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Daniel
 */
public class EditarUsuario extends JFrame {

    // --- Paleta de Colores ---
    private final Color COLOR_HEADER_FOOTER = new Color(65, 114, 159);
    private final Color COLOR_OSCURO = new Color(11, 19, 84);
    private final Color COLOR_BEIGE = new Color(248, 246, 240);
    private final Color COLOR_BORDE = new Color(220, 220, 215);
    private final Color COLOR_VERDE_OSCURO = new Color(25, 130, 55); // Verde para "Confirmar"

    public EditarUsuario(
            Runnable onBack
    ) {
        setTitle("Editar Usuario");
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
        JLabel lblTitulo = new JLabel("Editar Usuario");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblTitulo.setForeground(COLOR_OSCURO);
        
        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(0, 0, 30, 0); 
        centerPanel.add(lblTitulo, gbcMain);

        // 2. Formulario (Tarjeta Beige)
        JPanel formCard = crearFormularioEdicion();
        
        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(formCard, gbcMain);

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
     * Construye el formulario con los datos pre-cargados del usuario
     */
    private JPanel crearFormularioEdicion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BEIGE);
        
        // Borde de la tarjeta
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(40, 60, 40, 60)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // --- Fila 1: Nombre de Usuario ---
        JLabel lblNombre = new JLabel("Nombre de Usuario:");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblNombre.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblNombre, gbc);

        JTextField txtNombre = new JTextField("Christiano", 15);
        estilizarInput(txtNombre);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(txtNombre, gbc);

        // --- Fila 2: Contraseña ---
        JLabel lblPass = new JLabel("Contrasenia:");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblPass.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField("1234567890", 15); // Valor dummy para los puntos
        estilizarInput(txtPass);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtPass, gbc);

        // --- Fila 3: Rol (Como texto estático según la imagen) ---
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblRol.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblRol, gbc);

        JLabel lblRolValor = new JLabel("Cajero");
        lblRolValor.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblRolValor.setForeground(Color.DARK_GRAY);
        // Le damos un pequeño margen izquierdo para alinear visualmente el texto con los inputs
        lblRolValor.setBorder(new EmptyBorder(0, 10, 0, 0)); 
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(lblRolValor, gbc);

        // --- Fila 4: Botón Confirmar ---
        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setBackground(COLOR_VERDE_OSCURO);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setOpaque(true);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_VERDE_OSCURO.darker(), 1, true), 
                new EmptyBorder(10, 40, 10, 40)
        ));

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(35, 0, 0, 0); // Separación superior para el botón
        panel.add(btnConfirmar, gbc);

        return panel;
    }

    /**
     * Método auxiliar para unificar el estilo de los campos
     */
    private void estilizarInput(JTextField campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campo.setPreferredSize(new Dimension(200, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EditarUsuario(
                    () -> {}
            ).setVisible(true);
        });
    }
}
