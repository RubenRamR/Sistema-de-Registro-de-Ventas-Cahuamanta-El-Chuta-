package daos;

import conexion.ConexionBD;
import entidades.Tipo;
import interfaces.ITipoDAO;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author chris
 */
public class TipoDAO implements ITipoDAO {

    private EntityManager em;

    public TipoDAO() {
        this.em = ConexionBD.getEntityManager();
    }

    @Override
    public void crear(Tipo tipo) {
        em.getTransaction().begin();
        em.persist(tipo);
        em.flush();
        em.getTransaction().commit();
    }

    @Override
    public Tipo obtener(Long id) {
        return em.find(Tipo.class, id);
    }

    @Override
    public Tipo obtenerPorNombre(String nombre) {
        List<Tipo> tipos = em.createQuery(
                "SELECT t FROM Tipo t WHERE LOWER(t.nombre) = LOWER(:nombre)",
                Tipo.class
        )
                .setParameter("nombre", nombre)
                .setMaxResults(1)
                .getResultList();
        return tipos.isEmpty() ? null : tipos.getFirst();
    }

    @Override
    public List<Tipo> obtenerTodos() {
        return em.createQuery("SELECT t FROM Tipo t", Tipo.class).getResultList();
    }

    @Override
    public void actualizar(Tipo tipo) {
        em.getTransaction().begin();
        em.merge(tipo);
        em.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        Tipo tipo = em.find(Tipo.class, id);
        if (tipo != null) {
            em.getTransaction().begin();
            em.remove(tipo);
            em.getTransaction().commit();
        }
    }
}
