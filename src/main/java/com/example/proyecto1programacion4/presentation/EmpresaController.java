package com.example.proyecto1programacion4.presentation;

import com.example.proyecto1programacion4.logic.Puesto;
import com.example.proyecto1programacion4.logic.Service;
import com.example.proyecto1programacion4.logic.Empresa;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/presentation/empresa")
public class EmpresaController {

    @Autowired
    private Service service;

    @GetMapping("/dashboard")
    public String dashboard(Model model){
    //Obtener el nombre de la empresa al iniciar secion
        String nombreEmpresa="Pollitos.ca";
        model.addAttribute("nombreEmpresa", "Pollitos.ca");
        return "/presentation/empresa/dashboard_empresa";
    }

    @GetMapping("/show")
    public String showPuestos(Model model){
        //model.addAttribute("puestos", service.puestosPorEmpresa());//busca todos los puestos asociados a una empresa
        return "/presentation/empresa/PuestosView";
    }

    @GetMapping("/new")
    public String newPuesto(Model model){
        model.addAttribute("puesto", new Puesto());
        return "/presentation/empresa/PuestoForm";
    }
}
