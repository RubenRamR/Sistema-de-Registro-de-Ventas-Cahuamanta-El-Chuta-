package mappers;

import dtos.DetalleVentaDTO;
import dtos.VentaDTO;
import entidades.DetalleVenta;
import entidades.Venta;
import java.util.ArrayList;
import java.util.List;

public class VentaMapper {

    public static Venta toEntity(VentaDTO ventaDTO) {
        Venta venta = new Venta();
        venta.setIdVenta(ventaDTO.getIdVenta());
        venta.setTotal(ventaDTO.getTotal());
        venta.setFechaHora(ventaDTO.getFechaHora());
        venta.setFolio(ventaDTO.getFolio());
        venta.setMetodoPago(ventaDTO.getMetodoPago());
        return venta;
    }

    public static VentaDTO toDTO(Venta venta) {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setIdVenta(venta.getIdVenta());
        ventaDTO.setTotal(venta.getTotal());
        ventaDTO.setFechaHora(venta.getFechaHora());
        ventaDTO.setFolio(venta.getFolio());
        ventaDTO.setMetodoPago(venta.getMetodoPago());

        if (venta.getUsuario() != null) {
            ventaDTO.setIdUsuario(venta.getUsuario().getIdUsuario());
            ventaDTO.setNombreUsuario(venta.getUsuario().getNombre());
        }

        List<DetalleVentaDTO> detalles = new ArrayList<>();
        if (venta.getDetallesVenta() != null) {
            for (DetalleVenta detalle : venta.getDetallesVenta()) {
                DetalleVentaDTO detalleDTO = new DetalleVentaDTO();
                detalleDTO.setIdDetalleVenta(detalle.getIdDetalleVenta());
                detalleDTO.setCantidad(detalle.getCantidad());
                detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
                detalleDTO.setSubtotal(detalle.getSubtotal());
                if (detalle.getProducto() != null) {
                    detalleDTO.setIdProducto(detalle.getProducto().getIdProducto());
                    detalleDTO.setNombreProducto(detalle.getProducto().getNombre());
                }
                detalles.add(detalleDTO);
            }
        }
        ventaDTO.setDetalles(detalles);
        return ventaDTO;
    }
}
