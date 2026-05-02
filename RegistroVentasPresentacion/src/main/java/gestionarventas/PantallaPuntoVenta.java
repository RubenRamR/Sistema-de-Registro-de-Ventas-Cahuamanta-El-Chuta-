package gestionarventas;

import componentes.TarjetaProducto;
import dtos.DetalleVentaDTO;
import dtos.ProductoDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import utils.EstiloUI;

/**
 *
 * @author Chris/luise
 */
public class PantallaPuntoVenta extends JFrame {

    private List<ProductoDTO> inventario;
    private final List<ProductoDTO> inventarioCompleto;
    private List<DetalleVentaDTO> carritoCompras;

    private JPanel panelProductos;
    private DefaultListModel<DetalleVentaDTO> modeloCarrito;
    private JList<DetalleVentaDTO> listaCarrito;
    private JLabel lblTotal;
    private JButton btnQuitarUno;
    private JButton btnQuitarTodo;

    private final List<JLabel> chipsCategoria = new ArrayList<>();
    private String categoriaActiva = null;

    private final Consumer<List<DetalleVentaDTO>> onRegistrarVenta;
    private final Function<String, List<ProductoDTO>> onCategoriaSeleccionada;
    private final Runnable onBack;

    public PantallaPuntoVenta(
            Consumer<List<DetalleVentaDTO>> onRegistrarVenta,
            Function<String, List<ProductoDTO>> onCategoriaSeleccionada,
            List<ProductoDTO> inventario,
            Runnable onBack,
            List<DetalleVentaDTO> carritoCompras
    ) {
        this.onRegistrarVenta = onRegistrarVenta;
        this.onCategoriaSeleccionada = onCategoriaSeleccionada;
        this.inventario = inventario != null ? inventario : new ArrayList<>();
        this.inventarioCompleto = new ArrayList<>(this.inventario);
        this.onBack = onBack;
        this.carritoCompras = carritoCompras != null ? carritoCompras : new ArrayList<>();

        setTitle("Punto de venta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utils.EstiloUI.aplicarTamanioMinimo(this);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(EstiloUI.crearBarraSuperior("Punto de venta"), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
        add(EstiloUI.crearBarraInferior(EstiloUI.crearBotonRegresar(onBack)), BorderLayout.SOUTH);

        for (DetalleVentaDTO item : this.carritoCompras) {
            modeloCarrito.addElement(item);
        }
        actualizarTotal();
        actualizarBotonesCarrito();
    }

    private JPanel crearContenido() {
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBackground(EstiloUI.COLOR_FONDO);
        fondo.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 12);
        fondo.add(crearLadoIzquierdo(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 12, 0, 0);
        fondo.add(crearLadoDerecho(), gbc);

        return fondo;
    }

    private JPanel crearLadoIzquierdo() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(EstiloUI.COLOR_FONDO);

        JPanel barraCategorias = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barraCategorias.setOpaque(false);
        barraCategorias.add(crearChip("Todos", null));
        barraCategorias.add(crearChip("Caldos y platos", "CALDOS_Y_PLATOS"));
        barraCategorias.add(crearChip("Tacos", "TACOS"));
        barraCategorias.add(crearChip("Bebidas", "BEBIDAS"));
        panel.add(barraCategorias, BorderLayout.NORTH);

        panelProductos = new JPanel(new GridLayout(0, 3, 16, 16));
        panelProductos.setBackground(EstiloUI.COLOR_FONDO);
        cargarProductos();

        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setBackground(EstiloUI.COLOR_FONDO);
        envoltorio.add(panelProductos, BorderLayout.NORTH);

        JScrollPane scrollProductos = new JScrollPane(envoltorio);
        scrollProductos.setBorder(BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE, 1, true));
        scrollProductos.getViewport().setBackground(EstiloUI.COLOR_FONDO);
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollProductos, BorderLayout.CENTER);
        return panel;
    }

    private JLabel crearChip(String texto, String categoriaId) {
        JLabel chip = new JLabel(texto, SwingConstants.CENTER);
        chip.setOpaque(true);
        chip.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chip.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chip.putClientProperty("categoriaId", categoriaId);
        chip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarCategoria(categoriaId);
            }
        });
        chipsCategoria.add(chip);
        aplicarEstadoChip(chip, categoriaId == null);
        return chip;
    }

    private void aplicarEstadoChip(JLabel chip, boolean activo) {
        if (activo) {
            chip.setBackground(EstiloUI.COLOR_BARRA);
            chip.setForeground(Color.WHITE);
        } else {
            chip.setBackground(EstiloUI.COLOR_TARJETA);
            chip.setForeground(EstiloUI.COLOR_TEXTO);
        }
        chip.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE, 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
    }

    private void seleccionarCategoria(String categoriaId) {
        categoriaActiva = categoriaId;
        for (JLabel chip : chipsCategoria) {
            String id = (String) chip.getClientProperty("categoriaId");
            boolean activo = (id == null && categoriaId == null) || (id != null && id.equals(categoriaId));
            aplicarEstadoChip(chip, activo);
        }

        if (categoriaId == null) {
            inventario = new ArrayList<>(inventarioCompleto);
        } else {
            List<ProductoDTO> filtrado = onCategoriaSeleccionada.apply(categoriaId);
            inventario = filtrado != null ? filtrado : new ArrayList<>();
        }
        cargarProductos();
    }

    private void cargarProductos() {
        panelProductos.removeAll();
        for (ProductoDTO producto : inventario) {
            panelProductos.add(crearTarjetaProducto(producto));
        }
        panelProductos.revalidate();
        panelProductos.repaint();
    }

    private TarjetaProducto crearTarjetaProducto(ProductoDTO producto) {
        String nombreSinEspacios = producto.getNombre().replace(" ", "_").replace("/", "_");
        String nombreLimpio = Normalizer.normalize(nombreSinEspacios, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        String nombreArchivo = nombreLimpio + ".png";

        TarjetaProducto tarjeta = new TarjetaProducto(
                producto.getNombre(),
                producto.getPrecio().doubleValue(),
                nombreArchivo
        );
        tarjeta.setPreferredSize(new Dimension(0, 220));
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                agregarProductoUno(producto);
            }
        });
        return tarjeta;
    }

    private JPanel crearLadoDerecho() {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 14));
        tarjeta.setBackground(EstiloUI.COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE, 1, true),
                new EmptyBorder(20, 22, 20, 22)
        ));

        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setOpaque(false);

        JLabel lblTitulo = new JLabel("Carrito");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(EstiloUI.COLOR_TEXTO);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblPista = new JLabel("Toca un producto para agregarlo");
        lblPista.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPista.setForeground(EstiloUI.COLOR_MUTED);
        lblPista.setAlignmentX(LEFT_ALIGNMENT);

        encabezado.add(lblTitulo);
        encabezado.add(Box.createVerticalStrut(2));
        encabezado.add(lblPista);
        tarjeta.add(encabezado, BorderLayout.NORTH);

        modeloCarrito = new DefaultListModel<>();
        listaCarrito = new JList<>(modeloCarrito);
        listaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaCarrito.setCellRenderer(new RenderItemCarrito());
        listaCarrito.setBackground(EstiloUI.COLOR_TARJETA);
        listaCarrito.setFixedCellHeight(48);
        listaCarrito.addListSelectionListener(e -> actualizarBotonesCarrito());

        JScrollPane scrollLista = new JScrollPane(listaCarrito);
        scrollLista.setBorder(BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE, 1, true));
        scrollLista.getViewport().setBackground(EstiloUI.COLOR_TARJETA);

        JPanel acciones = new JPanel(new GridLayout(1, 2, 8, 0));
        acciones.setOpaque(false);
        btnQuitarUno = EstiloUI.crearBoton("Quitar uno", EstiloUI.COLOR_SECUNDARIO);
        btnQuitarUno.setEnabled(false);
        btnQuitarUno.addActionListener(e -> quitarUnoSeleccionado());
        btnQuitarTodo = EstiloUI.crearBoton("Quitar todo", EstiloUI.COLOR_DANGER);
        btnQuitarTodo.setEnabled(false);
        btnQuitarTodo.addActionListener(e -> quitarTodoSeleccionado());
        acciones.add(btnQuitarUno);
        acciones.add(btnQuitarTodo);

        JPanel central = new JPanel(new BorderLayout(0, 12));
        central.setOpaque(false);
        central.add(scrollLista, BorderLayout.CENTER);
        central.add(acciones, BorderLayout.SOUTH);
        tarjeta.add(central, BorderLayout.CENTER);

        JPanel pie = new JPanel();
        pie.setLayout(new BoxLayout(pie, BoxLayout.Y_AXIS));
        pie.setOpaque(false);

        JPanel filaTotal = new JPanel(new BorderLayout());
        filaTotal.setBackground(EstiloUI.COLOR_AUXILIAR);
        filaTotal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE, 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));
        filaTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        filaTotal.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblEtiquetaTotal = new JLabel("Total");
        lblEtiquetaTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEtiquetaTotal.setForeground(EstiloUI.COLOR_TEXTO);

        lblTotal = new JLabel("$0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(EstiloUI.COLOR_TEXTO);

        filaTotal.add(lblEtiquetaTotal, BorderLayout.WEST);
        filaTotal.add(lblTotal, BorderLayout.EAST);

        JButton btnCobrar = EstiloUI.crearBoton("Cobrar", EstiloUI.COLOR_ACCION);
        btnCobrar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnCobrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        btnCobrar.setPreferredSize(new Dimension(0, 56));
        btnCobrar.setAlignmentX(LEFT_ALIGNMENT);
        btnCobrar.addActionListener(e -> {
            if (carritoCompras.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El carrito esta vacio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            onRegistrarVenta.accept(carritoCompras);
        });

        pie.add(filaTotal);
        pie.add(Box.createVerticalStrut(12));
        pie.add(btnCobrar);
        tarjeta.add(pie, BorderLayout.SOUTH);

        return tarjeta;
    }

    private void agregarProductoUno(ProductoDTO producto) {
        int indice = -1;
        for (int i = 0; i < carritoCompras.size(); i++) {
            if (carritoCompras.get(i).getProducto().getNombre().equals(producto.getNombre())) {
                indice = i;
                break;
            }
        }

        if (indice >= 0) {
            DetalleVentaDTO item = carritoCompras.get(indice);
            item.setCantidad(item.getCantidad() + 1);
            modeloCarrito.set(indice, item);
            listaCarrito.setSelectedIndex(indice);
        } else {
            DetalleVentaDTO nuevo = new DetalleVentaDTO(1, producto.getPrecio(), producto);
            carritoCompras.add(nuevo);
            modeloCarrito.addElement(nuevo);
        }

        actualizarTotal();
        actualizarBotonesCarrito();
    }

    private void quitarUnoSeleccionado() {
        int indice = listaCarrito.getSelectedIndex();
        if (indice < 0) {
            return;
        }
        DetalleVentaDTO item = carritoCompras.get(indice);
        if (item.getCantidad() > 1) {
            item.setCantidad(item.getCantidad() - 1);
            modeloCarrito.set(indice, item);
            listaCarrito.setSelectedIndex(indice);
        } else {
            carritoCompras.remove(indice);
            modeloCarrito.remove(indice);
        }
        actualizarTotal();
        actualizarBotonesCarrito();
    }

    private void quitarTodoSeleccionado() {
        int indice = listaCarrito.getSelectedIndex();
        if (indice < 0) {
            return;
        }
        carritoCompras.remove(indice);
        modeloCarrito.remove(indice);
        actualizarTotal();
        actualizarBotonesCarrito();
    }

    private void actualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVentaDTO item : carritoCompras) {
            total = total.add(item.getSubtotal());
        }
        lblTotal.setText("$" + total.setScale(2, RoundingMode.HALF_UP));
    }

    private void actualizarBotonesCarrito() {
        boolean haySeleccion = listaCarrito.getSelectedIndex() >= 0;
        btnQuitarUno.setEnabled(haySeleccion);
        btnQuitarTodo.setEnabled(haySeleccion);
    }

    private static class RenderItemCarrito extends JPanel implements ListCellRenderer<DetalleVentaDTO> {

        private final JLabel lblCantidad = new JLabel();
        private final JLabel lblNombre = new JLabel();
        private final JLabel lblSubtotal = new JLabel();

        RenderItemCarrito() {
            setLayout(new BorderLayout(12, 0));
            setBorder(new EmptyBorder(8, 14, 8, 14));

            lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblCantidad.setForeground(EstiloUI.COLOR_BARRA);
            lblCantidad.setPreferredSize(new Dimension(38, 24));

            lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);

            add(lblCantidad, BorderLayout.WEST);
            add(lblNombre, BorderLayout.CENTER);
            add(lblSubtotal, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends DetalleVentaDTO> list,
                DetalleVentaDTO value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            lblCantidad.setText(value.getCantidad() + "x");
            lblNombre.setText(value.getProducto().getNombre().replace("\n", " "));
            lblSubtotal.setText("$" + value.getSubtotal().setScale(2, RoundingMode.HALF_UP));

            if (isSelected) {
                setBackground(EstiloUI.COLOR_AUXILIAR);
            } else {
                setBackground(EstiloUI.COLOR_TARJETA);
            }
            lblNombre.setForeground(EstiloUI.COLOR_TEXTO);
            lblSubtotal.setForeground(EstiloUI.COLOR_TEXTO);
            setOpaque(true);
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PantallaPuntoVenta(
                a -> {
                },
                e -> new ArrayList<>(),
                null,
                () -> {
                },
                null
        ).setVisible(true));
    }
}
