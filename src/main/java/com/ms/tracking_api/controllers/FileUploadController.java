package com.ms.tracking_api.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")

public class FileUploadController {

    @PostMapping("/file")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Por favor, selecione um arquivo para enviar.";
        }

        try {
            // Aqui pode-se implementar a lógica para salvar o arquivo
            byte[] bytes = file.getBytes();
            // Exemplo: Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            // Files.write(path, bytes);

            return "O arquivo " + file.getOriginalFilename() + " foi enviado com sucesso.";
        } catch (Exception e) {
            return "Ocorreu um erro ao enviar o arquivo: " + e.getMessage();
        }
    }
}

