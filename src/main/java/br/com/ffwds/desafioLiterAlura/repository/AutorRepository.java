package br.com.ffwds.desafioLiterAlura.repository;

import br.com.ffwds.desafioLiterAlura.model.Autor;
import br.com.ffwds.desafioLiterAlura.model.Lingua;
import br.com.ffwds.desafioLiterAlura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository <Autor, Long>{

    @Query("SELECT a FROM Livro l JOIN l.autor a WHERE a.nome LIKE %:nome%")
    Optional<Autor> buscarAutorPorNome(String nome);

    @Query("SELECT l FROM Livro l JOIN l.autor a WHERE l.titulo LIKE %:nome%")
    Optional<Livro> buscarLivroPorNome(String nome);

    @Query("SELECT l FROM Autor a JOIN a.livros l")
    List<Livro> buscarTodosOsLivros();

    @Query("SELECT a FROM Autor a WHERE a.falecimento > :data")
    List<Autor> buscarAutoresVivos(Integer data);

    @Query("SELECT l FROM Autor a JOIN a.livros l WHERE l.lingua = :idioma ")
    List<Livro> buscarLivrosPorIdioma(Lingua idioma);

    @Query("SELECT l FROM Autor a JOIN a.livros l ORDER BY l.download DESC LIMIT 10")
    List<Livro> top10Livros();

    @Query("SELECT a FROM Autor a WHERE a.nascimento = :data")
    List<Autor> ListarAutoresPorNascimento(Integer data);

    @Query("SELECT a FROM Autor a WHERE a.falecimento = :data")
    List<Autor> ListarAutoresPorFalecimento(Integer data);
}
