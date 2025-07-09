package com.alura.libro;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibroApplication implements CommandLineRunner {
	private final AplicacionConsola aplicacion;

	public LibroApplication(AplicacionConsola aplicacion){
		this.aplicacion = aplicacion;
	}

	public static void main(String[] args) {

		SpringApplication.run(LibroApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		aplicacion.iniciar();
	}
}
