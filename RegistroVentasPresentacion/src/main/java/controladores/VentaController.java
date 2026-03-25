package controladores;

import GestionarVentas.MenuCajeroFrm;
import GestionarVentas.MetodoEfectivoFrm;
import GestionarVentas.MetodoPagoFrm;
import GestionarVentas.PuntoVentaFrm;
import IniciarSesion.PantallaGestionarUsuarios;
import aplicacion.AplicacionContexto;
import aplicacion.SesionActual;
import dtos.DetalleVentaDTO;
import dtos.ProductoDTO;
import dtos.UsuarioDTO;
import dtos.VentaDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class VentaController {

    private final PuntoVentaFrm vista;
    private final List<DetalleVentaDTO> carrito = new ArrayList<>();
    private MetodoPagoFrm metodoPagoFrm;
    private MetodoEfectivoFrm metodoEfectivoFrm;

    public VentaController(PuntoVentaFrm vista) {
        this.vista = vista;
        configurarVista();
        cargarProductos();
        refrescarCarrito();
    }

    private void configurarVista() {
        UsuarioDTO usuario = SesionActual.getUsuario();
        if (usuario != null) {
            vista.setNombreUsuario(usuario.getNombre());
        }
        vista.addAgregarListener(e -> agregarProducto());
        vista.addPagarListener(e -> abrirMetodoPago());
        vista.addVolverListener(e -> volverAlMenu());
    }

    private void cargarProductos() {
        vista.setProductos(AplicacionContexto.getProductoBO().obtenerTodosLosProductos());
    }

    private void agregarProducto() {
        ProductoDTO producto = vista.getProductoSeleccionado();
        if (producto == null) {
            vista.mostrarError("Selecciona un producto.");
            return;
        }

        int cantidad = vista.getCantidadSeleccionada();
        if (cantidad <= 0) {
            vista.mostrarError("La cantidad debe ser mayor a cero.");
            return;
        }

        DetalleVentaDTO existente = carrito.stream()
                .filter(item -> item.getIdProducto().equals(producto.getIdProducto()))
                .findFirst()
                .orElse(null);

        if (existente == null) {
            DetalleVentaDTO detalle = new DetalleVentaDTO();
            detalle.setIdProducto(producto.getIdProducto());
            detalle.setNombreProducto(producto.getNombre());
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
            carrito.add(detalle);
        } else {
            existente.setCantidad(existente.getCantidad() + cantidad);
            existente.setSubtotal(existente.getPrecioUnitario().multiply(BigDecimal.valueOf(existente.getCantidad())));
        }

        refrescarCarrito();
        vista.mostrarMensaje("Producto agregado al carrito.");
    }

    private void abrirMetodoPago() {
        if (carrito.isEmpty()) {
            vista.mostrarError("Agrega al menos un producto antes de continuar.");
            return;
        }

        metodoPagoFrm = new MetodoPagoFrm(obtenerTotal());
        metodoPagoFrm.addSeleccionMetodoListener(e -> seleccionarMetodoPago(e.getActionCommand()));
        metodoPagoFrm.addCancelarListener(e -> metodoPagoFrm.dispose());
        metodoPagoFrm.setVisible(true);
    }

    private void seleccionarMetodoPago(String metodo) {
        if (metodoPagoFrm != null) {
            metodoPagoFrm.dispose();
            metodoPagoFrm = null;
        }

        if ("Efectivo".equalsIgnoreCase(metodo)) {
            metodoEfectivoFrm = new MetodoEfectivoFrm(obtenerTotal());
            metodoEfectivoFrm.addActualizarPagoListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    actualizarCambio();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    actualizarCambio();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    actualizarCambio();
                }
            });
            metodoEfectivoFrm.addAceptarListener(e -> registrarVentaEfectivo());
            metodoEfectivoFrm.addCancelarListener(e -> metodoEfectivoFrm.dispose());
            metodoEfectivoFrm.setVisible(true);
            return;
        }

        registrarVenta(metodo, null);
    }

    private void actualizarCambio() {
        if (metodoEfectivoFrm == null) {
            return;
        }
        BigDecimal cambio = metodoEfectivoFrm.getPagoRecibido().subtract(obtenerTotal());
        metodoEfectivoFrm.setCambio(cambio.max(BigDecimal.ZERO));
    }

    private void registrarVentaEfectivo() {
        BigDecimal pago = metodoEfectivoFrm.getPagoRecibido();
        BigDecimal total = obtenerTotal();
        if (pago.compareTo(total) < 0) {
            metodoEfectivoFrm.mostrarError("El pago no cubre el total.");
            return;
        }
        registrarVenta("Efectivo", pago);
    }

    private void registrarVenta(String metodoPago, BigDecimal pago) {
        try {
            UsuarioDTO usuario = SesionActual.getUsuario();
            if (usuario == null) {
                vista.mostrarError("No hay una sesion activa.");
                return;
            }

            VentaDTO ventaDTO = new VentaDTO();
            ventaDTO.setIdUsuario(usuario.getIdUsuario());
            ventaDTO.setNombreUsuario(usuario.getNombre());
            ventaDTO.setMetodoPago(metodoPago);
            ventaDTO.setDetalles(new ArrayList<>(carrito));

            VentaDTO ventaGuardada = AplicacionContexto.getVentaBO().registrarVenta(ventaDTO);
            BigDecimal cambio = pago == null ? BigDecimal.ZERO : pago.subtract(ventaGuardada.getTotal());

            if (metodoEfectivoFrm != null) {
                metodoEfectivoFrm.dispose();
                metodoEfectivoFrm = null;
            }
            if (metodoPagoFrm != null) {
                metodoPagoFrm.dispose();
                metodoPagoFrm = null;
            }

            carrito.clear();
            refrescarCarrito();
            String mensaje = "Venta registrada con folio " + ventaGuardada.getFolio() + ".";
            if (pago != null) {
                mensaje += " Cambio: $" + cambio.setScale(2, RoundingMode.HALF_UP).toPlainString();
            }
            vista.mostrarMensaje(mensaje);
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        } catch (RuntimeException ex) {
            vista.mostrarError("No fue posible registrar la venta: " + ex.getMessage());
        }
    }

    private void refrescarCarrito() {
        vista.actualizarCarrito(carrito, obtenerTotal());
    }

    private BigDecimal obtenerTotal() {
        return carrito.stream()
                .map(DetalleVentaDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void volverAlMenu() {
        UsuarioDTO usuario = SesionActual.getUsuario();
        if (usuario != null && usuario.getTipoNombre() != null && usuario.getTipoNombre().toLowerCase().contains("caj")) {
            MenuCajeroFrm menu = new MenuCajeroFrm();
            new MenuCajeroController(menu);
            menu.setVisible(true);
        } else {
            PantallaGestionarUsuarios pantalla = new PantallaGestionarUsuarios();
            new PantallaGestionarUsuariosController(pantalla);
            pantalla.setVisible(true);
        }
        vista.dispose();
    }
}
