package aplicacion;

import dtos.ReporteVentasDTO;
import excepciones.NegocioException;
import java.time.LocalDate;

@FunctionalInterface
public interface ReporteConsultaOperacion {

    ReporteVentasDTO ejecutar(LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException;
}
