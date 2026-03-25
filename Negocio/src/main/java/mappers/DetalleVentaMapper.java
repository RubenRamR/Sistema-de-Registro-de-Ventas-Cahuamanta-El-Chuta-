package mappers;

import dtos.DetalleVentaDTO;
import entidades.DetalleVenta;

/**
 *
 * @author Daniel
 */
public class DetalleVentaMapper {
    
    public static DetalleVenta toEntity(DetalleVentaDTO detalleDTO) {
        DetalleVenta detalle = new DetalleVenta();
        
        detalle.setCantidad(detalleDTO.getCantidad());
        detalle.setProducto(ProductoMapper.toEntity(detalleDTO.getProducto()));
        detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
        detalle.setIdDetalleVenta(detalleDTO.getId());
        
        return detalle;
    }
    
    public static DetalleVentaDTO toDTO(DetalleVenta detalle) {
        DetalleVentaDTO detalleDTO = new DetalleVentaDTO();
        
        detalleDTO.setCantidad(detalle.getCantidad());
        detalleDTO.setProducto(ProductoMapper.toDTO(detalle.getProducto()));
        detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
        detalleDTO.setId(detalle.getIdDetalleVenta());
        
        return detalleDTO;
    }
}
