package mappers;

import dtos.UsuarioDTO;
import entidades.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(usuarioDTO.getIdUsuario());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setContrasenia(usuarioDTO.getContrasenia());
        return usuario;
    }

    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setContrasenia(usuario.getContrasenia());
        if (usuario.getTipo() != null) {
            usuarioDTO.setIdTipo(usuario.getTipo().getIdTipo());
            usuarioDTO.setTipoNombre(usuario.getTipo().getNombre());
        }
        return usuarioDTO;
    }
}
