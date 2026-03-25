package GestionarVentas;

import dtos.DetalleVentaDTO;
import dtos.ProductoDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class PuntoVentaFrm extends JFrame {

    private final JLabel lblUsuario = new JLabel("Punto de venta");
    private final JTable tblProductos = new JTable();
    private final JTable tblCarrito = new JTable();
    private final JSpinner spCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    private final JButton btnAgregar = new JButton("Agregar al carrito");
    private final JButton btnPagar = new JButton("Seleccionar metodo de pago");
    private final JButton btnVolver = new JButton("Volver");
    private final JLabel lblTotal = new JLabel("Total: $0.00");
    private final DefaultTableModel modeloProductos = new DefaultTableModel(new Object[]{"ID", "Producto", "Precio"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final DefaultTableModel modeloCarrito = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private List<ProductoDTO> productos = new ArrayList<>();

    public PuntoVentaFrm() {
        setTitle("Punto de venta");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        construirVista();
    }

    private void construirVista() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#2F6690"));
        header.setPreferredSize(new Dimension(0, 60));
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(lblUsuario, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        tblProductos.setModel(modeloProductos);
        tblCarrito.setModel(modeloCarrito);
        tblProductos.setRowHeight(28);
        tblCarrito.setRowHeight(28);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.55;
        center.add(new JScrollPane(tblProductos), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.45;
        center.add(new JScrollPane(tblCarrito), gbc);

        JPanel acciones = new JPanel(new GridBagLayout());
        acciones.setBackground(Color.WHITE);
        GridBagConstraints ac = new GridBagConstraints();
        ac.insets = new Insets(5, 5, 5, 5);

        ac.gridx = 0;
        acciones.add(new JLabel("Cantidad:"), ac);
        ac.gridx = 1;
        acciones.add(spCantidad, ac);
        ac.gridx = 2;
        acciones.add(btnAgregar, ac);
        ac.gridx = 3;
        acciones.add(btnPagar, ac);
        ac.gridx = 4;
        acciones.add(btnVolver, ac);
        ac.gridx = 5;
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        acciones.add(lblTotal, ac);

        add(center, BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH);
    }

    public void setNombreUsuario(String nombre) {
        lblUsuario.setText("Punto de venta - " + nombre);
    }

    public void setProductos(List<ProductoDTO> productos) {
        this.productos = productos;
        modeloProductos.setRowCount(0);
        for (ProductoDTO producto : productos) {
            modeloProductos.addRow(new Object[]{producto.getIdProducto(), producto.getNombre(), producto.getPrecio()});
        }
    }

    public ProductoDTO getProductoSeleccionado() {
        int fila = tblProductos.getSelectedRow();
        if (fila < 0 || fila >= productos.size()) {
            return null;
        }
        return productos.get(fila);
    }

    public int getCantidadSeleccionada() {
        return (Integer) spCantidad.getValue();
    }

    public void actualizarCarrito(List<DetalleVentaDTO> detalles, BigDecimal total) {
        modeloCarrito.setRowCount(0);
        for (DetalleVentaDTO detalle : detalles) {
            modeloCarrito.addRow(new Object[]{detalle.getNombreProducto(), detalle.getCantidad(), detalle.getPrecioUnitario(), detalle.getSubtotal()});
        }
        lblTotal.setText("Total: $" + total.setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    public void addAgregarListener(ActionListener listener) {
        btnAgregar.addActionListener(listener);
    }

    public void addPagarListener(ActionListener listener) {
        btnPagar.addActionListener(listener);
    }

    public void addVolverListener(ActionListener listener) {
        btnVolver.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
