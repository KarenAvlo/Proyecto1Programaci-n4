package com.example.proyecto1programacion4;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "usuario")
public class Usuario {
@jakarta.persistence.Id
@jakarta.validation.constraints.Size(max = 100)
@jakarta.persistence.Column(name = "email", nullable = false, length = 100)
private java.lang.String email;

@jakarta.validation.constraints.Size(max = 100)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "clave", nullable = false, length = 100)
private java.lang.String clave;

@jakarta.validation.constraints.Size(max = 20)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "tipo", nullable = false, length = 20)
private java.lang.String tipo;

@org.hibernate.annotations.ColumnDefault("0")
@jakarta.persistence.Column(name = "estado")
private java.lang.Boolean estado;



}