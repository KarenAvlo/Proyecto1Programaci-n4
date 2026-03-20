package com.example.proyecto1programacion4.logic;

import com.example.proyecto1programacion4.data.AdministradorRepository;
import com.example.proyecto1programacion4.data.EmpresaRepository;
import com.example.proyecto1programacion4.data.OferenteRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.proyecto1programacion4.data.UsuarioRepository;

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
    public void registrarOferente(Usuario u, Oferente o) throws Exception {
        if (usuarioRepository.existsById(u.getEmail())) {
            throw new Exception("El correo ya existe.");
        }
        u.setTipo("OFERENTE");
        u.setEstado(false);
        u.setClave(passwordEncoder.encode(u.getClave()));

        usuarioRepository.save(u);
        o.setEmail(u.getEmail());
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
    public List<Oferente> findOferentesPendientes() {
        return oferenteRepository.findAll().stream()
                .filter(o -> Boolean.FALSE.equals(o.getEstado()))
                .collect(Collectors.toList());
    }

    public List<Empresa> findEmpresasPendientes() {
        return empresaRepository.findAll().stream()
                .filter(e -> Boolean.FALSE.equals(e.getEstado()))
                .collect(Collectors.toList());
    }
}
