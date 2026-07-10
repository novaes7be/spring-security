package br.com.Novaes.spring_essentials_training.dto;

import java.math.BigDecimal;

public interface AvaliacoesFisicasProjection {

    Integer getIdAluno();
    String getNomeAluno();
    Integer getIdAvaliacao();
    BigDecimal getPeso();
    BigDecimal getAltura();
    BigDecimal getPorcentagemGorduraCorporal();
}
