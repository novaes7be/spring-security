package br.com.Novaes.spring_essentials_training.controller;

import br.com.Novaes.spring_essentials_training.database.model.ExerciciosEntity;
import br.com.Novaes.spring_essentials_training.database.repository.IExerciciosRepository;
import br.com.Novaes.spring_essentials_training.database.repository.ITreinosRepository;
import br.com.Novaes.spring_essentials_training.dto.ExercicioDto;
import br.com.Novaes.spring_essentials_training.dto.TreinoDto;
import br.com.Novaes.spring_essentials_training.exception.BadRequestException;
import br.com.Novaes.spring_essentials_training.exception.NotFoundException;
import br.com.Novaes.spring_essentials_training.service.ExerciciosService;
import br.com.Novaes.spring_essentials_training.service.TreinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/treinos")
@RequiredArgsConstructor
@Validated
public class TreinosController {

   private final TreinoService treinoService;

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public void criarTreino(@Valid @RequestBody TreinoDto treinoDto) throws NotFoundException, BadRequestException {
       treinoService.criarTreino(treinoDto);
   }




}
