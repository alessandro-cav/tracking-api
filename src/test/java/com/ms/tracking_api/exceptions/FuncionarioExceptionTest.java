package com.ms.tracking_api.exceptions;

import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.FuncionarioRequest;
import com.ms.tracking_api.entities.Funcionario;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.FuncionarioRepository;
import com.ms.tracking_api.services.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.modelmapper.ModelMapper;

public class FuncionarioExceptionTest {

    @InjectMocks
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioRepository repository;

    @Mock
    private Validator validator;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Teste para verificar o lançamento de exceção ao tentar salvar um Funcionário com CPF existente")
    public void naoPermitirFuncionarioComCpfExistente() {

        FuncionarioRequest funcionarioRequest = new FuncionarioRequest();
        funcionarioRequest.setCpf("12345678900");
        Funcionario funcionario = new Funcionario();
        funcionario.setCpf("12345678900");

        when(repository.findByCpf(funcionarioRequest.getCpf())).thenReturn(Optional.of(funcionario));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            funcionarioService.salvar(funcionarioRequest);
        });

        assertNotNull(funcionarioRequest.getCpf(), "O CPF não pode ser nulo");
        assertEquals(funcionarioRequest.getCpf() + " já cadastrado no sistema!", exception.getMessage());
        verify(repository, never()).save(any(Funcionario.class));

    }

    @Test
    @DisplayName("Teste para verificar o lançamento de exceção ao tentar salvar um Funcionário com CPF inválido")
    public void naoPermitirFuncionarioComCpfInvalido() {
        FuncionarioRequest funcionarioRequest = new FuncionarioRequest();
        funcionarioRequest.setCpf("12345");

        doThrow(new BadRequestException("CPF inválido" + funcionarioRequest.getCpf()))
                .when(validator).validaCPF(funcionarioRequest.getCpf());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            funcionarioService.salvar(funcionarioRequest);
        });
        assertNotNull(funcionarioRequest.getCpf(), "O CPF não pode ser nulo");
        assertEquals("CPF inválido: 12345", exception.getMessage());
        verify(repository, never()).save(any(Funcionario.class));

    }

    @Test
    @DisplayName("Teste para verificar o lançamento de exceção ao tentar salvar um Funcionário com EMAIL existente")
    public void naoPermitirFuncionarioComEmailExistente() {

        FuncionarioRequest funcionarioRequest = new FuncionarioRequest();
        funcionarioRequest.setEmail("root@gmail");
        Funcionario funcionario = new Funcionario();
        funcionario.setEmail("root@gmail");

        when(repository.findByEmail(funcionarioRequest.getEmail())).thenReturn(Optional.of(funcionario));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            funcionarioService.salvar(funcionarioRequest);
        });

        assertNotNull(funcionarioRequest.getEmail(), "O Email não pode ser nulo");
        assertEquals(funcionarioRequest.getEmail() + " já cadastrado no sistema!", exception.getMessage());
        verify(repository, never()).save(any(Funcionario.class));
    }

    @Test
    @DisplayName("Teste para verificar o lançamento de exceção ao tentar salvar um Funcionário com CPF inválido")
    public void naoPermitirFuncionarioComEmailInvalido() {
        FuncionarioRequest funcionarioRequest = new FuncionarioRequest();
        funcionarioRequest.setCpf("rotgmail.com");

        doThrow(new BadRequestException("Email inválido " + funcionarioRequest.getCpf()))
                .when(validator).validaCPF(funcionarioRequest.getCpf());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            funcionarioService.salvar(funcionarioRequest);
        });
        assertNotNull(funcionarioRequest.getEmail(), "O Email não pode ser nulo");
        assertEquals("Email inválido rotgmail.com", exception.getMessage());
        verify(repository, never()).save(any(Funcionario.class));
    }

    @Test
    @DisplayName("Teste para verificar o lançamento de exceção ao tentar buscar um Funcionário com id")
    public void execaoFuncionarioNaoEncontradoPeloId() {
        Long idFuncionario = 10L;
        when(repository.findById(idFuncionario)).thenReturn(Optional.empty());
        ObjetoNotFoundException exception = assertThrows(ObjetoNotFoundException.class, () -> {
            funcionarioService.buscarFuncionarioPeloId(idFuncionario);
        });
        assertEquals("Funcionario não encontrado!", exception.getMessage());
        verify(repository, times(1)).findById(idFuncionario);
    }

    @Test
    @DisplayName("Teste para lançar exceção ao excluir um Funcionário com ID inexistente")
    public void testDeleteFuncionarioNaoEncontrado() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        ObjetoNotFoundException exception = assertThrows(ObjetoNotFoundException.class, () -> {
            funcionarioService.delete(id);
        });
        assertEquals("Funcionario não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Teste para atualizar um Funcionário que não existe")
    public void testAtualizarFuncionarioNaoExistente() {
        Long id = 1L;
        FuncionarioRequest request = new FuncionarioRequest();
        request.setCpf("12345678900");
        request.setEmail("test@example.com");
        request.setGenero("MASCULINO");
        when(repository.findById(id)).thenReturn(Optional.empty());
        ObjetoNotFoundException exception = assertThrows(ObjetoNotFoundException.class, () -> {
            funcionarioService.atualizar(id, request);
        });
        verify(repository, times(1)).findById(id);
        assertEquals("Funcionario não encontrado!", exception.getMessage());
    }
}




