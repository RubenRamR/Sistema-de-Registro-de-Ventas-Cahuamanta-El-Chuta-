package GestionarVentas;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MetodoPagoFrm extends JFrame {

    private final JButton btnEfectivo = new JButton("Efectivo");
    private final JButton btnTarjeta = new JButton("Tarjeta");
    private final JButton btnTransferencia = new JButton("Transferencia");
    private final JButton btnCancelar = new JButton("Cancelar");

    public MetodoPagoFrm(BigDecimal total) {
        setTitle("Metodo de pago");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Total a cobrar: $" + total.setScale(2, RoundingMode.HALF_UP).toPlainString(), JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JPanel opciones = new JPanel(new GridLayout(2, 2, 10, 10));
        opciones.add(btnEfectivo);
        opciones.add(btnTarjeta);
        opciones.add(btnTransferencia);
        opciones.add(btnCancelar);
        opciones.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(opciones, BorderLayout.CENTER);

        btnEfectivo.setActionCommand("Efectivo");
        btnTarjeta.setActionCommand("Tarjeta");
        btnTransferencia.setActionCommand("Transferencia");
    }

    public void addSeleccionMetodoListener(ActionListener listener) {
        btnEfectivo.addActionListener(listener);
        btnTarjeta.addActionListener(listener);
        btnTransferencia.addActionListener(listener);
    }

    public void addCancelarListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }
}
