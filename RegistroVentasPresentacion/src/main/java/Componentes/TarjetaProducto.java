/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class TarjetaProducto extends JPanel {

    private Image backgroundImage;
    private final Color defaultBackgroundColor = new Color(200, 200, 200);
    private final Color pressedOverlayColor = new Color(0, 0, 0, 80);
    private Color currentOverlayColor = new Color(0, 0, 0, 0);

    public TarjetaProducto(String nombre, double precio, String rutaImagen) {
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(0, 220));
        setLayout(new BorderLayout());

        try
        {
            java.net.URL imgURL = getClass().getResource("/imagenes/" + rutaImagen);
            if (imgURL != null)
            {
                backgroundImage = new ImageIcon(imgURL).getImage();
            }
        } catch (Exception e)
        {
            backgroundImage = null;
        }

        JPanel pnlTexto = new JPanel();
        pnlTexto.setOpaque(false);
        pnlTexto.setLayout(new BoxLayout(pnlTexto, BoxLayout.Y_AXIS));
        pnlTexto.setBorder(new EmptyBorder(0, 15, 10, 0));

        String estiloShadow = "style='color: white; text-shadow: 2px 2px 3px black, -1px -1px 3px black, 1px -1px 3px black, -1px 1px 3px black;'";

        JLabel lblNombre = new JLabel("<html><body " + estiloShadow + ">" + nombre + "</body></html>");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNombre.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        JLabel lblPrecio = new JLabel("<html><body " + estiloShadow + ">" + String.format("$%.2f", precio) + "</body></html>");
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPrecio.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        pnlTexto.add(lblNombre);
        pnlTexto.add(lblPrecio);

        add(pnlTexto, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentOverlayColor = pressedOverlayColor;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentOverlayColor = new Color(0, 0, 0, 0);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (currentOverlayColor.getAlpha() > 0)
                {
                    currentOverlayColor = new Color(0, 0, 0, 0);
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RoundRectangle2D.Float roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25);
        g2.setClip(roundedRect);

        if (backgroundImage != null)
        {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

            LinearGradientPaint gradiente = new LinearGradientPaint(
                    0, getHeight() / 2, 0, getHeight(),
                    new float[]
                    {
                        0.0f, 1.0f
                    },
                    new Color[]
                    {
                        new Color(0, 0, 0, 0), new Color(0, 0, 0, 180)
                    }
            );
            g2.setPaint(gradiente);
            g2.fill(roundedRect);
        } else
        {
            g2.setColor(defaultBackgroundColor);
            g2.fill(roundedRect);
        }

        if (currentOverlayColor.getAlpha() > 0)
        {
            g2.setColor(currentOverlayColor);
            g2.fill(roundedRect);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
