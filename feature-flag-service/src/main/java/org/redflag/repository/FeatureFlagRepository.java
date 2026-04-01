package org.redflag.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.redflag.model.FeatureFlag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    @Query("""
            select exists (
            select 1
            from FeatureFlag ff
            where ff.organizationNode.organization.id = :organizationId
              and ff.name = :name)
            """)
    boolean existsByOrganizationIdAndName(Long organizationId, String name);

    @Query("""
            select exists (
            select 1
            from FeatureFlag ff
            where ff.organizationNode.id = :nodeId
              and ff.id = :flagId)
            """)
    Boolean isFeatureFlagInNode(Long flagId, Long nodeId);
    @Query("""
            select exists (
            select 1
            from FeatureFlag ff
            where ff.organizationNode.id = :nodeId
              and ff.name = :name)
            """)
    Boolean isFeatureFlagInNodeByFlagName(String name, Long nodeId);

    Optional<FeatureFlag> findByNameAndOrganizationNode_Id(String name, Long nodeId);

    @Query(value = """
            select * from feature_flag ff
            where ff.organization_node_id = :nodeId
            limit :limit offset :offset
            """, nativeQuery = true)
    List<FeatureFlag> findByOrganizationNodeId(Long nodeId, Integer limit, Integer offset);

    @Query(value = """
            select ff.*
            from organization_node on2
            join feature_flag ff on on2.id = ff.organization_node_id
            where on2.path @> (select o.path
                                from organization_node o
                                where o.id = :nodeId)
            limit :limit offset :offset
            """, nativeQuery = true)
    List<FeatureFlag> findAllByAncestorsOrganizationNodes(Long nodeId, Integer limit, Integer offset);

    @Query(value = """
            select ff.*
            from organization_node on2
            join feature_flag ff on on2.id = ff.organization_node_id
            where on2.path <@ (select o.path
                                from organization_node o
                                where o.id = :nodeId)
            limit :limit offset :offset
            """, nativeQuery = true)
    List<FeatureFlag> findAllByDescendantsOrganizationNodes(Long nodeId, Integer limit, Integer offset);

    @Query(value = """
            select ff.*
            from organization_node on2
            join feature_flag ff on on2.id = ff.organization_node_id
            where on2.path @> (select o.path
                                from organization_node o
                                where o.uuid = :nodeUuid)
            """, nativeQuery = true)
    List<FeatureFlag> findAllAncestorsFeatureFlagsByOrganizationNodeUuid(UUID nodeUuid);
}
