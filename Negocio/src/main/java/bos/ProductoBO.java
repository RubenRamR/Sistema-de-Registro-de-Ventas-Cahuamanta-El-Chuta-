package bos;

import dtos.ProductoDTO;
import entidades.Producto;
import interfaces.IProductoBO;
import interfaces.IProductoDAO;
import java.util.List;
import java.util.stream.Collectors;
import mappers.ProductoMapper;

public class ProductoBO implements IProductoBO {

    private final IProductoDAO productoDAO;

    public ProductoBO(IProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    @Override
    public void crearProducto(ProductoDTO productoDTO) {
        Producto producto = ProductoMapper.toEntity(productoDTO);
        productoDAO.crear(producto);
    }

    @Override
    public ProductoDTO obtenerProducto(Long id) {
        Producto producto = productoDAO.obtener(id);
        return producto == null ? null : ProductoMapper.toDTO(producto);
    }

    @Override
    public void actualizarProducto(ProductoDTO productoDTO) {
        Producto producto = ProductoMapper.toEntity(productoDTO);
        productoDAO.actualizar(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoDAO.eliminar(id);
    }

    @Override
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoDAO.obtenerTodos().stream()
                .map(ProductoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
