package daos;

import conexion.ConexionBD;
import entidades.DetalleVenta;
import entidades.Producto;
import entidades.Usuario;
import entidades.Venta;
import interfaces.IVentaDAO;
import java.util.List;
import javax.persistence.EntityManager;

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
    public Venta registrarVenta(Venta venta) {
        try {
            em.getTransaction().begin();

            if (venta.getUsuario() != null && venta.getUsuario().getIdUsuario() != null) {
                Usuario usuario = em.find(Usuario.class, venta.getUsuario().getIdUsuario());
                venta.setUsuario(usuario);
            }

            em.persist(venta);
            em.flush();

            if (venta.getDetallesVenta() != null) {
                for (DetalleVenta detalle : venta.getDetallesVenta()) {
                    if (detalle.getProducto() != null && detalle.getProducto().getIdProducto() != null) {
                        Producto producto = em.find(Producto.class, detalle.getProducto().getIdProducto());
                        detalle.setProducto(producto);
                    }
                    detalle.setVenta(venta);
                    em.persist(detalle);
                }
            }

            em.getTransaction().commit();
            return venta;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        }
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
