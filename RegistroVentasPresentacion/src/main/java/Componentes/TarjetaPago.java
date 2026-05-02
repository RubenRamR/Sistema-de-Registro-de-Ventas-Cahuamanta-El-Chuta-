package componentes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import utils.EstiloUI;

public class TarjetaPago extends JPanel {

    private static final int RADIO = 12;

    private final Color colorAcento;
    private final String texto;
    private boolean hover = false;
    private boolean presionado = false;
    private Image icono;

    public TarjetaPago(String texto, String rutaIcono, Runnable onClick) {
        this(texto, rutaIcono, onClick, EstiloUI.COLOR_ACCION);
    }

    public TarjetaPago(String texto, String rutaIcono, Runnable onClick, Color colorAcento) {
        this.texto = texto;
        this.colorAcento = colorAcento;

        setPreferredSize(new Dimension(260, 200));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setLayout(null);

        try {
            java.net.URL url = getClass().getResource("/imagenes/" + rutaIcono);
            if (url != null) {
                icono = new ImageIcon(url).getImage();
            }
        } catch (Exception e) {
            icono = null;
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                presionado = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                presionado = true;
                repaint();
                System.out.println("Seleccionado: " + texto);
                onClick.run();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                presionado = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();

        Color fondo;
        if (presionado) {
            fondo = EstiloUI.COLOR_AUXILIAR;
        } else if (hover) {
            fondo = EstiloUI.COLOR_TARJETA_BEIGE;
        } else {
            fondo = EstiloUI.COLOR_TARJETA;
        }

        // Cuerpo
        g2.setColor(fondo);
        g2.fillRoundRect(0, 0, w, h, RADIO, RADIO);

        // Banda lateral acentuada (clip a esquinas redondeadas)
        RoundRectangle2D shape = new RoundRectangle2D.Float(0, 0, w, h, RADIO, RADIO);
        g2.setClip(shape);
        g2.setColor(colorAcento);
        g2.fillRect(0, 0, 6, h);
        g2.setClip(null);

        // Borde
        g2.setColor(hover ? EstiloUI.COLOR_BORDE_INPUT : EstiloUI.COLOR_BORDE);
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f, RADIO, RADIO));

        // Icono centrado
        int iconSize = 80;
        if (icono != null) {
            int x = (w - iconSize) / 2 + 3;
            int y = 28;
            g2.drawImage(icono, x, y, iconSize, iconSize, null);
        }

        // Texto centrado abajo (serif)
        g2.setFont(new Font(EstiloUI.FUENTE_SECCION.getFamily(), Font.BOLD, 20));
        g2.setColor(EstiloUI.COLOR_TEXTO);
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(texto)) / 2 + 3;
        int ty = h - 26;
        g2.drawString(texto, tx, ty);

        g2.dispose();
    }
}
