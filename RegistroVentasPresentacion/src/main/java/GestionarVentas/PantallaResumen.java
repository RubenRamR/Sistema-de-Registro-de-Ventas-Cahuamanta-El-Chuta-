package gestionarventas;

import dtos.DetalleVentaDTO;
import dtos.ProductoDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import utils.EstiloUI;

/**
 *
 * @author Daniel
 */
public class PantallaResumen extends JFrame {

    public PantallaResumen(
            List<DetalleVentaDTO> detalles,
            Runnable onAceptar,
            Runnable onBack
    ) {
        setTitle("Resumen de venta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        utils.EstiloUI.aplicarTamanioMinimo(this);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(EstiloUI.crearBarraSuperior("Resumen de venta"), BorderLayout.NORTH);
        add(crearContenido(detalles, onAceptar), BorderLayout.CENTER);
        add(EstiloUI.crearBarraInferior(EstiloUI.crearBotonRegresar(onBack)), BorderLayout.SOUTH);
    }

    private JPanel crearContenido(List<DetalleVentaDTO> detalles, Runnable onAceptar) {
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(EstiloUI.COLOR_FONDO);
        fondo.setBorder(new EmptyBorder(28, 36, 28, 36));

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(EstiloUI.COLOR_TARJETA);
        tarjeta.setBorder(EstiloUI.bordeTarjeta());

        JLabel lblTitulo = new JLabel("Resumen de venta");
        lblTitulo.setFont(EstiloUI.FUENTE_TITULO_PANTALLA);
        lblTitulo.setForeground(EstiloUI.COLOR_TEXTO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Revisa los productos antes de continuar al cobro");
        lblSubtitulo.setFont(EstiloUI.FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(EstiloUI.COLOR_MUTED);
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelEncabezados = new JPanel(new BorderLayout());
        panelEncabezados.setBackground(EstiloUI.COLOR_AUXILIAR);
        panelEncabezados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE, 1, true),
                new EmptyBorder(10, 16, 10, 16)
        ));
        panelEncabezados.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        panelEncabezados.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHeaderProductos = new JLabel("Productos");
        lblHeaderProductos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeaderProductos.setForeground(EstiloUI.COLOR_TEXTO);

        JLabel lblHeaderSubtotal = new JLabel("Subtotal");
        lblHeaderSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeaderSubtotal.setForeground(EstiloUI.COLOR_TEXTO);

        panelEncabezados.add(lblHeaderProductos, BorderLayout.WEST);
        panelEncabezados.add(lblHeaderSubtotal, BorderLayout.EAST);

        JPanel panelListaProductos = new JPanel();
        panelListaProductos.setLayout(new BoxLayout(panelListaProductos, BoxLayout.Y_AXIS));
        panelListaProductos.setBackground(EstiloUI.COLOR_TARJETA);

        BigDecimal granTotal = BigDecimal.ZERO;
        for (DetalleVentaDTO detalle : detalles) {
            JPanel filaPanel = new JPanel(new BorderLayout());
            filaPanel.setBackground(EstiloUI.COLOR_TARJETA);
            filaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            filaPanel.setBorder(new EmptyBorder(8, 16, 8, 16));

            String textoProducto = detalle.getCantidad() + "  " + detalle.getProducto().getNombre();
            JLabel lblProducto = new JLabel(textoProducto);
            lblProducto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lblProducto.setForeground(EstiloUI.COLOR_TEXTO);

            JLabel lblSubtotal = new JLabel("$" + detalle.getSubtotal().toString());
            lblSubtotal.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lblSubtotal.setForeground(EstiloUI.COLOR_TEXTO);

            filaPanel.add(lblProducto, BorderLayout.WEST);
            filaPanel.add(lblSubtotal, BorderLayout.EAST);

            panelListaProductos.add(filaPanel);
            granTotal = granTotal.add(detalle.getSubtotal());
        }

        JScrollPane scrollProductos = new JScrollPane(panelListaProductos);
        scrollProductos.setBorder(BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE, 1, true));
        scrollProductos.getViewport().setBackground(EstiloUI.COLOR_TARJETA);
        scrollProductos.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollProductos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 360));

        JPanel panelTotal = new JPanel(new BorderLayout());
        panelTotal.setBackground(EstiloUI.COLOR_AUXILIAR);
        panelTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        panelTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTotal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE, 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblTextoTotal = new JLabel("Total");
        lblTextoTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTextoTotal.setForeground(EstiloUI.COLOR_TEXTO);

        JLabel lblMontoTotal = new JLabel("$" + granTotal.toString());
        lblMontoTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblMontoTotal.setForeground(EstiloUI.COLOR_TEXTO);

        panelTotal.add(lblTextoTotal, BorderLayout.WEST);
        panelTotal.add(lblMontoTotal, BorderLayout.EAST);

        JButton btnAceptar = EstiloUI.crearBoton("Continuar al cobro", EstiloUI.COLOR_ACCION);
        btnAceptar.setPreferredSize(new Dimension(220, 46));
        btnAceptar.setMaximumSize(new Dimension(220, 46));
        btnAceptar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAceptar.addActionListener(e -> onAceptar.run());

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createVerticalStrut(22));
        tarjeta.add(panelEncabezados);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(scrollProductos);
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(panelTotal);
        tarjeta.add(Box.createVerticalStrut(22));
        tarjeta.add(btnAceptar);

        fondo.add(tarjeta, BorderLayout.CENTER);
        return fondo;
    }

    // Método Main para probar la pantalla directamente
    public static void main(String[] args) {
        // Simulamos datos de tu DTO
        ProductoDTO p1 = new ProductoDTO(1L, "Taco de Cahuamanta", new BigDecimal("35.00"));
        ProductoDTO p2 = new ProductoDTO(2L, "Taco de Aleta", new BigDecimal("90.00"));

        DetalleVentaDTO d1 = new DetalleVentaDTO(1L, 1, new BigDecimal("35.00"), new BigDecimal("35.00"), p1);
        DetalleVentaDTO d2 = new DetalleVentaDTO(2L, 2, new BigDecimal("90.00"), new BigDecimal("180.00"), p2);

        List<DetalleVentaDTO> listaDePrueba = new ArrayList<>();
        listaDePrueba.add(d1);
        listaDePrueba.add(d2);

        SwingUtilities.invokeLater(() -> {
            new PantallaResumen(
                    listaDePrueba, 
                    () -> {},
                    () -> {}
            ).setVisible(true);
        });
    }
}