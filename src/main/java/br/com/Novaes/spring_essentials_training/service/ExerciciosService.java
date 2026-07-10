package br.com.Novaes.spring_essentials_training.service;

import br.com.Novaes.spring_essentials_training.controller.ExerciciosController;
import br.com.Novaes.spring_essentials_training.database.model.ExerciciosEntity;
import br.com.Novaes.spring_essentials_training.database.repository.IExerciciosRepository;
import br.com.Novaes.spring_essentials_training.dto.ExercicioDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciciosService  {

    private final IExerciciosRepository exerciciosRepository;

    public List<ExerciciosEntity> findAll() {
        return exerciciosRepository.findAll();
    }

    public void save(ExercicioDto exercicioDto) {

        ExerciciosEntity exercicio = ExerciciosEntity.builder()
                .nome(exercicioDto.getNome())
                .grupoMuscular(exercicioDto.getGrupoMuscular())
                .build();

        exerciciosRepository.save( exercicio);
    }

    public void deleteById(Integer id) {
        exerciciosRepository.deleteById(id);
    }
    public List<ExerciciosEntity> getExerciciosByGrupoMuscular(String grupoMuscular) {
        return exerciciosRepository.findAllByGrupoMuscular(grupoMuscular);
    }
}
