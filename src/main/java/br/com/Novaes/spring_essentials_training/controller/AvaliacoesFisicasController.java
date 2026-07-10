package br.com.Novaes.spring_essentials_training.controller;

import br.com.Novaes.spring_essentials_training.dto.AvaliacoesFisicasDto;
import br.com.Novaes.spring_essentials_training.dto.AvaliacoesFisicasProjection;
import br.com.Novaes.spring_essentials_training.exception.BadRequestException;
import br.com.Novaes.spring_essentials_training.exception.NotFoundException;
import br.com.Novaes.spring_essentials_training.service.AvaliacoesFisicasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/avaliacoes")
@RequiredArgsConstructor
@Validated
public class AvaliacoesFisicasController {

    private final AvaliacoesFisicasService avaliacoesFisicasService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarAvaliacaoFisica(@Valid @RequestBody AvaliacoesFisicasDto avaliacoesFisicasDto) throws NotFoundException, BadRequestException {
        avaliacoesFisicasService.criarAvaliacaoFisica(avaliacoesFisicasDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AvaliacoesFisicasProjection> getAllAvaliacoes() {
        return avaliacoesFisicasService.getAllAvaliacoes();
    }

    @GetMapping("/page/{page}/size/{size}")
    @ResponseStatus(HttpStatus.OK)
    public Page<AvaliacoesFisicasProjection> getAllAvaliacoesPageable(@PathVariable Integer page, @PathVariable Integer size) {
        return avaliacoesFisicasService.getAllAvaliacoesPageable(page, size)  ;
    }

}
