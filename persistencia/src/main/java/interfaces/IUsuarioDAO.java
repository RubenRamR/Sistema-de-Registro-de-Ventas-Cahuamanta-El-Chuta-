package interfaces;

import entidades.Usuario;
import java.util.List;

public interface IUsuarioDAO {

    void crear(Usuario usuario);

    Usuario obtener(Long id);

    Usuario obtenerPorCredenciales(String nombre, String contrasenia);

    List<Usuario> obtenerTodos();

    void actualizar(Usuario usuario);

    void eliminar(Long id);
}
