package com.alura.libro;

import com.alura.libro.client.GutendexClient;
import com.alura.libro.dto.AutorDTO;
import com.alura.libro.dto.LibroDTO;
import com.alura.libro.repository.AutorRepository;
import com.alura.libro.repository.LibroRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
@Component
public class AplicacionConsola {
    private final Scanner teclado = new Scanner(System.in);
    private final LibroRepository libroRepo;
    private final AutorRepository autorRepo;
    private final GutendexClient cliente;

    public AplicacionConsola(LibroRepository libroRepo, AutorRepository autorRepo, GutendexClient cliente) {
        this.libroRepo = libroRepo;
        this.autorRepo = autorRepo;
        this.cliente = cliente;
    }

    public void iniciar() {
        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();
            opcion = Integer.parseInt(teclado.nextLine());

            switch (opcion) {
                case 1 -> buscarYRegistrarLibro();
                case 2 -> listarLibros();
                case 3 -> listarAutores();
                case 4 -> autoresVivosEnAnio();
                case 5 -> listarPorIdioma();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("""
                ---- CATÁLOGO LITERALURA ----
                1. Buscar libro por título y registrar
                2. Listar libros
                3. Listar autores
                4. Listar autores vivos en un año
                5. Listar libros por idioma (EN, ES, PT, FR)
                0. Salir
                """);
    }

    private void buscarYRegistrarLibro() {
        System.out.print("Ingresa el título del libro: ");
        String titulo = teclado.nextLine();

        if (libroRepo.findByTituloIgnoreCase(titulo).isPresent()) {
            System.out.println("Este libro ya está registrado.");
            return;
        }

        Optional<LibroDTO> resultado = cliente.buscarLibro(titulo);

        if (resultado.isEmpty()) {
            System.out.println("Libro no encontrado.");
            return;
        }

        LibroDTO dto = resultado.get();
        AutorDTO autorDTO = dto.getAuthors().isEmpty() ? new AutorDTO() : dto.getAuthors().get(0);

        // Buscar autor por nombre
        Optional<Autor> autorOptional = autorRepo.findByNombreIgnoreCase(autorDTO.getName());

        Autor autor;
        if (autorOptional.isPresent()) {
            autor = autorOptional.get(); // autor ya está manejado por JPA
        } else {
            autor = new Autor();
            autor.setNombre(autorDTO.getName());
            autor.setNacimiento(autorDTO.getBirthYear());
            autor.setFallecimiento(autorDTO.getDeathYear());
            autor = autorRepo.save(autor); // persistimos y lo volvemos a asignar por si acaso
        }

        // Crear el libro y asociar correctamente el autor ya gestionado
        Libro libro = new Libro();
        libro.setTitulo(dto.getTitle());
        libro.setIdioma(dto.getLanguages().get(0));
        libro.setNumeroDeDescargas(dto.getDownloadCount());
        libro.setAutor(autor); // 👈 Este autor está garantizado como "attached"

        libroRepo.save(libro);

        System.out.println("Libro registrado: " + libro.getTitulo());
    }



    private void listarLibros() {
        libroRepo.findAll().forEach(l -> {
            System.out.printf("📖 %s (%s) - %s [%d descargas]%n", l.getTitulo(), l.getIdioma(), l.getAutor().getNombre(), l.getNumeroDeDescargas());
        });
    }

    private void listarAutores() {
        autorRepo.findAll().forEach(a -> {
            System.out.printf("👤 %s (%d - %d)%n", a.getNombre(), a.getNacimiento(), a.getFallecimiento());
        });
    }

    private void autoresVivosEnAnio() {
        System.out.print("Ingrese el año a consultar: ");
        Integer año = Integer.parseInt(teclado.nextLine());
        List<Autor> autores = autorRepo.findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqual(año, año);
        autores.forEach(a -> System.out.println("👤 " + a.getNombre()));
    }

    private void listarPorIdioma() {
        System.out.print("Ingrese el código de idioma (ES, EN, PT, FR): ");
        String idioma = teclado.nextLine();
        libroRepo.findByIdiomaIgnoreCase(idioma)
                .forEach(l -> System.out.printf("📚 %s - %s%n", l.getTitulo(), l.getAutor().getNombre()));
    }
}
