package Pruebaa;

import bos.VentaBO;
import dtos.ReporteVentasDTO;
import dtos.UsuarioDTO;
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

/**
 * Pruebas unitarias del CU03 - Generar Reporte de Ventas (rol Dueño).
 *
 * Cubre los casos de prueba CP-R-01 a CP-R-18 del plan de pruebas.
 */
class VentaBOReporteTest {

    @TempDir
    Path tempDir;

    // ---------- CP-R-01: Rango valido con ventas calcula totales y promedio ----------
    @Test
    void obtenerReporteVentasCalculaTotalesYPromedio() throws NegocioException {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(
                crearVenta(1L, LocalDateTime.of(2026, 4, 26, 10, 0), "100.00", "ANA", "EFECTIVO"),
                crearVenta(2L, LocalDateTime.of(2026, 4, 27, 11, 30), "250.50", "LUIS", "TARJETA")
        );
        VentaBO bo = new VentaBO(dao);

        ReporteVentasDTO reporte = bo.obtenerReporteVentas(
                LocalDate.of(2026, 4, 26), LocalDate.of(2026, 4, 27));

        assertEquals(2, reporte.getCantidadVentas());
        assertEquals(new BigDecimal("350.50"), reporte.getMontoTotal());
        assertEquals(new BigDecimal("175.25"), reporte.getPromedioVenta());
        assertEquals(2, reporte.getVentas().size());
    }

    // ---------- CP-R-02: Rango valido sin ventas ----------
    @Test
    void obtenerReporteVentasSinResultadosDevuelveTotalesEnCero() throws NegocioException {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of();
        VentaBO bo = new VentaBO(dao);

        ReporteVentasDTO reporte = bo.obtenerReporteVentas(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 2));

        assertEquals(0, reporte.getCantidadVentas());
        assertEquals(BigDecimal.ZERO, reporte.getMontoTotal());
        assertEquals(BigDecimal.ZERO, reporte.getPromedioVenta());
    }

    // ---------- CP-R-03: Mismo dia (inicio == fin) ----------
    @Test
    void obtenerReporteVentasMismoDiaConsultaRangoCompleto() throws NegocioException {
        FakeVentaDAO dao = new FakeVentaDAO();
        VentaBO bo = new VentaBO(dao);
        LocalDate fecha = LocalDate.of(2026, 4, 29);

        bo.obtenerReporteVentas(fecha, fecha);

        assertEquals(LocalDateTime.of(2026, 4, 29, 0, 0), dao.ultimaFechaInicio);
        assertEquals(LocalDateTime.of(2026, 4, 30, 0, 0), dao.ultimaFechaFin);
    }

    // ---------- CP-R-04: Fecha inicio nula ----------
    @Test
    void obtenerReporteVentasConFechaInicioNullLanzaError() {
        VentaBO bo = new VentaBO(new FakeVentaDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.obtenerReporteVentas(null, LocalDate.of(2026, 4, 29))
        );

        assertEquals("Debe seleccionar una fecha inicial y una fecha final.", error.getMessage());
    }

    // ---------- CP-R-05: Fecha fin nula ----------
    @Test
    void obtenerReporteVentasConFechaFinNullLanzaError() {
        VentaBO bo = new VentaBO(new FakeVentaDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.obtenerReporteVentas(LocalDate.of(2026, 4, 29), null)
        );

        assertEquals("Debe seleccionar una fecha inicial y una fecha final.", error.getMessage());
    }

    // ---------- CP-R-06: Fecha fin anterior a inicio ----------
    @Test
    void obtenerReporteVentasConRangoInvertidoLanzaError() {
        VentaBO bo = new VentaBO(new FakeVentaDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.obtenerReporteVentas(
                        LocalDate.of(2026, 4, 29), LocalDate.of(2026, 4, 1))
        );

        assertEquals("La fecha final no puede ser menor que la fecha inicial.", error.getMessage());
    }

    // ---------- CP-R-07: Promedio HALF_UP exacto ----------
    @Test
    void obtenerReporteVentasCalculaPromedioRedondeadoHalfUp() throws NegocioException {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(
                crearVenta(1L, LocalDateTime.of(2026, 4, 27, 9, 0), "100.00", "A", "EFECTIVO"),
                crearVenta(2L, LocalDateTime.of(2026, 4, 27, 10, 0), "200.00", "A", "EFECTIVO"),
                crearVenta(3L, LocalDateTime.of(2026, 4, 27, 11, 0), "300.00", "A", "EFECTIVO")
        );
        VentaBO bo = new VentaBO(dao);

        ReporteVentasDTO reporte = bo.obtenerReporteVentas(
                LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27));

        assertEquals(new BigDecimal("600.00"), reporte.getMontoTotal());
        assertEquals(new BigDecimal("200.00"), reporte.getPromedioVenta());
    }

    // ---------- CP-R-08: Promedio con division no exacta (redondeo a 2 decimales) ----------
    @Test
    void obtenerReporteVentasRedondeaPromedioConDivisionNoExacta() throws NegocioException {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(
                crearVenta(1L, LocalDateTime.of(2026, 4, 27, 9, 0), "100.00", "A", "EFECTIVO"),
                crearVenta(2L, LocalDateTime.of(2026, 4, 27, 10, 0), "100.00", "A", "EFECTIVO"),
                crearVenta(3L, LocalDateTime.of(2026, 4, 27, 11, 0), "100.01", "A", "EFECTIVO")
        );
        VentaBO bo = new VentaBO(dao);

        ReporteVentasDTO reporte = bo.obtenerReporteVentas(
                LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27));

        assertEquals(new BigDecimal("300.01"), reporte.getMontoTotal());
        // 300.01 / 3 = 100.0033... -> 100.00 (HALF_UP a 2 decimales)
        assertEquals(new BigDecimal("100.00"), reporte.getPromedioVenta());
    }

    // ---------- CP-R-09: Reporte ignora ventas con total null sin lanzar NPE ----------
    @Test
    void obtenerReporteVentasIgnoraVentasConTotalNull() throws NegocioException {
        FakeVentaDAO dao = new FakeVentaDAO();
        Venta ventaSinTotal = crearVenta(1L, LocalDateTime.of(2026, 4, 27, 9, 0), "100.00", "A", "EFECTIVO");
        ventaSinTotal.setTotal(null);
        dao.ventasPorRango = List.of(
                ventaSinTotal,
                crearVenta(2L, LocalDateTime.of(2026, 4, 27, 10, 0), "100.00", "A", "EFECTIVO")
        );
        VentaBO bo = new VentaBO(dao);

        ReporteVentasDTO reporte = bo.obtenerReporteVentas(
                LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27));

        assertEquals(2, reporte.getCantidadVentas());
        assertEquals(new BigDecimal("100.00"), reporte.getMontoTotal());
    }

    // ---------- CP-R-10: Exportar PDF cuando hay datos ----------
    @Test
    void exportarReporteVentasGeneraPdfCuandoHayDatos() throws Exception {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(
                crearVenta(7L, LocalDateTime.of(2026, 4, 27, 14, 15), "180.00", "CARLA", "TRANSFERENCIA")
        );
        VentaBO bo = new VentaBO(dao);
        Path pdf = tempDir.resolve("reporte-ventas.pdf");

        bo.exportarReporteVentas(LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27), pdf.toString());

        assertTrue(Files.exists(pdf));
        assertTrue(Files.size(pdf) > 0);
    }

    // ---------- CP-R-11: Exportar reporte sin datos lanza error ----------
    @Test
    void exportarReporteVentasSinDatosLanzaError() {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of();
        VentaBO bo = new VentaBO(dao);

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.exportarReporteVentas(
                        LocalDate.of(2026, 4, 27),
                        LocalDate.of(2026, 4, 27),
                        tempDir.resolve("reporte.pdf").toString())
        );

        assertEquals("No hay registros disponibles en el periodo seleccionado.", error.getMessage());
    }

    // ---------- CP-R-12: Ruta nula lanza error ----------
    @Test
    void exportarReporteVentasConRutaNullLanzaError() {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(
                crearVenta(1L, LocalDateTime.of(2026, 4, 27, 9, 0), "100.00", "A", "EFECTIVO")
        );
        VentaBO bo = new VentaBO(dao);

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.exportarReporteVentas(
                        LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27), null)
        );

        assertEquals("La ruta del archivo no es valida.", error.getMessage());
    }

    // ---------- CP-R-13: Ruta en blanco lanza error ----------
    @Test
    void exportarReporteVentasConRutaEnBlancoLanzaError() {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(
                crearVenta(1L, LocalDateTime.of(2026, 4, 27, 9, 0), "100.00", "A", "EFECTIVO")
        );
        VentaBO bo = new VentaBO(dao);

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.exportarReporteVentas(
                        LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27), "   ")
        );

        assertEquals("La ruta del archivo no es valida.", error.getMessage());
    }

    // ---------- CP-R-14: Ruta con carpetas inexistentes las crea ----------
    @Test
    void exportarReporteVentasCreaDirectoriosFaltantes() throws Exception {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(
                crearVenta(1L, LocalDateTime.of(2026, 4, 27, 9, 0), "100.00", "A", "EFECTIVO")
        );
        VentaBO bo = new VentaBO(dao);
        Path pdf = tempDir.resolve("nuevoDir/sub/reporte.pdf");

        bo.exportarReporteVentas(LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27), pdf.toString());

        assertTrue(Files.exists(pdf));
        assertTrue(Files.exists(pdf.getParent()));
    }

    // ---------- CP-R-16: Texto con acentos y enies se exporta sin fallar ----------
    @Test
    void exportarReporteVentasConCaracteresEspecialesNoFalla() throws Exception {
        FakeVentaDAO dao = new FakeVentaDAO();
        dao.ventasPorRango = List.of(
                crearVenta(1L, LocalDateTime.of(2026, 4, 27, 9, 0), "100.00", "José Núñez", "EFECTIVO")
        );
        VentaBO bo = new VentaBO(dao);
        Path pdf = tempDir.resolve("reporte-acentos.pdf");

        assertDoesNotThrow(() -> bo.exportarReporteVentas(
                LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27), pdf.toString()));
        assertTrue(Files.exists(pdf));
    }

    // ---------- CP-R-17: Reporte con muchas ventas genera multiples paginas ----------
    @Test
    void exportarReporteVentasGeneraMultiplesPaginasConMuchasVentas() throws Exception {
        FakeVentaDAO dao = new FakeVentaDAO();
        List<Venta> muchas = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            muchas.add(crearVenta((long) i,
                    LocalDateTime.of(2026, 4, 27, 9, 0).plusMinutes(i),
                    "50.00", "A", "EFECTIVO"));
        }
        dao.ventasPorRango = muchas;
        VentaBO bo = new VentaBO(dao);
        Path pdf = tempDir.resolve("reporte-grande.pdf");

        bo.exportarReporteVentas(LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27), pdf.toString());

        assertTrue(Files.exists(pdf));
        // Un PDF con 60 ventas debe pesar mas que el header solo (>1 KB)
        assertTrue(Files.size(pdf) > 1024);
    }

    // ---------- CP-R-18: Folio null en venta no lanza NPE al exportar ----------
    @Test
    void exportarReporteVentasConFolioNullNoLanzaError() throws Exception {
        FakeVentaDAO dao = new FakeVentaDAO();
        Venta venta = crearVenta(15L, LocalDateTime.of(2026, 4, 27, 9, 0), "100.00", "A", "EFECTIVO");
        // El campo folio se queda en null por defecto
        dao.ventasPorRango = List.of(venta);
        VentaBO bo = new VentaBO(dao);
        Path pdf = tempDir.resolve("reporte-folio-null.pdf");

        assertDoesNotThrow(() -> bo.exportarReporteVentas(
                LocalDate.of(2026, 4, 27), LocalDate.of(2026, 4, 27), pdf.toString()));
        assertTrue(Files.exists(pdf));
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
