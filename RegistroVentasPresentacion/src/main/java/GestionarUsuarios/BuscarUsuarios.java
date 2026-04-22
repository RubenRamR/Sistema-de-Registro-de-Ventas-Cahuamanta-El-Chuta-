package GestionarUsuarios;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Daniel
 */
public class BuscarUsuarios extends JFrame {

    // --- Paleta de Colores (Sin Cambios) ---
    private final Color COLOR_HEADER_FOOTER = new Color(65, 114, 159);
    private final Color COLOR_OSCURO = new Color(11, 19, 84);
    private final Color COLOR_BEIGE = new Color(248, 246, 240);
    private final Color COLOR_BORDE = new Color(220, 220, 215);

    private Runnable onSeleccionar;
    
    public BuscarUsuarios(
            Runnable onBack,
            String titulo,
            Runnable onSeleccionar
    ) {
        this.onSeleccionar = onSeleccionar;
        
        setTitle(titulo + " Usuarios");
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
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        // 1. Panel Superior (Título y Búsqueda)
        JPanel topCenterPanel = new JPanel();
        topCenterPanel.setLayout(new BoxLayout(topCenterPanel, BoxLayout.Y_AXIS));
        topCenterPanel.setBackground(Color.WHITE);
        topCenterPanel.setBorder(new EmptyBorder(30, 0, 20, 0));

        JLabel lblTitulo = new JLabel(titulo + " Usuarios");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblTitulo.setForeground(COLOR_OSCURO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        searchPanel.setBackground(Color.WHITE);
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JTextField txtBusqueda = new JTextField("", 25); // Más ancho
        txtBusqueda.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtBusqueda.setPreferredSize(new Dimension(300, 40));
        txtBusqueda.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

        searchPanel.add(lblNombre);
        searchPanel.add(txtBusqueda);

        topCenterPanel.add(lblTitulo);
        topCenterPanel.add(Box.createVerticalStrut(25)); // Espacio entre título y búsqueda
        topCenterPanel.add(searchPanel);

        centerPanel.add(topCenterPanel, BorderLayout.NORTH);

        // 2. Panel de Lista de Usuarios (Con Scroll)
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(new EmptyBorder(10, 50, 20, 50)); // Márgenes laterales de la lista

        // Agregamos varios usuarios para demostrar el Scroll
        listPanel.add(crearTarjetaLista("Usuario1", "Cajero"));
        listPanel.add(Box.createVerticalStrut(15));
        listPanel.add(crearTarjetaLista("Usuario2", "Dueño"));
        listPanel.add(Box.createVerticalStrut(15));
        listPanel.add(crearTarjetaLista("Usuario3", "Cajero"));
        listPanel.add(Box.createVerticalStrut(15));

        // Configuración del ScrollPane
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Ocultar borde del scroll
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll más rápido y fluido
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        centerPanel.add(scrollPane, BorderLayout.CENTER);
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
     * Crea una tarjeta diseñada para apilarse en una lista vertical
     */
    private JPanel crearTarjetaLista(String nombre, String puesto) {
        // Usamos BorderLayout para que los elementos se distribuyan en todo el ancho
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBackground(COLOR_BEIGE);
        
        // Borde redondeado y paddings
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // Forzar la altura máxima para que las tarjetas no se estiren al redimensionar
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Izquierda: Icono
        JPanel iconContainer = new JPanel(new GridBagLayout()); // Para centrar verticalmente el icono
        iconContainer.setBackground(COLOR_BEIGE);
        iconContainer.add(new AvatarIcon());
        panel.add(iconContainer, BorderLayout.WEST);

        // Centro: Textos
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(COLOR_BEIGE);
        
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblNombre.setForeground(COLOR_OSCURO); // Nombre en color azul oscuro
        
        JLabel lblPuesto = new JLabel(puesto);
        lblPuesto.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblPuesto.setForeground(Color.DARK_GRAY);

        // Alinear a la izquierda
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPuesto.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(Box.createVerticalGlue());
        textPanel.add(lblNombre);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(lblPuesto);
        textPanel.add(Box.createVerticalGlue());

        panel.add(textPanel, BorderLayout.CENTER);

        // Derecha: Botón
        JPanel btnContainer = new JPanel(new GridBagLayout()); // Centrar botón verticalmente
        btnContainer.setBackground(COLOR_BEIGE);
        
        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSeleccionar.setForeground(Color.WHITE);
        btnSeleccionar.setBackground(COLOR_OSCURO);
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setOpaque(true);
        btnSeleccionar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_OSCURO, 1, true),
                new EmptyBorder(8, 15, 8, 15)
        ));
        btnSeleccionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnContainer.add(btnSeleccionar);
        btnSeleccionar.addActionListener(e -> {
            onSeleccionar.run();
        });
        panel.add(btnContainer, BorderLayout.EAST);

        return panel;
    }

    /**
     * Clase interna para dibujar la silueta del usuario por código.
     */
    class AvatarIcon extends JPanel {
        public AvatarIcon() {
            setPreferredSize(new Dimension(40, 40));
            setBackground(COLOR_BEIGE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            g2.drawOval(10, 2, 20, 20); // Cabeza un poco más pequeña
            g2.drawArc(2, 24, 36, 36, 0, 180); // Cuerpo adaptado
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new BuscarUsuarios(
                    () -> {},
                    "",
                    () -> {}
            ).setVisible(true);
        });
    }
}