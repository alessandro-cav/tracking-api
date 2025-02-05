package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.ContaRequest;
import com.ms.tracking_api.dtos.responses.ContaResponse;
import com.ms.tracking_api.entities.Conta;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final UsuarioService usuarioService;

    private final ContaRepository contaRepository;

    private final ModelMapper modelMapper;


    @Transactional
    public ContaResponse salvarContaDoUsuario(Long idUsuario, ContaRequest request) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        this.contaRepository.findByNumero(request.getNumeroConta()).ifPresent(conta -> {
            throw new BadRequestException("Numero da conta: " + request.getNumeroConta() + " já cadastrado no sistema!");
        });
        this.contaRepository.findByChavePix(request.getChavePix()).ifPresent(conta -> {
            throw new BadRequestException("Chave pix: " + request.getChavePix() + " já cadastrado no sistema!");
        });

        Conta conta = this.modelMapper.map(request, Conta.class);
        conta.setUsuario(usuario);
        conta = this.contaRepository.save(conta);
        return this.modelMapper.map(conta, ContaResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> buscarTodasContaDoUsuario(Long idUsuario, PageRequest pageRequest) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        List<Conta> contas = this.contaRepository.findAllByUsuarioIdUsuario(usuario.getIdUsuario(), pageRequest);
        return contas.stream().map(conta -> {
            return this.modelMapper.map(conta, ContaResponse.class);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarContaDoUsuarioPeloId(Long idUsuario, Long idConta) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        Conta conta =  this.contaRepository.findByUsuarioIdUsuarioAndIdConta(usuario.getIdUsuario(), idConta)
                .orElseThrow(() -> new ObjetoNotFoundException("Conta não encontrada!"));
        return this.modelMapper.map(conta, ContaResponse.class);
    }

    @Transactional
    public void deleteContaDoUsuario(Long idUsuario, Long idConta) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        this.contaRepository.findByUsuarioIdUsuarioAndIdConta(usuario.getIdUsuario(), idConta).ifPresentOrElse(conta -> {
            try {
                this.contaRepository.delete(conta);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Conta não pode ser excluida, pois está vinculada em algum funcionario");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Conta não encontrado!");
        });
    }

    @Transactional
    public ContaResponse atualizarContaDoUsuario(Long idFuncionarios, Long idConta, ContaRequest request) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idFuncionarios);
        return  this.contaRepository.findByUsuarioIdUsuarioAndIdConta(usuario.getIdUsuario(), idConta).map(conta -> {
            if(request.getNumeroConta() != null) {
                if (!(conta.getNumero().equals(request.getNumeroConta()))) {
                    this.contaRepository.findByNumero(request.getNumeroConta()).ifPresent(c -> {
                        throw new BadRequestException("Numero da conta: " + request.getNumeroConta() + " já cadastrado no sistema!");
                    });
                }
            }
            if(request.getChavePix() != null) {
                if (!(conta.getChavePix().equals(request.getChavePix()))) {
                    this.contaRepository.findByChavePix(request.getChavePix()).ifPresent(c -> {
                        throw new BadRequestException("Chave pix: " + request.getChavePix() + " já cadastrado no sistema!");
                    });
                }
            }
            this.atualizarConta(conta, request);
            conta.setUsuario(usuario);
            conta = this.contaRepository.save(conta);
            return this.modelMapper.map(conta, ContaResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Conta não encontrada!"));
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> buscarContaDoUsuarioPorTipoChave(Long idUsuario, String tipo, PageRequest pageRequest) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        return this.contaRepository.findByUsuarioIdUsuarioAndTipoChave(usuario.getIdUsuario(), tipo, pageRequest).stream()
                .map(conta -> this.modelMapper.map(conta, ContaResponse.class))
                .collect(Collectors.toList());
    }

    private Conta atualizarConta(Conta conta, ContaRequest contaRequest) {
        conta.setTitularConta(contaRequest.getTitularConta() == null ? conta.getTitularConta() : contaRequest.getTitularConta());
        conta.setBanco(contaRequest.getBanco() == null ? conta.getBanco() : contaRequest.getBanco());
        conta.setAgencia(contaRequest.getAgencia() == null ? conta.getAgencia() : contaRequest.getAgencia());
        conta.setNumero(contaRequest.getNumeroConta() == null ? conta.getNumero() : contaRequest.getNumeroConta());
        conta.setTipoChave(contaRequest.getTipoChave() == null ? conta.getTipoChave() : contaRequest.getTipoChave());
        conta.setTipoConta(contaRequest.getTipoConta() == null ? conta.getTipoConta() : contaRequest.getTipoConta());
        conta.setChavePix(contaRequest.getChavePix() == null ? conta.getChavePix() : contaRequest.getChavePix());
        return conta;
    }
}
