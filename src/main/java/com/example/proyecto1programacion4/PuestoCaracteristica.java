package com.example.proyecto1programacion4;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "puesto_caracteristica")
public class PuestoCaracteristica {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Integer id;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
@jakarta.persistence.JoinColumn(name = "id_puesto")
private com.example.proyecto1programacion4.Puesto idPuesto;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
@jakarta.persistence.JoinColumn(name = "id_caracteristica")
private com.example.proyecto1programacion4.Caracteristica idCaracteristica;

@jakarta.persistence.Column(name = "nivel_deseado")
private java.lang.Integer nivelDeseado;



}