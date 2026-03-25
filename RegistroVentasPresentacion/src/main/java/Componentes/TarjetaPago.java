/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Componentes;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class TarjetaPago extends JPanel {

    private final Color colorBase = new Color(245, 245, 245);
    private final Color colorHover = new Color(230, 230, 230);
    private Color colorActual = colorBase;
    private Image icono;

    public TarjetaPago(String texto, String rutaIcono, Runnable onClick) {
        setPreferredSize(new Dimension(250, 180));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setLayout(new BorderLayout());

        try
        {
            java.net.URL imgURL = getClass().getResource("/imagenes/" + rutaIcono);
            if (imgURL != null)
            {
                icono = new ImageIcon(imgURL).getImage();
            }
        } catch (Exception e)
        {
        }

        JLabel lblTexto = new JLabel(texto, SwingConstants.CENTER);
        lblTexto.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTexto.setForeground(new Color(0, 0, 153));
        lblTexto.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(lblTexto, BorderLayout.NORTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                colorActual = colorHover;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                colorActual = colorBase;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Aquí disparas la lógica de pago
                System.out.println("Seleccionado: " + texto);
                onClick.run();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(colorActual);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        if (icono != null)
        {
            int size = 75;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2 + 10;
            g2.drawImage(icono, x, y, size, size, null);
        }

        g2.dispose();
    }
}
