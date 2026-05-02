package gestionarventas;

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

    private static final Color COLOR_BARRA = utils.EstiloUI.COLOR_BARRA;
    private static final Color COLOR_FONDO = utils.EstiloUI.COLOR_FONDO;
    private static final Color COLOR_TEXTO = utils.EstiloUI.COLOR_TEXTO;
    private static final Color COLOR_TARJETA = utils.EstiloUI.COLOR_TARJETA;
    private static final Color COLOR_BORDE = utils.EstiloUI.COLOR_BORDE;
    private static final Color COLOR_PRIMARIO = utils.EstiloUI.COLOR_ACCION;
    private static final Color COLOR_PELIGRO = utils.EstiloUI.COLOR_DANGER;
    private static final Color COLOR_SUAVE = utils.EstiloUI.COLOR_AUXILIAR;
    private static final Font FUENTE_TITULO = utils.EstiloUI.FUENTE_TITULO_PANTALLA;
    private static final Font FUENTE_SUBTITULO = new Font(utils.EstiloUI.FUENTE_SUBTITULO.getFamily(), Font.PLAIN, 16);
    private static final Font FUENTE_ETIQUETA = new Font(utils.EstiloUI.FUENTE_ETIQUETA.getFamily(), Font.BOLD, 17);
    private static final Font FUENTE_MONTO = utils.EstiloUI.FUENTE_TOTAL;
    private static final Font FUENTE_CAMPO = new Font(utils.EstiloUI.FUENTE_CAMPO.getFamily(), Font.PLAIN, 22);

    private final BigDecimal total;
    private final PagoEfectivoOperacion onAceptar;
    private final Runnable onBack;

    private static final String PLACEHOLDER_PAGO = "0.00";
    private static final Color COLOR_PLACEHOLDER = utils.EstiloUI.COLOR_MUTED;

    private JLabel lblCambioMonto;
    private JLabel lblEstado;
    private JLabel lblPagoSugerido;
    private JTextField txtPago;
    private JButton btnAceptar;
    private boolean placeholderActivo = true;

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
        utils.EstiloUI.aplicarTamanioMinimo(this);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
        add(crearBarraInferior(), BorderLayout.SOUTH);

        actualizarEstadoPago();
    }

    private JPanel crearBarraSuperior() {
        return utils.EstiloUI.crearHeader("Cobro en efectivo", "Paso 3 de 3");
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
            lblSubtitulo.setForeground(utils.EstiloUI.COLOR_MUTED);

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
            lblPagoSugerido = new JLabel("Monto mínimo sugerido: $" + formatearMonto(total));
            lblPagoSugerido.setAlignmentX(CENTER_ALIGNMENT);
            lblPagoSugerido.setFont(utils.EstiloUI.FUENTE_SUBTITULO);
            lblPagoSugerido.setForeground(utils.EstiloUI.COLOR_MUTED);
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

        txtPago = new JTextField();
        txtPago.setFont(FUENTE_CAMPO);
        txtPago.setHorizontalAlignment(SwingConstants.RIGHT);
        txtPago.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(utils.EstiloUI.COLOR_BORDE_INPUT, 1, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        mostrarPlaceholder();
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
        txtPago.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (placeholderActivo) {
                    placeholderActivo = false;
                    txtPago.setText("");
                    txtPago.setForeground(COLOR_TEXTO);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtPago.getText().isBlank()) {
                    mostrarPlaceholder();
                }
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
        JButton btnBack = utils.EstiloUI.crearBoton("←  Regresar", utils.EstiloUI.COLOR_BARRA);
        btnBack.setPreferredSize(new Dimension(160, 42));
        btnBack.addActionListener(e -> onBack.run());
        return utils.EstiloUI.crearBarraInferior(btnBack);
    }

    private JButton crearBoton(String texto, Color color) {
        return utils.EstiloUI.crearBoton(texto, color);
    }

    private void actualizarEstadoPago() {
        if (placeholderActivo)
        {
            lblCambioMonto.setText("$0.00");
            lblEstado.setText("Ingresa el monto recibido para continuar");
            lblEstado.setForeground(utils.EstiloUI.COLOR_MUTED);
            btnAceptar.setEnabled(false);
            return;
        }

        BigDecimal pago = obtenerPagoIngresado();
        if (pago == null)
        {
            lblCambioMonto.setText("$0.00");
            lblEstado.setText("Ingresa un monto válido para continuar");
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

    private void mostrarPlaceholder() {
        placeholderActivo = true;
        txtPago.setForeground(COLOR_PLACEHOLDER);
        txtPago.setText(PLACEHOLDER_PAGO);
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
