package bos;

import dtos.DetalleVentaDTO;
import dtos.ReporteVentasDTO;
import dtos.VentaDTO;
import entidades.DetalleVenta;
import entidades.Usuario;
import entidades.Venta;
import excepciones.NegocioException;
import interfaces.IVentaBO;
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
public class VentaBO implements IVentaBO {

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
        if (detalles == null || detalles.isEmpty())
        {
            throw new NegocioException("Sin productos.");
        }

        boolean cantidadInvalida = detalles.stream()
                .anyMatch(detalle -> detalle.getCantidad() <= 0);
        if (cantidadInvalida)
        {
            throw new NegocioException("Cantidades no seleccionadas.");
        }

        List<DetalleVenta> entidades = detalles.stream()
                .map(DetalleVentaMapper::toEntity)
                .toList();

        Venta ventaActual = new Venta();
        ventaActual.setDetallesVenta(entidades);
        ventaActual.setFechaHora(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVenta detalle : entidades)
        {
            detalle.setVenta(ventaActual);
            total = total.add(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }

        ventaActual.setTotal(total);
        this.venta = ventaActual;
    }

    public void crearVenta(Usuario sesion) {
        if (venta == null || sesion == null)
        {
            return;
        }
        venta.setUsuario(sesion);
        ventaDAO.crear(venta);
        venta = null;
    }

    public VentaDTO obtenerVentaActual() {
        if (venta == null)
        {
            return null;
        }
        return VentaMapper.toDTO(venta);
    }

    public void validarPagoEfectivoVentaActual(BigDecimal montoRecibido) throws NegocioException {
        if (venta == null || venta.getTotal() == null)
        {
            throw new NegocioException("No hay una venta activa para validar el pago.");
        }
        if (montoRecibido == null)
        {
            throw new NegocioException("Debe ingresar el monto recibido.");
        }
        if (montoRecibido.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new NegocioException("El monto recibido debe ser mayor a cero.");
        }
        if (montoRecibido.compareTo(venta.getTotal()) < 0)
        {
            throw new NegocioException("El pago en efectivo es insuficiente para cubrir el total.");
        }
    }

    public void setMetodoPago(String metodoPago) {
        if (venta != null)
        {
            venta.setMetodoPago(metodoPago);
        }
    }

    public List<DetalleVentaDTO> getProductosVenta() {
        if (venta == null || venta.getDetallesVenta() == null)
        {
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
        if (reporte.getVentas().isEmpty())
        {
            throw new NegocioException("No hay registros disponibles en el periodo seleccionado.");
        }
        if (rutaArchivo == null || rutaArchivo.isBlank())
        {
            throw new NegocioException("La ruta del archivo no es valida.");
        }

        Path destino = Path.of(rutaArchivo).toAbsolutePath();
        try
        {
            if (destino.getParent() != null)
            {
                Files.createDirectories(destino.getParent());
            }
            generarPdfReporte(reporte, destino);
        } catch (IOException e)
        {
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
        if (fechaInicio == null || fechaFin == null)
        {
            throw new NegocioException("Debe seleccionar una fecha inicial y una fecha final.");
        }
        if (fechaFin.isBefore(fechaInicio))
        {
            throw new NegocioException("La fecha final no puede ser menor que la fecha inicial.");
        }
    }

    private static final int ALINEAR_IZQ = 0;
    private static final int ALINEAR_DER = 2;

    private static final float MARGEN_PDF = 50f;
    private static final float ALTO_FILA = 18f;
    private static final float ALTO_HEADER_TABLA = 22f;
    private static final float[] ANCHOS_COLUMNAS = {115f, 75f, 130f, 105f, 87f};
    private static final String[] NOMBRES_COLUMNAS = {"Fecha", "Folio", "Usuario", "Metodo", "Total"};
    private static final int[] ALINEACIONES_COLUMNAS = {ALINEAR_IZQ, ALINEAR_IZQ, ALINEAR_IZQ, ALINEAR_IZQ, ALINEAR_DER};

    private void generarPdfReporte(ReporteVentasDTO reporte, Path destino) throws IOException {
        try (PDDocument documento = new PDDocument()) {
            PDPage pagina = new PDPage(PDRectangle.LETTER);
            documento.addPage(pagina);
            PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

            try {
                float anchoPagina = pagina.getMediaBox().getWidth();
                float altoPagina = pagina.getMediaBox().getHeight();
                float anchoContenido = anchoPagina - 2 * MARGEN_PDF;
                float y = altoPagina - MARGEN_PDF;

                // ===== ENCABEZADO =====
                contenido.setNonStrokingColor(0.16f, 0.32f, 0.55f);
                contenido.addRect(0, y - 4, anchoPagina, 4);
                contenido.fill();
                contenido.setNonStrokingColor(0f, 0f, 0f);

                y -= 32f;
                String titulo = "REPORTE DE VENTAS";
                float anchoTitulo = anchoTexto(PDType1Font.HELVETICA_BOLD, 22f, titulo);
                contenido.setNonStrokingColor(0.10f, 0.15f, 0.30f);
                escribirCelda(contenido, PDType1Font.HELVETICA_BOLD, 22f,
                        MARGEN_PDF + (anchoContenido - anchoTitulo) / 2f, y, titulo);
                contenido.setNonStrokingColor(0f, 0f, 0f);

                y -= 22f;
                String periodo = "Periodo del "
                        + reporte.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        + " al " + reporte.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                float anchoPeriodo = anchoTexto(PDType1Font.HELVETICA, 11f, periodo);
                contenido.setNonStrokingColor(0.40f, 0.40f, 0.40f);
                escribirCelda(contenido, PDType1Font.HELVETICA, 11f,
                        MARGEN_PDF + (anchoContenido - anchoPeriodo) / 2f, y, periodo);
                contenido.setNonStrokingColor(0f, 0f, 0f);

                y -= 26f;

                // ===== PANEL DE RESUMEN =====
                float altoResumen = 60f;
                float anchoTarjeta = anchoContenido / 3f;

                contenido.setStrokingColor(0.78f, 0.80f, 0.85f);
                contenido.addRect(MARGEN_PDF, y - altoResumen, anchoContenido, altoResumen);
                contenido.stroke();
                contenido.moveTo(MARGEN_PDF + anchoTarjeta, y - altoResumen);
                contenido.lineTo(MARGEN_PDF + anchoTarjeta, y);
                contenido.stroke();
                contenido.moveTo(MARGEN_PDF + 2 * anchoTarjeta, y - altoResumen);
                contenido.lineTo(MARGEN_PDF + 2 * anchoTarjeta, y);
                contenido.stroke();

                dibujarTarjetaResumen(contenido, MARGEN_PDF, y, anchoTarjeta,
                        "VENTAS", String.valueOf(reporte.getCantidadVentas()));
                dibujarTarjetaResumen(contenido, MARGEN_PDF + anchoTarjeta, y, anchoTarjeta,
                        "MONTO TOTAL", "$" + formatearMonto(reporte.getMontoTotal()));
                dibujarTarjetaResumen(contenido, MARGEN_PDF + 2 * anchoTarjeta, y, anchoTarjeta,
                        "PROMEDIO", "$" + formatearMonto(reporte.getPromedioVenta()));

                y -= altoResumen + 28f;

                // ===== TABLA =====
                y = dibujarHeaderTabla(contenido, MARGEN_PDF, y);

                boolean alterno = false;
                for (VentaDTO ventaDTO : reporte.getVentas()) {
                    if (y - ALTO_FILA < MARGEN_PDF + 30f) {
                        contenido.close();
                        pagina = new PDPage(PDRectangle.LETTER);
                        documento.addPage(pagina);
                        contenido = new PDPageContentStream(documento, pagina);
                        y = altoPagina - MARGEN_PDF;
                        y = dibujarHeaderTabla(contenido, MARGEN_PDF, y);
                    }

                    String[] valores = {
                        formatearFecha(ventaDTO),
                        obtenerFolio(ventaDTO),
                        obtenerUsuario(ventaDTO),
                        obtenerMetodoPago(ventaDTO),
                        "$" + formatearMonto(ventaDTO.getTotal())
                    };
                    y = dibujarFilaTabla(contenido, MARGEN_PDF, y, valores, alterno);
                    alterno = !alterno;
                }

                // Borde inferior + bordes laterales de la tabla
                contenido.setStrokingColor(0.78f, 0.80f, 0.85f);
                contenido.moveTo(MARGEN_PDF, y);
                contenido.lineTo(MARGEN_PDF + anchoContenido, y);
                contenido.stroke();

                // Total general bajo la tabla
                y -= 22f;
                String pieMonto = "Total general:  $" + formatearMonto(reporte.getMontoTotal());
                float anchoPie = anchoTexto(PDType1Font.HELVETICA_BOLD, 11f, pieMonto);
                contenido.setNonStrokingColor(0.10f, 0.15f, 0.30f);
                escribirCelda(contenido, PDType1Font.HELVETICA_BOLD, 11f,
                        MARGEN_PDF + anchoContenido - anchoPie, y, pieMonto);
                contenido.setNonStrokingColor(0f, 0f, 0f);
            } finally {
                contenido.close();
            }

            agregarPiesDePagina(documento);
            documento.save(destino.toFile());
        }
    }

    private float anchoTexto(PDType1Font fuente, float tamanio, String texto) throws IOException {
        return fuente.getStringWidth(limpiarTextoPdf(texto)) / 1000f * tamanio;
    }

    private void dibujarTarjetaResumen(PDPageContentStream contenido, float x, float yTop,
            float ancho, String etiqueta, String valor) throws IOException {
        float yEtiqueta = yTop - 20f;
        float yValor = yTop - 44f;

        float anchoEtiqueta = anchoTexto(PDType1Font.HELVETICA_BOLD, 9f, etiqueta);
        contenido.setNonStrokingColor(0.45f, 0.45f, 0.45f);
        escribirCelda(contenido, PDType1Font.HELVETICA_BOLD, 9f,
                x + (ancho - anchoEtiqueta) / 2f, yEtiqueta, etiqueta);

        float anchoValor = anchoTexto(PDType1Font.HELVETICA_BOLD, 16f, valor);
        contenido.setNonStrokingColor(0.10f, 0.15f, 0.30f);
        escribirCelda(contenido, PDType1Font.HELVETICA_BOLD, 16f,
                x + (ancho - anchoValor) / 2f, yValor, valor);

        contenido.setNonStrokingColor(0f, 0f, 0f);
    }

    private float dibujarHeaderTabla(PDPageContentStream contenido, float xInicio, float yTop) throws IOException {
        float yFondoInferior = yTop - ALTO_HEADER_TABLA;
        float anchoTotal = 0f;
        for (float a : ANCHOS_COLUMNAS) {
            anchoTotal += a;
        }

        contenido.setNonStrokingColor(0.16f, 0.32f, 0.55f);
        contenido.addRect(xInicio, yFondoInferior, anchoTotal, ALTO_HEADER_TABLA);
        contenido.fill();

        contenido.setNonStrokingColor(1f, 1f, 1f);
        float x = xInicio;
        float yTexto = yFondoInferior + 7f;
        for (int i = 0; i < NOMBRES_COLUMNAS.length; i++) {
            String nombre = NOMBRES_COLUMNAS[i];
            float anchoT = anchoTexto(PDType1Font.HELVETICA_BOLD, 10f, nombre);
            float xT = ALINEACIONES_COLUMNAS[i] == ALINEAR_DER
                    ? x + ANCHOS_COLUMNAS[i] - anchoT - 8f
                    : x + 8f;
            escribirCelda(contenido, PDType1Font.HELVETICA_BOLD, 10f, xT, yTexto, nombre);
            x += ANCHOS_COLUMNAS[i];
        }
        contenido.setNonStrokingColor(0f, 0f, 0f);
        return yFondoInferior;
    }

    private float dibujarFilaTabla(PDPageContentStream contenido, float xInicio, float yTop,
            String[] valores, boolean alterno) throws IOException {
        float yFondoInferior = yTop - ALTO_FILA;
        float anchoTotal = 0f;
        for (float a : ANCHOS_COLUMNAS) {
            anchoTotal += a;
        }

        if (alterno) {
            contenido.setNonStrokingColor(0.96f, 0.97f, 0.99f);
            contenido.addRect(xInicio, yFondoInferior, anchoTotal, ALTO_FILA);
            contenido.fill();
            contenido.setNonStrokingColor(0f, 0f, 0f);
        }

        float x = xInicio;
        float yTexto = yFondoInferior + 5f;
        contenido.setNonStrokingColor(0.20f, 0.20f, 0.20f);
        for (int i = 0; i < valores.length; i++) {
            String valor = ajustarPorAncho(PDType1Font.HELVETICA, 9f,
                    limpiarTextoPdf(valores[i]), ANCHOS_COLUMNAS[i] - 16f);
            float anchoT = anchoTexto(PDType1Font.HELVETICA, 9f, valor);
            float xT = ALINEACIONES_COLUMNAS[i] == ALINEAR_DER
                    ? x + ANCHOS_COLUMNAS[i] - anchoT - 8f
                    : x + 8f;
            escribirCelda(contenido, PDType1Font.HELVETICA, 9f, xT, yTexto, valor);
            x += ANCHOS_COLUMNAS[i];
        }
        contenido.setNonStrokingColor(0f, 0f, 0f);
        return yFondoInferior;
    }

    private String ajustarPorAncho(PDType1Font fuente, float tamanio, String texto, float anchoMax) throws IOException {
        if (texto == null) {
            return "";
        }
        if (anchoTexto(fuente, tamanio, texto) <= anchoMax) {
            return texto;
        }
        String t = texto;
        while (t.length() > 1 && anchoTexto(fuente, tamanio, t + "...") > anchoMax) {
            t = t.substring(0, t.length() - 1);
        }
        return t + "...";
    }

    private void agregarPiesDePagina(PDDocument documento) throws IOException {
        int total = documento.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            PDPage pagina = documento.getPage(i);
            try (PDPageContentStream c = new PDPageContentStream(documento, pagina,
                    PDPageContentStream.AppendMode.APPEND, true, true)) {
                float anchoPagina = pagina.getMediaBox().getWidth();

                String pie = "Pagina " + (i + 1) + " de " + total;
                float ancho = anchoTexto(PDType1Font.HELVETICA, 9f, pie);
                c.setNonStrokingColor(0.45f, 0.45f, 0.45f);
                escribirCelda(c, PDType1Font.HELVETICA, 9f,
                        (anchoPagina - ancho) / 2f, 30f, pie);

                String fecha = "Generado: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                float anchoFecha = anchoTexto(PDType1Font.HELVETICA, 9f, fecha);
                escribirCelda(c, PDType1Font.HELVETICA, 9f,
                        anchoPagina - MARGEN_PDF - anchoFecha, 30f, fecha);
                c.setNonStrokingColor(0f, 0f, 0f);
            }
        }
    }

    private void escribirCelda(PDPageContentStream contenido, PDType1Font fuente, float tamanio, float x, float y, String texto) throws IOException {
        contenido.beginText();
        contenido.setFont(fuente, tamanio);
        contenido.newLineAtOffset(x, y);
        contenido.showText(limpiarTextoPdf(texto));
        contenido.endText();
    }
    
    private String limpiarTextoPdf(String texto) {
        if (texto == null)
        {
            return "";
        }
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalizado.replaceAll("[^\\x20-\\x7E]", "?");
    }

    private String formatearFecha(VentaDTO ventaDTO) {
        if (ventaDTO.getFechaHora() == null)
        {
            return "Sin fecha";
        }
        return ventaDTO.getFechaHora().format(FORMATO_FECHA_HORA);
    }

    private String obtenerFolio(VentaDTO ventaDTO) {
        if (ventaDTO.getFolio() != null && !ventaDTO.getFolio().isBlank())
        {
            return ventaDTO.getFolio();
        }
        return ventaDTO.getIdVenta() == null ? "Sin folio" : "#" + ventaDTO.getIdVenta();
    }

    private String obtenerUsuario(VentaDTO ventaDTO) {
        if (ventaDTO.getUsuario() == null || ventaDTO.getUsuario().getNombre() == null)
        {
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
