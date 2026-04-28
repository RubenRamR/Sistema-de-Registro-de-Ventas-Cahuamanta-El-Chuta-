package aplicacion;

import excepciones.NegocioException;
import java.io.File;
import java.time.LocalDate;

@FunctionalInterface
public interface ReporteExportacionOperacion {

    void ejecutar(LocalDate fechaInicio, LocalDate fechaFin, File destino) throws NegocioException;
}
