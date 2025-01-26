package com.ms.tracking_api.enuns;

public enum StatusUsuario {

     ATIVO("ATIVO"), INATIVO("INATIVO");

     private String descricao;

     public static StatusUsuario buscarStatus(String st) {
          StatusUsuario status = null;
          for (StatusUsuario status1 : StatusUsuario.values()) {
               if (status1.getDescricao().equals(st.toUpperCase())) {
                    status = status1;
                    break;
               }
          }
          return status;
     }

     private StatusUsuario(String descricao) {
          this.descricao = descricao;
     }

     public String getDescricao() {
          return descricao;
     }
}
