package interfaces;

import entidades.Venta;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author chris
 */
public interface IVentaDAO {

    void crear(Venta venta);

    Venta obtener(Long id);

    List<Venta> obtenerTodos();

    List<Venta> obtenerPorRango(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    void actualizar(Venta venta);

    void eliminar(Long id);
}
