package com.example.proyecto1programacion4;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "puesto")
public class Puesto {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Integer id;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
@jakarta.persistence.JoinColumn(name = "email_empresa")
private com.example.proyecto1programacion4.Empresa emailEmpresa;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.Lob
@jakarta.persistence.Column(name = "descripcion", nullable = false)
private java.lang.String descripcion;

@jakarta.persistence.Column(name = "salario_ofrecido", precision = 10, scale = 2)
private java.math.BigDecimal salarioOfrecido;

@jakarta.validation.constraints.Size(max = 10)
@jakarta.persistence.Column(name = "tipo_publicacion", length = 10)
private java.lang.String tipoPublicacion;

@org.hibernate.annotations.ColumnDefault("1")
@jakarta.persistence.Column(name = "activo")
private java.lang.Boolean activo;

@org.hibernate.annotations.ColumnDefault("CURRENT_TIMESTAMP")
@jakarta.persistence.Column(name = "fecha_publicacion")
private java.time.Instant fechaPublicacion;



}