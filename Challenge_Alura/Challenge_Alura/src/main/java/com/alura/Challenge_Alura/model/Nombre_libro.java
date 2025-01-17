package com.alura.Challenge_Alura.model;

import jakarta.persistence.*;

/**
 * Clase que representa un libro en la base de datos.
 * Mapeada a la tabla "Nombre_Libro".
 */
@Entity
@Table(name = "Nombre_Libro")
public class Nombre_libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private int anoPublicacion;

    // Relación inversa con la entidad Autor.
    @ManyToOne
    @JoinColumn(name = "autor_id") // Nombre de la columna en la tabla "Nombre_Libro"
    private Autor_Libro autor;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnoPublicacion() {
        return anoPublicacion;
    }

    public void setAnoPublicacion(int anoPublicacion) {
        this.anoPublicacion = anoPublicacion;
    }

    public Autor_Libro getAutor() {
        return autor;
    }

    public void setAutor(Autor_Libro autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Libro: " + titulo + "\n" +
                "Año de publicación: " + anoPublicacion + "\n" +
                "Autor: " + (autor != null ? autor.getNombre() : "Desconocido");
    }
}
