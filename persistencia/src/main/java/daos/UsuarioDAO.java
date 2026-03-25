package daos;

import conexion.ConexionBD;
import entidades.Usuario;
import interfaces.IUsuarioDAO;
import java.util.List;
import javax.persistence.EntityManager;

public class UsuarioDAO implements IUsuarioDAO {

    private final EntityManager em;

    public UsuarioDAO() {
        this.em = ConexionBD.getEntityManager();
    }

    @Override
    public void crear(Usuario usuario) {
        em.getTransaction().begin();
        em.persist(usuario);
        em.flush();
        em.getTransaction().commit();
    }

    @Override
    public Usuario obtener(Long id) {
        return em.find(Usuario.class, id);
    }

    @Override
    public Usuario obtenerPorCredenciales(String nombre, String contrasenia) {
        List<Usuario> usuarios = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nombre = :nombre AND u.contrasenia = :contrasenia",
                Usuario.class)
                .setParameter("nombre", nombre)
                .setParameter("contrasenia", contrasenia)
                .setMaxResults(1)
                .getResultList();
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    @Override
    public void actualizar(Usuario usuario) {
        em.getTransaction().begin();
        em.merge(usuario);
        em.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = em.find(Usuario.class, id);
        if (usuario != null) {
            em.getTransaction().begin();
            em.remove(usuario);
            em.getTransaction().commit();
        }
    }
}
