package org.redflag.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.redflag.model.Organization;
import org.redflag.model.OrganizationNode;

import java.util.Optional;

@Repository
public interface OrganizationNodeRepository extends JpaRepository<OrganizationNode, Long> {
    @Query(value = "select exists (" +
            "select 1 from organization_node descendants join organization_node current_node on current_node.id = :nodeId " +
            "where current_node.path @> descendants.path AND current_node.path <> descendants.path)", nativeQuery = true)
    Boolean existsDescendants(Long nodeId);
    Boolean existsByOrganization_IdAndName(Long organizationId, String name);
    Boolean existsByOrganization_IdAndId(Long organizationId, Long nodeId);

    Optional<OrganizationNode> findByOrganization_IdAndId(Long organizationId, Long id);
    OrganizationNode findByName(String name);

    @Query(value = "with root as(" +
            "select path from organization_node where id = :nodeId)" +
            "delete from organization_node n using root r where n.path <@ r.path", nativeQuery = true)
    void deleteSubtree(Long nodeId);

}
