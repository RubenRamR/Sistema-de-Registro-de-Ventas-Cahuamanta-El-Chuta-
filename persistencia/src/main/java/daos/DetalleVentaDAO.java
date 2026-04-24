package daos;

import conexion.ConexionBD;
import entidades.DetalleVenta;
import interfaces.IDetalleVentaDAO;
import java.util.List;
import jakarta.persistence.EntityManager;

/**
 *
 * @author chris
 */
public class DetalleVentaDAO implements IDetalleVentaDAO {

    private EntityManager em;

    public DetalleVentaDAO() {
        this.em = ConexionBD.getEntityManager();
    }

    @Override
    public void crear(DetalleVenta detalleVenta) {
        em.getTransaction().begin();
        em.persist(detalleVenta);
        em.flush();
        em.getTransaction().commit();
    }

    @Override
    public DetalleVenta obtener(Long id) {
        return em.find(DetalleVenta.class, id);
    }

    @Override
    public void actualizar(DetalleVenta detalleVenta) {
        em.getTransaction().begin();
        em.merge(detalleVenta);
        em.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        DetalleVenta detalleVenta = em.find(DetalleVenta.class, id);
        if (detalleVenta != null) {
            em.getTransaction().begin();
            em.remove(detalleVenta);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<DetalleVenta> obtenerTodos() {
        // Devuelve todos los registros de DetalleVenta en la base de datos
        return em.createQuery("SELECT d FROM DetalleVenta d", DetalleVenta.class).getResultList();
    }
    
    @Override
    public List<DetalleVenta> obtenerPorIdVenta(Long id) {
        return em.createQuery("SELECT d FROM DetalleVenta d WHERE d.venta.idVenta = :idBuscado", DetalleVenta.class)
             .setParameter("idBuscado", id)
             .getResultList();
    }

}
