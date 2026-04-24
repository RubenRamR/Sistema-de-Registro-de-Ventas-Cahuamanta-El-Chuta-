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

    private final Color COLOR_HEADER_FOOTER = new Color(65, 114, 159);
    private final Color COLOR_OSCURO = new Color(11, 19, 84);
    private final Color COLOR_BEIGE = new Color(248, 246, 240);
    private final Color COLOR_BORDE = new Color(220, 220, 215);
    private final Color COLOR_ROJO = new Color(190, 50, 50);

    private final UsuarioDTO usuario;

    public EliminarUsuario(
            UsuarioDTO usuario,
            Runnable onBack,
            UsuarioOperacion onEliminar
    ) {
        this.usuario = usuario;

        setTitle("Eliminar Usuario");
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

        JLabel lblTitulo = new JLabel("Eliminar Usuario");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblTitulo.setForeground(COLOR_OSCURO);

        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(0, 0, 40, 0);
        centerPanel.add(lblTitulo, gbcMain);

        JPanel cardPanel = crearTarjetaInformacion(onEliminar);

        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(cardPanel, gbcMain);

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

    private JPanel crearTarjetaInformacion(UsuarioOperacion onEliminar) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(50, 80, 50, 80)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNombreLabel = new JLabel("Nombre de Usuario:");
        lblNombreLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(lblNombreLabel, gbc);

        JLabel lblNombreValor = new JLabel(usuario.getNombre());
        lblNombreValor.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblNombreValor.setForeground(Color.DARK_GRAY);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblNombreValor, gbc);

        JLabel lblRolLabel = new JLabel("Rol:");
        lblRolLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(lblRolLabel, gbc);

        JLabel lblRolValor = new JLabel("ADMIN".equalsIgnoreCase(usuario.getRol()) ? "Dueno" : "Cajero");
        lblRolValor.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblRolValor.setForeground(Color.DARK_GRAY);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblRolValor, gbc);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setBackground(COLOR_ROJO);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setOpaque(true);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_ROJO.darker(), 1, true),
                new EmptyBorder(10, 40, 10, 40)
        ));
        btnEliminar.addActionListener(e -> eliminarUsuario(onEliminar));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(40, 0, 0, 0);
        panel.add(btnEliminar, gbc);

        return panel;
    }

    private void eliminarUsuario(UsuarioOperacion onEliminar) {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "Desea eliminar al usuario " + usuario.getNombre() + "?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            onEliminar.ejecutar(usuario);
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            usuarioDemo.setRol("CAJERO");
            new EliminarUsuario(
                    usuarioDemo,
                    () -> {
                    },
                    usuarioDTO -> {
                    }
            ).setVisible(true);
        });
    }
}
