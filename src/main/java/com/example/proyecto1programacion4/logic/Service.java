package com.example.proyecto1programacion4.logic;

import com.example.proyecto1programacion4.data.*;//todos los repositorios
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private  AdministradorRepository administradorRepository;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired UsuarioRepository usuarioRepository; //para buscar email

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //---------------ADMINISTRADORES-----------------------------------

    @PostConstruct
    public void init() {
        // Solo lo creamos si no existe ya, para no duplicar admins cada vez que reinicies
        if (usuarioRepository.findByEmail("admin@bolsa.com") == null) {
            crearAdminInicial();
            System.out.println("-----> Administrador inicial creado: admin@bolsa.com / 123");
        }
    }

    //Creamos el único administrador
    public void crearAdminInicial() {
        Usuario admin = new Usuario();
        admin.setEmail("admin@bolsa.com");
        // Encriptamos la clave "123"
        admin.setClave(passwordEncoder.encode("123"));
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
                .filter(o -> o.getEmail() != null && Boolean.FALSE.equals(o.getEmail().getEstado()))
                .collect(Collectors.toList());
    }

    public List<Empresa> findEmpresasPendientes() {
        // Obtenemos todas las empresas y filtramos las que tienen usuario con estado false
        return empresaRepository.findAll()
                .stream()
                .filter(e -> e.getUsuario() != null && Boolean.FALSE.equals(e.getUsuario().getEstado()))
                .collect(Collectors.toList());
    }

    //---------------------------OFERENTES----------------------------------------
    public Iterable<Oferente>  findAllOferente() {
        return oferenteRepository.findAll();
    }

    public Oferente findByIdOferente(String email) {
        return oferenteRepository.findById(email).orElse(null);
    }

    @Transactional // Importante para que si falla uno, no se guarde el otro
    public void registrarOferente(Usuario usuario, Oferente oferente) {
        // 1. Encriptamos la clave recibida del formulario
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));

        // 2. Valores por defecto
        usuario.setTipo("OFERENTE");
        usuario.setEstado(false); // Pendiente de aprobación

        // 3. Guardamos usuario y vinculamos con oferente
        Usuario usuarioGuardado = administradorRepository.save(usuario);
        oferente.setEmail(usuarioGuardado);
        oferenteRepository.save(oferente);
    }

    //------------------------------EMPRESAS----------------------------
    public Iterable<Empresa> findAllEmpresas() {
        return empresaRepository.findAll();
    }

    public Empresa findByIdEmpresa(String email) {
        return empresaRepository.findById(email).orElse(null);
    }

    @Transactional
    public void registrarEmpresa(Usuario u, Empresa e) {
        //  Configurar los valores por defecto del usuario
        u.setTipo("EMPRESA");
        u.setEstado(false); // Pendiente de aprobación

        //  Vincular ambos objetos
        //  necesita tener el objeto usuario dentro
        e.setUsuario(u);
        e.setEmail(u.getEmail()); // Seteamos el ID manual si es necesario

        // 3. Guardar primero el Usuario (el padre)
        administradorRepository.save(u);

        // 4. Guardar la Empresa (el hijo que depende del ID del padre)
        empresaRepository.save(e);
    }

    //-------LOGIN----------
    public Usuario Login(String email,String clave) throws Exception{
        Usuario u = administradorRepository.findById(email).orElse(null);

        if (u==null ){
            throw new Exception("El usuario no existe");
        }

        if (!passwordEncoder.matches(clave, u.getClave())) {
            throw new Exception("Contraseña incorrecta.");
        }

        if(Boolean.FALSE.equals(u.getEstado())){
            throw new Exception("Su cuenta no ha sido aprobada por el administrador aún");
        }

        return u; // si todo bien, devuelve suario
    }



}
