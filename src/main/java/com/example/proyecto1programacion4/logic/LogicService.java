package com.example.proyecto1programacion4.logic;

import com.example.proyecto1programacion4.data.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class LogicService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; //para buscar email

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private RequisitoRepository requisitoRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    // --------------- INICIALIZACIÓN ----------------
    @PostConstruct
    public void init() {
        if (usuarioRepository.findByEmail("admin@bolsa.com") == null) {
            crearAdminInicial();
        }

        if (usuarioRepository.findByEmail("oferente@bolsa.com") == null) {
            crearOferenteInicial();
        }

        if (usuarioRepository.findByEmail("empresa@bolsa.com") == null) {
            crearEmpresaInicial();
        }

    }

    private void crearAdminInicial() {
        Usuario admin = new Usuario();
        admin.setEmail("admin@bolsa.com");
        admin.setClave(passwordEncoder.encode("123456"));
        admin.setTipo("ADMIN");
        admin.setEstado(true);
        usuarioRepository.save(admin);
    }

    private void crearOferenteInicial() {
        // Creamos el Usuario (Padre)
//        Usuario u = new Usuario();
        Oferente o = new Oferente();
        o.setEmail("oferente@bolsa.com");
        o.setClave(passwordEncoder.encode("123456"));
        o.setTipo("OFERENTE");
        o.setEstado(true); // Lo ponemos como aprobado para poder loguearnos ya
        //usuarioRepository.save(u);

        // Creamos el Oferente (Hijo)

        //o.setEmail(o.getEmail());
        o.setNombre("Juan");
        o.setApellido("Pérez");
        o.setCedula("1-1111-1111");
        o.setTelefono("8888-8888");
        // o.setEstado(true); // Si tu entidad Oferente tiene un campo estado, descomenta esto
        oferenteRepository.save(o);
    }

    private void crearEmpresaInicial() {
        // Creamos la Empresa (que según tu lógica hereda o tiene los campos de Usuario)
        Empresa e = new Empresa();
        e.setEmail("empresa@bolsa.com");
        e.setClave(passwordEncoder.encode("123456"));
        e.setTipo("EMPRESA");
        e.setEstado(true); // Aprobada para pruebas

        e.setNombre("Tech Solutions");
        e.setLocalizacion("San José, CR");
        e.setTelefono("7777-7777");
        e.setDescripcion("Empresa líder en tecnología");
        // Al usar herencia o @MapsId, solo necesitas guardar el repositorio de la Empresa
        empresaRepository.save(e);
    }

    // --------------- LÓGICA DE USUARIOS / ADMIN ----------------
    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public void aprobarUsuario(String email) {
        Usuario u = usuarioRepository.findById(email).orElse(null);
        if (u != null) {
            u.setEstado(true);
            usuarioRepository.save(u);
        }
    }


// --------------- LÓGICA DE OFERENTES ----------------
    public void registrarOferente(Oferente o) throws Exception {
        if (usuarioRepository.existsById(o.getEmail())) {
            throw new Exception("El correo ya existe.");
        }

        // Configuramos los campos heredados de Usuario dentro del objeto Oferente
        o.setTipo("OFERENTE");
        o.setEstado(false);
        o.setClave(passwordEncoder.encode(o.getClave()));

        // Al guardar el oferente, JPA guarda automáticamente en la tabla 'usuario'
        // y en la tabla 'oferente' por la herencia JOINED.
        oferenteRepository.save(o);
    }

    // --------------- LÓGICA DE EMPRESAS ----------------
    public void registrarEmpresa(Empresa e) throws Exception {
        if (usuarioRepository.existsById(e.getEmail())) {
            throw new Exception("El correo ya existe.");
        }
        e.setEmail(e.getEmail());
        e.setTipo("EMPRESA");
        e.setEstado(false);
        e.setClave(passwordEncoder.encode(e.getClave()));
        empresaRepository.save(e);
    }

    // --------------- DASHBOARD FILTROS ----------------
    public List<Usuario> findEmpresasPendientes() {
        return usuarioRepository.findByTipoAndEstado("EMPRESA", false);
    }

    public List<Usuario> findOferentesPendientes() {
        return usuarioRepository.findByTipoAndEstado("OFERENTE", false);
    }

    //================PUESTOS=============================
// Listar solo los puestos de una empresa específica
    public List<Puesto> listarPuestosPorEmpresa(Empresa empresa) {
        return puestoRepository.findByEmailEmpresa(empresa);
    }

    // Buscar un puesto por ID (útil para desactivar)
    public Puesto buscarPuestoPorId(Integer id) {
        return puestoRepository.findById(id).orElse(null);
    }

    // El método de guardar (que ya tienes) sirve para actualizar el estado 'activo'
    public void actualizarPuesto(Puesto p) {
        puestoRepository.save(p);
    }

    // Busca la empresa para asignarla al puesto
    public Empresa buscarEmpresaPorEmail(String email) {
        return empresaRepository.findById(email).orElse(null);
    }

    // Guarda el puesto y retorna el objeto con su ID generado
    public Puesto guardarPuesto(Puesto puesto) {
        return puestoRepository.save(puesto);
    }

    // Guarda cada característica vinculada al puesto
    public void guardarRequisito(Puesto puesto, String nombre, Integer nivel) {
        Requisito req = new Requisito();
        req.setPuesto(puesto);
        req.setNombre(nombre);
        req.setNivel(nivel);
        requisitoRepository.save(req);
    }

    // Este método lo usa tu EmpresaController.showPuestos
    public List<Puesto> findPuestosPorEmpresa(String email) {
        Empresa e = empresaRepository.findById(email).orElse(null);
        return puestoRepository.findByEmailEmpresa(e);
    }

    // En LogicService.java
    public List<Caracteristica> listarCaracteristicasAdmin() {

        return caracteristicaRepository.findAll();
    }

}
