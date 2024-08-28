package com.ms.tracking_api.controllers;

import com.ms.tracking_api.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor

@Tag(name = "Demo Controller", description = "APIs relacionadas ao demo controller")
@SecurityRequirement(name = "bearerAuth")
public class DemoController {

    @GetMapping
    @Operation(summary = "Demo acesso para autenticados", description = "Endpoint DemoController, para acessar depois de autenticado")
    public ResponseEntity<String> sayHello(Authentication authentication){

        Object autenticacao = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("autenticacao: " + autenticacao + "\n");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Principal: " + principal + "\n");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("Useraname " + userDetails.getUsername() + "\n");

        User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Usuario  " + usuario.toString());

        return ResponseEntity.ok("hello from segured endpoint");
    }


    @Operation(summary = "Upload de um arquivo", description = "Endpoint DemoController para upload de um arquivo por vez")
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Por favor, selecione um arquivo para enviar.";
        }

        try {
            // Aqui pode-se implementar a lógica para salvar o arquivo
            byte[] bytes = file.getBytes();

            // ESSAS DUAS LINHA ABAIXO INDICA AONDE SERA SALVO O ARQUIVO DE UPLOAD
            Path path = Paths.get("/Downloads/teste-upload/" + file.getOriginalFilename());
            Files.write(path, bytes);

           //CASO A LINHA A ACIMA NAO SEJA DECLARADA, O SPRING IRAR USAR ESSAS CONFIGURAÇÃO DE PASTA TEMPORARIA
           //spring.servlet.multipart.location=/tmp/uploads

            return "O arquivo " + file.getOriginalFilename() + " foi enviado com sucesso.";
        } catch (Exception e) {
            return "Ocorreu um erro ao enviar o arquivo: " + e.getMessage();
        }
    }


    @Operation(summary = "Upload de uma lista de arquivos", description = "Endpoint DemoController para upload de uma lista arquivos em uma  vez")
    @PostMapping(value = "/listaFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        if (files.isEmpty()) {
            return "Por favor, selecione ao menos um arquivo para enviar.";
        }

        StringBuilder responseMessage = new StringBuilder();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // Aqui pode-se implementar a lógica para salvar o arquivo na maquina
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get("/Downloads/teste-upload/" + file.getOriginalFilename());
                    Files.write(path, bytes);

                    responseMessage.append("O arquivo ").append(file.getOriginalFilename()).append(" foi enviado com sucesso.\n");
                } catch (Exception e) {
                    responseMessage.append("Ocorreu um erro ao enviar o arquivo ").append(file.getOriginalFilename()).append(": ").append(e.getMessage()).append("\n");
                }
            } else {
                responseMessage.append("O arquivo ").append(file.getOriginalFilename()).append(" está vazio.\n");
            }
        }

        return responseMessage.toString();
    }
}
