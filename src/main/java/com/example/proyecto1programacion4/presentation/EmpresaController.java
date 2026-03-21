package com.example.proyecto1programacion4.presentation;

import com.example.proyecto1programacion4.logic.*;
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


    @PostMapping("/save")
    public String savePuesto(@ModelAttribute("puesto") Puesto puesto,
                             @RequestParam(value = "caracteristicaIds", required = false) List<Integer> caracteristicaIds,
                             @RequestParam(value = "niveles", required = false) List<Integer> niveles,
                             Authentication auth) {

        String email = auth.getName();
        Empresa empresa = logicService.buscarEmpresaPorEmail(email);
        puesto.setEmailEmpresa(empresa);
        puesto.setActivo(true);

        // Guardamos el puesto
        Puesto puestoGuardado = logicService.guardarPuesto(puesto);

        // Guardamos las características solo si se seleccionaron
        if (caracteristicaIds != null && niveles != null) {
            for (int i = 0; i < caracteristicaIds.size(); i++) {
                // Verificamos que se haya seleccionado una opción válida (no el placeholder vacío)
                if (caracteristicaIds.get(i) != null && i < niveles.size()) {
                    logicService.guardarPuestoCaracteristica(puestoGuardado, caracteristicaIds.get(i), niveles.get(i));
                }
            }
        }
        return "redirect:/empresa/show";
    }

    @GetMapping("/desactivar/{id}")
    public String desactivarPuesto(@PathVariable("id") Integer id) {
        logicService.desactivarPuesto(id);
        return "redirect:/empresa/show";
    }

    @GetMapping("/buscar-candidatos/{id}")
    public String buscarCandidatos(@PathVariable("id") Integer id, Model model, Authentication auth) {
        model.addAttribute("correoUsuario", auth.getName());

        // Buscamos los matches usando el servicio
        List<CandidatoMatch> candidatos = logicService.buscarCandidatosParaPuesto(id);

        model.addAttribute("candidatos", candidatos);
        model.addAttribute("puestoId", id);

        return "CandidatoMatchView"; // El nombre de tu nuevo HTML
    }

    @GetMapping("/ver-perfil/{cedula}")
    public String verDetalleCandidato(@PathVariable("cedula") String cedula,
                                      @RequestParam(value = "puestoId", required = false) Integer puestoId,
                                      Model model,
                                      Authentication auth) {
        // 1. Enviamos el correo de la empresa para el navbar
        model.addAttribute("correoUsuario", auth.getName());

        // 2. Buscamos al oferente usando el método que creamos (por cédula)
        Oferente oferente = logicService.buscarOferentePorCedula(cedula);

        if (oferente != null) {
            // 3. Cargamos sus habilidades usando su cédula (que es como lo tienes en el Service)
            List<OferenteCaracteristica> habilidades = logicService.listarCaracteristicasOferente(cedula);

            model.addAttribute("oferente", oferente);
            model.addAttribute("habilidades", habilidades);
        }

        // 4. Pasamos el puestoId para que el botón "Volver" sepa a dónde regresar
        model.addAttribute("puestoId", puestoId);

        return "DetalleOferenteView";
    }

}
