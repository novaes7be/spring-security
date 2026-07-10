package br.com.Novaes.spring_essentials_training.controller;

import br.com.Novaes.spring_essentials_training.dto.LoginRequestDto;
import br.com.Novaes.spring_essentials_training.dto.RegisterRequestDto;
import br.com.Novaes.spring_essentials_training.dto.TokenResponseDto;
import br.com.Novaes.spring_essentials_training.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequestDto requestDto) throws Exception{
        authenticationService.register(requestDto);
    }

    @PostMapping("/login")
    public TokenResponseDto register(@RequestBody @Valid LoginRequestDto loginRequestDto) throws Exception{
        return authenticationService.login(loginRequestDto);
    }
}
