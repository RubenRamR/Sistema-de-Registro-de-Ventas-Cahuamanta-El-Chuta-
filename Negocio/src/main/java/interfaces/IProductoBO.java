/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dtos.ProductoDTO;
import java.util.List;

/**
 *
 * @author luise
 */
public interface IProductoBO {

    // Método para crear un producto
    void crearProducto(ProductoDTO productoDTO);

    // Método para obtener un producto por su ID
    ProductoDTO obtenerProducto(Long id);

    // Método para actualizar un producto
    void actualizarProducto(ProductoDTO productoDTO);

    // Método para eliminar un producto
    void eliminarProducto(Long id);

    // Método para obtener todos los productos
    List<ProductoDTO> obtenerTodosLosProductos();
}
