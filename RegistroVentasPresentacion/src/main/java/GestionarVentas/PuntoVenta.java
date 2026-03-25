package GestionarVentas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author MiCuenta
 */
public class PuntoVenta extends JFrame {

    public PuntoVenta() {
        setTitle("Punto Venta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Centrar en pantalla
        
        // Panel Principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Construir las dos mitades
        panelPrincipal.add(crearPanelIzquierdo(), BorderLayout.CENTER);
        panelPrincipal.add(crearPanelDerecho(), BorderLayout.EAST);
        
        add(panelPrincipal);
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 10));

        // --- 1. SECCIÓN DE CATEGORÍAS (ARRIBA) ---
        JPanel panelCategorias = new JPanel(new GridLayout(1, 3, 10, 0));
        panelCategorias.setPreferredSize(new Dimension(0, 100));

        JButton btnCaldos = crearBotonCategoria("CALDOS Y PLATOS", "5", new Color(244, 67, 54)); // Rojo
        JButton btnTacos = crearBotonCategoria("TACOS", "8", new Color(255, 152, 0)); // Naranja
        JButton btnBebidas = crearBotonCategoria("BEBIDAS", "6", new Color(33, 33, 255)); // Azul

        panelCategorias.add(btnCaldos);
        panelCategorias.add(btnTacos);
        panelCategorias.add(btnBebidas);

        // --- 2. SECCIÓN DE PRODUCTOS (CENTRO) ---
        // GridLayout con 0 filas (infinitas) y 3 columnas
        JPanel panelProductos = new JPanel(new GridLayout(0, 3, 15, 15));
        
        // Agregamos algunos productos de ejemplo basados en tu imagen
        panelProductos.add(crearBotonProducto("Taco de Cahuamanta", "$35"));
        panelProductos.add(crearBotonProducto("Taco de Moronga", "$35"));
        panelProductos.add(crearBotonProducto("Taco de Cahuamanta\ncon Camaron", "$50"));
        panelProductos.add(crearBotonProducto("Taco de Aleta", "$90"));
        panelProductos.add(crearBotonProducto("Taco de Camaron\nEmpanizado", "$55"));
        panelProductos.add(crearBotonProducto("Taco de Camaron\nCocido", "$80"));
        panelProductos.add(crearBotonProducto("Taco de Aleta y\nCamaron", "$110"));
        panelProductos.add(crearBotonProducto("Taco de Pescado\nFrito", "$50"));

        // Envolvemos los productos en un ScrollPane por si son muchos
        JScrollPane scrollProductos = new JScrollPane(panelProductos);
        scrollProductos.setBorder(null);
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16);

        panelIzquierdo.add(panelCategorias, BorderLayout.NORTH);
        panelIzquierdo.add(scrollProductos, BorderLayout.CENTER);

        return panelIzquierdo;
    }

    private JPanel crearPanelDerecho() {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setPreferredSize(new Dimension(350, 0));
        panelDerecho.setBorder(new EmptyBorder(10, 15, 10, 15));
        panelDerecho.setBackground(Color.WHITE);

        // --- SECCIÓN SELECCIONADO ---
        JLabel lblSeleccionado = new JLabel("Seleccionado");
        lblSeleccionado.setFont(new Font("Arial", Font.BOLD, 14));
        lblSeleccionado.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAgregar.setBackground(new Color(240, 240, 240));
        panelAgregar.setMaximumSize(new Dimension(400, 60));
        
        panelAgregar.add(new JLabel("Taco de Cahuamanta  "));
        panelAgregar.add(new JLabel("Cantidad: "));
        panelAgregar.add(new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)));
        
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(Color.BLUE);
        btnAgregar.setForeground(Color.WHITE);
        panelAgregar.add(btnAgregar);

        // --- SECCIÓN RESUMEN ---
        JLabel lblResumen = new JLabel("Resumen");
        lblResumen.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Usamos un JList para la lista de productos agregados
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        modeloLista.addElement("Taco de Cahuamanta (1)                  $80.00");
        JList<String> listaResumen = new JList<>(modeloLista);
        listaResumen.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollResumen = new JScrollPane(listaResumen);
        scrollResumen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // --- SECCIÓN TOTALES ---
        JPanel panelTotales = new JPanel(new GridLayout(3, 2, 5, 5));
        panelTotales.setBackground(Color.WHITE);
        panelTotales.setMaximumSize(new Dimension(400, 80));
        
        panelTotales.add(new JLabel("<html><b>Subtotal</b></html>"));
        panelTotales.add(new JLabel("<html><b>$80.00</b></html>", SwingConstants.RIGHT));
        panelTotales.add(new JLabel("........"));
        panelTotales.add(new JLabel(""));
        panelTotales.add(new JLabel("<html><b>Total</b></html>"));
        panelTotales.add(new JLabel("<html><b>$80.00</b></html>", SwingConstants.RIGHT));

        // --- BOTÓN COBRAR ---
        JButton btnCobrar = new JButton("Cobrar");
        btnCobrar.setBackground(Color.BLUE);
        btnCobrar.setForeground(Color.WHITE);
        btnCobrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCobrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnCobrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Armar el panel derecho
        panelDerecho.add(lblSeleccionado);
        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(panelAgregar);
        panelDerecho.add(Box.createVerticalStrut(20));
        panelDerecho.add(lblResumen);
        panelDerecho.add(Box.createVerticalStrut(5));
        panelDerecho.add(scrollResumen);
        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(panelTotales);
        panelDerecho.add(Box.createVerticalStrut(20));
        panelDerecho.add(btnCobrar);

        return panelDerecho;
    }

    // Métodos de ayuda para crear botones consistentes
    private JButton crearBotonCategoria(String titulo, String cantidad, Color color) {
        String html = "<html><center><b>" + titulo + "</b><br><br><br>" + cantidad + "</center></html>";
        JButton btn = new JButton(html);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return btn;
    }

    private JButton crearBotonProducto(String nombre, String precio) {
        // Reemplazamos saltos de línea normales por saltos HTML
        String nombreHtml = nombre.replace("\n", "<br>");
        String html = "<html><center>" + nombreHtml + "<br><font color='gray'>" + precio + "</font></center></html>";
        JButton btn = new JButton(html);
        btn.setBackground(new Color(220, 220, 220)); // Gris claro simulando la imagen vacía
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 150));
        return btn;
    }

    public static void main(String[] args) {
        // Cambiar el aspecto visual para que se vea más moderno que el estándar de Java
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new PuntoVenta().setVisible(true);
        });
    }
}
