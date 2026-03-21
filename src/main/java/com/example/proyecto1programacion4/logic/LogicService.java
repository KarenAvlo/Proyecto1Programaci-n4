package com.example.proyecto1programacion4.logic;

import com.example.proyecto1programacion4.data.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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

    @Autowired
    private OferenteCaracteristicaRepository oferenteCaracteristicaRepository;

    @Autowired
    private PuestoCaracteristicaRepository puestoCaracteristicaRepository;

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

    public Oferente buscarOferentePorCedula(String cedula) {
        return oferenteRepository.findByCedula(cedula).orElse(null);
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
    public void guardarRequisito(Puesto puesto, String nombreCaracteristica, Integer nivel) {
        // 1. Buscamos la entidad Caracteristica que coincida con el nombre seleccionado en el HTML
        // Debes tener un método en tu CaracteristicaRepository que busque por nombre
        Caracteristica carac = caracteristicaRepository.findByNombre(nombreCaracteristica);

        if (carac != null) {
            // 2. Usamos la clase que ya tienes definida: PuestoCaracteristica
            PuestoCaracteristica pc = new PuestoCaracteristica();
            pc.setIdPuesto(puesto); // Seteamos el puesto recién guardado
            pc.setIdCaracteristica(carac); // Seteamos la habilidad técnica
            pc.setNivelDeseado(nivel); // Seteamos el nivel (1-5)

            // 3. Guardamos en la tabla intermedia
            puestoCaracteristicaRepository.save(pc);
        }
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

    //================HABILIDADESPUESTOS=============================//

    public Oferente buscarOferentePorEmail(String email) {
        return oferenteRepository.findByEmail(email).orElse(null);
    }

    public List<OferenteCaracteristica> listarCaracteristicasOferente(String cedula) {
        return oferenteCaracteristicaRepository.findByCedulaOferenteCedula(cedula);
    }

    public List<Caracteristica> listaCaracteristicasPadre(){
        return caracteristicaRepository.findByIdPadreIsNull();
    }

    public void guardarOferenteCaracteristica(OferenteCaracteristica oc) {
        oferenteCaracteristicaRepository.save(oc);
    }

    //================CV=============================//

    public void guardarOferente(Oferente oferente) {
        oferenteRepository.save(oferente);
    }

    //=======================MATCH CANDIDATOS===========================

    public List<CandidatoMatch> buscarCandidatosParaPuesto(Integer idPuesto) {
        // 1. Obtener requisitos del puesto
        List<PuestoCaracteristica> requisitos = puestoCaracteristicaRepository.findAll()
                .stream().filter(pc -> pc.getIdPuesto().getId().equals(idPuesto))
                .collect(Collectors.toList());

        if (requisitos.isEmpty()) return new ArrayList<>();

        // 2. Obtener todos los oferentes activos
        List<Oferente> todosLosOferentes = oferenteRepository.findAll();
        List<CandidatoMatch> resultados = new ArrayList<>();

        for (Oferente oferente : todosLosOferentes) {
            // Habilidades del candidato actual
            List<OferenteCaracteristica> habilidadesCandidato =
                    oferenteCaracteristicaRepository.findByCedulaOferenteCedula(oferente.getCedula());

            int coincidencias = 0;
            for (PuestoCaracteristica req : requisitos) {
                for (OferenteCaracteristica hab : habilidadesCandidato) {
                    // Compara si es la misma habilidad y si el nivel del candidato es >= al deseado
                    if (hab.getIdCaracteristica().getId().equals(req.getIdCaracteristica().getId())
                            && hab.getNivel() >= req.getNivelDeseado()) {
                        coincidencias++;
                        break;
                    }
                }
            }

            // Calcular porcentaje (Regla de 3)
            double porcentaje = (double) coincidencias / requisitos.size() * 100;

            if (porcentaje > 0) { // Solo mostrar si tiene al menos algo de match
                CandidatoMatch match = new CandidatoMatch();
                match.setOferente(oferente);
                match.setPorcentaje(Math.round(porcentaje * 100.0) / 100.0); // Redondear 2 decimales
                match.setCoincidencias(coincidencias);
                resultados.add(match);
            }
        }

        // Ordenar de mayor a menor porcentaje
        resultados.sort((a, b) -> Double.compare(b.getPorcentaje(), a.getPorcentaje()));
        return resultados;
    }

    public void guardarPuestoCaracteristica(Puesto puesto, Integer caracteristicaId, Integer nivel) {
        if (caracteristicaId == null) return; // Evita errores si la fila estaba vacía

        Caracteristica car = caracteristicaRepository.findById(caracteristicaId).orElse(null);
        if (car != null) {
            PuestoCaracteristica pc = new PuestoCaracteristica();
            pc.setIdPuesto(puesto);
            pc.setIdCaracteristica(car);
            pc.setNivelDeseado(nivel);
            puestoCaracteristicaRepository.save(pc);
        }
    }

    public void desactivarPuesto(Integer id) {
        Puesto puesto = puestoRepository.findById(id).orElse(null);
        if (puesto != null) {
            puesto.setActivo(false);
            puestoRepository.save(puesto);
        }
    }


    /*=======VISTA PRINCIPAL=============*/
    // Este lo usas para la página principal (index) donde entra cualquiera
    public List<Puesto> listarPuestosPublicos() {
        return puestoRepository.findAll().stream()
                .filter(p -> p.getActivo() != null && p.getActivo()
                        && "PUBLICA".equalsIgnoreCase(p.getTipoPublicacion()))
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
                .limit(5)
                .collect(Collectors.toList());
    }

    // Este lo usas cuando un Oferente ya inició sesión
    public List<Puesto> listarPuestosParaOferenteLogueado() {
        return puestoRepository.findAll().stream()
                .filter(p -> p.getActivo() == true) // Ve públicas y privadas
                .collect(Collectors.toList());
    }

    public List<Puesto> listar5UltimosPuestosPublicos() {
        return puestoRepository.findAll().stream()
                .filter(p -> p.getActivo() && "PUBLICA".equalsIgnoreCase(p.getTipoPublicacion()))
                // Ordenamos por fechaRegistro de forma descendente (más nuevo primero)
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
                .limit(5)
                .collect(Collectors.toList());
    }

    //=========Buscar puestos===============//
    public List<Caracteristica> listarCategoriasRaiz() {
        return caracteristicaRepository.findByIdPadreIsNull();
    }

    public List<Caracteristica> listarSubcategorias(Caracteristica padre) {
        return caracteristicaRepository.findByIdPadre(padre); //
    }

    public List<Puesto> buscarPuestosFiltrados(List<Integer> ids, String moneda) {
        return puestoRepository.findAll().stream()
                .filter(p -> p.getActivo() != null && p.getActivo()
                        && "PUBLICA".equalsIgnoreCase(p.getTipoPublicacion()))
                // Filtro por Moneda (si se seleccionó una)
                .filter(p -> {
                    if (moneda == null || moneda.isEmpty()) return true;
                    return moneda.equalsIgnoreCase(p.getMoneda());
                })
                // Filtro por Características (si se seleccionaron)
                .filter(p -> {
                    if (ids == null || ids.isEmpty()) return true;
                    return p.getPuestoCaracteristicas().stream()
                            .anyMatch(pc -> ids.contains(pc.getIdCaracteristica().getId()));
                })
                .collect(Collectors.toList());
    }

}
