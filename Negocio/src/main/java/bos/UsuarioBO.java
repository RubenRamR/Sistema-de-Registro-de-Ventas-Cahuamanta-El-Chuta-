/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bos;

import dtos.UsuarioDTO;
import entidades.Usuario;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IUsuarioDAO;
import java.util.List;
import java.util.stream.Collectors;
import mappers.UsuarioMapper;

/**
 *
 * @author luise
 */
public class UsuarioBO {

    private IUsuarioDAO usuarioDAO;
    
    // Atributo para iniciar sesión
    private Usuario sesion;

    // Inyección de dependencia a través del constructor
    public UsuarioBO(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    // Crear un usuario en la base de datos
    public void crearUsuario(UsuarioDTO usuarioDTO) {
        // Convertimos el DTO a entidad usando el mapper
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuarioDAO.crear(usuario);  // Llamamos al DAO para persistir la entidad
    }

    // Obtener un usuario por su ID
    public UsuarioDTO obtenerUsuario(Long id) {
        Usuario usuario = usuarioDAO.obtener(id);  // El DAO obtiene la entidad directamente
        return UsuarioMapper.toDTO(usuario);  // Convertimos la entidad a DTO
    }

    // Actualizar un usuario
    public void actualizarUsuario(UsuarioDTO usuarioDTO) {
        // Convertimos el DTO a entidad
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuarioDAO.actualizar(usuario);  // Llamamos al DAO para actualizar la entidad
    }

    // Eliminar un usuario
    public void eliminarUsuario(Long id) {
        usuarioDAO.eliminar(id);  // Llamada al DAO para eliminar la venta
    }

    // Obtener todos los usuarios
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();  // El DAO obtiene las entidades directamente
        return usuarios.stream()
                        .map(UsuarioMapper::toDTO)  // Convertimos cada entidad a DTO
                        .collect(Collectors.toList());
    }
    
    public void iniciarSesion(String nombre, String contrasenia) throws NegocioException {
        try {
            sesion = usuarioDAO.obtener(nombre, contrasenia);
        } catch(PersistenciaException e) {
            throw new NegocioException("Usuario o contraseña incorrectos.");
        }
    }
    
    public Usuario obtenerSesion() {
        return sesion;
    }
    
    public void cerrarSesion() {
        sesion = null;
    }
}