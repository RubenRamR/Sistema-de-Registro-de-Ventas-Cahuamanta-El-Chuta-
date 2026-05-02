package gestionarusuarios;

import dtos.UsuarioDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import utils.EstiloUI;

/**
 *
 * @author Daniel
 */
public class BuscarUsuarios extends JFrame {

    private final Color COLOR_HEADER_FOOTER = EstiloUI.COLOR_BARRA;
    private final Color COLOR_OSCURO = EstiloUI.COLOR_TEXTO;
    private final Color COLOR_BEIGE = EstiloUI.COLOR_TARJETA_BEIGE;
    private final Color COLOR_BORDE = EstiloUI.COLOR_BORDE;

    private final List<UsuarioDTO> usuarios;
    private final Consumer<UsuarioDTO> onSeleccionar;
    private final JPanel listPanel;

    public BuscarUsuarios(
            Runnable onBack,
            String titulo,
            List<UsuarioDTO> usuarios,
            Consumer<UsuarioDTO> onSeleccionar
    ) {
        this.usuarios = usuarios != null ? usuarios : new ArrayList<>();
        this.onSeleccionar = onSeleccionar;

        setTitle(titulo + " Usuarios");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(EstiloUI.crearBarraSuperior(titulo + " usuarios"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(EstiloUI.COLOR_FONDO);

        JPanel topCenterPanel = new JPanel();
        topCenterPanel.setLayout(new BoxLayout(topCenterPanel, BoxLayout.Y_AXIS));
        topCenterPanel.setBackground(EstiloUI.COLOR_FONDO);
        topCenterPanel.setBorder(new EmptyBorder(30, 0, 20, 0));

        JLabel lblTitulo = new JLabel(titulo + " usuarios");
        lblTitulo.setFont(EstiloUI.FUENTE_TITULO_PANTALLA);
        lblTitulo.setForeground(COLOR_OSCURO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        searchPanel.setBackground(EstiloUI.COLOR_FONDO);
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblNombre.setForeground(EstiloUI.COLOR_TEXTO);

        JTextField txtBusqueda = new JTextField("", 25);
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtBusqueda.setPreferredSize(new Dimension(300, 40));
        txtBusqueda.setBorder(EstiloUI.bordeInput());

        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarUsuarios(txtBusqueda.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarUsuarios(txtBusqueda.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarUsuarios(txtBusqueda.getText());
            }
        });

        searchPanel.add(lblNombre);
        searchPanel.add(txtBusqueda);

        topCenterPanel.add(lblTitulo);
        topCenterPanel.add(Box.createVerticalStrut(25));
        topCenterPanel.add(searchPanel);

        centerPanel.add(topCenterPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(EstiloUI.COLOR_FONDO);
        listPanel.setBorder(new EmptyBorder(10, 50, 20, 50));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(EstiloUI.COLOR_FONDO);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        add(EstiloUI.crearBarraInferior(EstiloUI.crearBotonRegresar(onBack)), BorderLayout.SOUTH);

        filtrarUsuarios("");
    }

    private void filtrarUsuarios(String textoBusqueda) {
        listPanel.removeAll();

        List<UsuarioDTO> usuariosFiltrados = usuarios.stream()
                .filter(usuario -> textoBusqueda == null
                        || textoBusqueda.isBlank()
                        || usuario.getNombre().toLowerCase().contains(textoBusqueda.trim().toLowerCase()))
                .toList();

        if (usuariosFiltrados.isEmpty()) {
            JLabel lblSinResultados = new JLabel("No hay usuarios para mostrar.");
            lblSinResultados.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            lblSinResultados.setForeground(Color.DARK_GRAY);
            lblSinResultados.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(lblSinResultados);
        } else {
            for (UsuarioDTO usuario : usuariosFiltrados) {
                listPanel.add(crearTarjetaLista(usuario));
                listPanel.add(Box.createVerticalStrut(15));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel crearTarjetaLista(UsuarioDTO usuario) {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel iconContainer = new JPanel(new GridBagLayout());
        iconContainer.setBackground(COLOR_BEIGE);
        iconContainer.add(new AvatarIcon());
        panel.add(iconContainer, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(COLOR_BEIGE);

        JLabel lblNombre = new JLabel(usuario.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(COLOR_OSCURO);

        JLabel lblPuesto = new JLabel(formatearRol(usuario.getRol()));
        lblPuesto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblPuesto.setForeground(Color.DARK_GRAY);

        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPuesto.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(Box.createVerticalGlue());
        textPanel.add(lblNombre);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(lblPuesto);
        textPanel.add(Box.createVerticalGlue());

        panel.add(textPanel, BorderLayout.CENTER);

        JPanel btnContainer = new JPanel(new GridBagLayout());
        btnContainer.setBackground(COLOR_BEIGE);

        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSeleccionar.setForeground(Color.WHITE);
        btnSeleccionar.setBackground(COLOR_OSCURO);
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setOpaque(true);
        btnSeleccionar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_OSCURO, 1, true),
                new EmptyBorder(8, 15, 8, 15)
        ));
        btnSeleccionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSeleccionar.addActionListener(e -> onSeleccionar.accept(usuario));

        btnContainer.add(btnSeleccionar);
        panel.add(btnContainer, BorderLayout.EAST);

        return panel;
    }

    private String formatearRol(String rol) {
        if ("ADMIN".equalsIgnoreCase(rol)) {
            return "Dueno";
        }
        if ("CAJERO".equalsIgnoreCase(rol)) {
            return "Cajero";
        }
        return rol == null ? "Sin rol" : rol;
    }

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

            g2.drawOval(10, 2, 20, 20);
            g2.drawArc(2, 24, 36, 36, 0, 180);
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
                    () -> {
                    },
                    "Buscar",
                    new ArrayList<>(),
                    usuario -> {
                    }
            ).setVisible(true);
        });
    }
}
