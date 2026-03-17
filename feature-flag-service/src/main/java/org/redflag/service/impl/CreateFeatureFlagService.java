package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.create.CreateFeatureFlagRequest;
import org.redflag.dto.featureflag.create.CreateFeatureFlagResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.AbstractService;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class CreateFeatureFlagService extends AbstractService<CreateFeatureFlagRequest, CreateFeatureFlagResponse> {
    private final FeatureFlagRepository featureFlagRepository;
    private final OrganizationNodeRepository organizationNodeRepository;

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
        if (featureFlagRepository.existsByOrganizationNode_Organization_IdAndName(request.getOrganizationId(), request.getName())) {
            throw ErrorCatalog.NOT_UNIQUE_FEATURE_FLAG_NAME_IN_ORGANIZATION.getException();
        }

    }

    @Override
    protected CreateFeatureFlagResponse logic(CreateFeatureFlagRequest request) {
        OrganizationNode organizationNode = organizationNodeRepository.findById(request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        FeatureFlag featureFlag = new FeatureFlag()
                .setName(request.getName())
                .setValue(request.getValue())
                .setOrganizationNode(organizationNode);
        featureFlagRepository.save(featureFlag);
        return new CreateFeatureFlagResponse(featureFlag.getId(),
                featureFlag.getOrganizationNode().getId(),
                featureFlag.getName(),
                featureFlag.getValue(),
                featureFlag.getVersion());
    }
}
