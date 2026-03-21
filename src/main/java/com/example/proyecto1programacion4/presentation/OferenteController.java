package com.example.proyecto1programacion4.presentation;

import com.example.proyecto1programacion4.logic.LogicService;
import com.example.proyecto1programacion4.logic.OferenteCaracteristica;
import com.example.proyecto1programacion4.logic.SecurityConfig;
import com.example.proyecto1programacion4.logic.Oferente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/presentation/oferente")
public class OferenteController {

    @Autowired
    LogicService service;

    @GetMapping("/show")
    public String dashboard(Model model) {
        //obtener nombre el oferente al iniciar seccion
        String nombreOferente ="Juann Perez";
        model.addAttribute("nombreOferente",nombreOferente);
        return "dashboard_oferente";
    }

    @GetMapping("/habilidades")
    public String verHabilidades(Authentication auth, Model model) {
        String email = auth.getName();//correo el oferente
        Oferente oferente = service.buscarOferentePorEmail(email); //se busca al  oferente
        if (oferente != null) {
            String cedula = oferente.getCedula();
            model.addAttribute("listaHabilidades", service.listarCaracteristicasOferente(cedula));
            model.addAttribute("nuevaHabilidad", service.listaCaracteristicasPadre());
            model.addAttribute("nuevaCategoria", new OferenteCaracteristica());
        }
        return "oferente_habilidades"; // Nombre de tu archivo HTML
    }

    @PostMapping("/saveCaracteristica")
    public String saveCaracteristica(
            @ModelAttribute("nuevaHabilidad")OferenteCaracteristica nuevaCaracteristica, Authentication auth) {
            String emmail = auth.getName();
            Oferente oferente = service.buscarOferentePorEmail(emmail);
            if (oferente != null) {
                nuevaCaracteristica.setCedulaOferente(oferente);
                service.guardarOferenteCaracteristica(nuevaCaracteristica);
            }
        return "redirect:/presentation/oferente/habilidades";
    }

    @GetMapping("/CV")
    public String formularioCV() {
        return "CVform";
    }

    @PostMapping("/cvUpload")
    public String procesarCV(@RequestParam("archivo") MultipartFile file, Authentication auth) {
        if (!file.isEmpty() && file.getContentType().equals("application/pdf")) {
            try{
                String folder = "uploads/";
                String fileName = auth.getName()+"_cv.pdf";//se usa el email para que sea unico
                Path path= Paths.get(folder + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                Oferente oferente = service.buscarOferentePorEmail(auth.getName());
                if (oferente != null) {
                    oferente.setCurriculoPath(fileName); // Guardamos solo el nombre o la ruta relativa
                    service.guardarOferente(oferente);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/presentation/oferente/show";
    }
}
