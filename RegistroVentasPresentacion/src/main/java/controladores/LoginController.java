package controladores;

import GestionarVentas.MenuCajeroFrm;
import IniciarSesion.PantallaGestionarUsuarios;
import IniciarSesion.PantallaLogin;
import aplicacion.AplicacionContexto;
import aplicacion.SesionActual;
import dtos.UsuarioDTO;
import java.text.Normalizer;
import java.util.Locale;

public class LoginController {

    private final PantallaLogin vista;

    public LoginController(PantallaLogin vista) {
        this.vista = vista;
        this.vista.addLoginListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String nombre = vista.getNombreUsuario();
        String contrasenia = vista.getContrasenia();

        if (nombre.isBlank() || contrasenia.isBlank()) {
            vista.mostrarError("Captura usuario y contrasenia.");
            return;
        }

        UsuarioDTO usuario = AplicacionContexto.getUsuarioBO().iniciarSesion(nombre, contrasenia);
        if (usuario == null) {
            vista.mostrarError("Credenciales invalidas.");
            return;
        }

        SesionActual.iniciar(usuario);
        abrirVistaPorRol(usuario);
        vista.dispose();
    }

    private void abrirVistaPorRol(UsuarioDTO usuario) {
        String rol = normalizar(usuario.getTipoNombre());

        if (rol.contains("dueno") || rol.contains("duenio") || rol.contains("admin") || rol.contains("propietario")) {
            PantallaGestionarUsuarios pantalla = new PantallaGestionarUsuarios();
            new PantallaGestionarUsuariosController(pantalla);
            pantalla.setVisible(true);
            return;
        }

        if (rol.contains("cajero")) {
            MenuCajeroFrm menu = new MenuCajeroFrm();
            new MenuCajeroController(menu);
            menu.setVisible(true);
            return;
        }

        vista.mostrarError("El usuario no tiene un tipo valido para entrar al sistema.");
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return "";
        }
        String normalizado = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalizado.toLowerCase(Locale.ROOT);
    }
}
