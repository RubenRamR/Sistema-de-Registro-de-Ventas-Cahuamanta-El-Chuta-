/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mappers;

import dtos.UsuarioDTO;
import entidades.Usuario;

/**
 *
 * @author luise
 */
public class UsuarioMapper {

    // Convertir de UsuarioDTO a Usuario (para persistir en la base de datos)
    public static Usuario toEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(usuarioDTO.getIdUsuario());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setContrasenia(usuarioDTO.getContrasenia());
        return usuario;
    }

    // Convertir de Usuario a UsuarioDTO (para enviar los datos a la capa de presentación o negocio)
    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setContrasenia(usuario.getContrasenia());
        return usuarioDTO;
    }
}
