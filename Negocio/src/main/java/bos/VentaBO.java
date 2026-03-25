/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bos;

import dtos.DetalleVentaDTO;
import dtos.VentaDTO;
import entidades.DetalleVenta;
import entidades.Usuario;
import entidades.Venta;
import excepciones.NegocioException;
import interfaces.IVentaDAO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import mappers.DetalleVentaMapper;
import mappers.VentaMapper;

/**
 *
 * @author luise
 */

public class VentaBO {

    private IVentaDAO ventaDAO;  // Usamos la interfaz IVentaDAO
    private Venta venta;

    // Inyección de dependencia a través del constructor
    public VentaBO(IVentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
    }

    // Crear una venta en la base de datos
    public void crearVenta(VentaDTO ventaDTO) {
        // Convertimos el DTO a entidad usando el mapper
        Venta venta = VentaMapper.toEntity(ventaDTO);
        ventaDAO.crear(venta);  // Llamamos al DAO para persistir la entidad
    }
    
    public void crearVenta(List<DetalleVentaDTO> detalles) throws NegocioException {
        if (detalles.isEmpty()) {
            throw new NegocioException("Sin productos.");
        }
        boolean menorIgual0 = detalles.stream()
                .anyMatch(p -> p.getCantidad() <= 0);
        if (menorIgual0) throw new NegocioException("Cantidades no seleccionadas.");
        
        List<DetalleVenta> entitys = detalles.stream()
                .map(DetalleVentaMapper::toEntity)
                .toList();
        
        Venta venta = new Venta();
        venta.setDetallesVenta(entitys);
        venta.setFechaHora(LocalDateTime.now());
        
        BigDecimal bD = BigDecimal.ZERO;
        for (DetalleVenta entity : entitys) {
            bD = bD.add(entity.getPrecioUnitario());
        }
        
        System.out.println(entitys.getFirst().getPrecioUnitario());
        System.out.println(detalles);
        System.out.println(bD);
        
        venta.setTotal(bD);
        System.out.println(venta.getTotal());
        
        this.venta = venta;
    }
    
    public void crearVenta(Usuario sesion) {
        venta.setUsuario(sesion);
        ventaDAO.crear(venta);
        venta = null;
    }
    
    public VentaDTO obtenerVentaActual() {
        return VentaMapper.toDTO(venta);
    }
    
    public void setMetodoPago(String metodoPago) {
        venta.setMetodoPago(metodoPago);
    }
    
    public List<DetalleVentaDTO> getProductosVenta() {
        return venta.getDetallesVenta().stream()
                .map(DetalleVentaMapper::toDTO)
                .toList();
    }

    // Obtener una venta por su ID
    public VentaDTO obtenerVenta(Long id) {
        Venta venta = ventaDAO.obtener(id);  // El DAO obtiene la entidad directamente
        return VentaMapper.toDTO(venta);  // Convertimos la entidad a DTO
    }

    // Actualizar una venta
    public void actualizarVenta(VentaDTO ventaDTO) {
        // Convertimos el DTO a entidad
        Venta venta = VentaMapper.toEntity(ventaDTO);
        ventaDAO.actualizar(venta);  // Llamamos al DAO para actualizar la entidad
    }

    // Eliminar una venta
    public void eliminarVenta(Long id) {
        ventaDAO.eliminar(id);  // Llamada al DAO para eliminar la venta
    }

    // Obtener todas las ventas
    public List<VentaDTO> obtenerTodasLasVentas() {
        List<Venta> ventas = ventaDAO.obtenerTodos();  // El DAO obtiene las entidades directamente
        return ventas.stream()
                     .map(VentaMapper::toDTO)  // Convertimos cada entidad a DTO
                     .collect(Collectors.toList());
    }
}