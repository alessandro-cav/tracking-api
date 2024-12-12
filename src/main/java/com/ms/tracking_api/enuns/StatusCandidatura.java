package com.ms.tracking_api.enuns;

public enum StatusCandidatura {

     ACEITA("ACEITA"), RECUSADA("RECUSADA"), PENDENTE("PENDENTE");

     private String descricao;

     public static StatusCandidatura buscarRole(String sc) {
          StatusCandidatura candidatura = null;
          for (StatusCandidatura candidatura1 : StatusCandidatura.values()) {
               if (candidatura1.getDescricao().equals(sc.toUpperCase())) {
                    candidatura = candidatura1;
                    break;
               }
          }
          return candidatura;
     }

     private StatusCandidatura(String descricao) {
          this.descricao = descricao;
     }

     public String getDescricao() {
          return descricao;
     }
}
