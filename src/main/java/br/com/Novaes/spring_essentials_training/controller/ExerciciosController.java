package br.com.Novaes.spring_essentials_training.controller;

import br.com.Novaes.spring_essentials_training.database.model.ExerciciosEntity;
import br.com.Novaes.spring_essentials_training.database.repository.IExerciciosRepository;
import br.com.Novaes.spring_essentials_training.dto.ExercicioDto;
import br.com.Novaes.spring_essentials_training.service.ExerciciosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/exercicios")
@RequiredArgsConstructor
@Validated
public class ExerciciosController {

    private final ExerciciosService exerciciosService;
    private final IExerciciosRepository iExerciciosRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExerciciosEntity> findAll(){
        return exerciciosService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveExercise(@Valid @RequestBody ExercicioDto exercicioDto) {
        exerciciosService.save(exercicioDto);
    }

    @GetMapping("/grupos/{grupoMuscular}")
    @ResponseStatus(HttpStatus.OK)
    public List<ExerciciosEntity> getExerciseByGrupoMuscular(@PathVariable String grupoMuscular){
        return exerciciosService.getExerciciosByGrupoMuscular(grupoMuscular);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        exerciciosService.deleteById(id);
    }

}
