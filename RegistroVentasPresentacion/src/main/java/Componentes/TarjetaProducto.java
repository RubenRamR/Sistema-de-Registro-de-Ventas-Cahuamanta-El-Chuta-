package Componentes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import utils.EstiloUI;

public class TarjetaProducto extends JPanel {

    private static final int RADIO = 12;

    private Image imgFondo;
    private boolean hover = false;
    private boolean presionado = false;
    private final String nombre;
    private final double precio;

    public TarjetaProducto(String nombre, double precio, String rutaImagen) {
        this.nombre = nombre;
        this.precio = precio;
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(0, 220));
        setLayout(null);

        try {
            java.net.URL url = getClass().getResource("/imagenes/" + rutaImagen);
            if (url != null) {
                imgFondo = new ImageIcon(url).getImage();
            }
        } catch (Exception e) {
            imgFondo = null;
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
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();

        RoundRectangle2D shape = new RoundRectangle2D.Float(0, 0, w, h, RADIO, RADIO);
        g2.clip(shape);

        // Imagen o placeholder
        if (imgFondo != null) {
            g2.drawImage(imgFondo, 0, 0, w, h, this);
        } else {
            g2.setColor(EstiloUI.COLOR_TARJETA_BEIGE);
            g2.fillRect(0, 0, w, h);
            g2.setColor(EstiloUI.COLOR_BORDE);
            g2.setFont(new Font(EstiloUI.FUENTE_SECCION.getFamily(), Font.BOLD, 28));
            FontMetrics fm = g2.getFontMetrics();
            String marca = "El Chuta";
            g2.drawString(marca, (w - fm.stringWidth(marca)) / 2, h / 2 - 16);
        }

        // Gradiente inferior para legibilidad
        int bandH = Math.max(86, h / 2);
        LinearGradientPaint grad = new LinearGradientPaint(
                0, h - bandH, 0, h,
                new float[]{0f, 1f},
                new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, 205)});
        g2.setPaint(grad);
        g2.fillRect(0, h - bandH, w, bandH);

        // Nombre + precio sobre el gradiente
        int padX = 16;
        int padBottom = 14;

        Font fNombre = new Font(EstiloUI.FUENTE_BOTON.getFamily(), Font.BOLD, 14);
        Font fPrecio = new Font(EstiloUI.FUENTE_PRECIO.getFamily(), Font.BOLD, 22);

        FontMetrics fmP = g2.getFontMetrics(fPrecio);
        int yPrecio = h - padBottom - fmP.getDescent();

        FontMetrics fmN = g2.getFontMetrics(fNombre);
        int yNombre = yPrecio - fmP.getAscent() - 6;

        // Recorte con elipsis si no cabe
        String nombreDibujado = nombre;
        int maxAncho = w - padX * 2;
        if (fmN.stringWidth(nombre) > maxAncho) {
            String tmp = nombre;
            while (tmp.length() > 0 && fmN.stringWidth(tmp + "…") > maxAncho) {
                tmp = tmp.substring(0, tmp.length() - 1);
            }
            nombreDibujado = tmp.trim() + "…";
        }

        g2.setColor(Color.WHITE);
        g2.setFont(fNombre);
        g2.drawString(nombreDibujado, padX, yNombre);

        g2.setFont(fPrecio);
        g2.drawString(String.format("$%.0f", precio), padX, yPrecio);

        // Estados
        if (presionado) {
            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillRect(0, 0, w, h);
        } else if (hover) {
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(EstiloUI.COLOR_ACCION);
            g2.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, RADIO, RADIO));
        }

        g2.dispose();
    }
}
