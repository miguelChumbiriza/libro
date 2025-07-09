package com.alura.libro.dto;

import java.util.List;

public class GutendexResponse {
    private List<LibroDTO> results;

    public List<LibroDTO> getResults() {
        return results;
    }

    public void setResults(List<LibroDTO> results) {
        this.results = results;
    }
}
