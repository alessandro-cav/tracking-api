package com.ms.tracking_api.enuns;

public enum TipoAtividade {

    ENTRADA("ENTRADA"), SAIDA("SAIDA");

    private String descricao;

    public static TipoAtividade buscarTipo(String rl) {
        TipoAtividade atividade = null;
        for (TipoAtividade tipoAtividade  : TipoAtividade.values()) {
            if (tipoAtividade.getDescricao().equals(rl.toUpperCase())) {
                atividade = tipoAtividade;
                break;
            }
        }
        return atividade;
    }

    private TipoAtividade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


