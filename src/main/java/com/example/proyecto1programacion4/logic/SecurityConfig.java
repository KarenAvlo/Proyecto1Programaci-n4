package com.example.proyecto1programacion4.logic;

import com.example.proyecto1programacion4.data.*;//todos los repositorios
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service("service")
public class SecurityConfig {
    @Autowired
    private  AdministradorRepository administradorRepository;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; //para buscar email

    @Autowired
    private PasswordEncoder passwordEncoder;
    //sustituir despues
    //private BCryptPasswordEncoder passwordEncoder;

    //---------------ADMINISTRADORES-----------------------------------

    @PostConstruct
    public void init() {
        // Solo lo creamos si no existe ya, para no duplicar admins cada vez que reinicies
        if (usuarioRepository.findByEmail("admin@bolsa.com") == null) {
            crearAdminInicial();
            System.out.println("-----> Administrador inicial creado: admin@bolsa.com / 123456");
        }
    }

    //Creamos el único administrador
    public void crearAdminInicial() {
        Usuario admin = new Usuario();
        admin.setEmail("admin@bolsa.com");
        // Encriptamos la clave "123"
        admin.setClave(passwordEncoder.encode("123456"));
        admin.setTipo("ADMIN");
        admin.setEstado(true); // El admin debe estar aprobado siempre

        administradorRepository.save(admin);
    }

    public List<Usuario> findAll() { // Cambiar Iterable por List
        return administradorRepository.findAll();
    }

    public Usuario findById(String id) {
        return administradorRepository.findById(id).orElse(null);
    }

    public List<Usuario> findPendientes() {

        return administradorRepository.findAll()
                .stream()
                .filter(u -> u.getEstado() != null && !u.getEstado())
                .collect(Collectors.toList());
    }

    public void aprobarUsuario(String email) {
        Usuario u = administradorRepository.findById(email).orElse(null);
        if (u != null) {
            u.setEstado(true); // 1 = Aprobado
            administradorRepository.save(u);
        }
    }


    //-----------DASHBOARD DEL ADMIN-----------------

    public List<Oferente> findOferentesPendientes() {
        // Obtenemos todos los oferentes y filtramos los que tienen usuario con estado false
        return oferenteRepository.findAll()
                .stream()
                .filter(o -> o.getEstado() != null && Boolean.FALSE.equals(o.getEstado()))
                .collect(Collectors.toList());
    }

    public List<Empresa> findEmpresasPendientes() {
        // Obtenemos todas las empresas y filtramos las que tienen usuario con estado false
        return empresaRepository.findAll()
                .stream()
                .filter(e -> e.getEstado() != null && Boolean.FALSE.equals(e.getEstado()))
                .collect(Collectors.toList());
    }

    //---------------------------OFERENTES----------------------------------------
    public Iterable<Oferente>  findAllOferente() {
        return oferenteRepository.findAll();
    }

    public Oferente findByIdOferente(String email) {
        return oferenteRepository.findById(email).orElse(null);
    }

    @Transactional
    public void registrarOferente(Usuario u, Oferente o) throws Exception {
        // VALIDACIÓN: ¿Ya existe el correo?
        if (usuarioRepository.existsById(u.getEmail())) {
            throw new Exception("El correo electrónico ya se encuentra registrado.");
        }

        u.setTipo("OFERENTE");
        u.setEstado(false); //requiere aprobacion del admi
        u.setClave(passwordEncoder.encode(u.getClave()));

        usuarioRepository.save(u); // Guardar padre

        o.setEmail(u.getEmail());
        oferenteRepository.save(o); // Guardar hijo
    }

    //------------------------------EMPRESAS----------------------------
    public Iterable<Empresa> findAllEmpresas() {
        return empresaRepository.findAll();
    }

    public Empresa findByIdEmpresa(String email) {
        return empresaRepository.findById(email).orElse(null);
    }

    // ... dentro de la clase Service.java ...

    @Transactional
    public void registrarEmpresa(Empresa e) throws Exception {
        // 1. Validación de duplicados
        if (usuarioRepository.existsById(e.getEmail())) {
            throw new Exception("El correo electrónico ya se encuentra registrado.");
        }

        // 2. Configuración del perfil de Usuario
        e.setTipo("EMPRESA");
        e.setEstado(false); // Requiere aprobación del admin según tu lógica
        e.setClave(passwordEncoder.encode(e.getClave()));

        // 3. Persistir SOLO la Empresa
        // Hibernate hará los dos INSERTs automáticamente (tabla usuario y tabla empresa)
        empresaRepository.save(e);

        // 3. Persistir el Usuario primero para que exista en la DB
        // Esto es necesario porque Empresa depende de que este email ya exista
        //Usuario usuarioGuardado = usuarioRepository.save(u); //no es necesario gracias a la erencia
        // 4. Vincular la Empresa con el Usuario guardado
        //e.setUsuario(usuarioGuardado); //no es necesario gracias a la erencia
        // IMPORTANTE: Con @MapsId, no es necesario hacer e.setEmail(...)
        // Hibernate lo extraerá automáticamente de usuarioGuardado
        // 5. Guardar la Empresa
    }

    //-------LOGIN----------
    public Usuario Login(String email,String clave) throws Exception{
        //Usuario u = administradorRepository.findById(email).orElse(null);
        Usuario u = usuarioRepository.findById(email).orElse(null);

        if (u==null ){
            throw new Exception("correo no existe");
        }

        if (!passwordEncoder.matches(clave, u.getClave())) {
            throw new Exception("clave incorrecta");
        }

        if(Boolean.FALSE.equals(u.getEstado())){
            throw new Exception("usuario no aprobado");
        }

        return u;
    }
}
