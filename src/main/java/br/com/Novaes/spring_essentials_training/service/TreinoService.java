package br.com.Novaes.spring_essentials_training.service;

import br.com.Novaes.spring_essentials_training.database.model.AlunosEntity;
import br.com.Novaes.spring_essentials_training.database.model.AvaliacoesFisicasEntity;
import br.com.Novaes.spring_essentials_training.database.model.ExerciciosEntity;
import br.com.Novaes.spring_essentials_training.database.model.TreinosEntity;
import br.com.Novaes.spring_essentials_training.database.repository.IAlunosRepository;
import br.com.Novaes.spring_essentials_training.database.repository.IExerciciosRepository;
import br.com.Novaes.spring_essentials_training.database.repository.ITreinosRepository;
import br.com.Novaes.spring_essentials_training.dto.TreinoDto;
import br.com.Novaes.spring_essentials_training.exception.BadRequestException;
import br.com.Novaes.spring_essentials_training.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TreinoService {

    private final IAlunosRepository alunosRepository;
    private final IExerciciosRepository exerciciosRepository;
    private final ITreinosRepository treinosRepository;

    public void criarTreino(TreinoDto treinoDto) throws NotFoundException, BadRequestException  {
        Set<ExerciciosEntity> exercicios = new HashSet<>();

        AlunosEntity aluno = alunosRepository.findById(treinoDto.getAlunoId())
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        TreinosEntity treino = treinosRepository.findByNomeAndId(treinoDto.getNome(), treinoDto.getAlunoId())
                .orElse(null);

        if (treino != null){
            throw new BadRequestException("Ja existe um treino com esse nome para esse aluno");
        }

        for (Integer exercicioId : treinoDto.getExercicioId()) {
            ExerciciosEntity exercicio = exerciciosRepository.findById(exercicioId)
                    .orElseThrow(() -> new NotFoundException(String.format("exercicio %s nao encontrado", exercicioId)));
            exercicios.add(exercicio);
        }

        treino = TreinosEntity.builder()
                .nome(treinoDto.getNome())
                .aluno(aluno)
                .exercicios(exercicios)
                .build();

        treinosRepository.save(treino);
    }
}
