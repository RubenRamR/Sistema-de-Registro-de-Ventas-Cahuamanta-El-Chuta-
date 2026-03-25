package bos;

import dtos.UsuarioDTO;
import entidades.Usuario;
import interfaces.IUsuarioBO;
import interfaces.IUsuarioDAO;
import java.util.List;
import java.util.stream.Collectors;
import mappers.UsuarioMapper;

public class UsuarioBO implements IUsuarioBO {

    private final IUsuarioDAO usuarioDAO;

    public UsuarioBO(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public UsuarioDTO iniciarSesion(String nombre, String contrasenia) {
        Usuario usuario = usuarioDAO.obtenerPorCredenciales(nombre, contrasenia);
        return usuario == null ? null : UsuarioMapper.toDTO(usuario);
    }

    @Override
    public void crearUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuarioDAO.crear(usuario);
    }

    @Override
    public UsuarioDTO obtenerUsuario(Long id) {
        Usuario usuario = usuarioDAO.obtener(id);
        return usuario == null ? null : UsuarioMapper.toDTO(usuario);
    }

    @Override
    public void actualizarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuarioDAO.actualizar(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioDAO.eliminar(id);
    }

    @Override
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioDAO.obtenerTodos().stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }
}
