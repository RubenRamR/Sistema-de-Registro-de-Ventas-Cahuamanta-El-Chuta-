package Control;

import GestionarVentas.MenuCajeroFrm;
import IniciarSesion.PantallaLogin;
import excepciones.NegocioException;
import fachada.INegocio;
import fachada.Negocio;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class ControlVista {
    
    INegocio negocio;
    JFrame frameActual;

    public ControlVista() {
        this.negocio = new Negocio();
    }
    
    public void mostrarPantallaLogin() {
        frameActual = new PantallaLogin((usuario, contrasenia) -> {
            try {
                negocio.iniciarSesion(usuario, contrasenia);
                mostrarPnlMenuCajero();
            } catch(NegocioException e) {
                JOptionPane.showMessageDialog(frameActual, e.getMessage(), "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            }
        });
        frameActual.setVisible(true);
    }
    
    public void mostrarPnlMenuCajero() {
        frameActual = new MenuCajeroFrm();
        frameActual.setVisible(true);
    }
}
