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
import utils.EstiloUI;

/**
 *
 * @author Daniel
 */
public class EditarUsuario extends JFrame {

    private final Color COLOR_HEADER_FOOTER = EstiloUI.COLOR_BARRA;
    private final Color COLOR_OSCURO = EstiloUI.COLOR_TEXTO;
    private final Color COLOR_BEIGE = EstiloUI.COLOR_TARJETA_BEIGE;
    private final Color COLOR_BORDE = EstiloUI.COLOR_BORDE;
    private final Color COLOR_VERDE_OSCURO = EstiloUI.COLOR_ACCION;

    private final UsuarioDTO usuario;
    private final JTextField txtNombre = new JTextField(15);
    private final JPasswordField txtPass = new JPasswordField(15);
    private final JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Cajero", "Dueno"});

    public EditarUsuario(
            UsuarioDTO usuario,
            Runnable onBack,
            UsuarioOperacion onGuardar
    ) {
        this.usuario = usuario;

        setTitle("Editar Usuario");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(EstiloUI.crearBarraSuperior("Editar usuario"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(EstiloUI.COLOR_FONDO);
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("Editar usuario");
        lblTitulo.setFont(EstiloUI.FUENTE_TITULO_PANTALLA);
        lblTitulo.setForeground(COLOR_OSCURO);

        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(0, 0, 30, 0);
        centerPanel.add(lblTitulo, gbcMain);

        JPanel formCard = crearFormularioEdicion(onGuardar);

        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(formCard, gbcMain);

        add(centerPanel, BorderLayout.CENTER);

        add(EstiloUI.crearBarraInferior(EstiloUI.crearBotonRegresar(onBack)), BorderLayout.SOUTH);
    }

    private JPanel crearFormularioEdicion(UsuarioOperacion onGuardar) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(40, 60, 40, 60)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblNombre = new JLabel("Nombre de Usuario:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);

        txtNombre.setText(usuario.getNombre());
        estilizarInput(txtNombre);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        JLabel lblPass = new JLabel("Contrasena:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPass.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblPass, gbc);

        txtPass.setText(usuario.getContrasenia());
        estilizarInput(txtPass);
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRol.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblRol, gbc);

        cmbRol.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cmbRol.setBackground(Color.WHITE);
        cmbRol.setPreferredSize(new Dimension(200, 35));
        cmbRol.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(2, 5, 2, 5)
        ));
        cmbRol.setSelectedItem("ADMIN".equalsIgnoreCase(usuario.getRol()) ? "Dueno" : "Cajero");
        gbc.gridx = 1;
        panel.add(cmbRol, gbc);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setBackground(COLOR_VERDE_OSCURO);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setOpaque(true);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_VERDE_OSCURO.darker(), 1, true),
                new EmptyBorder(10, 40, 10, 40)
        ));
        btnConfirmar.addActionListener(e -> actualizarUsuario(onGuardar));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(35, 0, 0, 0);
        panel.add(btnConfirmar, gbc);

        return panel;
    }

    private void actualizarUsuario(UsuarioOperacion onGuardar) {
        UsuarioDTO usuarioActualizado = new UsuarioDTO();
        usuarioActualizado.setIdUsuario(usuario.getIdUsuario());
        usuarioActualizado.setNombre(txtNombre.getText().trim());
        usuarioActualizado.setContrasenia(new String(txtPass.getPassword()));
        usuarioActualizado.setRol("Dueno".equals(cmbRol.getSelectedItem()) ? "ADMIN" : "CAJERO");

        try {
            onGuardar.ejecutar(usuarioActualizado);
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void estilizarInput(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
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
            UsuarioDTO usuarioDemo = new UsuarioDTO();
            usuarioDemo.setIdUsuario(1L);
            usuarioDemo.setNombre("usuario");
            usuarioDemo.setContrasenia("1234");
            usuarioDemo.setRol("CAJERO");
            new EditarUsuario(
                    usuarioDemo,
                    () -> {
                    },
                    usuarioDTO -> {
                    }
            ).setVisible(true);
        });
    }
}
