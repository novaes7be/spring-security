package br.com.Novaes.spring_essentials_training.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class AlunoDto {

    @NotBlank
    private String nome;
    @NotBlank
    private String email;
}
