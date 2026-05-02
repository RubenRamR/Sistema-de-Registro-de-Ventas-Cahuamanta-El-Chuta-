package gestionarusuarios;

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
import utils.EstiloUI;

/**
 *
 * @author Daniel
 */
public class EliminarUsuario extends JFrame {

    private final Color COLOR_HEADER_FOOTER = EstiloUI.COLOR_BARRA;
    private final Color COLOR_OSCURO = EstiloUI.COLOR_TEXTO;
    private final Color COLOR_BEIGE = EstiloUI.COLOR_TARJETA_BEIGE;
    private final Color COLOR_BORDE = EstiloUI.COLOR_BORDE;
    private final Color COLOR_ROJO = EstiloUI.COLOR_DANGER;

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

        add(EstiloUI.crearBarraSuperior("Eliminar usuario"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(EstiloUI.COLOR_FONDO);
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("Eliminar usuario");
        lblTitulo.setFont(EstiloUI.FUENTE_TITULO_PANTALLA);
        lblTitulo.setForeground(COLOR_OSCURO);

        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(0, 0, 40, 0);
        centerPanel.add(lblTitulo, gbcMain);

        JPanel cardPanel = crearTarjetaInformacion(onEliminar);

        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(cardPanel, gbcMain);

        add(centerPanel, BorderLayout.CENTER);

        add(EstiloUI.crearBarraInferior(EstiloUI.crearBotonRegresar(onBack)), BorderLayout.SOUTH);
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
        lblNombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(lblNombreLabel, gbc);

        JLabel lblNombreValor = new JLabel(usuario.getNombre());
        lblNombreValor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblNombreValor.setForeground(Color.DARK_GRAY);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblNombreValor, gbc);

        JLabel lblRolLabel = new JLabel("Rol:");
        lblRolLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(lblRolLabel, gbc);

        JLabel lblRolValor = new JLabel("ADMIN".equalsIgnoreCase(usuario.getRol()) ? "Dueno" : "Cajero");
        lblRolValor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblRolValor.setForeground(Color.DARK_GRAY);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblRolValor, gbc);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 16));
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
