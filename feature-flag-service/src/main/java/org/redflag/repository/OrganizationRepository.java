package org.redflag.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import org.redflag.model.Organization;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Boolean existsByName(String name);

    @Query(value = """ 
            select * from organization o
            limit :limit offset :offset
            """,  nativeQuery = true)
    List<Organization>  findAll(Integer limit, Integer offset);

}
