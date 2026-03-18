/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Tipo;
import java.util.List;

/**
 *
 * @author chris
 */
public interface ITipoDAO {

    void crear(Tipo tipo);

    Tipo obtener(Long id);

    List<Tipo> obtenerTodos();

    void actualizar(Tipo tipo);

    void eliminar(Long id);
}
