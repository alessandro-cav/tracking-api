package com.ms.tracking_api.enuns;

public enum StatusEvento {

    ABERTO("ABERTO"), FECHADO("FECHADO");

    private String descricao;

    public static StatusEvento buscarStatusEvento(String rl) {
        StatusEvento atividade = null;
        for (StatusEvento tipoAcesso : StatusEvento.values()) {
            if (tipoAcesso.getDescricao().equals(rl.toUpperCase())) {
                atividade = tipoAcesso;
                break;
            }
        }
        return atividade;
    }

    private StatusEvento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


