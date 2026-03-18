/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Usuario;
import java.util.List;

/**
 *
 * @author chris
 */
public interface IUsuarioDAO {

    void crear(Usuario usuario);

    Usuario obtener(Long id);

    List<Usuario> obtenerTodos();

    void actualizar(Usuario usuario);

    void eliminar(Long id);
}
