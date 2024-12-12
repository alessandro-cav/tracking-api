package com.ms.tracking_api.enuns;

public enum Role {

     ADMIN("ADMIN"), USUARIO("USUARIO");

     private String descricao;

     public static Role buscarRole(String rl) {
          Role role = null;
          for (Role role1 : Role.values()) {
               if (role1.getDescricao().equals(rl.toUpperCase())) {
                    role = role1;
                    break;
               }
          }
          return role;
     }

     private Role(String descricao) {
          this.descricao = descricao;
     }

     public String getDescricao() {
          return descricao;
     }
}
