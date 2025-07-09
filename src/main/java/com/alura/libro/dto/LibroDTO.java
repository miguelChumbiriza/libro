package com.alura.libro.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class LibroDTO {
    private String title;
    private List<AutorDTO> authors;
    private List<String> languages;

    @JsonAlias("download_count")
    private Integer downloadCount;

    public String getTitle() {
        return title;
    }

    public List<AutorDTO> getAuthors() {
        return authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }
}
