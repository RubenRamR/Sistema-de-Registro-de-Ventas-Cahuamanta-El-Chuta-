package GenerarReportes;

import com.github.lgooddatepicker.components.DatePicker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Daniel
 */
public class Reportes extends JFrame {

    // --- Paleta de Colores ---
    private final Color COLOR_HEADER_FOOTER = new Color(65, 114, 159);
    private final Color COLOR_OSCURO = new Color(11, 19, 84);
    private final Color COLOR_BEIGE = new Color(248, 246, 240);
    private final Color COLOR_BORDE = new Color(220, 220, 215);
    private final Color COLOR_ROJO = new Color(178, 34, 34); // Rojo oscuro para el PDF

    public Reportes(
            Runnable onBack
    ) {
        setTitle("Seleccionar Periodo");
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
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.anchor = GridBagConstraints.CENTER;

        // 1. Título
        JLabel lblTitulo = new JLabel("Reportes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblTitulo.setForeground(COLOR_OSCURO);
        
        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(0, 0, 20, 0); 
        centerPanel.add(lblTitulo, gbcMain);

        // 2. Tarjeta Beige (Fechas + Tabla + Botón)
        JPanel cardPanel = crearTarjetaReportes();
        
        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(cardPanel, gbcMain);

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

    private JPanel crearTarjetaReportes() {
        JPanel panel = new JPanel(new BorderLayout(0, 20)); // Espacio vertical entre zonas
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));
        panel.setPreferredSize(new Dimension(800, 450)); // Tamaño fijo para la tarjeta

        // --- ZONA SUPERIOR: Selector de Fechas ---
        JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelFechas.setBackground(COLOR_BEIGE);

        panelFechas.add(crearInputFecha("Fecha Inicio:"));
        panelFechas.add(crearInputFecha("Fecha Fin:"));

        panel.add(panelFechas, BorderLayout.NORTH);

        // --- ZONA CENTRAL: Tabla Estilizada ---
        JTable tabla = crearTablaAesthetica();
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        scrollPane.getViewport().setBackground(Color.WHITE); // Fondo blanco para la tabla
        
        panel.add(scrollPane, BorderLayout.CENTER);

        // --- ZONA INFERIOR: Botón PDF ---
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(COLOR_BEIGE);

        JButton btnPdf = new JButton("Descargar PDF");
        btnPdf.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnPdf.setForeground(Color.WHITE);
        btnPdf.setBackground(COLOR_ROJO);
        btnPdf.setFocusPainted(false);
        btnPdf.setOpaque(true);
        btnPdf.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPdf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_ROJO.darker(), 1, true),
                new EmptyBorder(10, 30, 10, 30)
        ));

        panelBoton.add(btnPdf);
        panel.add(panelBoton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearInputFecha(String etiqueta) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(COLOR_BEIGE);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        DatePicker datePicker = new DatePicker();
        datePicker.getComponentDateTextField().setEditable(false);

        panel.add(lbl);
        panel.add(datePicker); 
        
        return panel;
    }

    private JTable crearTablaAesthetica() {
        String[] columnas = {"Fecha", "Venta", "Total"};
        Object[][] datos = {
                {"24/11/25  10:00", "#101", "$250.00"},
                {"24/11/25  10:01", "#102", "$300.00"},
                {"24/11/25  10:02", "#103", "$150.00"},
                {"24/11/25  10:15", "#104", "$420.00"},
                {"24/11/25  10:30", "#105", "$80.00"}
        };

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable directamente
            }
        };

        JTable tabla = new JTable(modelo);

        // Estética general de la tabla
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabla.setRowHeight(35); // Filas más altas para respirar
        tabla.setShowVerticalLines(false); // Quitar líneas verticales
        tabla.setGridColor(COLOR_BORDE); // Color de línea horizontal muy suave
        tabla.setSelectionBackground(new Color(230, 240, 255)); // Azul muy claro al seleccionar
        tabla.setSelectionForeground(Color.BLACK);

        // Estética del Encabezado (Header)
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setBorder(new LineBorder(COLOR_BORDE, 1)); // Borde sutil al encabezado
        header.setPreferredSize(new Dimension(100, 40)); // Encabezado más alto

        // Alinear el contenido de las celdas a la izquierda con un pequeño margen
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        renderer.setBorder(new EmptyBorder(0, 10, 0, 0)); // Margen interno
        
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        return tabla;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Reportes(
                    () -> {}
            ).setVisible(true);
        });
    }
}
