/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mappers;

import dtos.VentaDTO;
import entidades.Venta;

/**
 *
 * @author luise
 */

public class VentaMapper {

    // Convertir de VentaDTO a Venta (para persistir en la base de datos)
    public static Venta toEntity(VentaDTO ventaDTO) {
        Venta venta = new Venta();
        venta.setIdVenta(ventaDTO.getIdVenta());
        venta.setTotal(ventaDTO.getTotal());
        venta.setFechaHora(ventaDTO.getFechaHora());
        venta.setFolio(ventaDTO.getFolio());
        venta.setMetodoPago(ventaDTO.getMetodoPago());
        return venta;
    }

    // Convertir de Venta a VentaDTO (para enviar los datos a la capa de presentación o negocio)
    public static VentaDTO toDTO(Venta venta) {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setIdVenta(venta.getIdVenta());
        ventaDTO.setTotal(venta.getTotal());
        ventaDTO.setFechaHora(venta.getFechaHora());
        ventaDTO.setFolio(venta.getFolio());
        ventaDTO.setMetodoPago(venta.getMetodoPago());
        return ventaDTO;
    }
}
