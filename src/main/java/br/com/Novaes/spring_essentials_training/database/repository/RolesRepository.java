package br.com.Novaes.spring_essentials_training.database.repository;

import br.com.Novaes.spring_essentials_training.database.model.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<RolesEntity, Integer> {

    Optional<RolesEntity> findByNome(String role);
}
