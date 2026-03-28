package org.redflag.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.redflag.entities.Session;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @EntityGraph(attributePaths = {"user", "user.roles"})
    Optional<Session> findById(Long id);

    void deleteByTtlBefore(LocalDateTime now);

    long countByUserIdAndTtlAfter(Long userId, LocalDateTime now);

    void deleteByUserId(Long userId);
}
