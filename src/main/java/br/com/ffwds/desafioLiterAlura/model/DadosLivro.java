package br.com.ffwds.desafioLiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(@JsonAlias("id") Long id,
                         @JsonAlias("title") String titulo,
                         @JsonAlias("authors") List<DadosAutor> autores,
                         @JsonAlias("languages") List<String> lingua,
                         @JsonAlias("copyright") String copyright,
                         @JsonAlias("download_count") Integer download) {
}
