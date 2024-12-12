package com.ms.tracking_api.enuns;

public enum StatusVaga {

     ABERTA("ABERTA"), FECHADA("FECHADA");

     private String descricao;

     public static StatusVaga buscarRole(String vg) {
          StatusVaga vaga = null;
          for (StatusVaga vaga1 : StatusVaga.values()) {
               if (vaga1.getDescricao().equals(vg.toUpperCase())) {
                    vaga = vaga1;
                    break;
               }
          }
          return vaga;
     }

     private StatusVaga(String descricao) {
          this.descricao = descricao;
     }

     public String getDescricao() {
          return descricao;
     }
}
