package com.ms.tracking_api.enuns;

public enum TipoConta {

    CONTA_CORRENTE("CONTA_CORRENTE"), CONTA_POUPANCA("CONTA_POUPANCA");

    private String descricao;

    public static TipoConta buscarTipoConta(String tc) {
        TipoConta conta = null;
        for (TipoConta tipoConta : TipoConta.values()) {
            if (tipoConta.getDescricao().equals(tc.toUpperCase())) {
                conta = tipoConta;
                break;
            }
        }
        return conta;
    }

    private TipoConta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


