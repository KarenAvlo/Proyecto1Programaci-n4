package com.example.proyecto1programacion4.data;

import com.example.proyecto1programacion4.logic.Empresa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends CrudRepository<Empresa,String> {

}
