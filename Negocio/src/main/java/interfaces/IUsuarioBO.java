package interfaces;

import dtos.UsuarioDTO;
import java.util.List;

public interface IUsuarioBO {

    UsuarioDTO iniciarSesion(String nombre, String contrasenia);

    void crearUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO obtenerUsuario(Long id);

    void actualizarUsuario(UsuarioDTO usuarioDTO);

    void eliminarUsuario(Long id);

    List<UsuarioDTO> obtenerTodosLosUsuarios();
}
