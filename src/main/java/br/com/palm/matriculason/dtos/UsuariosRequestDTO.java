package br.com.palm.matriculason.dtos;

import br.com.palm.matriculason.entities.Pessoas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class UsuariosRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Pessoas pessoa;

    @Size(min = 8, max = 12, message = "A senha deve ter entre 8 e 12 caracteres")
    private String senha;
}
