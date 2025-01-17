package com.alura.Challenge_Alura.principal;

import com.alura.Challenge_Alura.dto.AutorDTO;
import com.alura.Challenge_Alura.dto.LibroDTO;
import com.alura.Challenge_Alura.dto.RespuestaLibrosDTO;
import com.alura.Challenge_Alura.model.Autor_Libro;
import com.alura.Challenge_Alura.model.Nombre_libro;
import com.alura.Challenge_Alura.service.AutorService;
import com.alura.Challenge_Alura.service.ConsumoAPI;
import com.alura.Challenge_Alura.service.ConvierteDatos;
import com.alura.Challenge_Alura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Clase principal que maneja el menú de interacción con el usuario.
 * Esta clase proporciona opciones para buscar libros, listar libros registrados, listar autores registrados,
 * listar autores vivos en un año específico y listar libros por idioma.
 */
@Component
public class Principal {

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConvierteDatos convierteDatos;

    private static final String BASE_URL = "https://gutendex.com/books/";

    /**
     * Muestra el menú principal y maneja las opciones seleccionadas por el usuario.
     */
    public void ejecutar() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo(scanner);
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnAno(scanner);
                    break;
                case 5:
                    listarLibrosPorIdioma(scanner);
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    /**
     * Muestra el menú de opciones.
     */
    private void mostrarMenu() {
        System.out.println("--- LITERALURA ---");
        System.out.println("1 - Buscar libro por título");
        System.out.println("2 - Listar libros registrados");
        System.out.println("3 - Listar autores registrados");
        System.out.println("4 - Listar autores vivos en un año");
        System.out.println("5 - Listar libros por idioma");
        System.out.println("0 - Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Permite buscar un libro por título.
     */
    private void buscarLibroPorTitulo(Scanner scanner) {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        try {
            String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            String json = consumoAPI.obtenerDatos(BASE_URL + "?search=" + encodedTitulo);
            RespuestaLibrosDTO respuestaLibrosDTO = convierteDatos.obtenerDatos(json, RespuestaLibrosDTO.class);
            List<LibroDTO> librosDTO = respuestaLibrosDTO.getLibros();
            if (librosDTO.isEmpty()) {
                System.out.println("Libro no encontrado en la API");
            } else {
                boolean libroRegistrado = false;
                for (LibroDTO libroDTO : librosDTO) {
                    if (libroDTO.getTitulo().equalsIgnoreCase(titulo)) {
                        Optional<Nombre_libro> libroExistente = libroService.obtenerLibroPorTitulo(titulo);
                        if (libroExistente.isPresent()) {
                            System.out.println("Detalle: Clave (titulo)=(" + titulo + ") ya existe");
                            System.out.println("No se puede registrar el mismo libro más de una vez");
                            libroRegistrado = true;
                            break;
                        } else {
                            Nombre_libro libro = new Nombre_libro();
                            libro.setTitulo(libroDTO.getTitulo());


                            // Buscar o crear el Autor
                            AutorDTO primerAutorDTO = libroDTO.getAutores().get(0);
                            Autor_Libro autor = autorService.obtenerAutorPorNombre(primerAutorDTO.getNombre())
                                    .orElseGet(() -> {
                                        Autor_Libro nuevoAutor = new Autor_Libro();
                                        nuevoAutor.setNombre(primerAutorDTO.getNombre());
                                        nuevoAutor.setAnoNacimiento(primerAutorDTO.getAnoNacimiento());
                                        nuevoAutor.setAnoFallecimiento(primerAutorDTO.getAnoFallecimiento());
                                        return autorService.crearAutor(nuevoAutor);
                                    });

                            // Asociar el Autor al Libro
                            libro.setAutor(autor);

                            // Guardar el libro en la base de datos
                            libroService.crearLibro(libro);
                            System.out.println("Libro registrado: " + libro.getTitulo());
                            mostrarDetallesLibro(libroDTO);
                            libroRegistrado = true;
                            break;
                        }
                    }
                }
                if (!libroRegistrado) {
                    System.out.println("No se encontró un libro exactamente con el título '" + titulo + "' en la API");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener datos de la API: " + e.getMessage());
        }
    }

    /**
     * Lista los libros registrados en la base de datos.
     */
    private void listarLibrosRegistrados() {
        libroService.listarLibros().forEach(libro -> {
            System.out.println("------LIBRO--------");
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
        });
    }

    /**
     * Lista los autores registrados en la base de datos.
     */
    private void listarAutoresRegistrados() {
        autorService.listarAutores().forEach(autor -> {
            System.out.println("-------AUTOR-------");
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Fecha de nacimiento: " + autor.getAnoNacimiento());
            System.out.println("Fecha de fallecimiento: " + (autor.getAnoFallecimiento() != null ? autor.getAnoFallecimiento() : "Desconocido"));
            String libros = autor.getLibros().stream()
                    .map(Nombre_libro::getTitulo)
                    .collect(Collectors.joining(", "));
            System.out.println("Libros: [ " + libros + " ]");
        });
    }

    /**
     * Lista los autores vivos en un año específico.
     */
    private void listarAutoresVivosEnAno(Scanner scanner) {
        System.out.print("Ingrese el año vivo de autor(es) que desea buscar: ");
        int ano = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea
        List<Autor_Libro> autoresVivos = autorService.listarAutoresVivosEnAno(ano);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + ano);
        } else {
            autoresVivos.forEach(autor -> {
                System.out.println("-------AUTOR-------");
                System.out.println("Autor: " + autor.getNombre());
                System.out.println("Fecha de nacimiento: " + autor.getAnoNacimiento());
                System.out.println("Fecha de fallecimiento: " + (autor.getAnoFallecimiento() != null ? autor.getAnoFallecimiento() : "Desconocido"));
                System.out.println("Libros: " + autor.getLibros().size());
            });
        }
    }

    /**
     * Lista los libros en un idioma específico.
     */
    private void listarLibrosPorIdioma(Scanner scanner) {
        System.out.println("Ingrese el idioma:");
        System.out.println("es");
        System.out.println("en");
        System.out.println("fr");
        System.out.println("pt");
        String idioma = scanner.nextLine();
        if ("es".equalsIgnoreCase(idioma) || "en".equalsIgnoreCase(idioma) || "fr".equalsIgnoreCase(idioma) || "pt".equalsIgnoreCase(idioma)) {
            libroService.listarLibrosPorIdioma(idioma).forEach(libro -> {
                System.out.println("------LIBRO--------");
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
            });
        } else {
            System.out.println("Idioma no válido. Intente de nuevo.");
        }
    }

    /**
     * Muestra los detalles de un libro DTO.
     *
     * @param libroDTO El objeto LibroDTO cuyos detalles se van a mostrar.
     */
    private void mostrarDetallesLibro(LibroDTO libroDTO) {
        System.out.println("------LIBRO--------");
        System.out.println("Título: " + libroDTO.getTitulo());
        System.out.println("Autor: " + (libroDTO.getAutores().isEmpty() ? "Desconocido" : libroDTO.getAutores().get(0).getNombre()));
        System.out.println("Idioma: " + libroDTO.getIdiomas().get(0));
        System.out.println("Número de descargas: " + libroDTO.getNumeroDescargas());
    }
}
