package br.com.ffwds.desafioLiterAlura.principal;

import br.com.ffwds.desafioLiterAlura.model.*;
import br.com.ffwds.desafioLiterAlura.repository.AutorRepository;
import br.com.ffwds.desafioLiterAlura.service.ConsumoAPI;
import br.com.ffwds.desafioLiterAlura.service.ConverteDados;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner entrada = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private String URL_BASE = "https://gutendex.com/books/";
    private AutorRepository repository;

    public Principal(AutorRepository repository){
        this.repository = repository;
    }

    public void exibeMenu() {
        var opcao = -1;
        var menu = """
                -------------
                Escolha uma opção:
                1 - Buscar livro por título
                2 - Listar livros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos em determinado ano
                5 - Listar livros por idioma
                6 - Gerar estatísticas
                7 - Top 10 livros
                8 - Buscar autor por nome
                9 - Listar autores com outras consultas
                0 - Sair
                """;
        while (opcao != 0) {
            System.out.println(menu);
            try {
                opcao = entrada.nextInt();
                switch (opcao) {
                    case 1:
                        buscarLivroPorTitulo();
                        break;
                    case 2:
                        listarLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivos();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 6:
                        gerarEstatisticas();
                        break;
                    case 7:
                        top10Livros();
                        break;
                    case 8:
                        buscarAutorPorNome();
                        break;
                    case 9:
                        listarAutoresComOutrasConsultas();
                        break;
                    case 0:
                        System.out.println("Obrigado por usar o LiterAlura");
                        System.out.println("Encerrando a aplicação...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida: " + e.getMessage());

            }
        }
    }

    public void buscarLivroPorTitulo(){
        System.out.println("Digite o Livro que deseja buscar:");
        var nome = entrada.nextLine();
        var json = consumoAPI.obterDados(URL_BASE + "?search=" + nome.replace(" ","+"));
        var dados = conversor.obterDados(json, Dados.class);
        Optional<DadosLivro> livroBuscado = dados.livros().stream()
                .findFirst();

        if(livroBuscado.isPresent()){
            System.out.println(
                    "\n----- Livro -----" +
                            "\nTítulo: " + livroBuscado.get().titulo() +
                            "\nAutor: " + livroBuscado.get().autores().stream()
                            .map(a -> a.nome()).limit(1).collect(Collectors.joining())+
                            "\nIdioma: " + livroBuscado.get().lingua().stream().collect(Collectors.joining()) +
                            "\nNúmero de downloads: " + livroBuscado.get().download() +
                            "\n-----------------\n"
            );

            try{
                List<Livro> livroEncontrado = livroBuscado.stream().map(a -> new Livro(a)).collect(Collectors.toList());
                Autor autorAPI = livroBuscado.stream().
                        flatMap(l -> l.autores().stream()
                                .map(a -> new Autor(a)))
                        .collect(Collectors.toList()).stream().findFirst().get();
                Optional<Autor> autorBD = repository.buscarAutorPorNome(livroBuscado.get().autores().stream()
                        .map(a -> a.nome())
                        .collect(Collectors.joining()));
                Optional<Livro> livroOptional = repository.buscarLivroPorNome(nome);
                if (livroOptional.isPresent()) {
                    System.out.println("O livro já está salvo.");
                } else {
                    Autor autor;
                    if (autorBD.isPresent()) {
                        autor = autorBD.get();
                        System.out.println("O autor já está salvo!");
                    } else {
                        autor = autorAPI;
                        repository.save(autor);
                    }
                    autor.setLivros(livroEncontrado);
                    repository.save(autor);
                }
            } catch(Exception e) {
                System.out.println("Advertência! " + e.getMessage());
            }
        } else {
            System.out.println("Livro não encontrado!");
        }
    }

    public void listarLivrosRegistrados(){
        List<Livro> livros = repository.buscarTodosOsLivros();
        livros.forEach(l -> System.out.println(
                "----- Livro -----" +
                        "\nTítulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNome() +
                        "\nIdioma: " + l.getLingua().getIdioma() +
                        "\nNúmero de downloads: " + l.getDownload() +
                        "\n-----------------\n"
        ));
    }

    public void listarAutoresRegistrados(){
        List<Autor> autores = repository.findAll();
        System.out.println();
        autores.forEach(l-> System.out.println(
                "Autor: " + l.getNome() +
                        "\nData de nascimento: " + l.getNascimento() +
                        "\nData de falecimento: " + l.getFalecimento() +
                        "\nLivros: " + l.getLivros().stream()
                        .map(t -> t.getTitulo()).collect(Collectors.toList()) + "\n"
        ));
    }

    public void listarAutoresVivos(){
        System.out.println("Digite o ano para buscar autor(es) vivos que desejas buscar:");
        try{
            var fecha = Integer.valueOf(entrada.nextLine());
            List<Autor> autores = repository.buscarAutoresVivos(fecha);
            if(!autores.isEmpty()){
                System.out.println();
                autores.forEach(a -> System.out.println(
                        "Autor: " + a.getNome() +
                                "\nData de nascimento: " + a.getNascimento() +
                                "\nData de falecimento: " + a.getFalecimento() +
                                "\nLivros: " + a.getLivros().stream()
                                .map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n"
                ));
            } else{
                System.out.println("Não há autores vivos para os dados informados!");
            }
        } catch(NumberFormatException e){
            System.out.println("Digite uma data válida " + e.getMessage());
        }
    }

    public void listarLivrosPorIdioma(){
        var menu = """
                Digite o idioma desejado:
                es - Espanhol
                en - Inglês
                fr - Francês
                pt - Português
                """;
        System.out.println(menu);
        var idioma = entrada.nextLine();
        if(idioma.equalsIgnoreCase("es") || idioma.equalsIgnoreCase("en") ||
                idioma.equalsIgnoreCase("fr") || idioma.equalsIgnoreCase("pt")){
            Lingua lingua = Lingua.fromString(idioma);
            List<Livro> livros = repository.buscarLivrosPorIdioma(lingua);
            if(livros.isEmpty()){
                System.out.println("Não há livros registrados com o idioma informado!");
            } else{
                System.out.println();
                livros.forEach(l -> System.out.println(
                        "----- Livro -----" +
                                "\nTitulo: " + l.getTitulo() +
                                "\nAutor: " + l.getAutor().getNome() +
                                "\nIdioma: " + l.getLingua().getIdioma() +
                                "\nNúmero de downloads: " + l.getDownload() +
                                "\n-----------------\n"
                ));
            }
        } else{
            System.out.println("Introduce un idioma en el formato valido");
        }
    }

    public void gerarEstatisticas(){
        var json = consumoAPI.obterDados(URL_BASE);
        var dados = conversor.obterDados(json, Dados.class);
        IntSummaryStatistics est = dados.livros().stream()
                .filter(l -> l.download() > 0)
                .collect(Collectors.summarizingInt(DadosLivro::download));
        Integer media = (int) est.getAverage();
        System.out.println("\n----- ESTATÍSTICAS -----");
        System.out.println("Quantidade média de downloads: " + media);
        System.out.println("Quantidade máxima de downloads: " + est.getMax());
        System.out.println("Quantidade mínima de downloads: " + est.getMin());
        System.out.println("Total de registros: " + est.getCount());
        System.out.println("-----------------\n");
    }

    public void top10Livros(){
        List<Livro> Livros = repository.top10Livros();
        System.out.println();
        Livros.forEach(l -> System.out.println(
                "----- Livro -----" +
                        "\nTitulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNome() +
                        "\nIdioma: " + l.getLingua().getIdioma() +
                        "\nNumero de descargas: " + l.getDownload() +
                        "\n-----------------\n"
        ));
    }

    public void buscarAutorPorNome(){
        System.out.println("Digite o nome do autor que desejas buscar: ");
        var nome = entrada.nextLine();
        Optional<Autor> autor = repository.buscarAutorPorNome(nome);
        if(autor.isPresent()){
            System.out.println(
                    "\nAutor: " + autor.get().getNome() +
                            "\nData de nascimento: " + autor.get().getNascimento() +
                            "\nData de falecimento: " + autor.get().getFalecimento() +
                            "\nLivros: " + autor.get().getLivros().stream()
                            .map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n"
            );
        } else {
            System.out.println("O autor pesquisado não existe!");
        }
    }

    public void listarAutoresComOutrasConsultas(){
        var menu = """
                \nDigite a opção que deseja listar os autores\n
                1 - Listar autor por Año de nacimiento
                2 - Listar autor por año de fallecimiento\n
                """;
        System.out.println(menu);
        try{
            var opcao = Integer.valueOf(entrada.nextLine());
            switch (opcao){
                case 1:
                    ListarAutoresPorNascimento();
                    break;
                case 2:
                    ListarAutoresPorFalecimento();
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida: " + e.getMessage());
        }
    }

    public void ListarAutoresPorNascimento(){
        System.out.println("Digite o ano de nascimento que desejas buscar: ");
        try{
            var nascimento = Integer.valueOf(entrada.nextLine());
            List<Autor> autores = repository.ListarAutoresPorNascimento(nascimento);
            if(autores.isEmpty()){
                System.out.println("Não existem autores com o ano de nascimento igual a " + nascimento);
            } else {
                System.out.println();
                autores.forEach(a -> System.out.println(
                        "Autor: " + a.getNome() +
                                "\nData de nascimento: " + a.getNascimento() +
                                "\nData de falecimento: " + a.getFalecimento() +
                                "\nLivros: " + a.getLivros().stream().map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n"
                ));
            }
        } catch (NumberFormatException e){
            System.out.println("Año no valido: " + e.getMessage());
        }
    }

    public void ListarAutoresPorFalecimento(){
        System.out.println("Informe o ano que desejas buscar:");
        try{
            var falecimento = Integer.valueOf(entrada.nextLine());
            List<Autor> autores = repository.ListarAutoresPorFalecimento(falecimento);
            if(autores.isEmpty()){
                System.out.println("Não existem autores falecidos no ano " + falecimento);
            } else {
                System.out.println();
                autores.forEach(a -> System.out.println(
                        "Autor: " + a.getNome() +
                                "\nData de falecimento: " + a.getNascimento() +
                                "\nData de falecimento: " + a.getFalecimento() +
                                "\nLivros: " + a.getLivros().stream().map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n"
                ));
            }
        } catch (NumberFormatException e) {
            System.out.println("Opcão inválida: " + e.getMessage());
        }
    }
}
