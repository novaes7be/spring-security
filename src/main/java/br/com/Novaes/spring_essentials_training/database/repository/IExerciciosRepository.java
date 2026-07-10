package br.com.Novaes.spring_essentials_training.database.repository;

import br.com.Novaes.spring_essentials_training.database.model.ExerciciosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface IExerciciosRepository extends JpaRepository<ExerciciosEntity, Integer> {

    List<ExerciciosEntity> findAllByGrupoMuscular(String grupoMuscular);

   // @Query(value = """
    // SELECT e
    // FROM ExerciciosEntity e
    // WHERE UPPER(e.grupoMuscular) = UPPER(:grupoMuscular )
    // """)

   // List<Exercici  osEntity> findAllBygrupoMuscularJpql(@Param("grupoMuscular") String grupoMuscular);
}
