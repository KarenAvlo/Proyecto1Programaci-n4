package com.example.proyecto1programacion4.logic;

import com.example.proyecto1programacion4.data.*;//todos los repositorios
import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private  AdministradorRepository administradorRepository;
    @Autowired
    private OferenteRepository oferenteRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

    //Administradores
    public Iterable<Usuario>  findAll() {
        return administradorRepository.findAll();
    }
    public Usuario findById(String id) {
        return administradorRepository.findById(id).orElse(null);
    }

    //Oferentes
    public Iterable<Oferente>  findAllOferente() {
        return oferenteRepository.findAll();
    }
    public Oferente findByIdOferente(String email) {
        return oferenteRepository.findById(email).orElse(null);
    }

    //Empresas
    public Iterable<Empresa> findAllEmpresas() {
        return empresaRepository.findAll();
    }
    public Empresa findByIdEmpresa(String email) {
        return empresaRepository.findById(email).orElse(null);
    }



}
