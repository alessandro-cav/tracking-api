package com.ms.tracking_api.services;


import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.FiltroUsuarioRequest;
import com.ms.tracking_api.dtos.requests.UsuarioRequest;
import com.ms.tracking_api.dtos.responses.UserResponse;
import com.ms.tracking_api.dtos.responses.UsuarioResponse;
import com.ms.tracking_api.entities.Endereco;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.enuns.Status;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.repositories.UserRepository;
import com.ms.tracking_api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final ModelMapper modelMapper;

    private final UsuarioRepository repository;

    private final UserRepository userRepository;

    private final Validator validator;

    private final UserService userService;

    @Transactional
    public UsuarioResponse salvarMobile(UsuarioRequest usuarioRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.validator.validaCPF(usuarioRequest.getCpf());
        this.validator.validaEmail(usuarioRequest.getEmail());
        this.repository.findByCpf(usuarioRequest.getCpf()).ifPresent(usuario -> {
            throw new BadRequestException(usuarioRequest.getCpf() + " já cadastrado no sistema!");
        });
        if (!usuarioRequest.getEmail().equals(user.getEmail())) {
            this.repository.findByEmail(usuarioRequest.getEmail()).ifPresent(usuario -> {
                throw new BadRequestException(usuarioRequest.getEmail() + " já cadastrado no sistema!");
            });
        }
        Usuario usuario = this.modelMapper.map(usuarioRequest, Usuario.class);
        usuario.setIdUsuario(user.getId());
        Endereco endereco = getEndereco(usuario, usuarioRequest);
        usuario.setEndereco(endereco);
        usuario = this.repository.save(usuario);
        return gerarUsuarioResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarTodos(PageRequest pageRequest) {
        return this.repository.findAll(pageRequest).stream()
                .map(usuario -> gerarUsuarioResponse(usuario))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPeloId(Long id) {
        return this.repository.findById(id).map(usuario -> {
            return gerarUsuarioResponse(usuario);
        }).orElseThrow(() -> new BadRequestException("Usuário não encontrado!"));
    }

    @Transactional
    public void delete(Long id) {
        this.repository.findById(id).ifPresentOrElse(funcionario -> {
            this.repository.delete(funcionario);
        }, () -> {
            throw new BadRequestException("Usuário não encontrado!");
        });
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest usuarioRequest) {
        if(usuarioRequest.getCpf() != null) {
        this.validator.validaCPF(usuarioRequest.getCpf());
        }
        if(usuarioRequest.getEmail() != null) {
            this.validator.validaEmail(usuarioRequest.getEmail());
        }
        return this.repository.findById(id).map(usuario -> {
            if(usuarioRequest.getCpf() != null) {
                if (!(usuario.getCpf().equals(usuarioRequest.getCpf()))) {
                    this.repository.findByCpf(usuarioRequest.getCpf()).ifPresent(funcionario1 -> {
                        throw new BadRequestException(usuarioRequest.getCpf() + " já cadastrado no sistema!");
                    });
                }
            }
            if(usuarioRequest.getEmail() != null) {
                if (!(usuario.getEmail().equals(usuarioRequest.getEmail()))) {
                    this.repository.findByEmail(usuarioRequest.getEmail()).ifPresent(funcionario1 -> {
                        throw new BadRequestException(usuarioRequest.getEmail() + " já cadastrado no sistema!");
                    });
                }
            }
            usuario = this.atualizarUsuario(usuario, usuarioRequest);
            usuario.setEndereco(getEndereco(usuario, usuarioRequest));
            usuario = this.repository.save(usuario);
            return gerarUsuarioResponse(usuario);
        }).orElseThrow(() -> new BadRequestException("Usuário não encontrado!"));
    }

    @Transactional
    public void inativarUsuario(Long idUsuario) {
        User user = this.userService.findById(idUsuario);
        if (user.getStatus() == Status.INATIVO) {
            throw new BadRequestException("O usuário já está com o status INATIVO.");
        }
        user.setStatus(Status.INATIVO);
        this.userService.salvarNovoStatus(user);
    }


    @Transactional
    public void ativarUsuario(Long idUsuario) {
        User user = this.userService.findById(idUsuario);
        if (user.getStatus() == Status.ATIVO) {
            throw new BadRequestException("O usuário já está com o status ATIVO.");
        }
        user.setStatus(Status.ATIVO);
        this.userService.salvarNovoStatus(user);
    }

    public List<UserResponse> filtroUsuarioWeb(FiltroUsuarioRequest filtroUsuarioRequest,
                                            PageRequest pageRequest) {
        User user = this.modelMapper.map(filtroUsuarioRequest, User.class);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<User> example = Example.of(user, exampleMatcher);

        Page<User> users = this.userRepository.findAll(example, pageRequest);
        return users.stream().map(user1 -> {
            return this.modelMapper.map(user1, UserResponse.class);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Usuario buscarUsuarioPeloId(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new BadRequestException("Usuário não encontrado!"));
    }

    private Usuario atualizarUsuario(Usuario usuario, UsuarioRequest usuarioRequest) {
        usuario.setNome(usuarioRequest.getNome() == null ? usuario.getNome() : usuarioRequest.getNome());
        usuario.setCpf(usuarioRequest.getCpf() == null ? usuario.getCpf() : usuarioRequest.getCpf());
        usuario.setRg(usuarioRequest.getRg() == null ? usuario.getRg() : usuarioRequest.getRg());
        usuario.setTelefone(usuarioRequest.getTelefone() == null ? usuario.getTelefone() : usuarioRequest.getTelefone());
        usuario.setEmail(usuarioRequest.getEmail() == null ? usuario.getEmail() : usuarioRequest.getEmail());
        usuario.setDataNascimento(usuarioRequest.getDataNascimento() == null ? usuario.getDataNascimento() : usuarioRequest.getDataNascimento());
        usuario.setGenero(usuarioRequest.getGenero() == null ? usuario.getGenero() : usuarioRequest.getGenero());
        usuario.setValidadeASO(usuarioRequest.getValidadeASO() == null ? usuario.getValidadeASO() : usuarioRequest.getValidadeASO());
        usuario.setCurriculo(usuarioRequest.getCurriculo() == null ? usuario.getCurriculo() : usuarioRequest.getCurriculo());
        usuario.setAso(usuarioRequest.getAso() == null ? usuario.getAso() : usuarioRequest.getAso());
        usuario.setImagem(usuarioRequest.getImagem() == null ? usuario.getImagem() : usuarioRequest.getImagem());
        return usuario;
    }

    private Endereco getEndereco(Usuario usuario, UsuarioRequest usuarioRequest) {
        Endereco endereco = usuario.getEndereco() != null ? usuario.getEndereco() : new Endereco();
        endereco.setLogradouro(usuarioRequest.getLogradouro() == null ? endereco.getLogradouro() : usuarioRequest.getLogradouro());
        endereco.setNumero(usuarioRequest.getNumero() == null ? endereco.getNumero() : usuarioRequest.getNumero());
        endereco.setEstado(usuarioRequest.getEstado() == null ? endereco.getEstado() : usuarioRequest.getEstado());
        endereco.setCidade(usuarioRequest.getCidade() == null ? endereco.getCidade() : usuarioRequest.getCidade());
        endereco.setBairro(usuarioRequest.getBairro() == null ? endereco.getBairro() : usuarioRequest.getBairro());
        endereco.setCep(usuarioRequest.getCep() == null ? endereco.getCep() : usuarioRequest.getCep());
        return endereco;
    }

    private UsuarioResponse gerarUsuarioResponse(Usuario usuario) {
        UsuarioResponse usuarioResponse = this.modelMapper.map(usuario, UsuarioResponse.class);
        usuarioResponse.setGenero(usuario.getGenero());
        usuarioResponse.setLogradouro(usuario.getEndereco().getLogradouro());
        usuarioResponse.setNumero(usuario.getEndereco().getNumero());
        usuarioResponse.setEstado(usuario.getEndereco().getEstado());
        usuarioResponse.setCidade(usuario.getEndereco().getCidade());
        usuarioResponse.setBairro(usuario.getEndereco().getBairro());
        usuarioResponse.setCep(usuario.getEndereco().getCep());
        return usuarioResponse;
    }


}
