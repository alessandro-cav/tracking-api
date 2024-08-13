package com.ms.tracking_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_endereco")
@SequenceGenerator(name = "SQ_ENDERECO", allocationSize = 1, sequenceName = "SQ_ENDERECO")
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ENDERECO")
    private Long idEndereco;

    private String logradouro;

    private String numero;

    private String bairro;

    private String cep;

    private String cidade;

    private String estado;

    @OneToMany(mappedBy = "endereco")
    private List<FuncionarioEndereco> funcionarioEnderecos;

    @OneToMany(mappedBy = "endereco")
    private List<EventoEndereco> eventoEnderecos;

}
