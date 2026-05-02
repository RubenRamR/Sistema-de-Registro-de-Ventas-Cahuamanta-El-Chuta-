package iniciarsesion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.util.function.BiConsumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import utils.EstiloUI;

public class PantallaLogin extends JFrame {

    public PantallaLogin(BiConsumer<String, String> onIniciarSesion) {
        setTitle("El Chuta — Iniciar sesión");
        utils.EstiloUI.aplicarTamanioMinimo(this);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel split = new JPanel(new GridBagLayout());
        split.setBackground(EstiloUI.COLOR_FONDO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Panel marca (izquierda)
        gbc.gridx = 0;
        gbc.weightx = 0.55;
        split.add(crearPanelMarca(), gbc);

        // Panel formulario (derecha)
        gbc.gridx = 1;
        gbc.weightx = 0.45;
        split.add(crearPanelFormulario(onIniciarSesion), gbc);

        add(split, BorderLayout.CENTER);
    }

    private JPanel crearPanelMarca() {
        JPanel marca = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Degradado vertical sutil del navy del Mar de Cortés
                GradientPaint gp = new GradientPaint(
                        0, 0, EstiloUI.COLOR_BARRA,
                        0, getHeight(), EstiloUI.COLOR_BARRA_OSCURA);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // "Olas" decorativas en la esquina inferior — geometría sencilla, no clichés
                g2.setColor(new Color(255, 255, 255, 14));
                int baseY = getHeight() - 80;
                for (int i = 0; i < 3; i++) {
                    int y = baseY + i * 26;
                    g2.fillOval(-200 + i * 40, y, 600, 220);
                }

                // Regla horizontal acento bajo el wordmark
                g2.dispose();
            }
        };
        marca.setLayout(new GridBagLayout());
        marca.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, EstiloUI.COLOR_BARRA_ACENTO));

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(new EmptyBorder(0, 64, 0, 64));

        JLabel kicker = new JLabel("RESTAURANTE DE MARISCOS · SONORA");
        kicker.setForeground(new Color(220, 200, 170));
        kicker.setFont(EstiloUI.FUENTE_KICKER);
        kicker.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel wordmark = new JLabel("El Chuta");
        wordmark.setForeground(Color.WHITE);
        wordmark.setFont(new Font(EstiloUI.FUENTE_WORDMARK.getFamily(), Font.BOLD, 80));
        wordmark.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Cahuamanta · Tacos · Caldos");
        sub.setForeground(new Color(255, 246, 230));
        sub.setFont(new Font(EstiloUI.FUENTE_SECCION.getFamily(), Font.ITALIC, 24));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel regla = new JPanel();
        regla.setOpaque(true);
        regla.setBackground(EstiloUI.COLOR_BARRA_ACENTO);
        regla.setMaximumSize(new Dimension(86, 3));
        regla.setPreferredSize(new Dimension(86, 3));
        regla.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel(
                "<html><div style='width:380px; line-height:140%;'>"
                + "Sistema de registro de ventas para el punto de venta del local. "
                + "Inicia sesión para abrir la caja del turno."
                + "</div></html>");
        tagline.setForeground(new Color(220, 200, 170));
        tagline.setFont(EstiloUI.FUENTE_SUBTITULO);
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        contenido.add(kicker);
        contenido.add(Box.createVerticalStrut(14));
        contenido.add(wordmark);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(sub);
        contenido.add(Box.createVerticalStrut(22));
        contenido.add(regla);
        contenido.add(Box.createVerticalStrut(22));
        contenido.add(tagline);

        marca.add(contenido);
        return marca;
    }

    private JPanel crearPanelFormulario(BiConsumer<String, String> onIniciarSesion) {
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBackground(EstiloUI.COLOR_FONDO);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(0, 48, 0, 64));

        JLabel kicker = new JLabel("ACCESO AL SISTEMA");
        kicker.setFont(EstiloUI.FUENTE_KICKER);
        kicker.setForeground(EstiloUI.COLOR_ACCION);
        kicker.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("Iniciar sesión");
        titulo.setFont(EstiloUI.FUENTE_TITULO_PANTALLA);
        titulo.setForeground(EstiloUI.COLOR_TEXTO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Captura tu usuario y contraseña para entrar al punto de venta.");
        sub.setFont(EstiloUI.FUENTE_SUBTITULO);
        sub.setForeground(EstiloUI.COLOR_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtUsuario = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();

        form.add(kicker);
        form.add(Box.createVerticalStrut(10));
        form.add(titulo);
        form.add(Box.createVerticalStrut(8));
        form.add(sub);
        form.add(Box.createVerticalStrut(34));
        form.add(crearCampo("Usuario", txtUsuario));
        form.add(Box.createVerticalStrut(18));
        form.add(crearCampo("Contraseña", txtContrasena));
        form.add(Box.createVerticalStrut(28));

        JButton btnIniciar = EstiloUI.crearBoton("Entrar al sistema", EstiloUI.COLOR_ACCION);
        btnIniciar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnIniciar.setPreferredSize(new Dimension(0, 48));
        btnIniciar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnIniciar.addActionListener(e -> onIniciarSesion.accept(
                txtUsuario.getText(),
                new String(txtContrasena.getPassword())));
        form.add(btnIniciar);

        form.add(Box.createVerticalStrut(20));
        JLabel pie = new JLabel("Si no recuerdas tu contraseña, pídele al dueño que la restablezca.");
        pie.setFont(new Font(EstiloUI.FUENTE_CAMPO.getFamily(), Font.ITALIC, 12));
        pie.setForeground(EstiloUI.COLOR_MUTED);
        pie.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(pie);

        form.setMaximumSize(new Dimension(540, 600));
        form.setPreferredSize(new Dimension(540, 600));

        fondo.add(form);
        return fondo;
    }

    private JPanel crearCampo(String etiqueta, JTextField campo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(EstiloUI.FUENTE_ETIQUETA);
        lbl.setForeground(EstiloUI.COLOR_TEXTO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        campo.setFont(EstiloUI.FUENTE_CAMPO);
        campo.setBorder(EstiloUI.bordeInput());
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(7));
        panel.add(campo);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaLogin ventana = new PantallaLogin((a, b) -> {
            });
            ventana.setVisible(true);
        });
    }
}
