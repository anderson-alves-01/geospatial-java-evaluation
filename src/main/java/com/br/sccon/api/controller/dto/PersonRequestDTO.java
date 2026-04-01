package com.br.sccon.api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record PersonRequestDTO(
        Long id,

        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve ser no passado")
        LocalDate dataDeNascimento,

        @NotNull(message = "A data de admissão é obrigatória")
        @PastOrPresent(message = "A data de admissão não pode ser uma data futura")
        LocalDate dataDeAdmissao
) {}
