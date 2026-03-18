/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Venta;
import java.util.List;

/**
 *
 * @author chris
 */
public interface IVentaDAO {

    void crear(Venta venta);

    Venta obtener(Long id);

    List<Venta> obtenerTodos();

    void actualizar(Venta venta);

    void eliminar(Long id);
}
