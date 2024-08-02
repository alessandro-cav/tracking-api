package com.ms.tracking_api.controllers;

import com.ms.tracking_api.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
