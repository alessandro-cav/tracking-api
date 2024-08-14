package com.ms.tracking_api.enuns;

public enum Pix {

    CPF("CPF"), EMAIL("EMAIL"), CNPJ("CNPJ"), TELEFONE("TELEFONE");

    private String descricao;

    public static Pix buscarChavePix(String cp) {
        Pix pix = null;
        for (Pix pix1 : Pix.values()) {
            if (pix1.getDescricao().equals(cp.toUpperCase())) {
                pix = pix1;
                break;
            }
        }
        return pix;
    }

    private Pix(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


