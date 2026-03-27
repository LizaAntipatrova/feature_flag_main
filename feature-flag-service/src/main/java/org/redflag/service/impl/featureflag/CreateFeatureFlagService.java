package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.FeatureFlagDTO;
import org.redflag.dto.featureflag.create.CreateFeatureFlagRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.FeatureFlagDTOMapper;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class CreateFeatureFlagService extends BaseService<CreateFeatureFlagRequest, FeatureFlagDTO> {
    private final FeatureFlagRepository featureFlagRepository;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final FeatureFlagDTOMapper featureFlagDTOMapper;

    @Override
    protected void validateRequest(CreateFeatureFlagRequest request) {
        String name = request.getName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("name");
        }

        if (Objects.isNull(request.getValue())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("value");
        }
    }

    @Override
    protected void validateState(CreateFeatureFlagRequest request) {
        if (featureFlagRepository.existsByOrganizationIdAndName(request.getOrganizationId(), request.getName())) {
            throw ErrorCatalog.NOT_UNIQUE_FEATURE_FLAG_NAME_IN_ORGANIZATION.getException();
        }

    }

    @Override
    protected FeatureFlagDTO execute(CreateFeatureFlagRequest request) {
        OrganizationNode organizationNode = organizationNodeRepository.findById(request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        FeatureFlag featureFlag = new FeatureFlag()
                .setName(request.getName())
                .setValue(request.getValue())
                .setOrganizationNode(organizationNode);

        featureFlagRepository.save(featureFlag);

        return featureFlagDTOMapper.toFeatureFlagDTO(featureFlag);
    }
}
