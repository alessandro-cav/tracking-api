package com.ms.tracking_api.enuns;

public enum Genero {

    MASCULINO("MASCULINO"), FEMININO("FEMININO");

    private String descricao;

    public static Genero buscarGenero(String g) {
        Genero genero = null;
        for (Genero genero1 : Genero.values()) {
            if (genero1.getDescricao().equals(g.toUpperCase())) {
                genero = genero1;
                break;
            }
        }
        return genero;
    }

    private Genero(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


