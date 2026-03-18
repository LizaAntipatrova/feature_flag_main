package org.redflag.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.redflag.model.FeatureFlag;

import java.util.List;
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

    @Query(value = """
            select * from feature_flag ff 
            where ff.organization_node_id = :nodeId
            limit :limit offset :offset
            """, nativeQuery = true)
    List<FeatureFlag> findByOrganizationNode_Id(Long nodeId, Integer limit, Integer offset);

}
