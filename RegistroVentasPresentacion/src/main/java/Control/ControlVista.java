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
                mostrarMenuCajeroFrm();
            } catch(NegocioException e) {
                JOptionPane.showMessageDialog(frameActual, e.getMessage(), "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            }
        });
        frameActual.setVisible(true);
    }
    
    private void mostrarMenuCajeroFrm() {
        Runnable onGestionarVentas = () -> {
            frameActual.dispose();
            mostrarGestionarVentaFrm();
        };
        
        Runnable onBack = () -> {
            int seleccion = JOptionPane.showConfirmDialog(
                    frameActual, // Componente padre (null para centrar en pantalla)
                    "¿Desea cerrar la sesión actual?", // Mensaje
                    "¡Cuidado!", // Título de la ventana
                    JOptionPane.YES_NO_OPTION, // Tipo de botones (Sí/No)
                    JOptionPane.WARNING_MESSAGE // Tipo de icono (Triángulo de advertencia)
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
        frameActual = new GestionarVentaFrm(() -> {
            frameActual.dispose();
            mostrarPuntoVentaFrm();
        });
        frameActual.setVisible(true);
    }
    
    private void mostrarPuntoVentaFrm() {
        frameActual = new PuntoVenta(detalles -> {
            try {
                negocio.addProductosVenta(detalles);
                frameActual.dispose();
                mostrarMetodoPagoFrm();
            } catch (NegocioException e) {
                JOptionPane.showMessageDialog(frameActual, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        },
                e -> new ArrayList<>(),
                negocio.obtenerProductos()
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
                }
        );
        frameActual.setVisible(true);
    }
}
