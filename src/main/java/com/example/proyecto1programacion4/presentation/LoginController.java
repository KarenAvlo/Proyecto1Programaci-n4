package com.example.proyecto1programacion4.presentation;


import com.example.proyecto1programacion4.logic.SecurityConfig;
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
//@RequestMapping("/login")
//Aqui gestionamos el acceso
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas o usuario no aprobado.");
        }
        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión correctamente.");
        }
        return "login"; // Retorna login.html
    }
//
//     @Autowired
//    private SecurityConfig service;
//
//     @GetMapping
//    public String showLoginForm(){
//         return "login";  //Retornamos a la vista login.html
//     }
//
//    @PostMapping
//    public String login(@RequestParam String email, @RequestParam String clave,
//                        HttpSession session, Model model){
//        try {
//            Usuario usuario = service.Login(email, clave);
//            session.setAttribute("usuario", usuario);
//
//            System.out.println(usuario.getTipo());//revisar el tipo
//            // Redirección por tipo
//            if ("ADMIN".equals(usuario.getTipo())) return "redirect:/admin/dashboard";
//            if ("EMPRESA".equals(usuario.getTipo())) return "redirect:/presentation/empresa/dashboard";
//            return "redirect:/oferente/perfil";
//        }
//        catch (Exception e) {
//
//            if (e.getMessage().contains("correo")) {
//                model.addAttribute("errorCorreo", e.getMessage());
//            } else if (e.getMessage().contains("clave")) {
//                model.addAttribute("errorClave", e.getMessage());
//            } else {
//                model.addAttribute("error", e.getMessage());
//            }
//
//            return "login";
//        }
//    }

// El Logout también lo maneja Spring Security
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate(); // Limpia la sesión
//        return "redirect:/login?logout=true";
//    }

}
