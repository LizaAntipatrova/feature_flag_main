package org.redflag.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.redflag.model.OrganizationNode;

@Repository
public interface OrganizationNodeRepository extends JpaRepository<OrganizationNode, Long> {
    OrganizationNode findByName(String name);

}
