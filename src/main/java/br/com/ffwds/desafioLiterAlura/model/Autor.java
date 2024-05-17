package br.com.ffwds.desafioLiterAlura.model;

import jakarta.persistence.*;

import java.util.List;

public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nome;
    private Integer nascimento;
    private Integer falecimento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Livro> livros;

    public Autor(){

    }
    public Autor (DadosAutor dadosAutor){
        this.nome = dadosAutor.nome();
        this.nascimento = dadosAutor.nascimento();
        this.falecimento = dadosAutor.falecimento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNascimento() {
        return nascimento;
    }

    public void setNascimento(Integer nascimento) {
        this.nascimento = nascimento;
    }

    public Integer getFalecimento() {
        return falecimento;
    }

    public void setFalecimento(Integer falecimento) {
        this.falecimento = falecimento;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    @Override
    public String toString() {
        return
                ", nome = " + nome + '\'' +
                ", nascimento = " + nascimento +
                ", falecimento = " + falecimento;
    }
}
