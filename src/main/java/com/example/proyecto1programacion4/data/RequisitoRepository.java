package com.example.proyecto1programacion4.data;

import com.example.proyecto1programacion4.logic.Empresa;
import com.example.proyecto1programacion4.logic.Puesto;
import com.example.proyecto1programacion4.logic.Requisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequisitoRepository extends JpaRepository<Requisito, Integer> {

}
