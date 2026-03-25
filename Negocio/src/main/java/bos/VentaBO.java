package bos;

import dtos.DetalleVentaDTO;
import dtos.VentaDTO;
import entidades.DetalleVenta;
import entidades.Producto;
import entidades.Usuario;
import entidades.Venta;
import interfaces.IVentaBO;
import interfaces.IVentaDAO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import mappers.VentaMapper;

public class VentaBO implements IVentaBO {

    private static final DateTimeFormatter FORMATO_FOLIO = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final IVentaDAO ventaDAO;

    public VentaBO(IVentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
    }

    @Override
    public VentaDTO registrarVenta(VentaDTO ventaDTO) {
        validarVenta(ventaDTO);

        LocalDateTime fechaHora = ventaDTO.getFechaHora() != null ? ventaDTO.getFechaHora() : LocalDateTime.now();

        Venta venta = new Venta();
        venta.setFechaHora(fechaHora);
        venta.setFolio(generarFolio(ventaDTO, fechaHora));
        venta.setMetodoPago(ventaDTO.getMetodoPago());

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(ventaDTO.getIdUsuario());
        venta.setUsuario(usuario);

        BigDecimal total = BigDecimal.ZERO;
        List<DetalleVenta> detalles = new ArrayList<>();
        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalle.setSubtotal(detalleDTO.getSubtotal() != null
                    ? detalleDTO.getSubtotal()
                    : detalleDTO.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));

            Producto producto = new Producto();
            producto.setIdProducto(detalleDTO.getIdProducto());
            detalle.setProducto(producto);
            detalle.setVenta(venta);
            detalles.add(detalle);
            total = total.add(detalle.getSubtotal());
        }

        venta.setDetallesVenta(detalles);
        venta.setTotal(total);

        return VentaMapper.toDTO(ventaDAO.registrarVenta(venta));
    }

    @Override
    public void crearVenta(VentaDTO ventaDTO) {
        Venta venta = VentaMapper.toEntity(ventaDTO);
        ventaDAO.crear(venta);
    }

    @Override
    public VentaDTO obtenerVenta(Long id) {
        Venta venta = ventaDAO.obtener(id);
        return venta == null ? null : VentaMapper.toDTO(venta);
    }

    @Override
    public void actualizarVenta(VentaDTO ventaDTO) {
        Venta venta = VentaMapper.toEntity(ventaDTO);
        ventaDAO.actualizar(venta);
    }

    @Override
    public void eliminarVenta(Long id) {
        ventaDAO.eliminar(id);
    }

    @Override
    public List<VentaDTO> obtenerTodasLasVentas() {
        return ventaDAO.obtenerTodos().stream()
                .map(VentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validarVenta(VentaDTO ventaDTO) {
        if (ventaDTO == null) {
            throw new IllegalArgumentException("La venta es obligatoria");
        }
        if (ventaDTO.getIdUsuario() == null) {
            throw new IllegalArgumentException("La venta debe tener un usuario");
        }
        if (ventaDTO.getMetodoPago() == null || ventaDTO.getMetodoPago().isBlank()) {
            throw new IllegalArgumentException("La venta debe tener un metodo de pago");
        }
        if (ventaDTO.getDetalles() == null || ventaDTO.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe incluir al menos un producto");
        }
        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            if (detalleDTO.getIdProducto() == null) {
                throw new IllegalArgumentException("Cada detalle debe tener un producto");
            }
            if (detalleDTO.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
            }
            if (detalleDTO.getPrecioUnitario() == null) {
                throw new IllegalArgumentException("Cada detalle debe tener precio unitario");
            }
        }
    }

    private String generarFolio(VentaDTO ventaDTO, LocalDateTime fechaHora) {
        if (ventaDTO.getFolio() != null && !ventaDTO.getFolio().isBlank()) {
            return ventaDTO.getFolio();
        }
        return "V-" + fechaHora.format(FORMATO_FOLIO);
    }
}
