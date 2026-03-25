package fachada;

import excepciones.NegocioException;

/**
 *
 * @author Daniel
 */
public interface INegocio {
    
    public void iniciarSesion(String nombre, String contrasenia) throws NegocioException;
}
