package fachada;

import bos.UsuarioBO;
import daos.UsuarioDAO;
import excepciones.NegocioException;

/**
 *
 * @author Daniel
 */
public class Negocio implements INegocio {
    
    UsuarioBO usuarioBO;

    public Negocio() {
        usuarioBO = new UsuarioBO(new UsuarioDAO());
    }

    @Override
    public void iniciarSesion(String nombre, String contrasenia) throws NegocioException {
        usuarioBO.iniciarSesion(nombre, contrasenia);
    }
}
