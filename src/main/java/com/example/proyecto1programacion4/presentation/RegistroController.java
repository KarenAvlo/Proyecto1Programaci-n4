package com.example.proyecto1programacion4.presentation;


import com.example.proyecto1programacion4.data.UsuarioRepository;
import com.example.proyecto1programacion4.logic.Empresa;
import com.example.proyecto1programacion4.logic.Oferente;
import com.example.proyecto1programacion4.logic.Service;
import com.example.proyecto1programacion4.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registro")
public class RegistroController {
    @Autowired
    private UsuarioRepository usuarioRepository; //para buscar correos duplicados

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Service service;

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
            Model model) {

        try {
            Usuario u = new Usuario();
            u.setEmail(email);
            u.setClave(clave);

            if ("OFERENTE".equals(tipo)) {
                Oferente o = new Oferente();
                o.setCedula(cedula);
                o.setNombre(nombre);
                o.setApellido(apellido);
                service.registrarOferente(u, o);
            } else if ("EMPRESA".equals(tipo)) {
                Empresa e = new Empresa();
                e.setNombre(nombre);
                service.registrarEmpresa(u, e);
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
