package br.com.Novaes.spring_essentials_training.service;

import br.com.Novaes.spring_essentials_training.database.model.AlunosEntity;
import br.com.Novaes.spring_essentials_training.database.model.AvaliacoesFisicasEntity;
import br.com.Novaes.spring_essentials_training.database.model.TreinosEntity;
import br.com.Novaes.spring_essentials_training.database.repository.IAlunosRepository;
import br.com.Novaes.spring_essentials_training.database.repository.IAvaliacoesFisicasRepository;
import br.com.Novaes.spring_essentials_training.database.repository.ITreinosRepository;
import br.com.Novaes.spring_essentials_training.dto.AlunoDto;
import br.com.Novaes.spring_essentials_training.exception.BadRequestException;
import br.com.Novaes.spring_essentials_training.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final IAlunosRepository alunosRepository;
    private final ITreinosRepository treinosRepository;
    private final IAvaliacoesFisicasRepository avaliacoesFisicasRepository;


    public void criarAluno(AlunoDto alunoDto) throws BadRequestException {
        AlunosEntity aluno = alunosRepository.findByEmail(alunoDto.getEmail())
                .orElse(null);

        if (aluno != null) {
            throw  new BadRequestException("aluno ja cadastrado com este nome");
        }
        alunosRepository.save(AlunosEntity.builder()
                .nome(alunoDto.getNome())
                .email(alunoDto.getEmail())
                .build());
    }

    public AvaliacoesFisicasEntity getAlunoAvaliacao(Integer alunoid) throws NotFoundException {
         AlunosEntity aluno = alunosRepository.findById(alunoid)
                 .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

         AvaliacoesFisicasEntity avaliacao = aluno.getAvaliacaoFisica();

         if (avaliacao == null) {
             throw  new NotFoundException("Avaliacao fisica nao encontrada para aluno");
         }

         return avaliacao;
    }
    @Transactional
    public void deletarAluno(Integer alunoId) throws NotFoundException {

        //transacao begin
        AlunosEntity aluno = alunosRepository.findById(alunoId)
                .orElseThrow(() -> new NotFoundException("Aluno nao encontrado"));

        //1- deletar treinos do aluno
        List<Integer> treinosAlunosIds = aluno.getTreinos().stream()
                .map(TreinosEntity::getId)
                .toList();

        treinosRepository.deleteAllById(treinosAlunosIds);

        //2- deletar o aluno
        alunosRepository.deleteById(alunoId);

        //3- deletar avaliacao fisica
        avaliacoesFisicasRepository.deleteById(aluno.getAvaliacaoFisica().getId());

        //transacao commit
    }
}
