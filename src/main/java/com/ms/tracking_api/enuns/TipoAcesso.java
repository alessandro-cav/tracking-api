package com.ms.tracking_api.enuns;

public enum TipoAcesso {

    ENTRADA("ENTRADA"), SAIDA("SAIDA");

    private String descricao;

    public static TipoAcesso buscarTipo(String rl) {
        TipoAcesso atividade = null;
        for (TipoAcesso tipoAcesso : TipoAcesso.values()) {
            if (tipoAcesso.getDescricao().equals(rl.toUpperCase())) {
                atividade = tipoAcesso;
                break;
            }
        }
        return atividade;
    }

    private TipoAcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


