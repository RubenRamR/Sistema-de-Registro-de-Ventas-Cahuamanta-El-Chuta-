package utils;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Identidad visual de "El Chuta — Cahuamanta".
 * Paleta inspirada en mariscos sonorenses: papel cálido, navy del Mar de
 * Cortés, acento terracota tipo chile guajillo. Tipografías mezclan serif
 * (Georgia) para titulares editoriales y Segoe UI para formularios.
 */
public final class EstiloUI {

    private EstiloUI() {
    }

    // === PALETA ===
    // Fondos
    public static final Color COLOR_FONDO          = new Color(247, 242, 232);
    public static final Color COLOR_FONDO_ALT      = new Color(241, 233, 218);
    public static final Color COLOR_TARJETA        = new Color(255, 253, 248);
    public static final Color COLOR_TARJETA_BEIGE  = new Color(245, 238, 224);

    // Marca / barras
    public static final Color COLOR_BARRA          = new Color(20, 50, 70);
    public static final Color COLOR_BARRA_OSCURA   = new Color(13, 33, 47);
    public static final Color COLOR_BARRA_ACENTO   = new Color(204, 92, 38);

    // Texto / bordes
    public static final Color COLOR_TEXTO          = new Color(36, 28, 22);
    public static final Color COLOR_MUTED          = new Color(112, 99, 86);
    public static final Color COLOR_BORDE          = new Color(220, 208, 188);
    public static final Color COLOR_BORDE_INPUT    = new Color(190, 175, 152);

    // Acciones
    public static final Color COLOR_ACCION         = new Color(204, 92, 38);
    public static final Color COLOR_ACCION_OSCURA  = new Color(170, 70, 24);
    public static final Color COLOR_SECUNDARIO     = new Color(76, 142, 145);
    public static final Color COLOR_DANGER         = new Color(168, 50, 45);
    public static final Color COLOR_AUXILIAR       = new Color(232, 222, 199);

    // Categorías coordinadas
    public static final Color COLOR_CATEGORIA_ROJO    = new Color(170, 60, 50);
    public static final Color COLOR_CATEGORIA_NARANJA = new Color(204, 122, 48);
    public static final Color COLOR_CATEGORIA_MORADO  = new Color(98, 70, 110);
    public static final Color COLOR_CATEGORIA_AZUL    = new Color(50, 95, 120);

    // === FUENTES ===
    private static final String SERIF   = familiaDisponible("Georgia", "Cambria", "Times New Roman");
    private static final String SANS    = familiaDisponible("Segoe UI", "Tahoma", "Arial");
    private static final String SANS_SB = familiaDisponible("Segoe UI Semibold", "Segoe UI", "Tahoma");

    public static final Font FUENTE_WORDMARK        = new Font(SERIF,   Font.BOLD,  26);
    public static final Font FUENTE_TITULO_PANTALLA = new Font(SERIF,   Font.BOLD,  36);
    public static final Font FUENTE_TITULO_BARRA    = new Font(SANS_SB, Font.PLAIN, 16);
    public static final Font FUENTE_SECCION         = new Font(SERIF,   Font.BOLD,  22);

    public static final Font FUENTE_SUBTITULO       = new Font(SANS,    Font.PLAIN, 15);
    public static final Font FUENTE_ETIQUETA        = new Font(SANS_SB, Font.PLAIN, 14);
    public static final Font FUENTE_CAMPO           = new Font(SANS,    Font.PLAIN, 15);
    public static final Font FUENTE_BOTON           = new Font(SANS_SB, Font.PLAIN, 14);
    public static final Font FUENTE_BOTON_GRANDE    = new Font(SANS_SB, Font.PLAIN, 17);
    public static final Font FUENTE_TABLA           = new Font(SANS,    Font.PLAIN, 13);
    public static final Font FUENTE_TABLA_HEADER    = new Font(SANS_SB, Font.PLAIN, 13);
    public static final Font FUENTE_PRECIO          = new Font(SERIF,   Font.BOLD,  22);
    public static final Font FUENTE_TOTAL           = new Font(SERIF,   Font.BOLD,  34);
    public static final Font FUENTE_KICKER          = new Font(SANS_SB, Font.PLAIN, 11);

    private static String familiaDisponible(String... candidatas) {
        Set<String> instaladas = new HashSet<>();
        for (String f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            instaladas.add(f);
        }
        for (String f : candidatas) {
            if (instaladas.contains(f)) {
                return f;
            }
        }
        return Font.DIALOG;
    }

    // === HEADER GLOBAL ===

    /**
     * Header con wordmark "El Chuta", subtítulo del módulo, regla acento abajo.
     * @param tituloPantalla aparece en mayúsculas pequeñas como kicker
     */
    public static JPanel crearBarraSuperior(String tituloPantalla) {
        return crearHeader(tituloPantalla, null);
    }

    public static JPanel crearHeader(String tituloPantalla, String kickerDerecha) {
        JPanel cont = new JPanel(new BorderLayout());
        cont.setBackground(COLOR_BARRA);
        cont.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_BARRA_ACENTO));
        cont.setPreferredSize(new Dimension(0, 78));

        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));

        JPanel marca = new JPanel();
        marca.setOpaque(false);
        marca.setLayout(new BoxLayout(marca, BoxLayout.Y_AXIS));

        JLabel wm = new JLabel("El Chuta");
        wm.setFont(FUENTE_WORDMARK);
        wm.setForeground(Color.WHITE);
        wm.setAlignmentX(Component.LEFT_ALIGNMENT);

        String subtxt = (tituloPantalla == null || tituloPantalla.isEmpty())
                ? "CAHUAMANTA · MARISCOS"
                : tituloPantalla.toUpperCase();
        JLabel sub = new JLabel(subtxt);
        sub.setFont(FUENTE_KICKER);
        sub.setForeground(new Color(220, 200, 170));
        sub.setBorder(BorderFactory.createEmptyBorder(2, 1, 0, 0));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        marca.add(wm);
        marca.add(sub);
        fila.add(marca, BorderLayout.WEST);

        if (kickerDerecha != null && !kickerDerecha.isEmpty()) {
            JLabel right = new JLabel(kickerDerecha.toUpperCase());
            right.setFont(FUENTE_KICKER);
            right.setForeground(new Color(220, 200, 170));
            fila.add(right, BorderLayout.EAST);
        }

        cont.add(fila, BorderLayout.CENTER);
        return cont;
    }

    public static JPanel crearBarraInferior(JButton... botones) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12));
        panel.setBackground(COLOR_BARRA_OSCURA);
        panel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_BARRA_ACENTO));
        panel.setPreferredSize(new Dimension(0, 64));
        for (JButton b : botones) {
            panel.add(b);
        }
        return panel;
    }

    // === BOTONES ===

    /**
     * Botón sólido, sin bordes pintados, con hover e indicador de presionado
     * dibujados a mano para evitar el look L&F del sistema.
     */
    public static JButton crearBoton(String texto, final Color fondo) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c;
                if (!isEnabled()) {
                    c = mezclar(fondo, Color.GRAY, 0.55f);
                } else if (getModel().isPressed()) {
                    c = mezclar(fondo, Color.BLACK, 0.18f);
                } else if (getModel().isRollover()) {
                    c = mezclar(fondo, Color.WHITE, 0.10f);
                } else {
                    c = fondo;
                }
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(FUENTE_BOTON);
        b.setForeground(Color.WHITE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        return b;
    }

    public static JButton crearBotonRegresar(Runnable onBack) {
        JButton b = crearBoton("←  Regresar", COLOR_BARRA);
        b.setPreferredSize(new Dimension(160, 42));
        b.addActionListener(e -> onBack.run());
        return b;
    }

    /**
     * Botón secundario: fondo crema, texto oscuro, borde sutil.
     */
    public static JButton crearBotonGhost(String texto) {
        JButton b = new JButton(texto);
        b.setFont(FUENTE_BOTON);
        b.setForeground(COLOR_TEXTO);
        b.setBackground(COLOR_TARJETA);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // === BORDES ===

    public static Border bordeTarjeta() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(26, 28, 26, 28));
    }

    public static Border bordeInput() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_INPUT, 1, true),
                BorderFactory.createEmptyBorder(9, 12, 9, 12));
    }

    // === FÁBRICAS DE LABELS ===

    public static JLabel crearTituloPantalla(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_TITULO_PANTALLA);
        l.setForeground(COLOR_TEXTO);
        return l;
    }

    public static JLabel crearSubtitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_SUBTITULO);
        l.setForeground(COLOR_MUTED);
        return l;
    }

    public static JLabel crearKicker(String texto) {
        JLabel l = new JLabel(texto.toUpperCase());
        l.setFont(FUENTE_KICKER);
        l.setForeground(COLOR_ACCION);
        return l;
    }

    /**
     * Regla horizontal fina, color borde, útil para separar secciones.
     */
    public static JPanel crearRegla() {
        JPanel r = new JPanel();
        r.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        r.setBackground(COLOR_BORDE);
        return r;
    }

    /**
     * Mezcla dos colores en proporción t (0..1).
     */
    public static Color mezclar(Color a, Color b, float t) {
        return new Color(
                Math.round(a.getRed()   * (1 - t) + b.getRed()   * t),
                Math.round(a.getGreen() * (1 - t) + b.getGreen() * t),
                Math.round(a.getBlue()  * (1 - t) + b.getBlue()  * t));
    }
}
