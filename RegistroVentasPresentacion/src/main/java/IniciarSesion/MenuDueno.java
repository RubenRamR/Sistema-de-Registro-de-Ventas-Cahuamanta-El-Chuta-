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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Daniel
 */
public class MenuDueno extends JFrame {

    public MenuDueno(
            Runnable onBack,
            Runnable onGestionarUsuarios,
            Runnable onHistorialVentas,
            Runnable onGenerarReportes
    ) {
        setTitle("Menu Dueno");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color colorHeaderFooter = new Color(65, 114, 159);
        Color colorBotones = new Color(0, 116, 183);
        Color colorOscuro = new Color(11, 19, 84);
        Color colorFondoBeige = new Color(245, 245, 240);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(colorHeaderFooter);
        headerPanel.setPreferredSize(new Dimension(800, 50));
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbcMain = new GridBagConstraints();

        JLabel lblTitulo = new JLabel("Menu de Opciones");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitulo.setForeground(colorOscuro);
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(0, 0, 30, 0);
        centerPanel.add(lblTitulo, gbcMain);

        JPanel panelBeige = new JPanel(new GridBagLayout());
        panelBeige.setBackground(colorFondoBeige);
        panelBeige.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.insets = new Insets(15, 15, 15, 15);

        JButton btnUsuarios = crearBoton("Gestionar Usuarios", colorBotones);
        btnUsuarios.addActionListener(e -> onGestionarUsuarios.run());
        gbcBotones.gridx = 0;
        gbcBotones.gridy = 0;
        panelBeige.add(btnUsuarios, gbcBotones);

        JButton btnReportes = crearBoton("Generar Reportes", colorBotones);
        btnReportes.addActionListener(e -> onGenerarReportes.run());
        gbcBotones.gridx = 1;
        gbcBotones.gridy = 0;
        panelBeige.add(btnReportes, gbcBotones);

        JButton btnVentas = crearBoton("Historial de Ventas", colorBotones);
        btnVentas.addActionListener(e -> onHistorialVentas.run());
        gbcBotones.gridx = 0;
        gbcBotones.gridy = 1;
        gbcBotones.gridwidth = 2;
        panelBeige.add(btnVentas, gbcBotones);

        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(panelBeige, gbcMain);
        add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        footerPanel.setBackground(colorHeaderFooter);
        footerPanel.setPreferredSize(new Dimension(800, 60));

        JButton btnRegresar = new JButton("<");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 24));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setBackground(colorOscuro);
        btnRegresar.setPreferredSize(new Dimension(60, 60));
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setOpaque(true);
        btnRegresar.addActionListener(e -> onBack.run());
        footerPanel.add(btnRegresar);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorFondo);
        boton.setPreferredSize(new Dimension(260, 80));
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createEmptyBorder());
        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MenuDueno(
                    () -> {},
                    () -> {},
                    () -> {},
                    () -> {}
            ).setVisible(true);
        });
    }
}
