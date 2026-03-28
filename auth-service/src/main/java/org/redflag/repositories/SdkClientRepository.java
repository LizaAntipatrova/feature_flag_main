package org.redflag.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.redflag.entities.SdkClient;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SdkClientRepository extends JpaRepository<SdkClient, Long> {

    Optional<SdkClient> findByLogin(UUID login);
}


