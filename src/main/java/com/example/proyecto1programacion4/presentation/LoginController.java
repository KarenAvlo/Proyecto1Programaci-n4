package com.example.proyecto1programacion4.presentation;


import com.example.proyecto1programacion4.logic.Service;
import com.example.proyecto1programacion4.logic.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
//Aqui gestionamos el acceso
public class LoginController {

     @Autowired
    private Service service;

     @GetMapping
    public String showLoginForm(){
         return "login";  //Retornamos a la vista login.html
     }

    @PostMapping
    public String login(@RequestParam String email, @RequestParam String clave,
                        HttpSession session, Model model){
        try {
            Usuario usuario = service.Login(email, clave);
            session.setAttribute("usuario", usuario);

            // Redirección por tipo
            if ("ADMIN".equals(usuario.getTipo())) return "redirect:/admin/dashboard";
            if ("EMPRESA".equals(usuario.getTipo())) return "redirect:/empresa/perfil";
            return "redirect:/oferente/perfil";

        } catch (Exception e) {
            // AQUÍ es donde se crea la variable que el th:if busca
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Limpia la sesión
        return "redirect:/login?logout=true";
    }

}
