package com.example.proyecto1programacion4.presentation;


import com.example.proyecto1programacion4.logic.ReporteService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.io.IOException;

@Controller
@RequestMapping("/admin/reportes")
public class ReporteController {
    @Autowired
    private ReporteService reporteService;

    @GetMapping("/pagina")
    public String mostrarPaginaReportes(){
        return "admin_reportes";
    }

    @GetMapping("PDFPuestos")
    public void descargarPuestos(@RequestParam("mes") int mes, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=reporte_puestos.pdf");
        reporteService.generarReporteEmpresas(response, mes);
    }

    @GetMapping("/PDFOferentes")
    public void descargarOferentes(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=reporte_oferentes.pdf");
        reporteService.generarReporteOferentes(response);
    }
}
