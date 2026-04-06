package org.redflag.repositories;

import io.lettuce.core.dynamic.annotation.Param;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.jpa.repository.JpaRepository;
import jakarta.transaction.Transactional;
import org.redflag.entities.UiClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UiClientRepository extends JpaRepository<UiClient, Long> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<UiClient> findByLogin(String login);

    Optional<UUID> findUuidDepartamentByLogin(String login);

    Optional<UUID> findUuidDepartamentById(Long id);

    Boolean existsByLogin(String login);

    @EntityGraph(attributePaths = {"roles"})
    List<UiClient> findAll();

    @EntityGraph(attributePaths = {"roles"})
    List<UiClient> findByUuidDepartament(UUID uuidDepartament);

    @Transactional
    void deleteAllByUuidDepartamentIn(List<UUID> departaments);

    int countByUuidDepartamentIn(List<UUID> departaments);

}
