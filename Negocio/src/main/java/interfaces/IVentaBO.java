/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dtos.VentaDTO;
import java.util.List;

/**
 *
 * @author luise
 */
public interface IVentaBO {

    // Método para crear una venta
    void crearVenta(VentaDTO ventaDTO);

    // Método para obtener una venta por su ID
    VentaDTO obtenerVenta(Long id);

    // Método para actualizar una venta
    void actualizarVenta(VentaDTO ventaDTO);

    // Método para eliminar una venta
    void eliminarVenta(Long id);

    // Método para obtener todas las ventas
    List<VentaDTO> obtenerTodasLasVentas();
}
