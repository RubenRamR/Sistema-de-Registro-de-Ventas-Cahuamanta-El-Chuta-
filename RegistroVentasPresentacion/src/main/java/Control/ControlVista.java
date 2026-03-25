package Control;

import GestionarVentas.GestionarVentaFrm;
import GestionarVentas.MenuCajeroFrm;
import GestionarVentas.MetodoEfectivoFrm;
import GestionarVentas.MetodoPagoFrm;
import GestionarVentas.PantallaResumen;
import GestionarVentas.PuntoVenta;
import IniciarSesion.PantallaLogin;
import dtos.VentaDTO;
import excepciones.NegocioException;
import fachada.INegocio;
import fachada.Negocio;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class ControlVista {
    
    INegocio negocio;
    JFrame frameActual;

    public ControlVista() {
        this.negocio = new Negocio();
    }
    
    public void mostrarPantallaLogin() {
        frameActual = new PantallaLogin((usuario, contrasenia) -> {
            try {
                negocio.iniciarSesion(usuario, contrasenia);
                frameActual.dispose();
                mostrarMenuCajeroFrm();
            } catch(NegocioException e) {
                JOptionPane.showMessageDialog(frameActual, e.getMessage(), "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            }
        });
        frameActual.setVisible(true);
    }
    
    public void mostrarMenuCajeroFrm() {
        frameActual = new MenuCajeroFrm(() -> {
            frameActual.dispose();
            mostrarGestionarVentaFrm();
        });
        frameActual.setVisible(true);
    }
    
    public void mostrarGestionarVentaFrm() {
        frameActual = new GestionarVentaFrm(() -> {
            frameActual.dispose();
            mostrarPuntoVentaFrm();
        });
        frameActual.setVisible(true);
    }
    
    public void mostrarPuntoVentaFrm() {
        frameActual = new PuntoVenta(detalles -> {
            try {
                negocio.addProductosVenta(detalles);
                frameActual.dispose();
                mostrarMetodoPagoFrm();
            } catch (NegocioException e) {
                JOptionPane.showMessageDialog(frameActual, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        },
                () -> new ArrayList<>(),
                negocio.obtenerProductos()
        );
        frameActual.setVisible(true);
    }
    
    public void mostrarMetodoPagoFrm() {
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
                }
        );
        frameActual.setVisible(true);
    }
    
    public void mostrarPagoEfectivoFrm() {
        VentaDTO venta = negocio.getVentaActual();
        System.out.println(venta);
        frameActual = new MetodoEfectivoFrm(
                venta.getTotal(),
                () -> {
                    negocio.setMetodoPagoVentaActual("EFECTIVO");
                    frameActual.dispose();
                    mostrarPantallaResumen();
                            }
        );
        frameActual.setVisible(true);
    }
    
    public void mostrarPantallaResumen() {
        frameActual = new PantallaResumen(
                negocio.getProductosVenta(),
                () -> {
                    negocio.registrarVentaActual();
                    JOptionPane.showMessageDialog(frameActual, "Venta registrada.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    
                    frameActual.dispose();
                    mostrarMenuCajeroFrm();
                }
        );
        frameActual.setVisible(true);
    }
}
