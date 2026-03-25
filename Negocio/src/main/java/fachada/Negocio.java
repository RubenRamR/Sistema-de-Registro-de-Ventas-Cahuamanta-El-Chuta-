package fachada;

import bos.UsuarioBO;
import bos.VentaBO;
import daos.UsuarioDAO;
import daos.VentaDAO;
import dtos.DetalleVentaDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Negocio implements INegocio {
    
    UsuarioBO usuarioBO;
    VentaBO ventaBO;

    public Negocio() {
        usuarioBO = new UsuarioBO(new UsuarioDAO());
        ventaBO = new VentaBO(new VentaDAO());
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
    public void getVentaActual() {
        ventaBO.obtenerVentaActual();
    }
}
