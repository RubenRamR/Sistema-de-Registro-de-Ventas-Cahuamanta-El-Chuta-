package daos;

import conexion.ConexionBD;
import entidades.Venta;
import interfaces.IVentaDAO;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author chris
 */
public class VentaDAO implements IVentaDAO {

    private EntityManager em;

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
        return em.createQuery("SELECT v FROM Venta v", Venta.class).getResultList();
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
