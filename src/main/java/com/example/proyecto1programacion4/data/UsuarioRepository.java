package com.example.proyecto1programacion4.data;

import com.example.proyecto1programacion4.logic.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // Al añadir esto, Spring "entiende" que debe buscar por la columna email
    Usuario findByEmail(String email);
    List<Usuario> findByTipoAndEstado(String tipo, Boolean estado);
}
