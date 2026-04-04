package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.FeatureFlagDTO;
import org.redflag.dto.featureflag.get.GetFeatureFlagByNameRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.FeatureFlagDTOMapper;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class GetFeatureFlagByNameService extends BaseService<GetFeatureFlagByNameRequest, FeatureFlagDTO> {
    private final FeatureFlagRepository featureFlagRepository;
    private final FeatureFlagDTOMapper featureFlagDTOMapper;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;

    @Override
    protected void validateRequest(GetFeatureFlagByNameRequest request) {
        String name = request.getFlagName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("flagName");
        }
    }

    @Override
    protected void validateState(GetFeatureFlagByNameRequest request) {
        authRightsToNodeValidator.checkIsAuthNodeInOrganization(request.getOrganizationId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
        linkedEntityValidator.checkIsFeatureFlagInNodeByName(request.getFlagName(), request.getNodeId());
    }

    @Override
    protected FeatureFlagDTO execute(GetFeatureFlagByNameRequest request) {
        FeatureFlag featureFlag = featureFlagRepository
                .findByNameAndOrganizationNode_Id(request.getFlagName(), request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        return featureFlagDTOMapper.toFeatureFlagDTO(featureFlag);
    }
}
