package Pruebaa;

import bos.VentaBO;
import dtos.VentaDTO;
import entidades.Usuario;
import entidades.Venta;
import interfaces.IVentaDAO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del CU02 - Consultar Historial de Ventas (rol Dueño).
 *
 * Cubre los casos de prueba CP-H-01 a CP-H-06 del plan de pruebas.
 */
class VentaBOHistorialTest {

    // ---------- CP-H-01: Ventas del dia con registros ----------
    @Test
    void obtenerVentasPorFechaDelDiaActualDevuelveVentas() {
        FakeVentaDAO dao = new FakeVentaDAO();
        LocalDate hoy = LocalDate.now();
        dao.ventasPorRango = List.of(
                crearVenta(1L, hoy.atTime(9, 0), "120.00", "ANA", "EFECTIVO"),
                crearVenta(2L, hoy.atTime(13, 30), "85.50", "LUIS", "TARJETA"),
                crearVenta(3L, hoy.atTime(18, 45), "200.00", "ANA", "EFECTIVO")
        );
        VentaBO bo = new VentaBO(dao);

        List<VentaDTO> resultado = bo.obtenerVentasPorFecha(hoy);

        assertEquals(3, resultado.size());
    }

    // ---------- CP-H-02: Ventas del dia sin registros ----------
    @Test
    void obtenerVentasPorFechaDelDiaSinRegistrosDevuelveListaVacia() {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of();
        VentaBO bo = new VentaBO(dao);

        List<VentaDTO> resultado = bo.obtenerVentasPorFecha(LocalDate.now());

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ---------- CP-H-03: Ventas por fecha pasada con registros ----------
    @Test
    void obtenerVentasPorFechaPasadaDevuelveVentasDelDia() {
        FakeVentaDAO dao = new FakeVentaDAO();
        LocalDate fecha = LocalDate.of(2026, 4, 20);
        dao.ventasPorRango = List.of(
                crearVenta(1L, fecha.atTime(10, 0), "100.00", "ANA", "EFECTIVO"),
                crearVenta(2L, fecha.atTime(11, 0), "200.00", "ANA", "EFECTIVO"),
                crearVenta(3L, fecha.atTime(12, 0), "300.00", "LUIS", "TARJETA"),
                crearVenta(4L, fecha.atTime(13, 0), "400.00", "LUIS", "EFECTIVO"),
                crearVenta(5L, fecha.atTime(14, 0), "500.00", "ANA", "TARJETA")
        );
        VentaBO bo = new VentaBO(dao);

        List<VentaDTO> resultado = bo.obtenerVentasPorFecha(fecha);

        assertEquals(5, resultado.size());
        assertEquals(1L, resultado.get(0).getIdVenta());
    }

    // ---------- CP-H-04: Ventas por fecha futura sin registros ----------
    @Test
    void obtenerVentasPorFechaFuturaDevuelveListaVacia() {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of();
        VentaBO bo = new VentaBO(dao);

        List<VentaDTO> resultado = bo.obtenerVentasPorFecha(LocalDate.of(2030, 1, 1));

        assertTrue(resultado.isEmpty());
    }

    // ---------- CP-H-05: Fecha null toma el dia actual ----------
    @Test
    void obtenerVentasPorFechaNullUsaFechaActual() {
        FakeVentaDAO dao = new FakeVentaDAO();
        VentaBO bo = new VentaBO(dao);

        bo.obtenerVentasPorFecha(null);

        LocalDate hoy = LocalDate.now();
        assertEquals(hoy.atStartOfDay(), dao.ultimaFechaInicio);
        assertEquals(hoy.plusDays(1).atStartOfDay(), dao.ultimaFechaFin);
    }

    // ---------- CP-H-06: Rango limita a [00:00 del dia, 00:00 del siguiente) ----------
    @Test
    void obtenerVentasPorFechaConsultaRangoDelDiaCompleto() {
        FakeVentaDAO dao = new FakeVentaDAO();
        VentaBO bo = new VentaBO(dao);
        LocalDate fecha = LocalDate.of(2026, 4, 27);

        bo.obtenerVentasPorFecha(fecha);

        assertEquals(LocalDateTime.of(2026, 4, 27, 0, 0), dao.ultimaFechaInicio);
        assertEquals(LocalDateTime.of(2026, 4, 28, 0, 0), dao.ultimaFechaFin);
    }

    // ===================== Helpers =====================
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

    // ===================== Fakes =====================
    private static class FakeVentaDAO implements IVentaDAO {

        List<Venta> ventasPorRango = List.of();
        LocalDateTime ultimaFechaInicio;
        LocalDateTime ultimaFechaFin;

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
