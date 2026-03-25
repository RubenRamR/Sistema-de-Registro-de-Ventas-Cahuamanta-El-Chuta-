package interfaces;

import dtos.VentaDTO;
import java.util.List;

public interface IVentaBO {

    VentaDTO registrarVenta(VentaDTO ventaDTO);

    void crearVenta(VentaDTO ventaDTO);

    VentaDTO obtenerVenta(Long id);

    void actualizarVenta(VentaDTO ventaDTO);

    void eliminarVenta(Long id);

    List<VentaDTO> obtenerTodasLasVentas();
}
