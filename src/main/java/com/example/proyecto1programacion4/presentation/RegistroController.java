package com.example.proyecto1programacion4.presentation;


import com.example.proyecto1programacion4.data.UsuarioRepository;
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

    @GetMapping("/empresa")
    public String mostrarFormEmpresa(){
        return "form_empresa";
    }

    @GetMapping("/oferente")
    public String mostrarFormOferente(){
        return "form_oferente";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@RequestParam String email, @RequestParam String clave, @RequestParam String nombre,
    @RequestParam String tipo, Model model, RedirectAttributes redirectAttributes){

        //Primero debemos validar si el correo existe, no pueden haber 2 empresas con el mismo correo
        if(usuarioRepository.findByEmail(email)!=null){
            model.addAttribute("¡error!, Este correo ya está en uso. Por favor, intente con otro correo");

            //si hay error lo devolvemos al formulario que corresponde

            return tipo.equals("Empresa")? "form_empresa": "form_oferente";
        }

        //Siguiente paso es el nuevo usuario
        Usuario nuevoUsuario= new Usuario();
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setTipo(tipo); //EMPRESA O OFERENTE
        nuevoUsuario.setEstado(false); //desactivado hasta que el admi apruebe

        //encriptacion de la contraseña
        String claveEncriptada=passwordEncoder.encode(clave);
        nuevoUsuario.setClave(claveEncriptada);

        //guardamos en la base de datos
        usuarioRepository.save(nuevoUsuario);

        //mensaje de éxito
        redirectAttributes.addFlashAttribute("mensajeExito", "Registro completado con éxito. Espere a que el administrador active su cuenta.");
        return "redirect:/login";
    }

}
