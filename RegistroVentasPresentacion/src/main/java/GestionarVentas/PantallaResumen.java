package GestionarVentas;

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

/**
 *
 * @author Daniel
 */
public class PantallaResumen extends JFrame {

    // Colores basados en tu imagen
    private final Color COLOR_BANNER = new Color(51, 102, 153); // Azul medio/grisáceo
    private final Color COLOR_TITULO = new Color(0, 0, 102); // Azul marino oscuro
    private final Color COLOR_TEXTO_AZUL = new Color(30, 30, 180); // Azul vibrante
    private final Color COLOR_BOTON = new Color(0, 115, 190); // Azul claro para el botón

    public PantallaResumen(
            List<DetalleVentaDTO> detalles,
            Runnable onAceptar,
            Runnable onBack
    ) {
        setTitle("Resumen de Compra");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana, no toda la app
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. BANNER SUPERIOR
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(COLOR_BANNER);
        panelNorte.setPreferredSize(new Dimension(0, 60)); // Alto fijo, ancho dinámico
        add(panelNorte, BorderLayout.NORTH);

        // 2. BANNER INFERIOR (MODIFICADO PARA INCLUIR EL BOTÓN)
        // Usamos FlowLayout.LEFT con márgenes en 0 para pegar el botón a la izquierda
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        panelSur.setBackground(COLOR_BANNER);
        panelSur.setPreferredSize(new Dimension(0, 60));

        // --- NUEVO: Botón de regresar "<" ---
        JButton btnRegresar = new JButton("<");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 24));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setBackground(new Color(0, 0, 100)); // Azul oscuro para contrastar
        btnRegresar.setOpaque(true);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setPreferredSize(new Dimension(70, 60)); // Mismo alto que el panelSur (60)
        
        // Acción: Cierra esta ventana (PantallaResumen) y te deja en la anterior
        btnRegresar.addActionListener(e -> onBack.run()); 

        panelSur.add(btnRegresar);
        add(panelSur, BorderLayout.SOUTH);

        // 3. CONTENIDO CENTRAL (BLANCO)
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBackground(Color.WHITE);
        panelCentro.setBorder(new EmptyBorder(30, 50, 30, 50)); // Márgenes internos (Arriba, Izquierda, Abajo, Derecha)

        // --- Título Principal ---
        JLabel lblTitulo = new JLabel("Resumen de Venta");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitulo.setForeground(COLOR_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // --- Encabezados de Tabla ---
        JPanel panelEncabezados = new JPanel(new BorderLayout());
        panelEncabezados.setBackground(Color.WHITE);
        panelEncabezados.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel lblHeaderProductos = new JLabel("Productos");
        lblHeaderProductos.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblHeaderProductos.setForeground(COLOR_TEXTO_AZUL);
        
        JLabel lblHeaderSubtotal = new JLabel("Subtotal");
        lblHeaderSubtotal.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblHeaderSubtotal.setForeground(COLOR_TEXTO_AZUL);
        
        panelEncabezados.add(lblHeaderProductos, BorderLayout.WEST);
        panelEncabezados.add(lblHeaderSubtotal, BorderLayout.EAST);

        // --- Lista Dinámica de Productos ---
        JPanel panelListaProductos = new JPanel();
        panelListaProductos.setLayout(new BoxLayout(panelListaProductos, BoxLayout.Y_AXIS));
        panelListaProductos.setBackground(Color.WHITE);
        
        BigDecimal granTotal = BigDecimal.ZERO;

        // Recorremos tu DTO para armar las filas
        for (DetalleVentaDTO detalle : detalles) {
            JPanel filaPanel = new JPanel(new BorderLayout());
            filaPanel.setBackground(Color.WHITE);
            filaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            filaPanel.setBorder(new EmptyBorder(5, 0, 5, 0)); // Separación entre filas

            // Ejemplo: "1 Taco de Cahuamanta"
            String textoProducto = detalle.getCantidad() + " " + detalle.getProducto().getNombre();
            JLabel lblProducto = new JLabel(textoProducto);
            lblProducto.setFont(new Font("SansSerif", Font.PLAIN, 16));

            // Ejemplo: "$35"
            JLabel lblSubtotal = new JLabel("$" + detalle.getSubtotal().toString());
            lblSubtotal.setFont(new Font("SansSerif", Font.PLAIN, 16));

            filaPanel.add(lblProducto, BorderLayout.WEST);
            filaPanel.add(lblSubtotal, BorderLayout.EAST);
            
            panelListaProductos.add(filaPanel);
            
            // Sumamos al total general
            granTotal = granTotal.add(detalle.getSubtotal());
        }

        // Envolver la lista en un ScrollPane sin bordes (por si son muchos productos)
        JScrollPane scrollProductos = new JScrollPane(panelListaProductos);
        scrollProductos.setBorder(BorderFactory.createEmptyBorder());
        scrollProductos.setBackground(Color.WHITE);
        scrollProductos.getViewport().setBackground(Color.WHITE);

        // --- Total ---
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.setBackground(Color.WHITE);
        panelTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JLabel lblTextoTotal = new JLabel("Total:  ");
        lblTextoTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTextoTotal.setForeground(COLOR_TEXTO_AZUL);
        
        JLabel lblMontoTotal = new JLabel("$" + granTotal.toString());
        lblMontoTotal.setFont(new Font("SansSerif", Font.PLAIN, 18));
        
        panelTotal.add(lblTextoTotal);
        panelTotal.add(lblMontoTotal);

        // --- Botón Aceptar ---
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnAceptar.setBackground(COLOR_BOTON);
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        // Truco para que respete el color
        btnAceptar.setOpaque(true);
        btnAceptar.setBorderPainted(false);
        btnAceptar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAceptar.setPreferredSize(new Dimension(150, 40));
        btnAceptar.setMaximumSize(new Dimension(150, 40));
        
        btnAceptar.addActionListener(e -> onAceptar.run());

        // --- Ensamblar el Centro ---
        panelCentro.add(lblTitulo);
        panelCentro.add(Box.createVerticalStrut(40)); // Espacio en blanco
        panelCentro.add(panelEncabezados);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(scrollProductos);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelTotal);
        panelCentro.add(Box.createVerticalStrut(30));
        panelCentro.add(btnAceptar);

        add(panelCentro, BorderLayout.CENTER);
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