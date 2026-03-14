package org.redflag.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.redflag.model.FeatureFlag;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {

}
