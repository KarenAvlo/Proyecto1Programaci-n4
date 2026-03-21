package com.example.proyecto1programacion4.data;


import com.example.proyecto1programacion4.logic.PuestoCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuestoCaracteristicaRepository extends JpaRepository<PuestoCaracteristica, Integer> {
    // Aquí puedes agregar métodos de búsqueda personalizados si los necesitas después
}
