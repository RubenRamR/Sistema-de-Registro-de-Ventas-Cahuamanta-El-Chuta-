/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bos;

import dtos.ProductoDTO;
import entidades.Producto;
import interfaces.IProductoDAO;
import java.util.List;
import java.util.stream.Collectors;
import mappers.ProductoMapper;

/**
 *
 * @author luise
 */

public class ProductoBO {

    private IProductoDAO productoDAO;

    // Inyección de dependencia a través del constructor
    public ProductoBO(IProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    // Crear un producto en la base de datos
    public void crearProducto(ProductoDTO productoDTO) {
        // Convertimos el DTO a entidad usando el mapper
        Producto producto = ProductoMapper.toEntity(productoDTO);
        productoDAO.crear(producto);  // Llamamos al DAO para persistir la entidad
    }

    // Obtener un producto por su ID
    public ProductoDTO obtenerProducto(Long id) {
        Producto producto = productoDAO.obtener(id);  // El DAO obtiene la entidad directamente
        return ProductoMapper.toDTO(producto);  // Convertimos la entidad a DTO
    }
    
    public List<ProductoDTO> obtenerProducto(String categoria) {
        List<Producto> productos = productoDAO.obtener(categoria);
        return productos.stream()
                        .map(ProductoMapper::toDTO)  // Convertimos cada entidad a DTO
                        .collect(Collectors.toList());
    }

    // Actualizar un producto
    public void actualizarProducto(ProductoDTO productoDTO) {
        // Convertimos el DTO a entidad
        Producto producto = ProductoMapper.toEntity(productoDTO);
        productoDAO.actualizar(producto);  // Llamamos al DAO para actualizar la entidad
    }

    // Eliminar un producto
    public void eliminarProducto(Long id) {
        productoDAO.eliminar(id);  // Llamada al DAO para eliminar la venta
    }

    // Obtener todos los productos
    public List<ProductoDTO> obtenerTodosLosProductos() {
        List<Producto> productos = productoDAO.obtenerTodos();  // El DAO obtiene las entidades directamente
        return productos.stream()
                        .map(ProductoMapper::toDTO)  // Convertimos cada entidad a DTO
                        .collect(Collectors.toList());
    }
}