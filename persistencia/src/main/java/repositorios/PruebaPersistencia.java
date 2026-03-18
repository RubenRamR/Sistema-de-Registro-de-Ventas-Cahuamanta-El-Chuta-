/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package repositorios;

/**
 *
 * @author chris
 */
import conexion.ConexionBD;
import daos.ProductoDAO;
import entidades.Producto;


import java.math.BigDecimal;

public class PruebaPersistencia {

    public static void main(String[] args) {
        // Crear una instancia de Producto
        Producto producto = new Producto();
        producto.setNombre("Cahuamanta");
        producto.setPrecio(BigDecimal.valueOf(150));

        // Crear una instancia de ProductoDAO para manejar la persistencia
        ProductoDAO productoDAO = new ProductoDAO();

        // Guardar el producto en la base de datos
        productoDAO.crear(producto);

        // Recuperar el producto por su id
        Producto productoRecuperado = productoDAO.obtener(producto.getIdProducto());
        System.out.println("Producto recuperado: " + productoRecuperado.getNombre() + " - Precio: " + productoRecuperado.getPrecio());

        // Actualizar el producto
        productoRecuperado.setPrecio(BigDecimal.valueOf(180));
        productoDAO.actualizar(productoRecuperado);

        // Verificar si se actualizó correctamente
        Producto productoActualizado = productoDAO.obtener(productoRecuperado.getIdProducto());
        System.out.println("Producto actualizado: " + productoActualizado.getNombre() + " - Nuevo Precio: " + productoActualizado.getPrecio());

        //Eliminar el producto
        productoDAO.eliminar(productoActualizado.getIdProducto());

        //Verificar si se eliminó correctamente
        Producto productoEliminado = productoDAO.obtener(productoActualizado.getIdProducto());
        if (productoEliminado == null) {
            System.out.println("Producto eliminado con éxito.");
        } else {
            System.out.println("Error al eliminar el producto.");
        }

        // Cerrar la conexión
        ConexionBD.cerrarConexion();
    }
}