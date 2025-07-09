package com.alura.libro.client;

import com.alura.libro.dto.GutendexResponse;
import com.alura.libro.dto.LibroDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
@Component
public class GutendexClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://gutendex.com/books/?search=";

    public Optional<LibroDTO> buscarLibro(String titulo) {
        String url = BASE_URL + titulo.replace(" ", "+");
        ResponseEntity<GutendexResponse> response = restTemplate.getForEntity(url, GutendexResponse.class);
        return response.getBody().getResults().stream().findFirst();
    }
}
