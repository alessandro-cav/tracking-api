package com.ms.tracking_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_upload_arquivo")
@SequenceGenerator(name = "SQ_UPLOAD", allocationSize = 1, sequenceName = "SQ_UPLOAD")
public class UploadArquivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_UPLOAD")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "funcionario")
    private Funcionario funcionario;

    @Lob
    private byte[] documento;
}
