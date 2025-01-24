package br.com.palm.matriculason.services;

import java.util.Locale;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.palm.matriculason.dtos.UsuariosDTO;
import br.com.palm.matriculason.entities.Administradores;
import br.com.palm.matriculason.entities.Alunos;
import br.com.palm.matriculason.entities.Usuarios;
import br.com.palm.matriculason.exceptions.ResourceNotFoundException;
import br.com.palm.matriculason.filters.UsuariosFilter;
import br.com.palm.matriculason.repositories.UsuariosRepository;
import br.com.palm.matriculason.services.specifications.UsuariosSpecification;

@Service
public class UsuariosService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UsuariosRepository usuariosRepository;

    public UsuariosDTO salvarAtualizacao(UsuariosDTO usuariosDTO) {
        Optional<Usuarios> usuarioExistenteOpt = usuariosRepository.findById(usuariosDTO.getId());
        if (usuarioExistenteOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        Usuarios usuarioExistente = usuarioExistenteOpt.get();
        if (usuariosDTO.getPessoa() instanceof Alunos) {
            Alunos alunos = (Alunos) usuariosDTO.getPessoa();
            Alunos alunoExistente = (Alunos) usuarioExistente.getPessoa();
            if (!alunoExistente.getCpf().equals(alunos.getCpf())) {
                throw new RuntimeException("CPF do aluno não pode ser alterado");
            }
            if (alunos.getMatricula() != null && !alunos.getMatricula().equals(alunoExistente.getMatricula())) {
                alunoExistente.setMatricula(alunos.getMatricula());
            }
            if (alunos.getNome() != null) {
                alunoExistente.setNome(alunos.getNome());
            }
            if (alunos.getEmail() != null) {
                alunoExistente.setEmail(alunos.getEmail());
            }
            usuarioExistente.setPessoa(alunoExistente);
        } else if (usuariosDTO.getPessoa() instanceof Administradores) {
            Administradores administradores = (Administradores) usuariosDTO.getPessoa();
            Administradores administradoresExistente = (Administradores) usuarioExistente.getPessoa();
            if(!administradoresExistente.getCpf().equals(administradores.getCpf())) {
                throw new RuntimeException("CPF do administrador não pode ser alterado");
            }
            if (administradores.getNome() != null) {
                administradoresExistente.setNome(administradores.getNome());
            }
            if (administradores.getEmail() != null) {
                administradoresExistente.setEmail(administradores.getEmail());
            }
            if (administradores.getCargo() != null) {
                administradoresExistente.setCargo(administradores.getCargo());
            }
            if (administradores.getDepartamento() != null) {
                administradoresExistente.setDepartamento(administradores.getDepartamento());
            }
            usuarioExistente.setPessoa(administradoresExistente);
        }
        if (usuariosDTO.getSenha() != null) {
            usuarioExistente.setSenha(usuariosDTO.getSenha());
        }
        usuarioExistente = usuariosRepository.save(usuarioExistente);
        return modelMapper.map(usuarioExistente, UsuariosDTO.class);
    }

    public UsuariosDTO cadastrarAluno(UsuariosDTO usuarioDto) {
        validarSenhasIguais(usuarioDto);
        Usuarios usuario = modelMapper.map(usuarioDto, Usuarios.class);
        usuario.setUsername(usuarioDto.getPessoa().getCpf());
        usuario.setStatus(true);
        return modelMapper.map(usuariosRepository.save(usuario), UsuariosDTO.class);
    }

    public UsuariosDTO cadastrarAdministrador(UsuariosDTO usuarioDto) {
        validarSenhasIguais(usuarioDto);
        Usuarios usuario = modelMapper.map(usuarioDto, Usuarios.class);
        usuario.setUsername(usuarioDto.getPessoa().getCpf());
        usuario.setStatus(true);
        return modelMapper.map(usuariosRepository.save(usuario), UsuariosDTO.class);
    }

    public void validarSenhasIguais(UsuariosDTO usuarioDto) {
        if (usuarioDto.getConfirmarSenha() != null && !usuarioDto.getConfirmarSenha().equals(usuarioDto.getSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }
    }

    public void remover(Long id) {
        this.usuariosRepository.deleteById(id);
    }

    public UsuariosDTO buscarPeloIdOrFail(Long id) {
        return this.usuariosRepository.findById(id)
                .map(u -> modelMapper.map(u, UsuariosDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage(
                                "modelo.naoEncontrado",
                                new String[]{"Usuários", id.toString()},
                                Locale.getDefault()
                        )
                ));
    }

    public Page<UsuariosDTO> buscar(UsuariosFilter usuariosFilter, Pageable pageable) {
        return usuariosRepository.findAll(UsuariosSpecification.filtrar(usuariosFilter), pageable)
                .map(u -> modelMapper.map(u, UsuariosDTO.class));
    }
}
