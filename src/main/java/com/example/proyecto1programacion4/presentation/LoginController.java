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
}
