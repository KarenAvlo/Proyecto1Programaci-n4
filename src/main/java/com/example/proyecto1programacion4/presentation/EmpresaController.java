package com.example.proyecto1programacion4.presentation;

import com.example.proyecto1programacion4.logic.Empresa;
import com.example.proyecto1programacion4.logic.LogicService;
import com.example.proyecto1programacion4.logic.Puesto;
import com.example.proyecto1programacion4.logic.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private LogicService logicService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        // Obtenemos el correo directamente del objeto Auth
        model.addAttribute("correoUsuario", auth.getName());
        return "dashboard_empresa";
    }

    @GetMapping("/show")
    public String showPuestos(Authentication auth, Model model) {
        String email = auth.getName(); // Email del usuario logueado
        model.addAttribute("correoUsuario", email);
        model.addAttribute("puestos", logicService.findPuestosPorEmpresa(email));
        return "PuestosView";
    }

    @GetMapping("/new")
    public String newPuesto(Authentication auth, Model model) {
        model.addAttribute("puesto", new Puesto());
        // Pasamos las características predefinidas por el Administrador
        model.addAttribute("caracteristicasSistema", logicService.listarCaracteristicasAdmin());
        model.addAttribute("correoUsuario", auth.getName());
        return "publicar_puesto";
    }

    @PostMapping("/savePuesto") // Asegúrate de que coincida con el th:action de tu HTML
    public String guardarPuesto(
            @ModelAttribute("puesto") Puesto puesto,
            @RequestParam(value = "nomCaracteristica", required = false) String[] nombres,
            @RequestParam(value = "nivelCaracteristica", required = false) Integer[] niveles,
            Authentication auth
    ) {
        Empresa e = logicService.buscarEmpresaPorEmail(auth.getName());
        puesto.setEmailEmpresa(e);
        puesto.setActivo(true);

        Puesto guardado = logicService.guardarPuesto(puesto);

        if (nombres != null && niveles != null) {
            for (int i = 0; i < nombres.length; i++) {
                if (nombres[i] != null && !nombres[i].isEmpty()) {
                    logicService.guardarRequisito(guardado, nombres[i], niveles[i]);
                }
            }
        }
        return "redirect:/empresa/show";
    }


/*
    // Listar solo los puestos de una empresa específica
    public List<Puesto> listarPuestosPorEmpresa(Empresa empresa) {
        return puestoRepository.findByEmailEmpresa(empresa);
    }

    // Buscar un puesto por ID (útil para desactivar)
    public Puesto buscarPuestoPorId(Integer id) {
        return puestoRepository.findById(id).orElse(null);
    }

    // El método de guardar (que ya tienes) sirve para actualizar el estado 'activo'
    public void actualizarPuesto(Puesto p) {
        puestoRepository.save(p);
    }*/
}
