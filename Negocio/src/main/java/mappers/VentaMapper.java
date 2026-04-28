package mappers;

import dtos.VentaDTO;
import entidades.Venta;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author luise
 */
public class VentaMapper {

    public static Venta toEntity(VentaDTO ventaDTO) {
        Venta venta = new Venta();
        venta.setIdVenta(ventaDTO.getIdVenta());
        venta.setTotal(ventaDTO.getTotal());
        venta.setFechaHora(ventaDTO.getFechaHora());
        venta.setFolio(ventaDTO.getFolio());
        venta.setMetodoPago(ventaDTO.getMetodoPago());
        if (ventaDTO.getUsuario() != null) {
            venta.setUsuario(UsuarioMapper.toEntity(ventaDTO.getUsuario()));
        }
        return venta;
    }

    public static VentaDTO toDTO(Venta venta) {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setIdVenta(venta.getIdVenta());
        ventaDTO.setTotal(venta.getTotal());
        ventaDTO.setFechaHora(venta.getFechaHora());
        ventaDTO.setFolio(venta.getFolio());
        ventaDTO.setMetodoPago(venta.getMetodoPago());
        ventaDTO.setDetallesVenta(
                venta.getDetallesVenta() == null
                        ? new ArrayList<>()
                        : venta.getDetallesVenta().stream()
                                .map(DetalleVentaMapper::toDTO)
                                .collect(Collectors.toList())
        );
        if (venta.getUsuario() != null) {
            ventaDTO.setUsuario(UsuarioMapper.toDTO(venta.getUsuario()));
        }
        return ventaDTO;
    }
}
