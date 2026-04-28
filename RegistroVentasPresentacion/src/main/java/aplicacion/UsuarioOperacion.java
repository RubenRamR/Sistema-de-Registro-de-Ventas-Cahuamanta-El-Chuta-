package aplicacion;

import dtos.UsuarioDTO;
import excepciones.NegocioException;

@FunctionalInterface
public interface UsuarioOperacion {

    void ejecutar(UsuarioDTO usuarioDTO) throws NegocioException;
}
