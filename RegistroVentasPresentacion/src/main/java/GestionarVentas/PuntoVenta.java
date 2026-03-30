package GestionarVentas;

import dtos.DetalleVentaDTO;
import dtos.ProductoDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
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

    // --- VARIABLES DE ESTADO Y COMPONENTES VISUALES ---
    private List<ProductoDTO> inventario;
    private List<DetalleVentaDTO> carritoCompras;
    private ProductoDTO productoSeleccionadoActual;

    private JLabel lblProductoSeleccionado;
    private JSpinner spinnerCantidad;
    private DefaultListModel<String> modeloListaResumen;
    private JList<String> listaResumen; 
    private JLabel lblSubtotal;
    private JLabel lblTotal;
    
    private JPanel panelProductos;
    
    // Acciones de ControlVista
    private Consumer<List<DetalleVentaDTO>> onRegistrarVenta;
    private Function<String, List<ProductoDTO>> onCategoriaSeleccionada;

    public PuntoVenta(
            Consumer<List<DetalleVentaDTO>> onRegistrarVenta,
            Function<String, List<ProductoDTO>> onCategoriaSeleccionada,
            List<ProductoDTO> inventario
    ) {
        this.onRegistrarVenta = onRegistrarVenta;
        this.onCategoriaSeleccionada = onCategoriaSeleccionada;
        this.inventario = inventario;
        
        setTitle("Punto Venta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        carritoCompras = new ArrayList<>();
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel panelDer = crearPanelDerecho(); 
        JPanel panelIzq = crearPanelIzquierdo();

        panelPrincipal.add(panelIzq, BorderLayout.CENTER);
        panelPrincipal.add(panelDer, BorderLayout.EAST);
        
        add(panelPrincipal);
    }
    
    private void actualizarInventario() {
        panelProductos.removeAll();
        for (ProductoDTO producto : inventario) {
            panelProductos.add(crearBotonProducto(producto));
        }
        
        panelProductos.revalidate();
        panelProductos.repaint();
    }
    
    private JPanel crearPanelIzquierdo() {
        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 10));

        JPanel panelCategorias = new JPanel(new GridLayout(1, 3, 10, 0));
        panelCategorias.setPreferredSize(new Dimension(0, 100));

        panelCategorias.add(crearBotonCategoria("CALDOS Y PLATOS", "5", new Color(244, 67, 54)));
        panelCategorias.add(crearBotonCategoria("TACOS", "8", new Color(255, 152, 0)));
        panelCategorias.add(crearBotonCategoria("BEBIDAS", "6", new Color(33, 33, 255)));

        panelProductos = new JPanel(new GridLayout(0, 3, 15, 15));
        
        for (ProductoDTO producto : inventario) {
            panelProductos.add(crearBotonProducto(producto));
        }

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

        JLabel lblTituloSeleccionado = new JLabel("Seleccionado");
        lblTituloSeleccionado.setFont(new Font("Arial", Font.BOLD, 14));
        
        // ¡CORRECCIÓN VISUAL!: Aumentamos el tamaño y los márgenes (FlowLayout con espaciado)
        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelAgregar.setBackground(new Color(240, 240, 240));
        // Aumentamos la altura de 60 a 90 para que haya espacio si el botón baja a la siguiente línea
        panelAgregar.setPreferredSize(new Dimension(350, 90)); 
        panelAgregar.setMaximumSize(new Dimension(400, 90));
        
        lblProductoSeleccionado = new JLabel("Ninguno...                ");
        lblProductoSeleccionado.setFont(new Font("Arial", Font.BOLD, 12));
        
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(Color.BLUE);
        btnAgregar.setForeground(Color.WHITE);
        // ¡CORRECCIÓN VISUAL!: Obligar a Swing a pintar el fondo del botón
        btnAgregar.setOpaque(true);
        btnAgregar.setBorderPainted(false);
        
        btnAgregar.addActionListener(e -> agregarAlResumen());

        panelAgregar.add(lblProductoSeleccionado);
        panelAgregar.add(new JLabel(" Cantidad: "));
        panelAgregar.add(spinnerCantidad);
        panelAgregar.add(btnAgregar);

        // --- RESUMEN ---
        JLabel lblResumen = new JLabel("Resumen");
        lblResumen.setFont(new Font("Arial", Font.BOLD, 14));
        
        modeloListaResumen = new DefaultListModel<>();
        listaResumen = new JList<>(modeloListaResumen);
        listaResumen.setBackground(new Color(245, 245, 245));
        listaResumen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        
        JScrollPane scrollResumen = new JScrollPane(listaResumen);
        scrollResumen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JButton btnEliminar = new JButton("Quitar Producto Seleccionado");
        btnEliminar.setForeground(Color.RED);
        btnEliminar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEliminar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnEliminar.addActionListener(e -> eliminarDelResumen());

        // --- TOTALES ---
        JPanel panelTotales = new JPanel(new GridLayout(3, 2, 5, 5));
        panelTotales.setBackground(Color.WHITE);
        panelTotales.setMaximumSize(new Dimension(400, 80));
        
        lblSubtotal = new JLabel("<html><b>$0.00</b></html>", SwingConstants.RIGHT);
        lblTotal = new JLabel("<html><b>$0.00</b></html>", SwingConstants.RIGHT);

        panelTotales.add(new JLabel("<html><b>Subtotal</b></html>"));
        panelTotales.add(lblSubtotal);
        panelTotales.add(new JLabel("........"));
        panelTotales.add(new JLabel(""));
        panelTotales.add(new JLabel("<html><b>Total</b></html>"));
        panelTotales.add(lblTotal);

        // --- BOTÓN COBRAR ---
        JButton btnCobrar = new JButton("Cobrar");
        btnCobrar.setBackground(Color.BLUE);
        btnCobrar.setForeground(Color.WHITE);
        // ¡CORRECCIÓN VISUAL!: Obligar a pintar el fondo azul
        btnCobrar.setOpaque(true);
        btnCobrar.setBorderPainted(false);
        btnCobrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCobrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnCobrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnCobrar.addActionListener(e -> {
            onRegistrarVenta.accept(carritoCompras);
        });

        // Armado del panel
        panelDerecho.add(lblTituloSeleccionado);
        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(panelAgregar);
        panelDerecho.add(Box.createVerticalStrut(20));
        panelDerecho.add(lblResumen);
        panelDerecho.add(Box.createVerticalStrut(5));
        panelDerecho.add(scrollResumen);
        panelDerecho.add(Box.createVerticalStrut(5)); 
        panelDerecho.add(btnEliminar); 
        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(panelTotales);
        panelDerecho.add(Box.createVerticalStrut(20));
        panelDerecho.add(btnCobrar);

        return panelDerecho;
    }

    private JButton crearBotonCategoria(String titulo, String cantidad, Color color) {
        String html = "<html><center><b>" + titulo + "</b><br><br><br>" + cantidad + "</center></html>";
        JButton btn = new JButton(html);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        // ¡CORRECCIÓN VISUAL!: Forzar color en categorías
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        
        btn.addActionListener(e -> {
            inventario = onCategoriaSeleccionada.apply(titulo);
            actualizarInventario();
        });
        
        return btn;
    }

    private JButton crearBotonProducto(ProductoDTO producto) {
        String nombreHtml = producto.getNombre().replace("\n", "<br>");
        String html = "<html><center>" + nombreHtml + "<br><font color='gray'>$" + 
                      producto.getPrecio().setScale(2, RoundingMode.HALF_UP) + "</font></center></html>";
        
        JButton btn = new JButton(html);
        btn.setBackground(new Color(220, 220, 220));
        btn.setFocusPainted(false);
        // ¡CORRECCIÓN VISUAL!: Forzar color gris en los productos
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 150));
        
        btn.addActionListener(e -> {
            productoSeleccionadoActual = producto;
            lblProductoSeleccionado.setText(producto.getNombre().replace("\n", " "));
            spinnerCantidad.setValue(1); 
        });
        
        return btn;
    }

    // --- LÓGICA DE NEGOCIO ---

    private void agregarAlResumen() {
        if (productoSeleccionadoActual == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad = (Integer) spinnerCantidad.getValue();
        DetalleVentaDTO nuevoItem = new DetalleVentaDTO(
                cantidad, 
                productoSeleccionadoActual.getPrecio(), 
                productoSeleccionadoActual);
        
        carritoCompras.add(nuevoItem);

        String nombreEnLinea = productoSeleccionadoActual.getNombre().replace("\n", " ");
        String textoLista = String.format("%s (%d)  ---  $%s", 
                nombreEnLinea, 
                cantidad, 
                nuevoItem.getSubtotal().setScale(2, RoundingMode.HALF_UP));
        
        modeloListaResumen.addElement(textoLista);
        actualizarTotales();
    }

    private void eliminarDelResumen() {
        int index = listaResumen.getSelectedIndex(); 
        
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la lista 'Resumen' para quitarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        carritoCompras.remove(index);
        modeloListaResumen.remove(index);
        actualizarTotales();
    }

    private void cobrar() {
        if (carritoCompras.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, "¡Cobro realizado con éxito!\nImprimiendo ticket...", "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
        
        carritoCompras.clear();
        modeloListaResumen.clear();
        productoSeleccionadoActual = null;
        lblProductoSeleccionado.setText("Ninguno...                ");
        spinnerCantidad.setValue(1);
        actualizarTotales();
    }

    private void actualizarTotales() {
        BigDecimal granTotal = BigDecimal.ZERO;
        
        for (DetalleVentaDTO item : carritoCompras) {
            granTotal = granTotal.add(item.getSubtotal());
        }

        String totalFormateado = "<html><b>$" + granTotal.setScale(2, RoundingMode.HALF_UP) + "</b></html>";
        lblSubtotal.setText(totalFormateado);
        lblTotal.setText(totalFormateado);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> new PuntoVenta(
                a -> {},
                e -> new ArrayList<>(),
                null
        ).setVisible(true));
    }
}