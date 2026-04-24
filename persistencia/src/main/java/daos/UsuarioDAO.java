package daos;

import conexion.ConexionBD;
import entidades.Usuario;
import excepciones.PersistenciaException;
import interfaces.IUsuarioDAO;
import java.util.List;
import jakarta.persistence.EntityManager;

/**
 *
 * @author chris
 */
public class UsuarioDAO implements IUsuarioDAO {

    private EntityManager em;

    public UsuarioDAO() {
        this.em = ConexionBD.getEntityManager();
    }

    @Override
    public void crear(Usuario usuario) {
        em.getTransaction().begin();
        em.persist(usuario);
        em.flush(); // Forzar commit inmediato
        em.getTransaction().commit();
    }

    @Override
    public Usuario obtener(Long id) {
        return em.find(Usuario.class, id);
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
    
    @Override
    public Usuario obtener(String nombre, String contrasenia) throws PersistenciaException {
        try {
            String nombreLimpio = nombre != null ? nombre.trim() : "";
            String contraseLimpia = contrasenia != null ? contrasenia.trim() : "";
            
            // Debug: log de los valores
            System.out.println("[DEBUG] Buscando usuario: '" + nombreLimpio + "' con contraseña: '" + contraseLimpia + "'");
            
            Usuario usuarioEncontrado = em.createQuery(
                "SELECT u FROM Usuario u LEFT JOIN FETCH u.tipo WHERE LOWER(u.nombre) = LOWER(:nombre) AND u.contrasenia = :contrasenia", Usuario.class)
                .setParameter("nombre", nombreLimpio)
                .setParameter("contrasenia", contraseLimpia)
                .getSingleResult();
            
            System.out.println("[DEBUG] Usuario encontrado: " + usuarioEncontrado.getNombre() + ", Rol: " + (usuarioEncontrado.getTipo() != null ? usuarioEncontrado.getTipo().getNombre() : "SIN ROL"));
            return usuarioEncontrado;
        } catch(Exception e) {
            System.err.println("[DEBUG] Error en búsqueda: " + e.getMessage());
            System.err.println("[DEBUG] Todos los usuarios en BD:");
            try {
                em.createQuery("SELECT u FROM Usuario u LEFT JOIN FETCH u.tipo", Usuario.class)
                    .getResultList()
                    .forEach(u -> System.err.println("  - " + u.getNombre() + " (rol: " + (u.getTipo() != null ? u.getTipo().getNombre() : "null") + ")"));
            } catch(Exception ex) {
                System.err.println("Error listando usuarios: " + ex.getMessage());
            }
            throw new PersistenciaException("Usuario no encontrado.");
        }
    }

    @Override
    public boolean existeNombre(String nombre) {
        Long total = em.createQuery(
                "SELECT COUNT(u) FROM Usuario u WHERE LOWER(u.nombre) = LOWER(:nombre)",
                Long.class
        )
                .setParameter("nombre", nombre)
                .getSingleResult();
        return total > 0;
    }
}
