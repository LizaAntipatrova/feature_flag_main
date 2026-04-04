package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.FeatureFlagDTO;
import org.redflag.dto.featureflag.FeatureFlagIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.FeatureFlagDTOMapper;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;

@Singleton
@RequiredArgsConstructor
public class GetFeatureFlagByIdService extends BaseService<FeatureFlagIdDTO, FeatureFlagDTO> {
    private final FeatureFlagRepository featureFlagRepository;
    private final FeatureFlagDTOMapper featureFlagDTOMapper;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;

    @Override
    protected void validateState(FeatureFlagIdDTO request) {
        authRightsToNodeValidator.checkIsAuthNodeInOrganization(request.getOrganizationId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
        linkedEntityValidator.checkIsFeatureFlagInNode(request.getFlagId(), request.getNodeId());
    }


    @Override
    protected FeatureFlagDTO execute(FeatureFlagIdDTO request) {
        FeatureFlag featureFlag = featureFlagRepository.findById(request.getFlagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        return featureFlagDTOMapper.toFeatureFlagDTO(featureFlag);
    }
}
