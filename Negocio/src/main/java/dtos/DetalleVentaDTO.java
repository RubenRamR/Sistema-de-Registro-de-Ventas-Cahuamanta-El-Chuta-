package dtos;

import java.math.BigDecimal;

/**
 *
 * @author Daniel
 */
public class DetalleVentaDTO {
    
    private Long id;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private ProductoDTO producto;

    public DetalleVentaDTO() {
    }

    public DetalleVentaDTO(Long id, int cantidad, BigDecimal precioUnitario, ProductoDTO producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.producto = producto;
    }

    public DetalleVentaDTO(int cantidad, BigDecimal precioUnitario, ProductoDTO producto) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.producto = producto;
    }

    public DetalleVentaDTO(Long id, int cantidad, BigDecimal precioUnitario, BigDecimal subtotal, ProductoDTO producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public BigDecimal getSubtotal() {
        return BigDecimal.valueOf(cantidad).multiply(precioUnitario);
    }

    @Override
    public String toString() {
        return "DetalleVentaDTO{" + "id=" + id + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + ", subtotal=" + subtotal + ", producto=" + producto + '}';
    }
    
}
