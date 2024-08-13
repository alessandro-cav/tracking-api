package com.ms.tracking_api.enuns;

public enum ChavePix {

    CPF("CPF"), EMAIL("EMAIL"), CNPJ("CNPJ"), TELEFONE("TELEFONE");

    private String descricao;

    public static ChavePix buscarChavePix(String cp) {
        ChavePix chavePix = null;
        for (ChavePix chavePix1 : ChavePix.values()) {
            if (chavePix1.getDescricao().equals(cp.toUpperCase())) {
                chavePix = chavePix1;
                break;
            }
        }
        return chavePix;
    }

    private ChavePix(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


