package fachada;

import bos.ProductoBO;
import bos.UsuarioBO;
import bos.VentaBO;
import daos.DetalleVentaDAO;
import daos.ProductoDAO;
import daos.TipoDAO;
import daos.UsuarioDAO;
import daos.VentaDAO;
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
public class Negocio implements INegocio {

    private final UsuarioBO usuarioBO;
    private final VentaBO ventaBO;
    private final ProductoBO productoBO;

    public Negocio() {
        usuarioBO = new UsuarioBO(new UsuarioDAO(), new TipoDAO());
        ventaBO = new VentaBO(new VentaDAO());
        productoBO = new ProductoBO(new ProductoDAO(), new DetalleVentaDAO());
    }

    @Override
    public void iniciarSesion(String nombre, String contrasenia) throws NegocioException {
        usuarioBO.iniciarSesion(nombre, contrasenia);
    }

    @Override
    public void cerrarSesionActual() {
        usuarioBO.cerrarSesion();
    }

    @Override
    public UsuarioDTO getSesionActual() {
        return usuarioBO.obtenerSesionActual();
    }

    @Override
    public void addProductosVenta(List<DetalleVentaDTO> detalles) throws NegocioException {
        ventaBO.crearVenta(detalles);
    }

    @Override
    public VentaDTO getVentaActual() {
        return ventaBO.obtenerVentaActual();
    }

    @Override
    public void validarPagoEfectivoVentaActual(BigDecimal montoRecibido) throws NegocioException {
        ventaBO.validarPagoEfectivoVentaActual(montoRecibido);
    }

    @Override
    public void setMetodoPagoVentaActual(String metodoPago) {
        ventaBO.setMetodoPago(metodoPago);
    }

    @Override
    public List<DetalleVentaDTO> getProductosVenta() {
        return ventaBO.getProductosVenta();
    }

    @Override
    public void registrarVentaActual() {
        ventaBO.crearVenta(usuarioBO.obtenerSesion());
    }

    @Override
    public List<ProductoDTO> obtenerProductos() {
        return productoBO.obtenerTodosLosProductos();
    }

    @Override
    public List<ProductoDTO> obtenerProductos(String categoria) {
        return productoBO.obtenerProducto(categoria);
    }

    @Override
    public List<VentaDTO> obtenerVentasDelDia() {
        return ventaBO.obtenerVentasPorFecha(LocalDate.now());
    }

    @Override
    public List<VentaDTO> obtenerVentasPorFecha(LocalDate fecha) {
        return ventaBO.obtenerVentasPorFecha(fecha);
    }

    @Override
    public ReporteVentasDTO obtenerReporteVentas(LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException {
        return ventaBO.obtenerReporteVentas(fechaInicio, fechaFin);
    }

    @Override
    public void exportarReporteVentas(LocalDate fechaInicio, LocalDate fechaFin, String rutaArchivo) throws NegocioException {
        ventaBO.exportarReporteVentas(fechaInicio, fechaFin, rutaArchivo);
    }

    @Override
    public List<DetalleVentaDTO> obtenerDetallesVentaPorIdVenta(Long id) {
        return productoBO.obtenerDetallesVentaPorIdVenta(id);
    }

    @Override
    public void crearUsuario(UsuarioDTO usuarioDTO) throws NegocioException {
        usuarioBO.crearUsuario(usuarioDTO);
    }

    @Override
    public void actualizarUsuario(UsuarioDTO usuarioDTO) throws NegocioException {
        usuarioBO.actualizarUsuario(usuarioDTO);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioBO.eliminarUsuario(id);
    }

    @Override
    public UsuarioDTO obtenerUsuario(Long id) {
        return usuarioBO.obtenerUsuario(id);
    }

    @Override
    public List<UsuarioDTO> obtenerUsuarios() {
        return usuarioBO.obtenerTodosLosUsuarios();
    }
}
