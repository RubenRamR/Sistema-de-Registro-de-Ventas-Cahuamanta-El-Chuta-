package aplicacion;

import excepciones.NegocioException;
import java.math.BigDecimal;

@FunctionalInterface
public interface PagoEfectivoOperacion {

    void ejecutar(BigDecimal montoRecibido) throws NegocioException;
}
