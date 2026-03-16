package com.example.proyecto1programacion4.presentation;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String home(){
        return "index"; //buscará el index.html
    }

    // Ruta para el botón "Buscar Puestos"
    @GetMapping("/puestos/buscar")
    public String buscarPuestos() {
        return "buscar_puestos"; // Crea este HTML en templates
    }

    // Ruta para el botón "Registro Empresa"
   /* @GetMapping("/registro/empresa")
    public String registroEmpresa() {
        return "form_empresa"; // Crea este HTML en templates
    }

    // Ruta para el botón "Registro Oferente"
    @GetMapping("/registro/oferente")
    public String registroOferente() {
        return "form_oferente"; // Crea este HTML en templates
    }*/
}
