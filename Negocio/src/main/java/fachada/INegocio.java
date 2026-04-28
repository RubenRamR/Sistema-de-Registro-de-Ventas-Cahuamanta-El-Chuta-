package fachada;

import dtos.DetalleVentaDTO;
import dtos.ProductoDTO;
import dtos.ReporteVentasDTO;
import dtos.UsuarioDTO;
import dtos.VentaDTO;
import excepciones.NegocioException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Daniel
 */
public interface INegocio {

    void iniciarSesion(String nombre, String contrasenia) throws NegocioException;

    void cerrarSesionActual();

    UsuarioDTO getSesionActual();

    void addProductosVenta(List<DetalleVentaDTO> detalles) throws NegocioException;

    VentaDTO getVentaActual();

    void validarPagoEfectivoVentaActual(BigDecimal montoRecibido) throws NegocioException;

    void setMetodoPagoVentaActual(String metodoPago);

    List<DetalleVentaDTO> getProductosVenta();

    void registrarVentaActual();

    List<ProductoDTO> obtenerProductos();

    List<ProductoDTO> obtenerProductos(String categoria);

    List<VentaDTO> obtenerVentasDelDia();

    List<VentaDTO> obtenerVentasPorFecha(LocalDate fecha);

    ReporteVentasDTO obtenerReporteVentas(LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException;

    void exportarReporteVentas(LocalDate fechaInicio, LocalDate fechaFin, String rutaArchivo) throws NegocioException;

    List<DetalleVentaDTO> obtenerDetallesVentaPorIdVenta(Long id);

    void crearUsuario(UsuarioDTO usuarioDTO) throws NegocioException;

    void actualizarUsuario(UsuarioDTO usuarioDTO) throws NegocioException;

    void eliminarUsuario(Long id);

    UsuarioDTO obtenerUsuario(Long id);

    List<UsuarioDTO> obtenerUsuarios();
}
