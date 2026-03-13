package com.example.proyecto1programacion4;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "caracteristica")
public class Caracteristica {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Integer id;

@jakarta.validation.constraints.Size(max = 100)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "nombre", nullable = false, length = 100)
private java.lang.String nombre;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
@jakarta.persistence.JoinColumn(name = "id_padre")
private com.example.proyecto1programacion4.Caracteristica idPadre;



}