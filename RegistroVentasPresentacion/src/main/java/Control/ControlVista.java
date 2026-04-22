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
import dtos.VentaDTO;
import excepciones.NegocioException;
import fachada.INegocio;
import fachada.Negocio;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class ControlVista {
    
    private INegocio negocio;
    private JFrame frameActual;

    public ControlVista() {
        this.negocio = new Negocio();
    }
    
    public void mostrarPantallaLogin() {
        frameActual = new PantallaLogin((usuario, contrasenia) -> {
            try {
                negocio.iniciarSesion(usuario, contrasenia);
                frameActual.dispose();
                mostrarMenuDuenoFrm();
            } catch(NegocioException e) {
                JOptionPane.showMessageDialog(frameActual, e.getMessage(), "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            }
        });
        frameActual.setVisible(true);
    }
    
    private void mostrarMenuDuenoFrm() {
        frameActual = new MenuDueno(
                () -> {
                    int seleccion = JOptionPane.showConfirmDialog(
                            frameActual,
                            "¿Desea cerrar la sesión actual?",
                            "¡Cuidado!",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (seleccion == JOptionPane.YES_OPTION) {
                        frameActual.dispose();
                        mostrarPantallaLogin();
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
        frameActual = new PantallaGestionarUsuarios(
                () -> {
                    frameActual.dispose();
                    mostrarMenuDuenoFrm();
                },
                opcion -> {
                    frameActual.dispose();
                    switch (opcion) {
                        case "AGREGAR" -> {
                            mostrarAgregarUsuarioFrm();
                        }
                        case "EDITAR" -> {
                            mostrarBuscarUsuariosFrm(
                                    opcion.substring(0, 1).toUpperCase() + opcion.substring(1).toLowerCase(),
                                    () -> {
                                        frameActual.dispose();
                                        mostrarEditarUsuario();
                                    }
                            );
                        }
                        case "ELIMINAR" -> {
                            mostrarBuscarUsuariosFrm(
                                    opcion.substring(0, 1).toUpperCase() + opcion.substring(1).toLowerCase(),
                                    () -> {
                                        frameActual.dispose();
                                        mostrarEliminarUsuarioFrm();
                                    }
                            );
                        }
                        default -> {
                            mostrarMenuDuenoFrm();
                        }
                    }
                }
        );
        frameActual.setVisible(true);
    }
    
    private void mostrarBuscarUsuariosFrm(String titulo, Runnable onSeleccionar) {
        frameActual = new BuscarUsuarios(
                () -> {
                    frameActual.dispose();
                    mostrarGestionarUsuariosFrm();
                },
                titulo,
                onSeleccionar
        );
        frameActual.setVisible(true);
    }
    
    private void mostrarEliminarUsuarioFrm() {
        frameActual = new EliminarUsuario(
                null,
                () -> {
                    frameActual.dispose();
                    mostrarBuscarUsuariosFrm(
                            "ELIMINAR",
                            () -> {
                                frameActual.dispose();
                                mostrarEliminarUsuarioFrm();
                            }
                    );
                }
        );
        frameActual.setVisible(true);
    }
    
    private void mostrarAgregarUsuarioFrm() {
        frameActual = new AgregarUsuario(
                () -> {
                    frameActual.dispose();
                    mostrarGestionarUsuariosFrm();
                }
        );
        frameActual.setVisible(true);
    }
    
    private void mostrarEditarUsuario() {
        frameActual = new EditarUsuario(
                () -> {
                    frameActual.dispose();
                    mostrarBuscarUsuariosFrm(
                            "EDITAR",
                            () -> {
                                frameActual.dispose();
                                mostrarEditarUsuario();
                            }
                    );
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
        Runnable onGestionarVentas = () -> {
            frameActual.dispose();
            mostrarGestionarVentaFrm();
        };
        
        Runnable onBack = () -> {
            int seleccion = JOptionPane.showConfirmDialog(
                    frameActual,
                    "¿Desea cerrar la sesión actual?",
                    "¡Cuidado!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (seleccion == JOptionPane.YES_OPTION) {
                frameActual.dispose();
                mostrarPantallaLogin();
            }
        };
        
        frameActual = new MenuCajeroFrm(
                onGestionarVentas,
                onBack
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
            System.out.println(ventaDTO);
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
        System.out.println(venta);
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
