package com.example.proyecto1programacion4;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "empresa")
public class Empresa {
@jakarta.persistence.Id
@jakarta.validation.constraints.Size(max = 100)
@jakarta.persistence.Column(name = "email", nullable = false, length = 100)
private java.lang.String email;

@jakarta.persistence.MapsId
@jakarta.persistence.OneToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "email", nullable = false)
private com.example.proyecto1programacion4.Usuario usuario;

@jakarta.validation.constraints.Size(max = 100)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "nombre", nullable = false, length = 100)
private java.lang.String nombre;

@jakarta.validation.constraints.Size(max = 200)
@jakarta.persistence.Column(name = "localizacion", length = 200)
private java.lang.String localizacion;

@jakarta.validation.constraints.Size(max = 20)
@jakarta.persistence.Column(name = "telefono", length = 20)
private java.lang.String telefono;

@jakarta.persistence.Lob
@jakarta.persistence.Column(name = "descripcion")
private java.lang.String descripcion;



}