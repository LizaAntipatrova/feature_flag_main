package org.redflag.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.redflag.model.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
