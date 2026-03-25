package controladores;

import GestionarVentas.MenuCajeroFrm;
import GestionarVentas.PuntoVentaFrm;
import IniciarSesion.PantallaLogin;
import aplicacion.SesionActual;

public class MenuCajeroController {

    private final MenuCajeroFrm vista;

    public MenuCajeroController(MenuCajeroFrm vista) {
        this.vista = vista;
        if (SesionActual.getUsuario() != null) {
            this.vista.setNombreUsuario(SesionActual.getUsuario().getNombre());
        }
        this.vista.addGestionarVentasListener(e -> abrirPuntoVenta());
        this.vista.addCerrarSesionListener(e -> cerrarSesion());
    }

    private void abrirPuntoVenta() {
        PuntoVentaFrm puntoVentaFrm = new PuntoVentaFrm();
        new VentaController(puntoVentaFrm);
        puntoVentaFrm.setVisible(true);
        vista.dispose();
    }

    private void cerrarSesion() {
        SesionActual.cerrar();
        PantallaLogin login = new PantallaLogin();
        new LoginController(login);
        login.setVisible(true);
        vista.dispose();
    }
}
