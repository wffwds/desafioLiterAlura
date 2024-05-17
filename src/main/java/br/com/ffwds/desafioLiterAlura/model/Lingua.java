package br.com.ffwds.desafioLiterAlura.model;

public enum Lingua {
    ES("es"),
    EN("en"),
    FR("fr"),
    PT("pt");

    private String idioma;

    Lingua(String idioma) {
        this.idioma = idioma;
    }

    public static Lingua fromString(String text){
        for (Lingua lingua : Lingua.values()){
            if(lingua.idioma.equalsIgnoreCase(text)){
                return lingua;
            }
        }
        throw new IllegalArgumentException("Nenhum idioma encontrado! " + text);
    }

    public String getIdioma(){
        return this.idioma;
    }
}
