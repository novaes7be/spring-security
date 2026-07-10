package br.com.Novaes.spring_essentials_training.service;

import br.com.Novaes.spring_essentials_training.database.model.AlunosEntity;
import br.com.Novaes.spring_essentials_training.database.model.AvaliacoesFisicasEntity;
import br.com.Novaes.spring_essentials_training.database.repository.IAlunosRepository;
import br.com.Novaes.spring_essentials_training.database.repository.IAvaliacoesFisicasRepository;
import br.com.Novaes.spring_essentials_training.dto.AvaliacoesFisicasDto;
import br.com.Novaes.spring_essentials_training.dto.AvaliacoesFisicasProjection;
import br.com.Novaes.spring_essentials_training.dto.AvaliacoesFisicasProjection;
import br.com.Novaes.spring_essentials_training.exception.BadRequestException;
import br.com.Novaes.spring_essentials_training.exception.NotFoundException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AvaliacoesFisicasService {

   private final IAlunosRepository alunosRepository;
   private final IAvaliacoesFisicasRepository avaliacoesFisicasRepository;

   public void criarAvaliacaoFisica(AvaliacoesFisicasDto avaliacoesFisicasDto) throws NotFoundException, BadRequestException {
       AlunosEntity aluno =  alunosRepository.
               findById(avaliacoesFisicasDto.getAlunoId())
               .orElseThrow(() -> new NotFoundException("ALuno não encontrado"));

       AvaliacoesFisicasEntity avaliacoesFisicas = aluno.getAvaliacaoFisica();

       if (avaliacoesFisicas != null) {
           throw new BadRequestException("[ BAD REQUEST ] Avaliaçao fisica ja cadastrada para este aluno  [ BAD REQUEST ]");
       }

       avaliacoesFisicas = AvaliacoesFisicasEntity.builder()
               .peso(avaliacoesFisicasDto.getPeso())
               .altura(avaliacoesFisicasDto.getAltura())
               .porcentagemGorduraCorporal(avaliacoesFisicasDto.getPorcentagemGorduraCorporal())
               .build();

       aluno.setAvaliacaoFisica(avaliacoesFisicas);
       alunosRepository.save(aluno);
   }

   public List<AvaliacoesFisicasProjection> getAllAvaliacoes(){
       return avaliacoesFisicasRepository.getAllAvaliacoes();
   }

   public Page<AvaliacoesFisicasProjection> getAllAvaliacoesPageable(Integer page, Integer size) {
       return avaliacoesFisicasRepository.getAllAvaliacoesPage(PageRequest.of(page,size));
   }
}
