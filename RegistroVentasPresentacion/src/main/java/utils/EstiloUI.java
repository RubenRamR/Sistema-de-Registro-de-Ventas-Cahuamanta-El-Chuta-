package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Paleta y helpers de estilo compartidos entre todas las pantallas.
 * Centraliza colores, fuentes y construccion de barras/botones para mantener
 * un look consistente sin duplicar valores en cada JFrame.
 */
public final class EstiloUI {

    private EstiloUI() {
    }

    public static final Color COLOR_BARRA = new Color(18, 63, 94);
    public static final Color COLOR_BARRA_OSCURA = new Color(12, 38, 57);
    public static final Color COLOR_FONDO = new Color(244, 247, 243);
    public static final Color COLOR_TARJETA = new Color(255, 255, 255);
    public static final Color COLOR_TARJETA_BEIGE = new Color(248, 246, 240);
    public static final Color COLOR_TEXTO = new Color(25, 41, 56);
    public static final Color COLOR_MUTED = new Color(92, 105, 117);
    public static final Color COLOR_BORDE = new Color(214, 222, 230);
    public static final Color COLOR_BORDE_INPUT = new Color(187, 199, 210);
    public static final Color COLOR_ACCION = new Color(28, 143, 124);
    public static final Color COLOR_SECUNDARIO = new Color(214, 124, 37);
    public static final Color COLOR_DANGER = new Color(182, 60, 54);
    public static final Color COLOR_AUXILIAR = new Color(223, 235, 229);

    public static final Color COLOR_CATEGORIA_ROJO = new Color(178, 70, 70);
    public static final Color COLOR_CATEGORIA_NARANJA = new Color(206, 130, 60);
    public static final Color COLOR_CATEGORIA_MORADO = new Color(122, 84, 152);
    public static final Color COLOR_CATEGORIA_AZUL = new Color(60, 110, 168);

    public static final Font FUENTE_TITULO_BARRA = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FUENTE_TITULO_PANTALLA = new Font("Segoe UI", Font.BOLD, 40);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.PLAIN, 17);
    public static final Font FUENTE_ETIQUETA = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FUENTE_CAMPO = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FUENTE_TABLA = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 14);

    public static JPanel crearBarraSuperior(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BARRA);
        panel.setPreferredSize(new Dimension(0, 72));

        JLabel lbl = new JLabel(titulo);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(FUENTE_TITULO_BARRA);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 0));
        panel.add(lbl, BorderLayout.WEST);
        return panel;
    }

    public static JPanel crearBarraInferior(JButton... botones) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 10));
        panel.setBackground(COLOR_BARRA);
        panel.setPreferredSize(new Dimension(0, 72));
        for (JButton b : botones) {
            panel.add(b);
        }
        return panel;
    }

    public static JButton crearBoton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setForeground(Color.WHITE);
        boton.setBackground(fondo);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    public static JButton crearBotonRegresar(Runnable onBack) {
        JButton boton = crearBoton("Regresar", COLOR_BARRA_OSCURA);
        boton.setPreferredSize(new Dimension(150, 46));
        boton.addActionListener(e -> onBack.run());
        return boton;
    }

    public static Border bordeTarjeta() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(30, 32, 30, 32)
        );
    }

    public static Border bordeInput() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_INPUT, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }
}
