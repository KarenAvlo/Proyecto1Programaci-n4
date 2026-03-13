package com.example.proyecto1programacion4;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "oferente")
public class Oferente {
@jakarta.persistence.Id
@jakarta.validation.constraints.Size(max = 20)
@jakarta.persistence.Column(name = "cedula", nullable = false, length = 20)
private java.lang.String cedula;

@jakarta.validation.constraints.Size(max = 50)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "nombre", nullable = false, length = 50)
private java.lang.String nombre;

@jakarta.validation.constraints.Size(max = 50)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "apellido", nullable = false, length = 50)
private java.lang.String apellido;

@jakarta.validation.constraints.Size(max = 50)
@jakarta.persistence.Column(name = "nacionalidad", length = 50)
private java.lang.String nacionalidad;

@jakarta.validation.constraints.Size(max = 20)
@jakarta.persistence.Column(name = "telefono", length = 20)
private java.lang.String telefono;

@jakarta.validation.constraints.Size(max = 200)
@jakarta.persistence.Column(name = "residencia", length = 200)
private java.lang.String residencia;

@jakarta.persistence.OneToOne(fetch = jakarta.persistence.FetchType.LAZY)
@jakarta.persistence.JoinColumn(name = "email")
private com.example.proyecto1programacion4.Usuario email;

@jakarta.validation.constraints.Size(max = 255)
@jakarta.persistence.Column(name = "curriculo_path")
private java.lang.String curriculoPath;



}