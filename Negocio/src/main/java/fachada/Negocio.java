package fachada;

import bos.ProductoBO;
import bos.UsuarioBO;
import bos.VentaBO;
import daos.ProductoDAO;
import daos.UsuarioDAO;
import daos.VentaDAO;
import dtos.DetalleVentaDTO;
import dtos.ProductoDTO;
import dtos.VentaDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Negocio implements INegocio {
    
    UsuarioBO usuarioBO;
    VentaBO ventaBO;
    ProductoBO productoBO;

    public Negocio() {
        usuarioBO = new UsuarioBO(new UsuarioDAO());
        ventaBO = new VentaBO(new VentaDAO());
        productoBO = new ProductoBO(new ProductoDAO());
    }

    @Override
    public void iniciarSesion(String nombre, String contrasenia) throws NegocioException {
        usuarioBO.iniciarSesion(nombre, contrasenia);
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
}
