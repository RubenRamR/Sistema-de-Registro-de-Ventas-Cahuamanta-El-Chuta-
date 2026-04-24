/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dtos.UsuarioDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author luise
 */
public interface IUsuarioBO {

    // Método para crear un usuario
    void crearUsuario(UsuarioDTO usuarioDTO) throws NegocioException;

    // Método para obtener un usuario por su ID
    UsuarioDTO obtenerUsuario(Long id);

    // Método para actualizar un usuario
    void actualizarUsuario(UsuarioDTO usuarioDTO) throws NegocioException;

    // Método para eliminar un usuario
    void eliminarUsuario(Long id);

    // Método para obtener todos los usuarios
    List<UsuarioDTO> obtenerTodosLosUsuarios();
}
