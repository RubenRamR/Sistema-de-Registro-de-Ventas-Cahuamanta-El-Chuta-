package controladores;

import GestionarVentas.PuntoVentaFrm;
import IniciarSesion.PantallaGestionarUsuarios;
import IniciarSesion.PantallaLogin;
import aplicacion.SesionActual;

public class PantallaGestionarUsuariosController {

    private final PantallaGestionarUsuarios vista;

    public PantallaGestionarUsuariosController(PantallaGestionarUsuarios vista) {
        this.vista = vista;
        if (SesionActual.getUsuario() != null) {
            this.vista.setNombreUsuario(SesionActual.getUsuario().getNombre());
        }
        this.vista.addAbrirPuntoVentaListener(e -> abrirPuntoVenta());
        this.vista.addCerrarSesionListener(e -> cerrarSesion());
        this.vista.addAccionUsuariosListener(e -> this.vista.mostrarMensaje("La gestion de usuarios sigue pendiente en esta rama."));
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
