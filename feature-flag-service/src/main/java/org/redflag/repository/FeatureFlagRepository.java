package org.redflag.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.redflag.model.FeatureFlag;

import java.util.Optional;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    @Query("""
    select exists (
    select 1
    from FeatureFlag ff
    where ff.organizationNode.organization.id = :organizationId
      and ff.name = :name)
    """)
    boolean existsByOrganizationNode_Organization_IdAndName(Long organizationId, String name);
    Optional<FeatureFlag> findByName(String name);

}
