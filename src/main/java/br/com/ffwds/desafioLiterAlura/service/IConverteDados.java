package br.com.ffwds.desafioLiterAlura.service;

public interface IConverteDados {
        <T> T obterDados(String json, Class<T> classe);
}
