package GestionarUsuarios;

import aplicacion.UsuarioOperacion;
import dtos.UsuarioDTO;
import excepciones.NegocioException;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
 * @author MiCuenta
 */
public class AgregarUsuario extends JFrame {

    private final Color COLOR_HEADER_FOOTER = new Color(65, 114, 159);
    private final Color COLOR_OSCURO = new Color(11, 19, 84);
    private final Color COLOR_BEIGE = new Color(248, 246, 240);
    private final Color COLOR_BORDE = new Color(220, 220, 215);
    private final Color COLOR_VERDE = new Color(76, 209, 87);

    private final JTextField txtNombre = new JTextField(15);
    private final JPasswordField txtPass = new JPasswordField(15);
    private final JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Cajero", "Dueno"});

    public AgregarUsuario(
            Runnable onBack,
            UsuarioOperacion onGuardar
    ) {
        setTitle("Agregar Usuario");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_HEADER_FOOTER);
        headerPanel.setPreferredSize(new Dimension(800, 50));
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("Agregar Usuario");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblTitulo.setForeground(COLOR_OSCURO);

        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(0, 0, 30, 0);
        centerPanel.add(lblTitulo, gbcMain);

        JPanel formCard = crearFormulario(onGuardar);

        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(formCard, gbcMain);

        add(centerPanel, BorderLayout.CENTER);

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
        btnRegresar.addActionListener(e -> onBack.run());
        footerPanel.add(btnRegresar);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel crearFormulario(UsuarioOperacion onGuardar) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(40, 60, 40, 60)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblNombre = new JLabel("Nombre de Usuario:");
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblNombre.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);

        estilizarInput(txtNombre);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        JLabel lblPass = new JLabel("Contrasena:");
        lblPass.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblPass.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblPass, gbc);

        estilizarInput(txtPass);
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblRol.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblRol, gbc);

        cmbRol.setFont(new Font("SansSerif", Font.PLAIN, 16));
        cmbRol.setBackground(Color.WHITE);
        cmbRol.setPreferredSize(new Dimension(200, 35));
        cmbRol.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(2, 5, 2, 5)
        ));
        gbc.gridx = 1;
        panel.add(cmbRol, gbc);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setBackground(COLOR_VERDE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setOpaque(true);
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 180, 70), 1, true),
                new EmptyBorder(10, 40, 10, 40)
        ));
        btnAgregar.addActionListener(e -> guardarUsuario(onGuardar));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(btnAgregar, gbc);

        return panel;
    }

    private void guardarUsuario(UsuarioOperacion onGuardar) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(txtNombre.getText().trim());
        usuarioDTO.setContrasenia(new String(txtPass.getPassword()));
        usuarioDTO.setRol("Dueno".equals(cmbRol.getSelectedItem()) ? "ADMIN" : "CAJERO");

        try {
            onGuardar.ejecutar(usuarioDTO);
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
            new AgregarUsuario(
                    () -> {
                    },
                    usuarioDTO -> {
                    }
            ).setVisible(true);
        });
    }
}
