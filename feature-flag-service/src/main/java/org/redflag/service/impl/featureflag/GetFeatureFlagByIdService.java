package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.featureflag.FeatureFlagDTO;
import org.redflag.dto.featureflag.FeatureFlagIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.FeatureFlagDTOMapper;

@Singleton
@RequiredArgsConstructor
public class GetFeatureFlagByIdService extends BaseService<FeatureFlagIdDTO, FeatureFlagDTO> {
    private final FeatureFlagRepository featureFlagRepository;
    private final FeatureFlagDTOMapper featureFlagDTOMapper;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateState(FeatureFlagIdDTO request) {
        if (!organizationNodeRepository.isNodeInOrganization(
                authenticationProvider.getAuthenticationNodeUuid(),
                request.getOrganizationId())){
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
        if (!organizationNodeRepository.isNodeInOrganization(
                request.getNodeId(),
                request.getOrganizationId())){
            throw ErrorCatalog.NO_SUCH_NODE_IN_ORGANIZATION.getException();
        }
        if (!featureFlagRepository.isFeatureFlagInNode(
                request.getFlagId(),
                request.getNodeId())){
            throw ErrorCatalog.NO_SUCH_FLAG_IN_NODE.getException();
        }
    }

    @Override
    protected FeatureFlagDTO execute(FeatureFlagIdDTO request) {
        FeatureFlag featureFlag = featureFlagRepository.findById(request.getFlagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        return featureFlagDTOMapper.toFeatureFlagDTO(featureFlag);
    }
}
