/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import conexion.ConexionBD;
import entidades.Producto;
import interfaces.IProductoDAO;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author chris
 */
public class ProductoDAO implements IProductoDAO{

    private EntityManager em;

    public ProductoDAO() {
        this.em = ConexionBD.getEntityManager();  // Usamos el EntityManager de la clase Conexion
    }

    @Override
    public void crear(Producto producto) {
        try {
            em.getTransaction().begin();
            em.persist(producto);
            em.flush();  // Fuerza el commit de los cambios
            em.getTransaction().commit();
            System.out.println("Producto insertado con éxito: " + producto.getNombre());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al insertar el producto: " + e.getMessage());
        }
    }

    @Override
    public Producto obtener(Long id) {
        return em.find(Producto.class, id);
    }

    @Override
    public List<Producto> obtenerTodos() {
        return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
    }

    @Override
    public void actualizar(Producto producto) {
        em.getTransaction().begin();
        em.merge(producto);
        em.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = em.find(Producto.class, id);
        if (producto != null) {
            em.getTransaction().begin();
            em.remove(producto);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Producto> obtener(String categoria) {
        return em.createQuery("SELECT p FROM Producto p WHERE p.categoria = :cat", Producto.class)
             .setParameter("cat", categoria)
             .getResultList();
    }
}
