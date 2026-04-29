package Pruebaa;

import bos.UsuarioBO;
import dtos.UsuarioDTO;
import entidades.Tipo;
import entidades.Usuario;
import excepciones.NegocioException;
import interfaces.ITipoDAO;
import interfaces.IUsuarioDAO;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del CU01 - Gestionar Usuarios (rol Dueño).
 *
 * Cubre los casos de prueba CP-U-01 a CP-U-18 del plan de pruebas.
 */
class UsuarioBOTest {

    // ---------- CP-U-01: Crear usuario valido CAJERO ----------
    @Test
    void crearUsuarioPersisteCajeroValido() throws NegocioException {
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        FakeTipoDAO tipoDAO = new FakeTipoDAO();
        UsuarioBO bo = new UsuarioBO(usuarioDAO, tipoDAO);

        UsuarioDTO dto = construirDTO(null, "juan", "123", "CAJERO");
        bo.crearUsuario(dto);

        assertEquals(1, usuarioDAO.almacenados.size());
        assertEquals("juan", usuarioDAO.almacenados.get(0).getNombre());
        assertEquals("CAJERO", usuarioDAO.almacenados.get(0).getTipo().getNombre());
    }

    // ---------- CP-U-02: Crear usuario "DUEÑO" se normaliza a ADMIN ----------
    @Test
    void crearUsuarioNormalizaRolDuenoAAdmin() throws NegocioException {
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        FakeTipoDAO tipoDAO = new FakeTipoDAO();
        UsuarioBO bo = new UsuarioBO(usuarioDAO, tipoDAO);

        bo.crearUsuario(construirDTO(null, "ana", "abc", "DUEÑO"));

        assertEquals(1, usuarioDAO.almacenados.size());
        assertEquals("ADMIN", usuarioDAO.almacenados.get(0).getTipo().getNombre());
    }

    // ---------- CP-U-03: Nombre vacio ----------
    @Test
    void crearUsuarioConNombreVacioLanzaError() {
        UsuarioBO bo = new UsuarioBO(new FakeUsuarioDAO(), new FakeTipoDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.crearUsuario(construirDTO(null, "", "x", "CAJERO"))
        );

        assertEquals("El nombre de usuario es obligatorio.", error.getMessage());
    }

    // ---------- CP-U-04: Nombre con solo espacios ----------
    @Test
    void crearUsuarioConNombreEnBlancoLanzaError() {
        UsuarioBO bo = new UsuarioBO(new FakeUsuarioDAO(), new FakeTipoDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.crearUsuario(construirDTO(null, "   ", "x", "CAJERO"))
        );

        assertEquals("El nombre de usuario es obligatorio.", error.getMessage());
    }

    // ---------- CP-U-05: Contrasenia vacia ----------
    @Test
    void crearUsuarioConContraseniaVaciaLanzaError() {
        UsuarioBO bo = new UsuarioBO(new FakeUsuarioDAO(), new FakeTipoDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.crearUsuario(construirDTO(null, "luis", "", "CAJERO"))
        );

        assertEquals("La contrasenia es obligatoria.", error.getMessage());
    }

    // ---------- CP-U-06: Rol invalido ----------
    @Test
    void crearUsuarioConRolInvalidoLanzaError() {
        UsuarioBO bo = new UsuarioBO(new FakeUsuarioDAO(), new FakeTipoDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.crearUsuario(construirDTO(null, "luis", "x", "GERENTE"))
        );

        assertEquals("Rol no valido.", error.getMessage());
    }

    // ---------- CP-U-07: Nombre duplicado (case insensitive) ----------
    @Test
    void crearUsuarioConNombreDuplicadoLanzaError() {
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        usuarioDAO.almacenados.add(crearEntidad(1L, "Juan", "abc", "CAJERO"));
        UsuarioBO bo = new UsuarioBO(usuarioDAO, new FakeTipoDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.crearUsuario(construirDTO(null, "JUAN", "x", "CAJERO"))
        );

        assertEquals("Ya existe un usuario con ese nombre.", error.getMessage());
    }

    // ---------- CP-U-08: DTO nulo ----------
    @Test
    void crearUsuarioConDTONuloLanzaError() {
        UsuarioBO bo = new UsuarioBO(new FakeUsuarioDAO(), new FakeTipoDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.crearUsuario(null)
        );

        assertEquals("Datos de usuario no enviados.", error.getMessage());
    }

    // ---------- CP-U-09: Obtener por ID existente ----------
    @Test
    void obtenerUsuarioPorIdExistenteDevuelveDTO() {
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        usuarioDAO.almacenados.add(crearEntidad(1L, "ana", "abc", "ADMIN"));
        UsuarioBO bo = new UsuarioBO(usuarioDAO, new FakeTipoDAO());

        UsuarioDTO resultado = bo.obtenerUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdUsuario());
        assertEquals("ana", resultado.getNombre());
        assertEquals("ADMIN", resultado.getRol());
    }

    // ---------- CP-U-10: Obtener por ID inexistente ----------
    @Test
    void obtenerUsuarioPorIdInexistenteDevuelveNull() {
        UsuarioBO bo = new UsuarioBO(new FakeUsuarioDAO(), new FakeTipoDAO());

        UsuarioDTO resultado = bo.obtenerUsuario(9999L);

        assertNull(resultado);
    }

    // ---------- CP-U-11: Listar todos los usuarios ----------
    @Test
    void obtenerTodosLosUsuariosDevuelveListaCompleta() {
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        usuarioDAO.almacenados.add(crearEntidad(1L, "ana", "abc", "ADMIN"));
        usuarioDAO.almacenados.add(crearEntidad(2L, "luis", "abc", "CAJERO"));
        usuarioDAO.almacenados.add(crearEntidad(3L, "juan", "abc", "CAJERO"));
        UsuarioBO bo = new UsuarioBO(usuarioDAO, new FakeTipoDAO());

        List<UsuarioDTO> lista = bo.obtenerTodosLosUsuarios();

        assertEquals(3, lista.size());
        assertEquals("ADMIN", lista.get(0).getRol());
        assertEquals("CAJERO", lista.get(1).getRol());
    }

    // ---------- CP-U-12: Listar cuando no hay registros ----------
    @Test
    void obtenerTodosLosUsuariosDevuelveListaVaciaCuandoNoHayRegistros() {
        UsuarioBO bo = new UsuarioBO(new FakeUsuarioDAO(), new FakeTipoDAO());

        List<UsuarioDTO> lista = bo.obtenerTodosLosUsuarios();

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    // ---------- CP-U-13: Actualizar usuario valido ----------
    @Test
    void actualizarUsuarioPersisteCambiosValidos() throws NegocioException {
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        usuarioDAO.almacenados.add(crearEntidad(1L, "juan", "abc", "CAJERO"));
        UsuarioBO bo = new UsuarioBO(usuarioDAO, new FakeTipoDAO());

        UsuarioDTO dto = construirDTO(1L, "juanp", "nueva", "CAJERO");
        bo.actualizarUsuario(dto);

        assertEquals(1, usuarioDAO.actualizados.size());
        assertEquals("juanp", usuarioDAO.actualizados.get(0).getNombre());
    }

    // ---------- CP-U-14: Actualizar sin ID ----------
    @Test
    void actualizarUsuarioSinIdLanzaError() {
        UsuarioBO bo = new UsuarioBO(new FakeUsuarioDAO(), new FakeTipoDAO());

        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.actualizarUsuario(construirDTO(null, "luis", "x", "CAJERO"))
        );

        assertEquals("Usuario no valido para actualizar.", error.getMessage());
    }

    // ---------- CP-U-15: Actualizar con nombre tomado por otro usuario ----------
    @Test
    void actualizarUsuarioConNombreTomadoPorOtroLanzaError() {
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        usuarioDAO.almacenados.add(crearEntidad(1L, "juan", "abc", "CAJERO"));
        usuarioDAO.almacenados.add(crearEntidad(2L, "ana", "abc", "CAJERO"));
        UsuarioBO bo = new UsuarioBO(usuarioDAO, new FakeTipoDAO());

        // El usuario id=2 quiere tomar el nombre "juan" que es de id=1
        NegocioException error = assertThrows(
                NegocioException.class,
                () -> bo.actualizarUsuario(construirDTO(2L, "juan", "x", "CAJERO"))
        );

        assertEquals("Ya existe un usuario con ese nombre.", error.getMessage());
    }

    // ---------- CP-U-16: Actualizar manteniendo su mismo nombre ----------
    @Test
    void actualizarUsuarioManteniendoSuMismoNombreEsPermitido() {
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        usuarioDAO.almacenados.add(crearEntidad(1L, "juan", "abc", "CAJERO"));
        UsuarioBO bo = new UsuarioBO(usuarioDAO, new FakeTipoDAO());

        assertDoesNotThrow(
                () -> bo.actualizarUsuario(construirDTO(1L, "juan", "nueva", "CAJERO"))
        );
    }

    // ---------- CP-U-17: Eliminar usuario existente ----------
    @Test
    void eliminarUsuarioExistenteInvocaDAO() {
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        usuarioDAO.almacenados.add(crearEntidad(2L, "luis", "abc", "CAJERO"));
        UsuarioBO bo = new UsuarioBO(usuarioDAO, new FakeTipoDAO());

        bo.eliminarUsuario(2L);

        assertEquals(List.of(2L), usuarioDAO.eliminados);
    }

    // ---------- CP-U-18: Eliminar ID inexistente no lanza error ----------
    @Test
    void eliminarUsuarioInexistenteNoLanzaError() {
        UsuarioBO bo = new UsuarioBO(new FakeUsuarioDAO(), new FakeTipoDAO());

        assertDoesNotThrow(() -> bo.eliminarUsuario(9999L));
    }

    // ===================== Helpers =====================
    private UsuarioDTO construirDTO(Long id, String nombre, String contrasenia, String rol) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(id);
        dto.setNombre(nombre);
        dto.setContrasenia(contrasenia);
        dto.setRol(rol);
        return dto;
    }

    private Usuario crearEntidad(Long id, String nombre, String contrasenia, String rolNombre) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setNombre(nombre);
        usuario.setContrasenia(contrasenia);

        Tipo tipo = new Tipo();
        tipo.setIdTipo(1L);
        tipo.setNombre(rolNombre);
        usuario.setTipo(tipo);
        return usuario;
    }

    // ===================== Fakes =====================
    private static class FakeUsuarioDAO implements IUsuarioDAO {

        final List<Usuario> almacenados = new ArrayList<>();
        final List<Usuario> actualizados = new ArrayList<>();
        final List<Long> eliminados = new ArrayList<>();

        @Override
        public void crear(Usuario usuario) {
            if (usuario.getIdUsuario() == null) {
                usuario.setIdUsuario((long) (almacenados.size() + 1));
            }
            almacenados.add(usuario);
        }

        @Override
        public Usuario obtener(Long id) {
            return almacenados.stream()
                    .filter(u -> id.equals(u.getIdUsuario()))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Usuario> obtenerTodos() {
            return new ArrayList<>(almacenados);
        }

        @Override
        public void actualizar(Usuario usuario) {
            actualizados.add(usuario);
        }

        @Override
        public void eliminar(Long id) {
            eliminados.add(id);
            almacenados.removeIf(u -> id.equals(u.getIdUsuario()));
        }

        @Override
        public Usuario obtener(String nombre, String contrasenia) throws PersistenciaException {
            return almacenados.stream()
                    .filter(u -> u.getNombre().equals(nombre) && u.getContrasenia().equals(contrasenia))
                    .findFirst()
                    .orElseThrow(() -> new PersistenciaException("No existe."));
        }

        @Override
        public boolean existeNombre(String nombre) {
            return almacenados.stream().anyMatch(u -> u.getNombre().equalsIgnoreCase(nombre));
        }
    }

    private static class FakeTipoDAO implements ITipoDAO {

        private final Map<String, Tipo> tiposPorNombre = new HashMap<>();
        private long secuencia = 1L;

        @Override
        public void crear(Tipo tipo) {
            tipo.setIdTipo(secuencia++);
            tiposPorNombre.put(tipo.getNombre(), tipo);
        }

        @Override
        public Tipo obtener(Long id) {
            return tiposPorNombre.values().stream()
                    .filter(t -> id.equals(t.getIdTipo()))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public Tipo obtenerPorNombre(String nombre) {
            return tiposPorNombre.get(nombre);
        }

        @Override
        public List<Tipo> obtenerTodos() {
            return new ArrayList<>(tiposPorNombre.values());
        }

        @Override
        public void actualizar(Tipo tipo) {
            tiposPorNombre.put(tipo.getNombre(), tipo);
        }

        @Override
        public void eliminar(Long id) {
            tiposPorNombre.values().removeIf(t -> id.equals(t.getIdTipo()));
        }
    }
}
