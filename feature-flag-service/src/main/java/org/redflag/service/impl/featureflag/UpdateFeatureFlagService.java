package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.featureflag.FeatureFlagDTO;
import org.redflag.dto.featureflag.update.UpdateFeatureFlagRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.FeatureFlagDTOMapper;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class UpdateFeatureFlagService extends BaseService<UpdateFeatureFlagRequest, FeatureFlagDTO> {
    private final FeatureFlagRepository featureFlagRepository;
    private final FeatureFlagDTOMapper featureFlagDTOMapper;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateRequest(UpdateFeatureFlagRequest request) {
        if (Objects.isNull(request.getValue())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("value");
        }
        if (Objects.isNull(request.getVersion())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("version");
        }
    }

    @Override
    protected void validateState(UpdateFeatureFlagRequest request) {
        if (!organizationNodeRepository.existsChildNodeInParentNodeByChildIdAndParentUuid(
                request.getNodeId(),
                authenticationProvider.getAuthenticationNodeUuid()
        )){
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
        FeatureFlag featureFlag = featureFlagRepository
                .findById(request.getFeatureFlagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (!featureFlag.getVersion().equals(request.getVersion())) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }
    }

    @Override
    protected FeatureFlagDTO execute(UpdateFeatureFlagRequest request) {
        FeatureFlag featureFlag = featureFlagRepository.findById(request.getFeatureFlagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        if (!featureFlag.getVersion().equals(request.getVersion())) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }
        featureFlag.setValue(request.getValue());

        FeatureFlag newFeatureFlag;
        try {
            newFeatureFlag = featureFlagRepository.update(featureFlag);
        } catch (OptimisticLockException e) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }

        return featureFlagDTOMapper.toFeatureFlagDTO(newFeatureFlag);
    }
}
