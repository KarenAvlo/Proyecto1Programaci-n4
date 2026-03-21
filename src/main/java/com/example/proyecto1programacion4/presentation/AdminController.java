package com.example.proyecto1programacion4.presentation;



import com.example.proyecto1programacion4.data.CaracteristicaRepository;
import com.example.proyecto1programacion4.logic.Caracteristica;
import com.example.proyecto1programacion4.logic.LogicService;
import com.example.proyecto1programacion4.logic.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private LogicService logicService; // Tu clase de lógica/servicio

    @Autowired
    private CaracteristicaRepository caracRepo;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        // Si authentication es null, ponemos un valor por defecto o redirigimos
        String email = (authentication != null) ? authentication.getName() : "Admin";
        model.addAttribute("correoUsuario", email);
        return "dashboard_admi";
    }

    @GetMapping("/empresas-pendientes")
    public String listarEmpresas(Authentication authentication, Model model) {
        // 1. Cargamos la lista de empresas
        model.addAttribute("empresas", logicService.findEmpresasPendientes());

        // 2. Cargamos el correo para el navbar
        String email = (authentication != null) ? authentication.getName() : "Admin";
        model.addAttribute("correoUsuario", email);

        return "empresas_pendientes";
    }

    @GetMapping("/oferentes-pendientes")
    public String listarOferentes(Authentication authentication, Model model) {
        // 1. Enviamos la lista de OFERENTES
        model.addAttribute("oferentes", logicService.findOferentesPendientes());

        // 2. Enviamos el correo para el nav
        model.addAttribute("correoUsuario", authentication.getName());

        // 3. Retornamos la vista (asegúrate de que esté en la carpeta admin si usas esa ruta)
        return "oferentes_pendientes";
    }

    @PostMapping("/autorizar/{email}")
    public String autorizar(@PathVariable String email, @RequestParam String tipo) {
        logicService.aprobarUsuario(email); // Método existente en tu SecurityConfig
        return "redirect:/admin/" + (tipo.equals("EMPRESA") ? "empresas" : "oferentes") + "-pendientes";
    }

    @GetMapping("/caracteristicas")
    public String listarCaracteristicas(@RequestParam(name = "padreId", required = false) Integer padreId, Model model, Authentication auth) {
        model.addAttribute("correoUsuario", auth.getName());
        model.addAttribute("raices", caracRepo.findByIdPadreIsNull());

        if (padreId == null) {
            model.addAttribute("lista", caracRepo.findByIdPadreIsNull());
            model.addAttribute("padre", null);
        } else {
            Caracteristica padreObj = caracRepo.findById(padreId).orElse(null);
            model.addAttribute("padre", padreObj);
            model.addAttribute("lista", caracRepo.findByIdPadre(padreObj));
        }
        // Si el archivo está en src/main/resources/templates/admi_caracteristicas.html
        return "admi_caracteristicas";
    }



    @GetMapping("/caracteristicas/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        caracRepo.deleteById(id);
        return "redirect:/admin/caracteristicas";
    }

    @PostMapping("/caracteristicas/save")
    public String guardarCaracteristica(@RequestParam("nombre") String nombre,
                                        @RequestParam(value = "padreId", required = false) Integer padreId) {
        Caracteristica nueva = new Caracteristica();
        nueva.setNombre(nombre);

        if (padreId != null) {
            // Buscamos el objeto padre para establecer la relación jerárquica
            Caracteristica padre = caracRepo.findById(padreId).orElse(null);
            nueva.setIdPadre(padre);
        }

        caracRepo.save(nueva);

        // Redirigimos a la misma página para ver los cambios
        return "redirect:/admin/caracteristicas" + (padreId != null ? "?padreId=" + padreId : "");
    }

    @ModelAttribute
    public void addAttributes(org.springframework.security.core.Authentication authentication, org.springframework.ui.Model model) {
        if (authentication != null) {
            model.addAttribute("correoUsuario", authentication.getName());
        }
    }
}
