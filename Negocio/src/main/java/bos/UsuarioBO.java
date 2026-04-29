package bos;

import dtos.UsuarioDTO;
import entidades.Tipo;
import entidades.Usuario;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.ITipoDAO;
import interfaces.IUsuarioDAO;
import java.util.List;
import java.util.stream.Collectors;
import mappers.UsuarioMapper;

/**
 *
 * @author luise
 */
public class UsuarioBO {

    private static final String ROL_ADMIN = "ADMIN";
    private static final String ROL_CAJERO = "CAJERO";

    private final IUsuarioDAO usuarioDAO;
    private final ITipoDAO tipoDAO;
    private Usuario sesion;

    public UsuarioBO(IUsuarioDAO usuarioDAO, ITipoDAO tipoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.tipoDAO = tipoDAO;
    }

    public void crearUsuario(UsuarioDTO usuarioDTO) throws NegocioException {
        validarUsuario(usuarioDTO, false);
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuario.setTipo(obtenerOCrearTipo(normalizarRol(usuarioDTO.getRol())));
        usuarioDAO.crear(usuario);
    }

    public UsuarioDTO obtenerUsuario(Long id) {
        Usuario usuario = usuarioDAO.obtener(id);
        return usuario == null ? null : normalizarUsuarioDTO(UsuarioMapper.toDTO(usuario));
    }

    public void actualizarUsuario(UsuarioDTO usuarioDTO) throws NegocioException {
        validarUsuario(usuarioDTO, true);
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuario.setTipo(obtenerOCrearTipo(normalizarRol(usuarioDTO.getRol())));
        usuarioDAO.actualizar(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioDAO.eliminar(id);
    }

    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioDAO.obtenerTodos().stream()
                .map(UsuarioMapper::toDTO)
                .map(this::normalizarUsuarioDTO)
                .collect(Collectors.toList());
    }

    public void iniciarSesion(String nombre, String contrasenia) throws NegocioException {
        if (nombre == null || nombre.isBlank() || contrasenia == null || contrasenia.isBlank())
        {
            throw new NegocioException("Debe ingresar usuario y contrasenia.");
        }

        try
        {
            sesion = usuarioDAO.obtener(nombre.trim(), contrasenia.trim());
        } catch (PersistenciaException e)
        {
            throw new NegocioException("Usuario o contrasenia incorrectos.");
        }
    }

    public Usuario obtenerSesion() {
        return sesion;
    }

    public UsuarioDTO obtenerSesionActual() {
        return sesion == null ? null : normalizarUsuarioDTO(UsuarioMapper.toDTO(sesion));
    }

    public void cerrarSesion() {
        sesion = null;
    }

    private void validarUsuario(UsuarioDTO usuarioDTO, boolean esActualizacion) throws NegocioException {
        if (usuarioDTO == null)
        {
            throw new NegocioException("Datos de usuario no enviados.");
        }
        if (esActualizacion && usuarioDTO.getIdUsuario() == null)
        {
            throw new NegocioException("Usuario no valido para actualizar.");
        }
        if (usuarioDTO.getNombre() == null || usuarioDTO.getNombre().isBlank())
        {
            throw new NegocioException("El nombre de usuario es obligatorio.");
        }
        if (usuarioDTO.getContrasenia() == null || usuarioDTO.getContrasenia().isBlank())
        {
            throw new NegocioException("La contrasenia es obligatoria.");
        }

        String rol = normalizarRol(usuarioDTO.getRol());
        if (!ROL_ADMIN.equals(rol) && !ROL_CAJERO.equals(rol))
        {
            throw new NegocioException("Rol no valido.");
        }

        boolean nombreOcupado = usuarioDAO.obtenerTodos().stream()
                .anyMatch(usuarioExistente
                        -> usuarioExistente.getNombre().equalsIgnoreCase(usuarioDTO.getNombre().trim())
                && (!esActualizacion || !usuarioExistente.getIdUsuario().equals(usuarioDTO.getIdUsuario()))
                );
        if (nombreOcupado)
        {
            throw new NegocioException("Ya existe un usuario con ese nombre.");
        }
    }

    private Tipo obtenerOCrearTipo(String rol) {
        Tipo tipo = tipoDAO.obtenerPorNombre(rol);
        if (tipo != null)
        {
            return tipo;
        }

        Tipo nuevoTipo = new Tipo();
        nuevoTipo.setNombre(rol);
        tipoDAO.crear(nuevoTipo);
        return tipoDAO.obtenerPorNombre(rol);
    }

    private String normalizarRol(String rol) {
        if (rol == null)
        {
            return "";
        }

        return switch (rol.trim().toUpperCase())
        {
            case "DUEÑO", "DUEÃ‘O", "DUENO", "ADMIN" ->
                ROL_ADMIN;
            case "CAJERO" ->
                ROL_CAJERO;
            default ->
                rol.trim().toUpperCase();
        };
    }

    private UsuarioDTO normalizarUsuarioDTO(UsuarioDTO usuarioDTO) {
        usuarioDTO.setRol(normalizarRol(usuarioDTO.getRol()));
        return usuarioDTO;
    }
}
