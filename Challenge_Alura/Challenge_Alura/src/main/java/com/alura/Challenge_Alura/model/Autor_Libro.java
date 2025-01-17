package com.alura.Challenge_Alura.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Clase que representa un autor en la base de datos.
 * Mapeada a la tabla "Autor_Libro".
 */
@Entity
@Table(name = "Autor_Libro")
public class Autor_Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private int anoNacimiento;

    private Integer anoFallecimiento;

    // Relación uno a muchos con la entidad Libro.
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nombre_libro> libros;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnoNacimiento() {
        return anoNacimiento;
    }

    public void setAnoNacimiento(int anoNacimiento) {
        this.anoNacimiento = anoNacimiento;
    }

    public Integer getAnoFallecimiento() {
        return anoFallecimiento;
    }

    public void setAnoFallecimiento(Integer anoFallecimiento) {
        this.anoFallecimiento = anoFallecimiento;
    }

    public List<Nombre_libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Nombre_libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "Autor: " + nombre + "\n" +
                "Fecha de nacimiento: " + anoNacimiento + "\n" +
                "Fecha de fallecimiento: " + (anoFallecimiento != null ? anoFallecimiento : "Desconocido") + "\n" +
                "Libros: " + (libros != null ? libros.size() : "Sin libros registrados");
    }
}
