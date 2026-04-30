/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author rramirez
 */
public class ButtonRenderer extends javax.swing.JButton implements javax.swing.table.TableCellRenderer {


    public ButtonRenderer() {
        setOpaque(true);
        setBackground(EstiloUI.COLOR_SECUNDARIO);
        setForeground(java.awt.Color.WHITE);
        setFont(EstiloUI.FUENTE_TABLA_HEADER);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Ver Detalle" : value.toString());
        return this;

    }
}
