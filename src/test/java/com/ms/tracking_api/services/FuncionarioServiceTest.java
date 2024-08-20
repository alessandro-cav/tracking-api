package com.ms.tracking_api.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import com.ms.tracking_api.configs.validations.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.ms.tracking_api.dtos.requests.FuncionarioRequest;
import com.ms.tracking_api.dtos.responses.FuncionarioResponse;
import com.ms.tracking_api.entities.Funcionario;
import com.ms.tracking_api.enuns.Genero;
import com.ms.tracking_api.repositories.FuncionarioRepository;

public class FuncionarioServiceTest {

    @InjectMocks
    private FuncionarioService funcionarioService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FuncionarioRepository repository;

    @Mock
    private Validator validator;

    @Mock
    private Genero genero;

    @Mock
    private FuncionarioRequest funcionarioRequest;
    @Mock
    private Funcionario funcionario;
    @Mock
    private FuncionarioResponse funcionarioResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        funcionarioRequest = new FuncionarioRequest();
        funcionario = new Funcionario();
        funcionarioResponse = new FuncionarioResponse();
    }

    @Test
    public void testeSalvar() {
        doNothing().when(validator).validaCPF(funcionarioRequest.getCpf());
        doNothing().when(validator).validaEmail(funcionarioRequest.getEmail());

        try (MockedStatic<Genero> mockedGenero = mockStatic(Genero.class)) {
            mockedGenero.when(() -> Genero.buscarGenero(funcionarioRequest.getGenero())).thenReturn(Genero.MASCULINO);

            when(repository.findByCpf(funcionarioRequest.getCpf())).thenReturn(Optional.empty()); // cpf nao existe

            when(modelMapper.map(funcionarioRequest, Funcionario.class)).thenReturn(funcionario);
            when(repository.save(funcionario)).thenReturn(funcionario);
            when(modelMapper.map(funcionario, FuncionarioResponse.class)).thenReturn(funcionarioResponse);

            FuncionarioResponse resultado = funcionarioService.salvar(funcionarioRequest);

            assertNotNull(resultado);
            assertEquals(funcionarioRequest.getCpf(), resultado.getCpf());
            assertEquals(funcionarioRequest.getEmail(), resultado.getEmail());
            assertEquals(funcionarioRequest.getGenero(), resultado.getGenero());
            assertEquals(funcionarioRequest.getDataNascimento(), resultado.getDataNascimento());

            verify(validator, times(1)).validaCPF(funcionarioRequest.getCpf());
            verify(validator, times(1)).validaEmail(funcionarioRequest.getEmail());
            verify(repository, times(1)).findByCpf(funcionarioRequest.getCpf());
            verify(repository, times(1)).save(funcionario);
            verify(modelMapper, times(1)).map(funcionarioRequest, Funcionario.class);
            verify(modelMapper, times(1)).map(funcionario, FuncionarioResponse.class);

            mockedGenero.verify(() -> Genero.buscarGenero(funcionarioRequest.getGenero()), times(1));
        }
    }


    @Test
    @DisplayName("Teste para buscar todos os Funcionários")
    public void testBuscarTodos() {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Funcionario> paginaFuncionarios = new PageImpl<>(List.of(funcionario));

        when(repository.findAll(pageRequest)).thenReturn(paginaFuncionarios);
        when(modelMapper.map(funcionario, FuncionarioResponse.class)).thenReturn(funcionarioResponse);

        List<FuncionarioResponse> result = funcionarioService.buscarTodos(pageRequest);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(funcionarioResponse, result.get(0));
    }

    @Test
    @DisplayName("Teste para buscar Funcionário Response pelo ID")
    public void testBuscarPeloId() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(funcionario));
        when(modelMapper.map(funcionario, FuncionarioResponse.class)).thenReturn(funcionarioResponse);
        FuncionarioResponse result = funcionarioService.buscarPeloId(id);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Teste para buscar um Funcionário pelo ID ")
    public void testBuscarFuncionarioPeloIdComSucesso() {
        Long id = 1L;
        Funcionario funcionario = new Funcionario();
        funcionario.setIdFuncionario(id);
        when(repository.findById(id)).thenReturn(Optional.of(funcionario));
        Funcionario result = funcionarioService.buscarFuncionarioPeloId(id);
        assertNotNull(result);
        assertEquals(id, result.getIdFuncionario());
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Teste para excluir um Funcionário com sucesso")
    public void testDeleteFuncionario() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(funcionario));
        funcionarioService.delete(id);
        verify(repository, times(1)).delete(funcionario);
    }

    @Test
    @DisplayName("Teste para buscar Funcionários por nome")
    public void testBuscarPorNome() {
        String nome = "dani";
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(repository.findByNomeContainingIgnoreCase(nome, pageRequest)).thenReturn(List.of(funcionario));
        when(modelMapper.map(funcionario, FuncionarioResponse.class)).thenReturn(funcionarioResponse);
        List<FuncionarioResponse> result = funcionarioService.buscarPorNome(nome, pageRequest);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }


    @Test
    @DisplayName("Teste para atualizar um Funcionário com sucesso")
    public void testAtualizarFuncionarioComSucesso() {
        Long id = 1L;
        FuncionarioRequest request = new FuncionarioRequest();
        request.setCpf("12345678900");
        request.setEmail("newemail@example.com");
        request.setGenero("M");

        Funcionario funcionario = new Funcionario();
        funcionario.setCpf("98765432100");
        funcionario.setEmail("old@example.com");
        FuncionarioResponse response = new FuncionarioResponse();

        when(repository.findById(id)).thenReturn(Optional.of(funcionario));
        when(repository.findByCpf(request.getCpf())).thenReturn(Optional.empty());
        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(modelMapper.map(request, Funcionario.class)).thenReturn(funcionario);
        when(repository.save(funcionario)).thenReturn(funcionario);
        when(modelMapper.map(funcionario, FuncionarioResponse.class)).thenReturn(response);

        FuncionarioResponse result = funcionarioService.atualizar(id, request);

        assertNotNull(result);
        assertEquals(response, result);

        verify(validator, times(1)).validaCPF(request.getCpf());
        verify(validator, times(1)).validaEmail(request.getEmail());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).findByCpf(request.getCpf());
        verify(repository, times(1)).findByEmail(request.getEmail());
        verify(repository, times(1)).save(funcionario);
    }

    @Test
    @DisplayName("Teste para atualizar um Funcionário com CPF e Email diferentes")
    public void testAtualizarFuncionarioComCpfEEmailDiferentes() {
        Long id = 1L;
        FuncionarioRequest request = new FuncionarioRequest();
        request.setCpf("12345678900");
        request.setEmail("marcos@example.com");
        request.setGenero("M");

        Funcionario funcionario = new Funcionario();
        funcionario.setCpf("98765432100");
        funcionario.setEmail("other@example.com");

        FuncionarioResponse response = new FuncionarioResponse();

        when(repository.findById(id)).thenReturn(Optional.of(funcionario));
        when(repository.findByCpf(request.getCpf())).thenReturn(Optional.empty());
        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(modelMapper.map(request, Funcionario.class)).thenReturn(funcionario);
        when(repository.save(funcionario)).thenReturn(funcionario);
        when(modelMapper.map(funcionario, FuncionarioResponse.class)).thenReturn(response);

        FuncionarioResponse result = funcionarioService.atualizar(id, request);

        assertNotNull(result);
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).findByCpf(request.getCpf());
        verify(repository, times(1)).findByEmail(request.getEmail());
        verify(repository, times(1)).save(funcionario);
    }

    @Test
    @DisplayName("Teste para atualizar um Funcionário com CPF e Email iguais")
    public void testAtualizarFuncionarioComCpfEEmailIguais() {
        Long id = 1L;
        FuncionarioRequest request = new FuncionarioRequest();
        request.setCpf("12345678900");
        request.setEmail("test@example.com");
        request.setGenero("FEMININO");
        request.setNome("maria");

        Funcionario funcionario = new Funcionario();
        funcionario.setCpf("12345678900");
        funcionario.setEmail("test@example.com");
        funcionario.setNome("carla");


        FuncionarioResponse response = new FuncionarioResponse();

        when(repository.findById(id)).thenReturn(Optional.of(funcionario));
        when(repository.findByCpf(request.getCpf())).thenReturn(Optional.empty());
        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(modelMapper.map(request, Funcionario.class)).thenReturn(funcionario);
        when(repository.save(funcionario)).thenReturn(funcionario);
        when(modelMapper.map(funcionario, FuncionarioResponse.class)).thenReturn(response);

        FuncionarioResponse result = funcionarioService.atualizar(id, request);

        assertNotNull(result);
        assertEquals(request.getCpf(), funcionario.getCpf());
        assertEquals(request.getEmail(), funcionario.getEmail());

        verify(repository, times(1)).findById(id);
        verify(repository, never()).findByCpf(request.getCpf());
        verify(repository, never()).findByEmail(request.getEmail());
        verify(repository, times(1)).save(funcionario);
    }


}