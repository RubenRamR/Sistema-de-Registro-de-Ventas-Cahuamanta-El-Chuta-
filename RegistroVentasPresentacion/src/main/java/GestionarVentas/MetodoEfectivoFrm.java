package GestionarVentas;

import aplicacion.PagoEfectivoOperacion;
import excepciones.NegocioException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class MetodoEfectivoFrm extends JFrame {

    private static final Color COLOR_BARRA = new Color(18, 63, 94);
    private static final Color COLOR_FONDO = new Color(245, 247, 243);
    private static final Color COLOR_TEXTO = new Color(25, 41, 56);
    private static final Color COLOR_TARJETA = new Color(255, 255, 255);
    private static final Color COLOR_BORDE = new Color(214, 222, 230);
    private static final Color COLOR_PRIMARIO = new Color(28, 143, 124);
    private static final Color COLOR_PELIGRO = new Color(182, 60, 54);
    private static final Color COLOR_SUAVE = new Color(233, 240, 235);
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 44);
    private static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.PLAIN, 18);
    private static final Font FUENTE_ETIQUETA = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FUENTE_MONTO = new Font("Segoe UI", Font.BOLD, 30);
    private static final Font FUENTE_CAMPO = new Font("Segoe UI", Font.PLAIN, 24);

    private final BigDecimal total;
    private final PagoEfectivoOperacion onAceptar;
    private final Runnable onBack;

    private JLabel lblCambioMonto;
    private JLabel lblEstado;
    private JLabel lblPagoSugerido;
    private JTextField txtPago;
    private JButton btnAceptar;

    public MetodoEfectivoFrm(
            BigDecimal total,
            PagoEfectivoOperacion onAceptar,
            Runnable onBack
    ) {
        this.total = total == null ? BigDecimal.ZERO : total;
        this.onAceptar = onAceptar;
        this.onBack = onBack;

        setTitle("Pago en efectivo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
        add(crearBarraInferior(), BorderLayout.SOUTH);

        actualizarEstadoPago();
    }

    private JPanel crearBarraSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BARRA);
        panel.setPreferredSize(new Dimension(0, 72));

        JLabel lblMarca = new JLabel("Cobro en efectivo");
        lblMarca.setForeground(Color.WHITE);
        lblMarca.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMarca.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 0));
        panel.add(lblMarca, BorderLayout.WEST);
        return panel;
    }

        private JPanel crearContenido() {
            JPanel fondo = new JPanel(new GridBagLayout());
            fondo.setBackground(COLOR_FONDO);

            JPanel tarjeta = new JPanel();
            tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
            tarjeta.setBackground(COLOR_TARJETA);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                    BorderFactory.createEmptyBorder(36, 42, 36, 42)
            ));

            JLabel lblTitulo = new JLabel("Efectivo");
            lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
            lblTitulo.setFont(FUENTE_TITULO);
            lblTitulo.setForeground(COLOR_TEXTO);

            JLabel lblSubtitulo = new JLabel("Confirma el cobro antes de registrar la venta");
            lblSubtitulo.setAlignmentX(CENTER_ALIGNMENT);
            lblSubtitulo.setFont(FUENTE_SUBTITULO);
            lblSubtitulo.setForeground(new Color(97, 109, 122));

            tarjeta.add(lblTitulo);
            tarjeta.add(Box.createVerticalStrut(8));
            tarjeta.add(lblSubtitulo);
            tarjeta.add(Box.createVerticalStrut(28));
            tarjeta.add(crearFilaResumen("Total a cobrar", "$" + formatearMonto(total), false));
            tarjeta.add(Box.createVerticalStrut(12));
            tarjeta.add(crearPanelPago());
            tarjeta.add(Box.createVerticalStrut(12));
            tarjeta.add(crearFilaResumen("Cambio", "$0.00", true));
            tarjeta.add(Box.createVerticalStrut(12));

            lblEstado = new JLabel("Ingresa el monto recibido para continuar");
            lblEstado.setAlignmentX(CENTER_ALIGNMENT);
            lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 16));
            tarjeta.add(lblEstado);

            tarjeta.add(Box.createVerticalStrut(12));
            lblPagoSugerido = new JLabel("Monto minimo sugerido: $" + formatearMonto(total));
            lblPagoSugerido.setAlignmentX(CENTER_ALIGNMENT);
            lblPagoSugerido.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lblPagoSugerido.setForeground(new Color(101, 115, 126));
            tarjeta.add(lblPagoSugerido);

            tarjeta.add(Box.createVerticalStrut(24));
            tarjeta.add(crearPanelAcciones());

            tarjeta.setPreferredSize(new Dimension(700, tarjeta.getPreferredSize().height));

            fondo.add(tarjeta);
            return fondo;
        }

    private JPanel crearFilaResumen(String etiqueta, String valorInicial, boolean filaCambio) {
        JPanel panel = new JPanel(new BorderLayout(18, 0));
        panel.setBackground(filaCambio ? COLOR_SUAVE : COLOR_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(FUENTE_ETIQUETA);
        lblEtiqueta.setForeground(COLOR_TEXTO);

        JLabel lblValor = new JLabel(valorInicial, SwingConstants.RIGHT);
        lblValor.setFont(FUENTE_MONTO);
        lblValor.setForeground(COLOR_TEXTO);

        panel.add(lblEtiqueta, BorderLayout.WEST);
        panel.add(lblValor, BorderLayout.EAST);

        if (filaCambio)
        {
            lblCambioMonto = lblValor;
        }

        return panel;
    }

    private JPanel crearPanelPago() {
        JPanel panel = new JPanel(new BorderLayout(18, 0));
        panel.setBackground(COLOR_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));

        JLabel lblEtiqueta = new JLabel("Pago recibido");
        lblEtiqueta.setFont(FUENTE_ETIQUETA);
        lblEtiqueta.setForeground(COLOR_TEXTO);

        txtPago = new JTextField("0.00");
        txtPago.setFont(FUENTE_CAMPO);
        txtPago.setForeground(COLOR_TEXTO);
        txtPago.setHorizontalAlignment(SwingConstants.RIGHT);
        txtPago.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(187, 199, 210), 1, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        ((AbstractDocument) txtPago.getDocument()).setDocumentFilter(new DecimalFilter());
        txtPago.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarEstadoPago();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarEstadoPago();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarEstadoPago();
            }
        });

        panel.add(lblEtiqueta, BorderLayout.WEST);
        panel.add(txtPago, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_TARJETA);

        btnAceptar = crearBoton("Registrar pago", COLOR_PRIMARIO);
        btnAceptar.setPreferredSize(new Dimension(250, 54));
        btnAceptar.addActionListener(e -> confirmarPago());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 8, 0, 8);
        panel.add(btnAceptar, gbc);
        return panel;
    }

    private JPanel crearBarraInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 10));
        panel.setBackground(COLOR_BARRA);
        panel.setPreferredSize(new Dimension(0, 72));

        JButton btnBack = crearBoton("Regresar", new Color(12, 38, 57));
        btnBack.setPreferredSize(new Dimension(150, 46));
        btnBack.addActionListener(e -> onBack.run());
        panel.add(btnBack);
        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void actualizarEstadoPago() {
        BigDecimal pago = obtenerPagoIngresado();
        if (pago == null)
        {
            lblCambioMonto.setText("$0.00");
            lblEstado.setText("Ingresa un monto valido para continuar");
            lblEstado.setForeground(COLOR_PELIGRO);
            btnAceptar.setEnabled(false);
            return;
        }

        BigDecimal cambio = pago.subtract(total).setScale(2, RoundingMode.HALF_UP);
        if (cambio.signum() < 0)
        {
            BigDecimal faltante = cambio.abs();
            lblCambioMonto.setText("$0.00");
            lblEstado.setText("Faltan $" + formatearMonto(faltante) + " para completar la venta");
            lblEstado.setForeground(COLOR_PELIGRO);
            btnAceptar.setEnabled(false);
            return;
        }

        lblCambioMonto.setText("$" + formatearMonto(cambio));
        lblEstado.setText("Pago suficiente. Puedes registrar la venta.");
        lblEstado.setForeground(COLOR_PRIMARIO.darker());
        btnAceptar.setEnabled(true);
    }

    private void confirmarPago() {
        BigDecimal pago = obtenerPagoIngresado();
        if (pago == null)
        {
            JOptionPane.showMessageDialog(this, "Ingresa un monto valido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try
        {
            onAceptar.ejecutar(pago);
        } catch (NegocioException e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal obtenerPagoIngresado() {
        String texto = txtPago.getText();
        if (texto == null || texto.isBlank())
        {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        try
        {
            return new BigDecimal(texto.replace(',', '.')).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e)
        {
            return null;
        }
    }

    private String formatearMonto(BigDecimal monto) {
        BigDecimal valor = monto == null ? BigDecimal.ZERO : monto;
        return valor.setScale(2, RoundingMode.HALF_UP).toString();
    }

    private static class DecimalFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String siguiente = new StringBuilder(actual).replace(offset, offset + length, text == null ? "" : text).toString();
            if (siguiente.isBlank() || siguiente.matches("\\d{0,7}([\\.,]\\d{0,2})?"))
            {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
