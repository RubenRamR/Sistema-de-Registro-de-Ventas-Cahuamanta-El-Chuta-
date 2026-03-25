package GestionarVentas;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

public class MetodoEfectivoFrm extends JFrame {

    private final JLabel lblTotalMonto = new JLabel();
    private final JTextField txtPago = new JTextField("0", 12);
    private final JLabel lblCambioMonto = new JLabel("0.00");
    private final JButton btnAceptar = new JButton("Aceptar");
    private final JButton btnCancelar = new JButton("Cancelar");

    public MetodoEfectivoFrm(BigDecimal total) {
        setTitle("Pago en efectivo");
        setSize(500, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        construirVista(total);
    }

    private void construirVista(BigDecimal total) {
        JPanel contenido = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titulo = new JLabel("Cobro en efectivo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contenido.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        contenido.add(new JLabel("Total:"), gbc);
        gbc.gridx = 1;
        lblTotalMonto.setText(total.setScale(2, RoundingMode.HALF_UP).toPlainString());
        contenido.add(lblTotalMonto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contenido.add(new JLabel("Pago recibido:"), gbc);
        gbc.gridx = 1;
        contenido.add(txtPago, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contenido.add(new JLabel("Cambio:"), gbc);
        gbc.gridx = 1;
        contenido.add(lblCambioMonto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        contenido.add(btnAceptar, gbc);
        gbc.gridx = 1;
        contenido.add(btnCancelar, gbc);

        add(contenido, BorderLayout.CENTER);
    }

    public BigDecimal getPagoRecibido() {
        try {
            return new BigDecimal(txtPago.getText().trim());
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }

    public void setCambio(BigDecimal cambio) {
        lblCambioMonto.setText(cambio.setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    public void addActualizarPagoListener(DocumentListener listener) {
        txtPago.getDocument().addDocumentListener(listener);
    }

    public void addAceptarListener(ActionListener listener) {
        btnAceptar.addActionListener(listener);
    }

    public void addCancelarListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
