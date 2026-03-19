package com.example.proyecto1programacion4.presentation;

import com.example.proyecto1programacion4.logic.SecurityConfig;
import com.example.proyecto1programacion4.logic.Oferente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/presentation/oferente")
public class OferenteController {

    @Autowired
    SecurityConfig service;

    @GetMapping("/show")
    public String dashboard(Model model) {
        //obtener nombre el oferente al iniciar seccion
        String nombreOferente ="Juann Perez";
        model.addAttribute("nombreOferente",nombreOferente);
        return "/presentation/oferente/dashboard_oferente";
    }

    @GetMapping("/view")
    public String viewHabilidades(Model model) {
        //model.addAttribute("habilidades", service.habilidadesPorOferante());
        return "/presentation/oferente/HabilidadesView";
        //crear un habilidades controller que maneje este view
    }

    @GetMapping("/new")
    public String newPuesto(Model model) {
        model.addAttribute("offerente", new Oferente());
        return "/presentation/oferente/CV";
    }

}
