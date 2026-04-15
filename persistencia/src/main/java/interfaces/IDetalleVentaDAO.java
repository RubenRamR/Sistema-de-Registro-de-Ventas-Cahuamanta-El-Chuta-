/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.DetalleVenta;
import java.util.List;

/**
 *
 * @author chris
 */
public interface IDetalleVentaDAO {

    void crear(DetalleVenta detalleVenta);

    DetalleVenta obtener(Long id);

    List<DetalleVenta> obtenerTodos();

    void actualizar(DetalleVenta detalleVenta);

    void eliminar(Long id);
    
    public List<DetalleVenta> obtenerPorIdVenta(Long id);
}
