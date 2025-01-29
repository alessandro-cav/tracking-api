package com.ms.tracking_api.enuns;

public enum TipoChave {

    CPF("CPF"), EMAIL("EMAIL"), CNPJ("CNPJ"), CELULAR("CELULAR");

    private String descricao;

    public static TipoChave buscartipoChave(String cp) {
        TipoChave tipoChave = null;
        for (TipoChave tipoChave1 : TipoChave.values()) {
            if (tipoChave1.getDescricao().equals(cp.toUpperCase())) {
                tipoChave = tipoChave1;
                break;
            }
        }
        return tipoChave;
    }

    private TipoChave(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


