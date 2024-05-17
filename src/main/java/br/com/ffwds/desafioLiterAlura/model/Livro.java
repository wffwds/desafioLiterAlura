package br.com.ffwds.desafioLiterAlura.model;

import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    private Long id;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Lingua lingua;
    private String copyright;
    private Integer download;
    @ManyToOne
    private Autor autor;

    public Livro() {
    }

    public Livro(DadosLivro livro){
        this.id = livro.id();
        this.titulo = livro.titulo();
        this.lingua = Lingua.fromString(livro.lingua().stream()
                .limit(1).collect(Collectors.joining()));
        this.copyright = livro.copyright();
        this.download = livro.download();
    }

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

    public Lingua getLingua() {
        return lingua;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Integer getDownload() {
        return download;
    }

    public void setDownload(Integer download) {
        this.download = download;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
