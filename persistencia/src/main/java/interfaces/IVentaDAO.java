package interfaces;

import entidades.Venta;
import java.util.List;

public interface IVentaDAO {

    void crear(Venta venta);

    Venta registrarVenta(Venta venta);

    Venta obtener(Long id);

    List<Venta> obtenerTodos();

    void actualizar(Venta venta);

    void eliminar(Long id);
}
