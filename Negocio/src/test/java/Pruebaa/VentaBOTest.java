package Pruebaa;

import bos.VentaBO;
import dtos.DetalleVentaDTO;
import dtos.ProductoDTO;
import dtos.ReporteVentasDTO;
import dtos.VentaDTO;
import entidades.Usuario;
import entidades.Venta;
import excepciones.NegocioException;
import interfaces.IVentaDAO;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class VentaBOTest {

    @TempDir
    Path tempDir;

    @Test
    void obtenerVentasPorFechaSolicitaRangoDelDiaCompleto() {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(crearVenta(10L, LocalDateTime.of(2026, 4, 27, 9, 30), "120.00", "ANA", "EFECTIVO"));
        VentaBO ventaBO = new VentaBO(dao);

        List<VentaDTO> resultado = ventaBO.obtenerVentasPorFecha(LocalDate.of(2026, 4, 27));

        assertEquals(LocalDateTime.of(2026, 4, 27, 0, 0), dao.ultimaFechaInicio);
        assertEquals(LocalDateTime.of(2026, 4, 28, 0, 0), dao.ultimaFechaFin);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getIdVenta());
    }

    @Test
    void obtenerReporteVentasCalculaTotalesYPromedio() throws NegocioException {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(
                crearVenta(1L, LocalDateTime.of(2026, 4, 26, 10, 0), "100.00", "ANA", "EFECTIVO"),
                crearVenta(2L, LocalDateTime.of(2026, 4, 27, 11, 30), "250.50", "LUIS", "TARJETA")
        );
        VentaBO ventaBO = new VentaBO(dao);

        ReporteVentasDTO reporte = ventaBO.obtenerReporteVentas(LocalDate.of(2026, 4, 26), LocalDate.of(2026, 4, 27));

        assertEquals(2, reporte.getCantidadVentas());
        assertEquals(new BigDecimal("350.50"), reporte.getMontoTotal());
        assertEquals(new BigDecimal("175.25"), reporte.getPromedioVenta());
        assertEquals(2, reporte.getVentas().size());
    }

    @Test
    void obtenerReporteVentasLanzaErrorSiRangoEsInvalido() {
        VentaBO ventaBO = new VentaBO(new FakeVentaDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> ventaBO.obtenerReporteVentas(LocalDate.of(2026, 4, 28), LocalDate.of(2026, 4, 27))
        );

        assertEquals("La fecha final no puede ser menor que la fecha inicial.", error.getMessage());
    }

    @Test
    void exportarReporteVentasLanzaErrorCuandoNoHayDatos() {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of();
        VentaBO ventaBO = new VentaBO(dao);

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> ventaBO.exportarReporteVentas(LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27), tempDir.resolve("reporte.pdf").toString())
        );

        assertEquals("No hay registros disponibles en el periodo seleccionado.", error.getMessage());
    }

    @Test
    void exportarReporteVentasGeneraPdfCuandoHayDatos() throws Exception {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(
                crearVenta(7L, LocalDateTime.of(2026, 4, 27, 14, 15), "180.00", "CARLA", "TRANSFERENCIA")
        );
        VentaBO ventaBO = new VentaBO(dao);
        Path pdf = tempDir.resolve("reporte-ventas.pdf");

        ventaBO.exportarReporteVentas(LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27), pdf.toString());

        assertTrue(Files.exists(pdf));
        assertTrue(Files.size(pdf) > 0);
    }

    @Test
    void validarPagoEfectivoVentaActualLanzaErrorSiMontoEsInsuficiente() throws Exception {
        VentaBO ventaBO = new VentaBO(new FakeVentaDAO());
        ventaBO.crearVenta(List.of(crearDetalle("48.00", 2)));

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> ventaBO.validarPagoEfectivoVentaActual(new BigDecimal("90.00"))
        );

        assertEquals("El pago en efectivo es insuficiente para cubrir el total.", error.getMessage());
    }

    @Test
    void validarPagoEfectivoVentaActualAceptaMontoSuficiente() throws Exception {
        VentaBO ventaBO = new VentaBO(new FakeVentaDAO());
        ventaBO.crearVenta(List.of(crearDetalle("48.00", 2)));

        assertDoesNotThrow(() -> ventaBO.validarPagoEfectivoVentaActual(new BigDecimal("96.00")));
    }

    private Venta crearVenta(Long id, LocalDateTime fechaHora, String total, String nombreUsuario, String metodoPago) {
        Venta venta = new Venta();
        venta.setIdVenta(id);
        venta.setFechaHora(fechaHora);
        venta.setTotal(new BigDecimal(total));
        venta.setMetodoPago(metodoPago);
        venta.setDetallesVenta(new ArrayList<>());

        Usuario usuario = new Usuario();
        usuario.setNombre(nombreUsuario);
        venta.setUsuario(usuario);

        return venta;
    }

    private DetalleVentaDTO crearDetalle(String precioUnitario, int cantidad) {
        ProductoDTO producto = new ProductoDTO();
        producto.setIdProducto(1L);
        producto.setNombre("Producto prueba");
        producto.setPrecio(new BigDecimal(precioUnitario));
        producto.setCategoria("General");

        DetalleVentaDTO detalle = new DetalleVentaDTO();
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(new BigDecimal(precioUnitario));
        detalle.setProducto(producto);
        return detalle;
    }

    private static class FakeVentaDAO implements IVentaDAO {

        private List<Venta> ventasPorRango = List.of();
        private LocalDateTime ultimaFechaInicio;
        private LocalDateTime ultimaFechaFin;

        @Override
        public void crear(Venta venta) {
        }

        @Override
        public Venta obtener(Long id) {
            return null;
        }

        @Override
        public List<Venta> obtenerTodos() {
            return ventasPorRango;
        }

        @Override
        public List<Venta> obtenerPorRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
            this.ultimaFechaInicio = fechaInicio;
            this.ultimaFechaFin = fechaFin;
            return ventasPorRango;
        }

        @Override
        public void actualizar(Venta venta) {
        }

        @Override
        public void eliminar(Long id) {
        }
    }
}
