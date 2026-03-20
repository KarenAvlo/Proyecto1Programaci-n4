package com.example.proyecto1programacion4.presentation;


import com.example.proyecto1programacion4.data.UsuarioRepository;
import com.example.proyecto1programacion4.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/registro")
public class RegistroController {
    @Autowired
    private UsuarioRepository usuarioRepository; //para buscar correos duplicados

    @Autowired
    private PasswordEncoder passwordEncoder;
    //private BCryptPasswordEncoder passwordEncoder; //cambiarlo luego

    @Autowired
    private LogicService service;

    @GetMapping("/empresa")
    public String mostrarFormEmpresa(){
        return "form_empresa";
    }

    @GetMapping("/oferente")
    public String mostrarFormOferente(){
        return "form_oferente";
    }

    @PostMapping("/guardar")
    public String guardar(
            @RequestParam("email") String email,
            @RequestParam("clave") String clave,
            @RequestParam("tipo") String tipo,
            @RequestParam(value = "cedula", required = false) String cedula,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "apellido", required = false) String apellido,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "localizacion", required = false) String localizacion,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            Model model) {

        try {
            Usuario u = new Usuario();
            u.setEmail(email);
            u.setClave(clave);

            if ("OFERENTE".equals(tipo)) {
                Oferente o = new Oferente();
                o.setEmail(email);
                o.setClave(clave);
                o.setTipo("OFERENTE");
                o.setCedula(cedula);
                o.setNombre(nombre);
                o.setApellido(apellido);
                service.registrarOferente(o);
            } else if ("EMPRESA".equals(tipo)) {
                Empresa e = new Empresa();
                e.setEmail(email);
                e.setClave(clave);
                e.setNombre(nombre);
                e.setTipo("EMPRESA");
                // --- ASIGNACIÓN DE LOS NUEVOS CAMPOS ---
                e.setTelefono(telefono);
                e.setLocalizacion(localizacion);
                e.setDescripcion(descripcion);
                // ---------------------------------------
                service.registrarEmpresa(e);
            }

            // Si llega aquí, todo salió BIEN
            return "redirect:/login?success";

        } catch (Exception e) {
            // Si entra aquí, algo FALLÓ (por eso te quedas en el registro)
            e.printStackTrace(); // MIRA TU CONSOLA DE IDE PARA VER EL ERROR REAL
            model.addAttribute("error", "Error: " + e.getMessage());
            return "EMPRESA".equals(tipo) ? "form_empresa" : "form_oferente";
        }

    }
}
