/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mappers;

import dtos.ProductoDTO;
import entidades.Producto;

/**
 *
 * @author luise
 */


public class ProductoMapper {

    // Convertir de ProductoDTO a Producto (para persistir en la base de datos)
    public static Producto toEntity(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setIdProducto(productoDTO.getIdProducto());
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        return producto;
    }

    // Convertir de Producto a ProductoDTO (para enviar los datos a la capa de presentación o negocio)
    public static ProductoDTO toDTO(Producto producto) {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setIdProducto(producto.getIdProducto());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setPrecio(producto.getPrecio());
        return productoDTO;
    }
}
