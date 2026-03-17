package com.example.proyecto1programacion4.presentation;


import com.example.proyecto1programacion4.data.UsuarioRepository;
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
    public String registrarOferente(
            @RequestParam String email,
            @RequestParam String clave,
            @RequestParam String cedula,
            Model model) {

        System.out.println("ENTRO AL REGISTRO OFERENTE");
        System.out.println("EMAIL: " + email);
        System.out.println("CLAVE: " + clave);
        System.out.println("CEDULA: " + cedula);

        try {

            // Validación básica
            if (email.isEmpty() || clave.isEmpty()|| cedula.isEmpty()) {
                model.addAttribute("errorCampos", "Todos los campos son obligatorios");
                return "form_oferente";
            }

            // Crear objetos manualmente (IMPORTANTE)
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setClave(clave);

            // Crear oferente
            Oferente oferente = new Oferente();
            oferente.setCedula(cedula); // ⚠️ Obligatorio para la PK
            // otros campos se pueden rellenar después

            // Llamar a TU método correcto
            service.registrarOferente(usuario, oferente);

            return "redirect:/login";

        } catch (Exception e) {

            String msg = e.getMessage().toLowerCase();

            if (msg.contains("correo") || msg.contains("email")) {
                model.addAttribute("errorCorreo", e.getMessage());
            } else if (msg.contains("clave")) {
                model.addAttribute("errorClave", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }

            return "form_oferente";
        }
    }
}
