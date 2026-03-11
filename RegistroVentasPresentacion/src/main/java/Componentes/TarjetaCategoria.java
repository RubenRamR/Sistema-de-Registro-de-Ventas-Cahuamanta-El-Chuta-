/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class TarjetaCategoria extends JPanel {
    
    private Color colorActual;
    private final Color colorBase;
    private final Color colorPresionado;

    public TarjetaCategoria(String titulo, String cantidad, Color color) {
        this.colorBase = color;
        this.colorActual = color;
        this.colorPresionado = color.darker();
        
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setLayout(new BorderLayout());

        // Título Centrado
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.CENTER);

        // Número en la esquina
        JLabel lblCantidad = new JLabel(cantidad);
        lblCantidad.setForeground(Color.WHITE);
        lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCantidad.setBorder(new EmptyBorder(0, 10, 5, 0));
        add(lblCantidad, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                colorActual = colorPresionado;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                colorActual = colorBase;
                repaint();
                
                // Insertar logica para filtrar la tabla
                System.out.println("Filtro aplicado: " + titulo);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                //
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                colorActual = colorBase;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(colorActual);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        
        g2.dispose();
        super.paintComponent(g);
    }
}