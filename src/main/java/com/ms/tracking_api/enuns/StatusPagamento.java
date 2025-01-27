package com.ms.tracking_api.enuns;

public enum StatusPagamento {

     PENDENTE("PENDENTE"), PAGO("PAGO");

     private String descricao;

     public static StatusPagamento buscarStatusPamento(String statusPagamento) {
          StatusPagamento status = null;
          for (StatusPagamento pagamento : StatusPagamento.values()) {
               if (pagamento.getDescricao().equals(statusPagamento.toUpperCase())) {
                    status = pagamento;
                    break;
               }
          }
          return status;
     }

     private StatusPagamento(String descricao) {
          this.descricao = descricao;
     }

     public String getDescricao() {
          return descricao;
     }
}
