package fachada;

import dtos.DetalleVentaDTO;
import dtos.ProductoDTO;
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
    public void addProductosVenta(List<DetalleVentaDTO> detalles) throws NegocioException;
    public VentaDTO getVentaActual();
    public void setMetodoPagoVentaActual(String metodoPago);
    public List<DetalleVentaDTO> getProductosVenta();
    public void registrarVentaActual();
    public List<ProductoDTO> obtenerProductos();
    public List<ProductoDTO> obtenerProductos(String categoria);
}
