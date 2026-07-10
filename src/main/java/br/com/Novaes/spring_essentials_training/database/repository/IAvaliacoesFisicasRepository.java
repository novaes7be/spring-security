package br.com.Novaes.spring_essentials_training.database.repository;

import br.com.Novaes.spring_essentials_training.database.model.AvaliacoesFisicasEntity;
import br.com.Novaes.spring_essentials_training.database.model.ExerciciosEntity;
import br.com.Novaes.spring_essentials_training.dto.AvaliacoesFisicasProjection;
import br.com.Novaes.spring_essentials_training.dto.AvaliacoesFisicasProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import javax.swing.*;
import java.util.List;


public interface IAvaliacoesFisicasRepository extends JpaRepository<AvaliacoesFisicasEntity, Integer> {

    @NativeQuery(value = """
        SELECT a.id                            idAluno,
               a.nome                          nomeAluno,
               af.id                            idAvaliacao,
               af.peso                          peso,
               af.altura                        altura,
               af.porcentagem_gordura_corporal   porcentagemGorduraCorporal
         FROM alunos a
         INNER JOIN avaliacoes_fisicas af
         ON af.id = a.avaliaçao_fisica_id
        """)

    List<AvaliacoesFisicasProjection> getAllAvaliacoes();

    @NativeQuery(value = """
        SELECT a.id                            idAluno,
               a.nome                          nomeAluno,
               af.id                            idAvaliacao,
               af.peso                          peso,
               af.altura                        altura,
               af.porcentagem_gordura_corporal   porcentagemGorduraCorporal
         FROM alunos a
         INNER JOIN avaliacoes_fisicas af
         ON af.id = a.avaliaçao_fisica_id
        """,
    countQuery = """
        SELECT count(af.id)
        FROM alunos a
        INNER JOIN avaliacoes_fisicas af
        ON af.id = a.avaliaçao_fisica_id
        """)

    Page<AvaliacoesFisicasProjection> getAllAvaliacoesPage(Pageable pageable);
}
