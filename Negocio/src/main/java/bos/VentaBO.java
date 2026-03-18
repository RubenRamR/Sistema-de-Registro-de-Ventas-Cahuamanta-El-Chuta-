/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bos;

import dtos.VentaDTO;
import entidades.Venta;
import interfaces.IVentaDAO;
import java.util.List;
import java.util.stream.Collectors;
import mappers.VentaMapper;

/**
 *
 * @author luise
 */

public class VentaBO {

    private IVentaDAO ventaDAO;  // Usamos la interfaz IVentaDAO

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