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
import java.text.Normalizer;
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
    private Runnable onBack;

    public PuntoVenta(
            Consumer<List<DetalleVentaDTO>> onRegistrarVenta,
            Function<String, List<ProductoDTO>> onCategoriaSeleccionada,
            List<ProductoDTO> inventario,
            Runnable onBack,
            List<DetalleVentaDTO> carritoCompras
    ) {
        this.onRegistrarVenta = onRegistrarVenta;
        this.onCategoriaSeleccionada = onCategoriaSeleccionada;
        this.inventario = inventario != null ? inventario : new ArrayList<>();
        this.onBack = onBack;
        this.carritoCompras = carritoCompras;

        setTitle("Punto Venta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        if (carritoCompras == null)
        {
            this.carritoCompras = new ArrayList<>();
        }

        // El panel principal que contiene tu lógica de venta
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(Color.WHITE); // Fondo blanco para coincidir con la imagen

        JPanel panelDer = crearPanelDerecho();
        JPanel panelIzq = crearPanelIzquierdo();

        panelPrincipal.add(panelIzq, BorderLayout.CENTER);
        panelPrincipal.add(panelDer, BorderLayout.EAST);

        // =================================================================
        // --- NUEVO CÓDIGO PARA EL MARCO (BANNERS AZULES SUPERIOR/INFERIOR) ---
        // =================================================================
        Color colorMarco = new Color(48, 95, 135); // Color azul similar a la imagen

        // 1. Panel Superior (Franja azul arriba)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(colorMarco);
        panelSuperior.setPreferredSize(new Dimension(0, 40));

        // 2. Panel Inferior (Franja azul abajo con botón)
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelInferior.setBackground(colorMarco);
        panelInferior.setPreferredSize(new Dimension(0, 50));

        // Botón de regresar "<"
        JButton btnRegresar = new JButton("<");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 24));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setBackground(new Color(0, 0, 100)); // Azul muy oscuro
        btnRegresar.setOpaque(true);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setPreferredSize(new Dimension(60, 50));

        // Acción del botón regresar (Para que lo configures)
        btnRegresar.addActionListener(e ->
        {
            onBack.run();
        });

        panelInferior.add(btnRegresar);

        // 3. Contenedor Raíz para juntar el marco y tu panel principal
        JPanel contenedorRaiz = new JPanel(new BorderLayout());
        contenedorRaiz.setBackground(Color.WHITE);

        contenedorRaiz.add(panelSuperior, BorderLayout.NORTH); // Franja superior
        contenedorRaiz.add(panelPrincipal, BorderLayout.CENTER); // Tu contenido original
        contenedorRaiz.add(panelInferior, BorderLayout.SOUTH);   // Franja inferior

        // Añadimos el contenedor raíz al JFrame
        add(contenedorRaiz);
        // =================================================================

        this.carritoCompras.forEach(dP ->
        {
            String nombreEnLinea = dP.getProducto().getNombre().replace("\n", " ");
            String textoLista = String.format("%s (%d)   ---   $%s",
                    nombreEnLinea,
                    dP.getCantidad(),
                    dP.getSubtotal().setScale(2, RoundingMode.HALF_UP));

            modeloListaResumen.addElement(textoLista);
        });
        actualizarTotales();
    }

    private void actualizarInventario() {
        panelProductos.removeAll();
        if (inventario != null)
        {
            for (ProductoDTO producto : inventario)
            {
                panelProductos.add(crearBotonProducto(producto));
            }
        }

        panelProductos.revalidate();
        panelProductos.repaint();
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 10));
        panelIzquierdo.setBackground(Color.WHITE);

        JPanel panelCategorias = new JPanel(new GridLayout(1, 3, 10, 0));
        panelCategorias.setPreferredSize(new Dimension(0, 100));
        panelCategorias.setBackground(Color.WHITE);

        panelCategorias.add(crearBotonCategoria("CALDOS Y PLATOS", "9", new Color(244, 67, 54), "CALDOS_Y_PLATOS"));
        panelCategorias.add(crearBotonCategoria("TACOS", "8", new Color(255, 152, 0), "TACOS"));
        panelCategorias.add(crearBotonCategoria("BEBIDAS", "4", new Color(33, 33, 255), "BEBIDAS"));

        panelProductos = new JPanel(new GridLayout(0, 3, 15, 15));
        panelProductos.setBackground(Color.WHITE);

        if (inventario != null)
        {
            for (ProductoDTO producto : inventario)
            {
                panelProductos.add(crearBotonProducto(producto));
            }
        }

        // --- EL FIX EMPIEZA AQUÍ ---
        // 1. Creamos un panel envoltorio
        JPanel contenedorGrilla = new JPanel(new BorderLayout());
        contenedorGrilla.setBackground(Color.WHITE);

        // 2. Agregamos el panelProductos al NORTE de este envoltorio. 
        // Esto fuerza a la cuadrícula a respetar su altura original y agruparse arriba.
        contenedorGrilla.add(panelProductos, BorderLayout.NORTH);

        // 3. Metemos el envoltorio al JScrollPane en lugar del panelProductos directamente
        JScrollPane scrollProductos = new JScrollPane(contenedorGrilla);
        scrollProductos.setBorder(null);
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16);
        scrollProductos.getViewport().setBackground(Color.WHITE);
        // --- EL FIX TERMINA AQUÍ ---

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

        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelAgregar.setBackground(new Color(240, 240, 240));
        panelAgregar.setPreferredSize(new Dimension(350, 90));
        panelAgregar.setMaximumSize(new Dimension(400, 90));

        lblProductoSeleccionado = new JLabel("Ninguno...                ");
        lblProductoSeleccionado.setFont(new Font("Arial", Font.BOLD, 12));

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(Color.BLUE);
        btnAgregar.setForeground(Color.WHITE);
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
        btnCobrar.setOpaque(true);
        btnCobrar.setBorderPainted(false);
        btnCobrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCobrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnCobrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCobrar.addActionListener(e ->
        {
            onRegistrarVenta.accept(carritoCompras);
        });

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

    private JButton crearBotonCategoria(String titulo, String cantidad, Color color, String categoria) {
        String html = "<html><center><b>" + titulo + "</b><br><br><br>" + cantidad + "</center></html>";
        JButton btn = new JButton(html);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        btn.addActionListener(e ->
        {
            inventario = onCategoriaSeleccionada.apply(categoria);
            actualizarInventario();
        });

        return btn;
    }

    // OJO: Cambiamos el tipo de retorno de JButton a JPanel
    private JPanel crearBotonProducto(ProductoDTO producto) {

        String nombreSinEspacios = producto.getNombre().replace(" ", "_").replace("/", "_");

        // 2. Le quita los acentos a las letras (ej. Camarón -> Camaron)
        String nombreLimpio = Normalizer.normalize(nombreSinEspacios, Normalizer.Form.NFD).replaceAll("\\p{M}", "");

        // 3. Le agregamos el .png
        String nombreArchivo = nombreLimpio + ".png";

        Componentes.TarjetaProducto tarjeta = new Componentes.TarjetaProducto(
                producto.getNombre(),
                producto.getPrecio().doubleValue(),
                nombreArchivo
        );

        // Le damos el tamaño que quieres que tenga en la cuadrícula
        tarjeta.setPreferredSize(new Dimension(0, 200)); // El 0 en el ancho deja que la columna defina el ancho, pero fuerza la altura a 200px        
        // Como es un JPanel, capturamos el clic con un MouseAdapter
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Esta es la misma lógica que tenías en el botón anterior
                productoSeleccionadoActual = producto;
                lblProductoSeleccionado.setText(producto.getNombre().replace("\n", " "));
                spinnerCantidad.setValue(1);
            }
        });

        return tarjeta;
    }

    // --- LÓGICA DE NEGOCIO ---
    private void agregarAlResumen() {
        if (productoSeleccionadoActual == null)
        {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        carritoCompras.stream()
                .anyMatch(p -> p.getProducto().getNombre().equals(productoSeleccionadoActual.getNombre()));
        if (carritoCompras.stream()
                .anyMatch(p -> p.getProducto().getNombre().equals(productoSeleccionadoActual.getNombre())))
        {
            JOptionPane.showMessageDialog(this, "El producto ya está agregado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad = (Integer) spinnerCantidad.getValue();
        DetalleVentaDTO nuevoItem = new DetalleVentaDTO(
                cantidad,
                productoSeleccionadoActual.getPrecio(),
                productoSeleccionadoActual);

        carritoCompras.add(nuevoItem);

        String nombreEnLinea = productoSeleccionadoActual.getNombre().replace("\n", " ");
        String textoLista = String.format("%s (%d)   ---   $%s",
                nombreEnLinea,
                cantidad,
                nuevoItem.getSubtotal().setScale(2, RoundingMode.HALF_UP));

        modeloListaResumen.addElement(textoLista);
        actualizarTotales();
    }

    private void eliminarDelResumen() {
        int index = listaResumen.getSelectedIndex();

        if (index == -1)
        {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la lista 'Resumen' para quitarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        carritoCompras.remove(index);
        modeloListaResumen.remove(index);
        actualizarTotales();
    }

    private void cobrar() {
        if (carritoCompras.isEmpty())
        {
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

        for (DetalleVentaDTO item : carritoCompras)
        {
            granTotal = granTotal.add(item.getSubtotal());
        }

        String totalFormateado = "<html><b>$" + granTotal.setScale(2, RoundingMode.HALF_UP) + "</b></html>";
        lblSubtotal.setText(totalFormateado);
        lblTotal.setText(totalFormateado);
    }

    public static void main(String[] args) {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new PuntoVenta(
                a ->
        {
        },
                e -> new ArrayList<>(),
                null, // Ojo: Aquí mandabas null, agregué una validación arriba para evitar NullPointerException.
                () ->
        {
        },
                null
        ).setVisible(true));
    }
}
