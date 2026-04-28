package daos;

import conexion.ConexionBD;
import entidades.Venta;
import interfaces.IVentaDAO;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author chris
 */
public class VentaDAO implements IVentaDAO {

    private final EntityManager em;

    public VentaDAO() {
        this.em = ConexionBD.getEntityManager();
    }

    @Override
    public void crear(Venta venta) {
        em.getTransaction().begin();
        em.persist(venta);
        em.flush();
        em.getTransaction().commit();
    }

    @Override
    public Venta obtener(Long id) {
        return em.find(Venta.class, id);
    }

    @Override
    public List<Venta> obtenerTodos() {
        return em.createQuery(
                "SELECT v FROM Venta v LEFT JOIN FETCH v.usuario ORDER BY v.fechaHora DESC",
                Venta.class
        ).getResultList();
    }

    @Override
    public List<Venta> obtenerPorRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return em.createQuery(
                "SELECT v FROM Venta v LEFT JOIN FETCH v.usuario "
                + "WHERE v.fechaHora >= :fechaInicio AND v.fechaHora < :fechaFin "
                + "ORDER BY v.fechaHora DESC",
                Venta.class
        )
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
    }

    @Override
    public void actualizar(Venta venta) {
        em.getTransaction().begin();
        em.merge(venta);
        em.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        Venta venta = em.find(Venta.class, id);
        if (venta != null) {
            em.getTransaction().begin();
            em.remove(venta);
            em.getTransaction().commit();
        }
    }
}
