package inicio;

import control.ControlVista;
import javax.swing.SwingUtilities;

/**
 *
 * @author Daniel
 */
public class Inicio {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControlVista cV = new ControlVista();
            cV.mostrarPantallaLogin();
        });
    }
    
}
