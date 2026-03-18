/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author chris
 */
public class ConexionBD  {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    // Método estático para obtener el EntityManager
    public static EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) {
            // Si el EntityManager no existe o está cerrado, lo creamos nuevamente
            emf = Persistence.createEntityManagerFactory("ConexionPU");  // Usamos el nombre de la unidad de persistencia definida en persistence.xml
            em = emf.createEntityManager();
        }
        return em;
    }

    // Método para cerrar el EntityManagerFactory cuando se termine de usar
    public static void cerrarConexion() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
