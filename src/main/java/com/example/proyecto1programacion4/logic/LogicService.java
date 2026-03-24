package com.example.proyecto1programacion4.logic;

import com.example.proyecto1programacion4.data.*;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.awt.Color;

// Para el manejo de excepciones que pide el método
import java.net.MalformedURLException;

@Service
@Transactional
public class LogicService {

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; //para buscar email

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private RequisitoRepository requisitoRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private OferenteCaracteristicaRepository oferenteCaracteristicaRepository;

    @Autowired
    private PuestoCaracteristicaRepository puestoCaracteristicaRepository;

    private final Path root = Paths.get("./uploads");

    // --------------- INICIALIZACIÓN ----------------
    @PostConstruct
    public void init() {
        if (usuarioRepository.findByEmail("admin@bolsa.com") == null) {
            crearAdminInicial();
        }

        if (usuarioRepository.findByEmail("oferente@bolsa.com") == null) {
            crearOferenteInicial();
        }

        if (usuarioRepository.findByEmail("empresa@bolsa.com") == null) {
            crearEmpresaInicial();
        }

    }

    private void crearAdminInicial() {
        Usuario admin = new Usuario();
        admin.setEmail("admin@bolsa.com");
        admin.setClave(passwordEncoder.encode("123456"));
        admin.setTipo("ADMIN");
        admin.setEstado(true);
        usuarioRepository.save(admin);
    }

    private void crearOferenteInicial() {
        // Creamos el Usuario (Padre)
//        Usuario u = new Usuario();
        Oferente o = new Oferente();
        o.setEmail("oferente@bolsa.com");
        o.setClave(passwordEncoder.encode("123456"));
        o.setTipo("OFERENTE");
        o.setEstado(true); // Lo ponemos como aprobado para poder loguearnos ya
        //usuarioRepository.save(u);

        // Creamos el Oferente (Hijo)

        //o.setEmail(o.getEmail());
        o.setNombre("Juan");
        o.setApellido("Pérez");
        o.setCedula("1-1111-1111");
        o.setTelefono("8888-8888");
        // o.setEstado(true); // Si tu entidad Oferente tiene un campo estado, descomenta esto
        oferenteRepository.save(o);
    }

    private void crearEmpresaInicial() {
        // Creamos la Empresa (que según tu lógica hereda o tiene los campos de Usuario)
        Empresa e = new Empresa();
        e.setEmail("empresa@bolsa.com");
        e.setClave(passwordEncoder.encode("123456"));
        e.setTipo("EMPRESA");
        e.setEstado(true); // Aprobada para pruebas

        e.setNombre("Tech Solutions");
        e.setLocalizacion("San José, CR");
        e.setTelefono("7777-7777");
        e.setDescripcion("Empresa líder en tecnología");
        // Al usar herencia o @MapsId, solo necesitas guardar el repositorio de la Empresa
        empresaRepository.save(e);
    }

    // --------------- LÓGICA DE USUARIOS / ADMIN ----------------
    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public void aprobarUsuario(String email) {
        Usuario u = usuarioRepository.findById(email).orElse(null);
        if (u != null) {
            u.setEstado(true);
            usuarioRepository.save(u);
        }
    }


// --------------- LÓGICA DE OFERENTES ----------------
    public void registrarOferente(Oferente o) throws Exception {
        if (usuarioRepository.existsById(o.getEmail())) {
            throw new Exception("El correo ya existe.");
        }

        // Configuramos los campos heredados de Usuario dentro del objeto Oferente
        o.setTipo("OFERENTE");
        o.setEstado(false);
        o.setClave(passwordEncoder.encode(o.getClave()));

        // Al guardar el oferente, JPA guarda automáticamente en la tabla 'usuario'
        // y en la tabla 'oferente' por la herencia JOINED.
        oferenteRepository.save(o);
    }

    // --------------- LÓGICA DE EMPRESAS ----------------
    public void registrarEmpresa(Empresa e) throws Exception {
        if (usuarioRepository.existsById(e.getEmail())) {
            throw new Exception("El correo ya existe.");
        }
        e.setEmail(e.getEmail());
        e.setTipo("EMPRESA");
        e.setEstado(false);
        e.setClave(passwordEncoder.encode(e.getClave()));
        empresaRepository.save(e);
    }

    // --------------- DASHBOARD FILTROS ----------------
    public List<Usuario> findEmpresasPendientes() {
        return usuarioRepository.findByTipoAndEstado("EMPRESA", false);
    }

    public List<Usuario> findOferentesPendientes() {
        return usuarioRepository.findByTipoAndEstado("OFERENTE", false);
    }

    //================PUESTOS=============================
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
    }

    // Busca la empresa para asignarla al puesto
    public Empresa buscarEmpresaPorEmail(String email) {
        return empresaRepository.findById(email).orElse(null);
    }

    // Guarda el puesto y retorna el objeto con su ID generado
    public Puesto guardarPuesto(Puesto puesto) {
        return puestoRepository.save(puesto);
    }

    // Guarda cada característica vinculada al puesto
    public void guardarRequisito(Puesto puesto, String nombre, Integer nivel) {
        Requisito req = new Requisito();
        req.setPuesto(puesto);
        req.setNombre(nombre);
        req.setNivel(nivel);
        requisitoRepository.save(req);
    }

    // Este método lo usa tu EmpresaController.showPuestos
    public List<Puesto> findPuestosPorEmpresa(String email) {
        Empresa e = empresaRepository.findById(email).orElse(null);
        return puestoRepository.findByEmailEmpresa(e);
    }

    // En LogicService.java
    public List<Caracteristica> listarCaracteristicasAdmin() {

        return caracteristicaRepository.findAll();
    }

    //================HABILIDADESPUESTOS=============================//

    public Oferente buscarOferentePorEmail(String email) {
        return oferenteRepository.findByEmail(email).orElse(null);
    }

    public List<OferenteCaracteristica> listarCaracteristicasOferente(String cedula) {
        return oferenteCaracteristicaRepository.findByCedulaOferente(cedula);
    }

    public List<Caracteristica> listaCaracteristicasPadre(){
        return caracteristicaRepository.findByIdPadreIsNull();
    }

    public void guardarOferenteCaracteristica(OferenteCaracteristica oc) {
        oferenteCaracteristicaRepository.save(oc);
    }

    //================CV=============================//

    public void guardarOferente(Oferente oferente) {
        oferenteRepository.save(oferente);
    }

    public Resource obtenerArchivoCV(String cedula) throws Exception {
        Oferente oferente = oferenteRepository.findByCedula(cedula)
                .orElseThrow(() -> new Exception("Oferente no encontrado con cédula: " + cedula));

        String nombreArchivo = oferente.getCurriculoPath();

        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            throw new Exception("El oferente no tiene un CV registrado.");
        }
        Path archivoPath = root.resolve(nombreArchivo).normalize();
        Resource recurso = new UrlResource(archivoPath.toUri());

        if (recurso.exists() || recurso.isReadable()) {
            return recurso;
        } else {
            throw new Exception("No se pudo leer el archivo: " + nombreArchivo);
        }
    }

    //================ADMIN-REPORTES-PDF================//
    public byte[] generarReporteEmpresasPDF(int mes) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();

        // Título Principal
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLUE);
        Paragraph titulo = new Paragraph("REPORTE MENSUAL DE PUESTOS - MES " + mes, fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" ")); // Espacio

        List<Puesto> puestos = puestoRepository.findByMes(mes);

        for (Puesto p : puestos) {
            document.add(new Paragraph("Empresa: " + p.getEmailEmpresa().getNombre(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph("Puesto: " + p.getDescripcion() + " | Salario: $" + p.getSalarioOfrecido()));

            // Tabla de Requisitos
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Cabecera de tabla
            table.addCell(new PdfPCell(new Phrase("Requisito Técnico", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
            table.addCell(new PdfPCell(new Phrase("Nivel Deseado", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));

            List<PuestoCaracteristica> pcList = puestoCaracteristicaRepository.findByIdPuesto(p);
            for (PuestoCaracteristica pc : pcList) {
                table.addCell(pc.getIdCaracteristica().getNombre());
                table.addCell(String.valueOf(pc.getNivelDeseado()));
            }
            document.add(table);
            document.add(new Paragraph("----------------------------------------------------------------------------------"));
        }

        document.close();
        return out.toByteArray();
    }

    // --- GENERAR REPORTE 2: OFERENTES Y ESTADÍSTICAS ---
    public byte[] generarReporteOferentesPDF() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("REPORTE GENERAL DE OFERENTES Y HABILIDADES", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph(" "));

        List<Oferente> oferentes = oferenteRepository.findAll();

        // Variables para estadísticas
        int totalHabilidadesGlobal = 0;
        double sumaNivelesGlobal = 0;

        for (Oferente o : oferentes) {
            document.add(new Paragraph("Candidato: " + o.getNombre() + " " + o.getApellido() + " (Céd: " + o.getCedula() + ")"));

            List<OferenteCaracteristica> habilidades = oferenteCaracteristicaRepository.findByCedulaOferente(o.getCedula());

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(80);
            for (OferenteCaracteristica oc : habilidades) {
                table.addCell(oc.getIdCaracteristica().getNombre());
                table.addCell("Nivel: " + oc.getNivel());

                // Acumulamos para las estadísticas
                totalHabilidadesGlobal++;
                sumaNivelesGlobal += oc.getNivel();
            }
            document.add(table);
            document.add(new Paragraph(" "));
        }

        // Calculos de las Estadisticas
        document.add(new Paragraph(" "));
        Paragraph estTitulo = new Paragraph("RESUMEN ESTADÍSTICO", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.DARK_GRAY));
        document.add(estTitulo);

        int totalCandidatos = oferentes.size();
        document.add(new Paragraph("Total de Candidatos Registrados: " + totalCandidatos));

        if (totalCandidatos > 0) {
            double promHabilidades = (double) totalHabilidadesGlobal / totalCandidatos;
            double promNivel = totalHabilidadesGlobal > 0 ? sumaNivelesGlobal / totalHabilidadesGlobal : 0;

            document.add(new Paragraph("Promedio de habilidades por candidato: " + String.format("%.2f", promHabilidades)));
            document.add(new Paragraph("Nivel técnico promedio general: " + String.format("%.2f", promNivel)));
        }

        document.close();
        return out.toByteArray();
    }
}
