package Control;

import GenerarReportes.Reportes;
import GestionarUsuarios.AgregarUsuario;
import GestionarUsuarios.BuscarUsuarios;
import GestionarUsuarios.EditarUsuario;
import GestionarUsuarios.EliminarUsuario;
import GestionarUsuarios.PantallaGestionarUsuarios;
import GestionarVentas.DetalleVenta;
import GestionarVentas.GestionarVenta;
import GestionarVentas.MenuCajeroFrm;
import GestionarVentas.MetodoEfectivoFrm;
import GestionarVentas.MetodoPagoFrm;
import GestionarVentas.PantallaResumen;
import GestionarVentas.PuntoVenta;
import IniciarSesion.MenuDueno;
import IniciarSesion.PantallaLogin;
import dtos.DetalleVentaDTO;
import dtos.UsuarioDTO;
import dtos.VentaDTO;
import excepciones.NegocioException;
import fachada.INegocio;
import fachada.Negocio;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class ControlVista {

    private final INegocio negocio;
    private JFrame frameActual;
    private UsuarioDTO usuarioSeleccionado;

    public ControlVista() {
        this.negocio = new Negocio();
    }

    public void mostrarPantallaLogin() {
        frameActual = new PantallaLogin((usuario, contrasenia) -> {
            try {
                negocio.iniciarSesion(usuario, contrasenia);
                frameActual.dispose();
                mostrarMenuSegunRol();
            } catch (NegocioException e) {
                JOptionPane.showMessageDialog(frameActual, e.getMessage(), "Error de autenticacion", JOptionPane.ERROR_MESSAGE);
            }
        });
        frameActual.setVisible(true);
    }

    private void mostrarMenuSegunRol() {
        UsuarioDTO sesionActual = negocio.getSesionActual();
        if (sesionActual == null) {
            JOptionPane.showMessageDialog(null, "No hay una sesion activa.", "Error", JOptionPane.ERROR_MESSAGE);
            mostrarPantallaLogin();
            return;
        }

        if ("ADMIN".equalsIgnoreCase(sesionActual.getRol())) {
            mostrarMenuDuenoFrm();
            return;
        }
        if ("CAJERO".equalsIgnoreCase(sesionActual.getRol())) {
            mostrarMenuCajeroFrm();
            return;
        }

        negocio.cerrarSesionActual();
        JOptionPane.showMessageDialog(null, "El usuario no tiene un rol valido.", "Error", JOptionPane.ERROR_MESSAGE);
        mostrarPantallaLogin();
    }

    private void cerrarSesionYVolverAlLogin() {
        negocio.cerrarSesionActual();
        if (frameActual != null) {
            frameActual.dispose();
        }
        mostrarPantallaLogin();
    }

    private void mostrarMenuDuenoFrm() {
        frameActual = new MenuDueno(
                () -> {
                    int seleccion = JOptionPane.showConfirmDialog(
                            frameActual,
                            "Desea cerrar la sesion actual?",
                            "Cuidado",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (seleccion == JOptionPane.YES_OPTION) {
                        cerrarSesionYVolverAlLogin();
                    }
                },
                () -> {
                    frameActual.dispose();
                    mostrarGestionarUsuariosFrm();
                },
                () -> {
                    frameActual.dispose();
                    mostrarReportesFrm();
                }
        );
        frameActual.setVisible(true);
    }

    private void mostrarGestionarUsuariosFrm() {
        usuarioSeleccionado = null;
        frameActual = new PantallaGestionarUsuarios(
                () -> {
                    frameActual.dispose();
                    mostrarMenuDuenoFrm();
                },
                opcion -> {
                    frameActual.dispose();
                    switch (opcion) {
                        case "AGREGAR" -> mostrarAgregarUsuarioFrm();
                        case "EDITAR" -> mostrarBuscarUsuariosFrm("Editar", usuario -> {
                            usuarioSeleccionado = usuario;
                            frameActual.dispose();
                            mostrarEditarUsuarioFrm();
                        });
                        case "ELIMINAR" -> mostrarBuscarUsuariosFrm("Eliminar", usuario -> {
                            usuarioSeleccionado = usuario;
                            frameActual.dispose();
                            mostrarEliminarUsuarioFrm();
                        });
                        default -> mostrarMenuDuenoFrm();
                    }
                }
        );
        frameActual.setVisible(true);
    }

    private void mostrarBuscarUsuariosFrm(String titulo, Consumer<UsuarioDTO> onSeleccionar) {
        frameActual = new BuscarUsuarios(
                () -> {
                    frameActual.dispose();
                    mostrarGestionarUsuariosFrm();
                },
                titulo,
                negocio.obtenerUsuarios(),
                onSeleccionar
        );
        frameActual.setVisible(true);
    }

    private void mostrarAgregarUsuarioFrm() {
        frameActual = new AgregarUsuario(
                () -> {
                    frameActual.dispose();
                    mostrarGestionarUsuariosFrm();
                },
                usuarioDTO -> {
                    negocio.crearUsuario(usuarioDTO);
                    JOptionPane.showMessageDialog(frameActual, "Usuario agregado correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    frameActual.dispose();
                    mostrarGestionarUsuariosFrm();
                }
        );
        frameActual.setVisible(true);
    }

    private void mostrarEditarUsuarioFrm() {
        if (usuarioSeleccionado == null) {
            mostrarGestionarUsuariosFrm();
            return;
        }

        frameActual = new EditarUsuario(
                usuarioSeleccionado,
                () -> {
                    frameActual.dispose();
                    mostrarBuscarUsuariosFrm("Editar", usuario -> {
                        usuarioSeleccionado = usuario;
                        frameActual.dispose();
                        mostrarEditarUsuarioFrm();
                    });
                },
                usuarioDTO -> {
                    negocio.actualizarUsuario(usuarioDTO);
                    usuarioSeleccionado = null;
                    JOptionPane.showMessageDialog(frameActual, "Usuario actualizado correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    frameActual.dispose();
                    mostrarGestionarUsuariosFrm();
                }
        );
        frameActual.setVisible(true);
    }

    private void mostrarEliminarUsuarioFrm() {
        if (usuarioSeleccionado == null) {
            mostrarGestionarUsuariosFrm();
            return;
        }

        frameActual = new EliminarUsuario(
                usuarioSeleccionado,
                () -> {
                    frameActual.dispose();
                    mostrarBuscarUsuariosFrm("Eliminar", usuario -> {
                        usuarioSeleccionado = usuario;
                        frameActual.dispose();
                        mostrarEliminarUsuarioFrm();
                    });
                },
                usuarioDTO -> {
                    negocio.eliminarUsuario(usuarioDTO.getIdUsuario());
                    usuarioSeleccionado = null;
                    JOptionPane.showMessageDialog(frameActual, "Usuario eliminado correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    frameActual.dispose();
                    mostrarGestionarUsuariosFrm();
                }
        );
        frameActual.setVisible(true);
    }

    private void mostrarReportesFrm() {
        frameActual = new Reportes(
                () -> {
                    frameActual.dispose();
                    mostrarMenuDuenoFrm();
                }
        );
        frameActual.setVisible(true);
    }

    private void mostrarMenuCajeroFrm() {
        frameActual = new MenuCajeroFrm(
                () -> {
                    frameActual.dispose();
                    mostrarGestionarVentaFrm();
                },
                () -> {
                    int seleccion = JOptionPane.showConfirmDialog(
                            frameActual,
                            "Desea cerrar la sesion actual?",
                            "Cuidado",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (seleccion == JOptionPane.YES_OPTION) {
                        cerrarSesionYVolverAlLogin();
                    }
                }
        );
        frameActual.setVisible(true);
    }

    private void mostrarGestionarVentaFrm() {
        frameActual = new GestionarVenta(
                () -> {
                    frameActual.dispose();
                    mostrarPuntoVentaFrm();
                },
                ventaDTO -> {
                    frameActual.dispose();
                    mostrarDetalleVentaFrm(ventaDTO);
                },
                () -> {
                    frameActual.dispose();
                    mostrarMenuCajeroFrm();
                },
                negocio.obtenerVentasDelDia()
        );
        frameActual.setVisible(true);
    }

    private void mostrarDetalleVentaFrm(VentaDTO ventaDTO) {
        ventaDTO.setDetallesVenta(negocio.obtenerDetallesVentaPorIdVenta(ventaDTO.getIdVenta()));

        Runnable cerrarPantalla = () -> {
            frameActual.dispose();
            mostrarGestionarVentaFrm();
        };

        frameActual = new DetalleVenta(
                ventaDTO,
                cerrarPantalla,
                cerrarPantalla
        );
        frameActual.setVisible(true);
    }

    private void mostrarPuntoVentaFrm() {
        List<DetalleVentaDTO> detallesVenta = null;
        if (negocio.getVentaActual() != null) {
            detallesVenta = negocio.getVentaActual().getDetallesVenta();
        }

        frameActual = new PuntoVenta(
                detalles -> {
                    try {
                        negocio.addProductosVenta(detalles);
                        frameActual.dispose();
                        mostrarMetodoPagoFrm();
                    } catch (NegocioException e) {
                        JOptionPane.showMessageDialog(frameActual, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                },
                categoria -> negocio.obtenerProductos(categoria),
                negocio.obtenerProductos(),
                () -> {
                    frameActual.dispose();
                    mostrarGestionarVentaFrm();
                },
                detallesVenta
        );
        frameActual.setVisible(true);
    }

    private void mostrarMetodoPagoFrm() {
        frameActual = new MetodoPagoFrm(
                () -> {
                    frameActual.dispose();
                    mostrarPagoEfectivoFrm();
                },
                () -> {
                    negocio.setMetodoPagoVentaActual("TARJETA");
                    frameActual.dispose();
                    mostrarPantallaResumen();
                },
                () -> {
                    negocio.setMetodoPagoVentaActual("TRANSFERENCIA");
                    frameActual.dispose();
                    mostrarPantallaResumen();
                },
                () -> {
                    frameActual.dispose();
                    mostrarPuntoVentaFrm();
                }
        );
        frameActual.setVisible(true);
    }

    private void mostrarPagoEfectivoFrm() {
        VentaDTO venta = negocio.getVentaActual();
        frameActual = new MetodoEfectivoFrm(
                venta.getTotal(),
                () -> {
                    negocio.setMetodoPagoVentaActual("EFECTIVO");
                    frameActual.dispose();
                    mostrarPantallaResumen();
                },
                () -> {
                    frameActual.dispose();
                    mostrarMetodoPagoFrm();
                }
        );
        frameActual.setVisible(true);
    }

    private void mostrarPantallaResumen() {
        frameActual = new PantallaResumen(
                negocio.getProductosVenta(),
                () -> {
                    negocio.registrarVentaActual();
                    JOptionPane.showMessageDialog(frameActual, "Venta registrada.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    frameActual.dispose();
                    mostrarMenuCajeroFrm();
                },
                () -> {
                    frameActual.dispose();
                    mostrarMetodoPagoFrm();
                }
        );
        frameActual.setVisible(true);
    }
}
