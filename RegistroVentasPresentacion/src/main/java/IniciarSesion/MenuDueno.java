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
            Runnable onGenerarReportes
    ) {
        // Configuración básica de la ventana
        setTitle("Menú Dueño");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setLayout(new BorderLayout());

        // --- Definición de Colores
        Color colorHeaderFooter = new Color(65, 114, 159); // Azul apagado
        Color colorBotones = new Color(0, 116, 183);       // Azul brillante
        Color colorOscuro = new Color(11, 19, 84);         // Azul marino oscuro
        Color colorFondoBeige = new Color(245, 245, 240);  // Beige claro

        // --- ENCABEZADO (Header) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(colorHeaderFooter);
        headerPanel.setPreferredSize(new Dimension(800, 50));
        add(headerPanel, BorderLayout.NORTH);

        // --- CONTENIDO CENTRAL (Center) ---
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();

        // Título Principal
        JLabel lblTitulo = new JLabel("Menú de Opciones");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitulo.setForeground(colorOscuro);
        
        gbcMain.gridx = 0; 
        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(0, 0, 30, 0); // Espacio debajo del título
        centerPanel.add(lblTitulo, gbcMain);

        // Panel Beige que contiene los botones
        JPanel panelBeige = new JPanel();
        panelBeige.setBackground(colorFondoBeige);
        panelBeige.setLayout(new GridBagLayout());
        // Darle un margen interno al panel beige para simular el recuadro de la imagen
        panelBeige.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60)); 
        
        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.insets = new Insets(15, 15, 15, 15); // Espacio entre botones

        // Botón: Gestionar Usuarios
        JButton btnUsuarios = crearBoton("Gestionar Usuarios", colorBotones);
        btnUsuarios.addActionListener(e -> {
            onGestionarUsuarios.run();
        });
        gbcBotones.gridx = 0; 
        gbcBotones.gridy = 0;
        panelBeige.add(btnUsuarios, gbcBotones);

        // Botón: Generar Reportes
        JButton btnReportes = crearBoton("Generar Reportes", colorBotones);
        btnReportes.addActionListener(e -> {
            onGenerarReportes.run();
        });
        gbcBotones.gridx = 1; 
        gbcBotones.gridy = 0;
        panelBeige.add(btnReportes, gbcBotones);

        // Botón: Consultar Historial Ventas (Centrado abajo)
//        JButton btnVentas = crearBoton("Consultar Historial Ventas", colorBotones);
//        gbcBotones.gridx = 0; 
//        gbcBotones.gridy = 1; 
//        gbcBotones.gridwidth = 2; // Ocupa dos columnas para centrarse
//        panelBeige.add(btnVentas, gbcBotones);

        // Añadir el panel beige al panel central
        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(panelBeige, gbcMain);

        add(centerPanel, BorderLayout.CENTER);

        // --- PIE DE PÁGINA (Footer) ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(colorHeaderFooter);
        footerPanel.setPreferredSize(new Dimension(800, 60));
        footerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Alinear a la izquierda sin márgenes

        // Botón de regreso "<"
        JButton btnRegresar = new JButton("<");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 24));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setBackground(colorOscuro);
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
     * Método auxiliar para crear botones con estilo uniforme
     */
    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorFondo);
        boton.setPreferredSize(new Dimension(260, 80));
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        // Quitar el borde por defecto para un diseño más plano (Flat Design)
        boton.setBorder(BorderFactory.createEmptyBorder()); 
        return boton;
    }

    public static void main(String[] args) {
        // Asegurar que la UI se ejecute en el hilo despachador de eventos
        SwingUtilities.invokeLater(() -> {
            try {
                // Usar el Look and Feel del sistema para mejor apariencia
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MenuDueno(
                    () -> {},
                    () -> {},
                    () -> {}
            ).setVisible(true);
        });
    }
}
