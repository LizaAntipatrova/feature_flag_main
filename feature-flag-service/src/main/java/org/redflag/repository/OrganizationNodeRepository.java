package org.redflag.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import jakarta.annotation.Nullable;
import org.redflag.model.OrganizationNode;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationNodeRepository extends JpaRepository<OrganizationNode, Long> {
    @Query(value = """
            select exists (
                        select 1 from organization_node descendants join organization_node current_node on current_node.id = :nodeId 
                        where current_node.path @> descendants.path AND current_node.path <> descendants.path)
            """, nativeQuery = true)
    Boolean existsDescendants(Long nodeId);

    Boolean existsByOrganization_IdAndName(Long organizationId, String name);

    @Query(value = """
            select exists (
            select 1
            from organization_node o
            where o.organization_id = :organizationId
                       and text2ltree(cast(o.organization_id as text)) = o.path
            )
            """, nativeQuery = true)
    Boolean existsRootNodeInOrganization(Long organizationId);

    Optional<OrganizationNode> findByOrganization_IdAndId(Long organizationId, Long id);


    @Query(value = """
            with root as(
            select path from organization_node where id = :nodeId)
            delete from organization_node n using root r where n.path <@ r.path
            """, nativeQuery = true)
    void deleteSubtree(Long nodeId);

    @Query(value = """
            select descendants.*
            from organization_node descendants
            where descendants.organization_id = :organizationId
                        and (cast(:parentId as bigint) is null
                        or (descendants.path <@ ( 
                                    select path from  organization_node 
                                    where id = :parentId and organization_id = :organizationId)))
            order by descendants.path
            limit :limit offset :offset;
            """, nativeQuery = true)
    List<OrganizationNode> findAllByOrganizationIdAndParentId(Long organizationId, @Nullable Long parentId, Integer limit, Integer offset);

    @Query(value = """
            select descendants.*
            from organization_node descendants
            where descendants.organization_id = :organizationId
                        and (descendants.path <@ (
                                    select path from  organization_node 
                                    where id = :rootId and organization_id = :organizationId))
            order by descendants.path
            """, nativeQuery = true)
    List<OrganizationNode> findSubtreeByOrganizationIdAndParentId(Long organizationId, Long rootId);

    @Query(value = """
              with child as (
                select path, id
                from organization_node
                where organization_id = :organizationId and id = :nodeId
              )
            select ancestors.*
            from organization_node ancestors, child
            where ancestors.path @> child.path
            order by nlevel(ancestors.path), ancestors.path
            """, nativeQuery = true)
    List<OrganizationNode> findAllAncestorsById(Long organizationId, Long nodeId);

    @Query(value = """
            with root as (
                select path, id
                from organization_node
                where organization_id = :organizationId and id = :nodeId
              )
            select child.*
            from organization_node child, root
            where child.path <@ root.path
              and nlevel(child.path) = nlevel(root.path) + 1
            order by child.path
            """, nativeQuery = true)
    List<OrganizationNode> findAllChildrenById(Long organizationId, Long nodeId);

    @Query(value = """
              with root as (
                select path, id
                from organization_node
                where organization_id = :organizationId and id = :nodeId
              )
            select descedants.*
            from organization_node descedants, root
            where descedants.path <@ root.path
                and(cast(:depth as bigint)is null or nlevel(descedants.path) <= nlevel(root.path) + :depth)
            order by nlevel(descedants.path), descedants.path
            """, nativeQuery = true)
    List<OrganizationNode> findAllDescendantsByIdAndDepth(Long organizationId, Long nodeId, @Nullable Integer depth);

}
