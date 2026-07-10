package br.com.Novaes.spring_essentials_training.dto;

public record TokenResponseDto(String token, long expiresIn) {
}
