package bos;

import dtos.DetalleVentaDTO;
import dtos.ReporteVentasDTO;
import dtos.VentaDTO;
import entidades.DetalleVenta;
import entidades.Usuario;
import entidades.Venta;
import excepciones.NegocioException;
import interfaces.IVentaDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import mappers.DetalleVentaMapper;
import mappers.VentaMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author luise
 */
public class VentaBO {

    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final IVentaDAO ventaDAO;
    private Venta venta;

    public VentaBO(IVentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
    }

    public void crearVenta(VentaDTO ventaDTO) {
        Venta ventaEntity = VentaMapper.toEntity(ventaDTO);
        ventaDAO.crear(ventaEntity);
    }

    public void crearVenta(List<DetalleVentaDTO> detalles) throws NegocioException {
        if (detalles == null || detalles.isEmpty()) {
            throw new NegocioException("Sin productos.");
        }

        boolean cantidadInvalida = detalles.stream()
                .anyMatch(detalle -> detalle.getCantidad() <= 0);
        if (cantidadInvalida) {
            throw new NegocioException("Cantidades no seleccionadas.");
        }

        List<DetalleVenta> entidades = detalles.stream()
                .map(DetalleVentaMapper::toEntity)
                .toList();

        Venta ventaActual = new Venta();
        ventaActual.setDetallesVenta(entidades);
        ventaActual.setFechaHora(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVenta detalle : entidades) {
            detalle.setVenta(ventaActual);
            total = total.add(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }

        ventaActual.setTotal(total);
        this.venta = ventaActual;
    }

    public void crearVenta(Usuario sesion) {
        if (venta == null || sesion == null) {
            return;
        }
        venta.setUsuario(sesion);
        ventaDAO.crear(venta);
        venta = null;
    }

    public VentaDTO obtenerVentaActual() {
        if (venta == null) {
            return null;
        }
        return VentaMapper.toDTO(venta);
    }

    public void validarPagoEfectivoVentaActual(BigDecimal montoRecibido) throws NegocioException {
        if (venta == null || venta.getTotal() == null) {
            throw new NegocioException("No hay una venta activa para validar el pago.");
        }
        if (montoRecibido == null) {
            throw new NegocioException("Debe ingresar el monto recibido.");
        }
        if (montoRecibido.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("El monto recibido debe ser mayor a cero.");
        }
        if (montoRecibido.compareTo(venta.getTotal()) < 0) {
            throw new NegocioException("El pago en efectivo es insuficiente para cubrir el total.");
        }
    }

    public void setMetodoPago(String metodoPago) {
        if (venta != null) {
            venta.setMetodoPago(metodoPago);
        }
    }

    public List<DetalleVentaDTO> getProductosVenta() {
        if (venta == null || venta.getDetallesVenta() == null) {
            return Collections.emptyList();
        }
        return venta.getDetallesVenta().stream()
                .map(DetalleVentaMapper::toDTO)
                .toList();
    }

    public VentaDTO obtenerVenta(Long id) {
        Venta ventaEntity = ventaDAO.obtener(id);
        return ventaEntity == null ? null : VentaMapper.toDTO(ventaEntity);
    }

    public void actualizarVenta(VentaDTO ventaDTO) {
        Venta ventaEntity = VentaMapper.toEntity(ventaDTO);
        ventaDAO.actualizar(ventaEntity);
    }

    public void eliminarVenta(Long id) {
        ventaDAO.eliminar(id);
    }

    public List<VentaDTO> obtenerTodasLasVentas() {
        return ventaDAO.obtenerTodos().stream()
                .map(VentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<VentaDTO> obtenerVentasPorFecha(LocalDate fecha) {
        LocalDate fechaConsulta = fecha == null ? LocalDate.now() : fecha;
        return ventaDAO.obtenerPorRango(
                fechaConsulta.atStartOfDay(),
                fechaConsulta.plusDays(1).atStartOfDay()
        ).stream()
                .map(VentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReporteVentasDTO obtenerReporteVentas(LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException {
        validarRangoFechas(fechaInicio, fechaFin);
        List<VentaDTO> ventas = ventaDAO.obtenerPorRango(
                fechaInicio.atStartOfDay(),
                fechaFin.plusDays(1).atStartOfDay()
        ).stream()
                .map(VentaMapper::toDTO)
                .collect(Collectors.toList());
        return construirReporte(fechaInicio, fechaFin, ventas);
    }

    public void exportarReporteVentas(LocalDate fechaInicio, LocalDate fechaFin, String rutaArchivo) throws NegocioException {
        ReporteVentasDTO reporte = obtenerReporteVentas(fechaInicio, fechaFin);
        if (reporte.getVentas().isEmpty()) {
            throw new NegocioException("No hay registros disponibles en el periodo seleccionado.");
        }
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            throw new NegocioException("La ruta del archivo no es valida.");
        }

        Path destino = Path.of(rutaArchivo).toAbsolutePath();
        try {
            if (destino.getParent() != null) {
                Files.createDirectories(destino.getParent());
            }
            generarPdfReporte(reporte, destino);
        } catch (IOException e) {
            throw new NegocioException("No fue posible generar el PDF del reporte.");
        }
    }

    private ReporteVentasDTO construirReporte(LocalDate fechaInicio, LocalDate fechaFin, List<VentaDTO> ventas) {
        ReporteVentasDTO reporte = new ReporteVentasDTO();
        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);
        reporte.setVentas(ventas);
        reporte.setCantidadVentas(ventas.size());

        BigDecimal montoTotal = ventas.stream()
                .map(VentaDTO::getTotal)
                .filter(total -> total != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        reporte.setMontoTotal(montoTotal);

        BigDecimal promedio = ventas.isEmpty()
                ? BigDecimal.ZERO
                : montoTotal.divide(BigDecimal.valueOf(ventas.size()), 2, RoundingMode.HALF_UP);
        reporte.setPromedioVenta(promedio);
        return reporte;
    }

    private void validarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException {
        if (fechaInicio == null || fechaFin == null) {
            throw new NegocioException("Debe seleccionar una fecha inicial y una fecha final.");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new NegocioException("La fecha final no puede ser menor que la fecha inicial.");
        }
    }

    private void generarPdfReporte(ReporteVentasDTO reporte, Path destino) throws IOException {
        try (PDDocument documento = new PDDocument()) {
            PDPage pagina = new PDPage(PDRectangle.LETTER);
            documento.addPage(pagina);

            float margen = 50f;
            float y = pagina.getMediaBox().getHeight() - margen;
            PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

            try {
                y = escribirLinea(contenido, PDType1Font.HELVETICA_BOLD, 18, margen, y, "Reporte de ventas");
                y = escribirLinea(contenido, PDType1Font.HELVETICA, 12, margen, y, "Periodo: "
                        + reporte.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        + " - "
                        + reporte.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                y = escribirLinea(contenido, PDType1Font.HELVETICA, 12, margen, y,
                        "Cantidad de ventas: " + reporte.getCantidadVentas());
                y = escribirLinea(contenido, PDType1Font.HELVETICA, 12, margen, y,
                        "Monto total: $" + reporte.getMontoTotal().setScale(2, RoundingMode.HALF_UP));
                y = escribirLinea(contenido, PDType1Font.HELVETICA, 12, margen, y,
                        "Promedio por venta: $" + reporte.getPromedioVenta().setScale(2, RoundingMode.HALF_UP));
                y -= 10f;

                y = escribirLinea(contenido, PDType1Font.COURIER_BOLD, 10, margen, y,
                        ajustarColumna("Fecha", 18)
                        + ajustarColumna("Venta", 10)
                        + ajustarColumna("Usuario", 18)
                        + ajustarColumna("Metodo", 16)
                        + "Total");

                for (VentaDTO ventaDTO : reporte.getVentas()) {
                    if (y < margen) {
                        contenido.close();
                        pagina = new PDPage(PDRectangle.LETTER);
                        documento.addPage(pagina);
                        contenido = new PDPageContentStream(documento, pagina);
                        y = pagina.getMediaBox().getHeight() - margen;
                    }

                    String fila = ajustarColumna(formatearFecha(ventaDTO), 18)
                            + ajustarColumna(obtenerFolio(ventaDTO), 10)
                            + ajustarColumna(obtenerUsuario(ventaDTO), 18)
                            + ajustarColumna(obtenerMetodoPago(ventaDTO), 16)
                            + "$" + formatearMonto(ventaDTO.getTotal());
                    y = escribirLinea(contenido, PDType1Font.COURIER, 9, margen, y, fila);
                }
            } finally {
                contenido.close();
            }

            documento.save(destino.toFile());
        }
    }

    private float escribirLinea(PDPageContentStream contenido, PDType1Font fuente, float tamanio, float x, float y, String texto) throws IOException {
        contenido.beginText();
        contenido.setFont(fuente, tamanio);
        contenido.newLineAtOffset(x, y);
        contenido.showText(limpiarTextoPdf(texto));
        contenido.endText();
        return y - (tamanio + 6f);
    }

    private String limpiarTextoPdf(String texto) {
        if (texto == null) {
            return "";
        }
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalizado.replaceAll("[^\\x20-\\x7E]", "?");
    }

    private String ajustarColumna(String valor, int longitud) {
        String limpio = limpiarTextoPdf(valor == null ? "" : valor);
        if (limpio.length() > longitud) {
            limpio = limpio.substring(0, Math.max(0, longitud - 1)) + "~";
        }
        return String.format("%-" + longitud + "s", limpio);
    }

    private String formatearFecha(VentaDTO ventaDTO) {
        if (ventaDTO.getFechaHora() == null) {
            return "Sin fecha";
        }
        return ventaDTO.getFechaHora().format(FORMATO_FECHA_HORA);
    }

    private String obtenerFolio(VentaDTO ventaDTO) {
        if (ventaDTO.getFolio() != null && !ventaDTO.getFolio().isBlank()) {
            return ventaDTO.getFolio();
        }
        return ventaDTO.getIdVenta() == null ? "Sin folio" : "#" + ventaDTO.getIdVenta();
    }

    private String obtenerUsuario(VentaDTO ventaDTO) {
        if (ventaDTO.getUsuario() == null || ventaDTO.getUsuario().getNombre() == null) {
            return "Sin usuario";
        }
        return ventaDTO.getUsuario().getNombre();
    }

    private String obtenerMetodoPago(VentaDTO ventaDTO) {
        return ventaDTO.getMetodoPago() == null ? "Sin metodo" : ventaDTO.getMetodoPago();
    }

    private String formatearMonto(BigDecimal monto) {
        return monto == null ? "0.00" : monto.setScale(2, RoundingMode.HALF_UP).toString();
    }
}

