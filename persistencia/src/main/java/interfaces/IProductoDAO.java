/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Producto;
import java.util.List;

/**
 *
 * @author chris
 */
public interface IProductoDAO {

    void crear(Producto producto);

    Producto obtener(Long id);

    List<Producto> obtenerTodos();

    void actualizar(Producto producto);

    void eliminar(Long id);
    
    List<Producto> obtener(String categoria);
}
