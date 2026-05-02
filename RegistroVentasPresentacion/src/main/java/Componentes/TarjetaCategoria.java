package Componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import utils.EstiloUI;

public class TarjetaCategoria extends JPanel {

    private static final int RADIO = 10;

    private final Color colorBase;
    private final Color colorOscuro;
    private final String titulo;
    private boolean hover = false;
    private boolean presionado = false;

    public TarjetaCategoria(String titulo, String cantidad, Color color) {
        this.titulo = titulo;
        this.colorBase = color;
        this.colorOscuro = EstiloUI.mezclar(color, Color.BLACK, 0.30f);

        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel kicker = new JLabel("CATEGORÍA");
        kicker.setForeground(new Color(255, 255, 255, 165));
        kicker.setFont(EstiloUI.FUENTE_KICKER);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font(EstiloUI.FUENTE_SECCION.getFamily(), Font.BOLD, 19));

        JPanel norte = new JPanel();
        norte.setOpaque(false);
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.add(kicker);
        norte.add(Box.createVerticalStrut(2));
        norte.add(lblTitulo);

        JLabel lblCantidad = new JLabel(cantidad + " productos");
        lblCantidad.setForeground(new Color(255, 255, 255, 200));
        lblCantidad.setFont(new Font(EstiloUI.FUENTE_CAMPO.getFamily(), Font.PLAIN, 12));

        add(norte, BorderLayout.NORTH);
        add(lblCantidad, BorderLayout.SOUTH);

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
                System.out.println("Filtro aplicado: " + titulo);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color base;
        if (presionado) {
            base = colorOscuro;
        } else if (hover) {
            base = EstiloUI.mezclar(colorBase, Color.WHITE, 0.06f);
        } else {
            base = colorBase;
        }

        // Cuerpo
        g2.setColor(base);
        g2.fillRoundRect(0, 0, w, h, RADIO, RADIO);

        // Banda inferior 4 px en tono más oscuro: peso editorial
        RoundRectangle2D shape = new RoundRectangle2D.Float(0, 0, w, h, RADIO, RADIO);
        g2.setClip(shape);
        g2.setColor(colorOscuro);
        g2.fillRect(0, h - 4, w, 4);

        g2.dispose();
        super.paintComponent(g);
    }
}
