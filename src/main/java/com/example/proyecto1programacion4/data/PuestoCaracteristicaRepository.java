package com.example.proyecto1programacion4.data;

import com.example.proyecto1programacion4.logic.Puesto;
import com.example.proyecto1programacion4.logic.PuestoCaracteristica;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoCaracteristicaRepository {
    List<PuestoCaracteristica> findByIdPuesto(Puesto puesto);
}
