package fachada;

import dtos.DetalleVentaDTO;
import dtos.ProductoDTO;
import dtos.UsuarioDTO;
import dtos.VentaDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author Daniel
 */
public interface INegocio {
    
    public void iniciarSesion(String nombre, String contrasenia) throws NegocioException;
    public void cerrarSesionActual();
    public UsuarioDTO getSesionActual();
    public void addProductosVenta(List<DetalleVentaDTO> detalles) throws NegocioException;
    public VentaDTO getVentaActual();
    public void setMetodoPagoVentaActual(String metodoPago);
    public List<DetalleVentaDTO> getProductosVenta();
    public void registrarVentaActual();
    public List<ProductoDTO> obtenerProductos();
    public List<ProductoDTO> obtenerProductos(String categoria);
    public List<VentaDTO> obtenerVentasDelDia();
    public List<DetalleVentaDTO> obtenerDetallesVentaPorIdVenta(Long id);
    public void crearUsuario(UsuarioDTO usuarioDTO) throws NegocioException;
    public void actualizarUsuario(UsuarioDTO usuarioDTO) throws NegocioException;
    public void eliminarUsuario(Long id);
    public UsuarioDTO obtenerUsuario(Long id);
    public List<UsuarioDTO> obtenerUsuarios();
}
