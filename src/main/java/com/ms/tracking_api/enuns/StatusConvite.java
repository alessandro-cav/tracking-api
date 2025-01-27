package com.ms.tracking_api.enuns;

public enum StatusConvite {

     PENDENTE("PENDENTE"), VALIDADO("VALIDADO");

     private String descricao;

     public static StatusConvite buscarStatusConvite(String cte) {
          StatusConvite status = null;
          for (StatusConvite convite : StatusConvite.values()) {
               if (convite.getDescricao().equals(cte.toUpperCase())) {
                    status = convite;
                    break;
               }
          }
          return status;
     }

     private StatusConvite(String descricao) {
          this.descricao = descricao;
     }

     public String getDescricao() {
          return descricao;
     }
}
