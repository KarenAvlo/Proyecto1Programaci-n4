package com.example.proyecto1programacion4.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "puesto")
public class Puesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_empresa")
    private Empresa emailEmpresa;

    @NotNull
    @Lob
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "salario_ofrecido", precision = 10, scale = 2)
    private BigDecimal salarioOfrecido;

    @Size(max = 10)
    @Column(name = "tipo_publicacion", length = 10)
    private String tipoPublicacion;

    @ColumnDefault("1")
    @Column(name = "activo")
    private Boolean activo;

//    @ColumnDefault("CURRENT_TIMESTAMP")
//    @Column(name = "fecha_publicacion")
    @Column(name = "fecha_publicacion", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Instant fechaPublicacion;


}