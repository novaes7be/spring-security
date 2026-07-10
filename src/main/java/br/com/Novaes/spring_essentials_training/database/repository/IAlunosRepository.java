package br.com.Novaes.spring_essentials_training.database.repository;

import br.com.Novaes.spring_essentials_training.database.model.AlunosEntity;
import br.com.Novaes.spring_essentials_training.database.model.ExerciciosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;


public interface IAlunosRepository extends JpaRepository<AlunosEntity, Integer> {

   // usando Optional por que o atributo é unique.
    Optional<AlunosEntity> findByEmail(String email);
}
