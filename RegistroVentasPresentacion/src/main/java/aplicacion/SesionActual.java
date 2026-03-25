package aplicacion;

import dtos.UsuarioDTO;

public final class SesionActual {

    private static UsuarioDTO usuario;

    private SesionActual() {
    }

    public static void iniciar(UsuarioDTO usuarioSesion) {
        usuario = usuarioSesion;
    }

    public static UsuarioDTO getUsuario() {
        return usuario;
    }

    public static void cerrar() {
        usuario = null;
    }
}
