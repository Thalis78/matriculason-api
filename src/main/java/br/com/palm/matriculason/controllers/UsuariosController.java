package br.com.palm.matriculason.controllers;

import br.com.palm.matriculason.dtos.UsuariosDTO;
import br.com.palm.matriculason.filters.UsuariosFilter;
import br.com.palm.matriculason.services.UsuariosService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("usuarios")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping
    public ResponseEntity<Page<UsuariosDTO>> pesquisar(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false, defaultValue = "0") Optional<Integer> pagina) {

        UsuariosFilter filtro = new UsuariosFilter();
        filtro.setUsername(username);
        filtro.setNome(nome);

        if (status != null) {
            filtro.setStatus(status.equalsIgnoreCase("ATIVO"));
        }

        Page<UsuariosDTO> usuarios = usuariosService.buscar(
                filtro,
                PageRequest.of(pagina.orElse(0) < 1 ? 0 : pagina.get(), 10)
        );

        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/alunos/novo-aluno")
    public ResponseEntity<UsuariosDTO> cadastrarAluno(@Valid @RequestBody UsuariosDTO usuariosDTO) {
        UsuariosDTO usuarioSalvo = usuariosService.cadastrarAluno(usuariosDTO);
        return ResponseEntity.ok(usuarioSalvo);
    }

    @PostMapping("/administradores/novo-administrador")
    public ResponseEntity<UsuariosDTO> cadastrarAdministrador(@Valid @RequestBody UsuariosDTO usuariosDTO) {
        UsuariosDTO usuarioSalvo = usuariosService.cadastrarAdministrador(usuariosDTO);
        return ResponseEntity.ok(usuarioSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuariosDTO> atualizar(@PathVariable("id") Long id, @Valid @RequestBody UsuariosDTO usuariosDTO) {
        usuariosDTO.setId(id);
        UsuariosDTO usuariosSalvo = usuariosService.salvarAtualizacao(usuariosDTO);
        return ResponseEntity.ok(usuariosSalvo);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
        usuariosService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuariosDTO> buscarPorId(@PathVariable("id") Long id) {
        UsuariosDTO usuario = usuariosService.buscarPeloIdOrFail(id);
        return ResponseEntity.ok(usuario);
    }
}
