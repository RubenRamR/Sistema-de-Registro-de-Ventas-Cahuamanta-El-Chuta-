package prueba;

import bos.ProductoBO;
import daos.DetalleVentaDAO;
import daos.ProductoDAO;
import dtos.ProductoDTO;
import interfaces.IProductoDAO;
import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) {
        // Paso 1: Crear un ProductoDTO
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("Producto Test");
        productoDTO.setPrecio(new BigDecimal("150.50"));

        // Paso 2: Crear la instancia del DAO y BO
        IProductoDAO productoDAO = new ProductoDAO();  // Usamos la implementación concreta del DAO
        ProductoBO productoBO = new ProductoBO(productoDAO, new DetalleVentaDAO());

        // Paso 3: Crear el producto
        System.out.println("Creando producto...");
        productoBO.crearProducto(productoDTO);  // Agregar el producto a través de la capa de negocio
        System.out.println("Producto creado con éxito.");

        // Paso 4: Obtener el producto por su ID
        Long idProducto = 1L;  // Supongamos que el ID del producto es 1
        ProductoDTO productoObtenido = productoBO.obtenerProducto(idProducto);
        System.out.println("Producto obtenido: " + productoObtenido.getNombre() + " - Precio: " + productoObtenido.getPrecio());

        // Paso 5: Actualizar el producto
        productoDTO.setPrecio(new BigDecimal("200.75"));  // Actualizamos el precio
        productoBO.actualizarProducto(productoDTO);
        System.out.println("Producto actualizado.");

        // Paso 6: Obtener el producto actualizado
        ProductoDTO productoActualizado = productoBO.obtenerProducto(idProducto);
        System.out.println("Producto actualizado: " + productoActualizado.getNombre() + " - Nuevo precio: " + productoActualizado.getPrecio());

        // Paso 7: Eliminar el producto
        productoBO.eliminarProducto(idProducto);
        System.out.println("Producto eliminado.");
    }
}