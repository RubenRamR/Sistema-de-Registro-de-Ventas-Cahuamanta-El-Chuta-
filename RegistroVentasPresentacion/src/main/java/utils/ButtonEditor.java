/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author rramirez
 */
public class ButtonEditor extends javax.swing.DefaultCellEditor {

    protected javax.swing.JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor(javax.swing.JCheckBox checkBox) {
        super(checkBox);
        button = new javax.swing.JButton();
        button.setOpaque(true);
        button.setBackground(EstiloUI.COLOR_SECUNDARIO);
        button.setForeground(java.awt.Color.WHITE);
        button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = (value == null) ? "Ver Detalle" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed)
        {
            javax.swing.JOptionPane.showMessageDialog(button, "Clic en Ver Detalle. Fila: " + label);
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
