package com.example.proyecto1programacion4.data;

import com.example.proyecto1programacion4.logic.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OferenteRepository extends JpaRepository<Oferente,String> {


    @Query("SELECT o FROM Oferente o WHERE o.estado = false")
    List<Oferente> findPendientes();

    Optional<Oferente> findByCedula(String cedula);

    Optional<Oferente> findByEmail(String email);
}