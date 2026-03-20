package com.example.proyecto1programacion4.presentation;



import com.example.proyecto1programacion4.data.CaracteristicaRepository;
import com.example.proyecto1programacion4.logic.Caracteristica;
import com.example.proyecto1programacion4.logic.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SecurityConfig securityService; // Tu clase de lógica/servicio

    @Autowired
    private CaracteristicaRepository caracRepo;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard_admi"; // Nombre de tu archivo en templates/
    }

    @GetMapping("/empresas-pendientes")
    public String listarEmpresas(Model model) {
        // Usamos el método que ya tienes en SecurityConfig
        model.addAttribute("empresas", securityService.findEmpresasPendientes());
        return "admin/empresas_pendientes";
    }

    @GetMapping("/oferentes-pendientes")
    public String listarOferentes(Model model) {
        model.addAttribute("oferentes", securityService.findOferentesPendientes());
        return "admin/oferentes_pendientes";
    }

    @PostMapping("/autorizar/{email}")
    public String autorizar(@PathVariable String email, @RequestParam String tipo) {
        securityService.aprobarUsuario(email); // Método existente en tu SecurityConfig
        return "redirect:/admin/" + (tipo.equals("EMPRESA") ? "empresas" : "oferentes") + "-pendientes";
    }

    @GetMapping("/caracteristicas")
    public String listarCaracteristicas(@RequestParam(name = "padreId", required = false) Integer padreId, Model model) {
        // Siempre necesitamos las raíces para el dropdown del formulario
        model.addAttribute("raices", caracRepo.findByIdPadreIsNull());

        if (padreId == null) {
            model.addAttribute("lista", caracRepo.findByIdPadreIsNull());
            model.addAttribute("padre", null);
        } else {
            Caracteristica padreObj = caracRepo.findById(padreId).orElse(null);
            model.addAttribute("padre", padreObj);
            // Aquí usamos el método corregido del repositorio
            model.addAttribute("lista", caracRepo.findByIdPadre(padreObj));
        }
        return "admin/caracteristicas";
    }

    // ... dentro de AdminController.java

    @GetMapping("/caracteristicas")
    public String mostrarPaginaCaracteristicas(Model model) {
        // 1. Obtenemos las categorías raíz (ej: Lenguajes, Bases de Datos)
        model.addAttribute("raices", caracRepo.findByIdPadreIsNull());

        // 2. Por defecto al entrar, la lista principal son las raíces
        model.addAttribute("lista", caracRepo.findByIdPadreIsNull());

        // 3. Retornamos el nombre EXACTO de tu nuevo archivo HTML
        return "admi_caracteristica";
    }
}
