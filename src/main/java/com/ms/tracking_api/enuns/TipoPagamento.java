package com.ms.tracking_api.enuns;

public enum TipoPagamento {

    PIX("PIX"), DINHEIRO("DINHEIRO");

    private String descricao;

    public static TipoPagamento buscarTipoPagamento(String tipoPagamento) {
        TipoPagamento status = null;
        for (TipoPagamento pagamento : TipoPagamento.values()) {
            if (pagamento.getDescricao().equals(tipoPagamento.toUpperCase())) {
                status = pagamento;
                break;
            }
        }
        return status;
    }

    private TipoPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


