package fachada;

import dtos.DetalleVentaDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author Daniel
 */
public interface INegocio {
    
    public void iniciarSesion(String nombre, String contrasenia) throws NegocioException;
    public void addProductosVenta(List<DetalleVentaDTO> detalles) throws NegocioException;
    public void getVentaActual();
}
