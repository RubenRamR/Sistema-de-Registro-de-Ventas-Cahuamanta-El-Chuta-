package aplicacion;

import bos.ProductoBO;
import bos.UsuarioBO;
import bos.VentaBO;
import daos.ProductoDAO;
import daos.UsuarioDAO;
import daos.VentaDAO;
import interfaces.IProductoBO;
import interfaces.IUsuarioBO;
import interfaces.IVentaBO;

public final class AplicacionContexto {

    private static final IUsuarioBO USUARIO_BO = new UsuarioBO(new UsuarioDAO());
    private static final IProductoBO PRODUCTO_BO = new ProductoBO(new ProductoDAO());
    private static final IVentaBO VENTA_BO = new VentaBO(new VentaDAO());

    private AplicacionContexto() {
    }

    public static IUsuarioBO getUsuarioBO() {
        return USUARIO_BO;
    }

    public static IProductoBO getProductoBO() {
        return PRODUCTO_BO;
    }

    public static IVentaBO getVentaBO() {
        return VENTA_BO;
    }
}
