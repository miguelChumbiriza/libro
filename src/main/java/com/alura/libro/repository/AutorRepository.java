package com.alura.libro.repository;

import com.alura.libro.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreIgnoreCase(String nombre);



    List<Autor> findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqual(Integer año1, Integer año2);
}
