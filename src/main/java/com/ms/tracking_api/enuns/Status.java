package com.ms.tracking_api.enuns;

public enum Status {

     ATIVO("ATIVO"), INATIVO("INATIVO");

     private String descricao;

     public static Status buscarStatus(String st) {
          Status status = null;
          for (Status status1 : Status.values()) {
               if (status1.getDescricao().equals(st.toUpperCase())) {
                    status = status1;
                    break;
               }
          }
          return status;
     }

     private Status(String descricao) {
          this.descricao = descricao;
     }

     public String getDescricao() {
          return descricao;
     }
}
