package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.featureflag.FeatureFlagDTO;
import org.redflag.dto.featureflag.get.GetLinkedFeatureFlagsRequest;
import org.redflag.dto.featureflag.get.GetLinkedFeatureFlagsResponse;
import org.redflag.dto.featureflag.get.RelationType;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.validator.PaginationParameterValidator;

import java.util.List;
import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class GetLinkedFeatureFlagsService extends BaseService<GetLinkedFeatureFlagsRequest, GetLinkedFeatureFlagsResponse> {
    private final FeatureFlagRepository featureFlagRepository;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateRequest(GetLinkedFeatureFlagsRequest request) {
        if (!PaginationParameterValidator.validateLimit(request.getLimit())) {
            throw ErrorCatalog.BAD_LIMIT.getException();
        }
        if (!PaginationParameterValidator.validateOffset(request.getOffset())) {
            throw ErrorCatalog.BAD_OFFSET.getException();
        }
        if (Objects.isNull(request.getRelation())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("relation");
        }
    }

    @Override
    protected void validateState(GetLinkedFeatureFlagsRequest request) {
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
    }

    @Override
    protected GetLinkedFeatureFlagsResponse execute(GetLinkedFeatureFlagsRequest request) {
        List<FeatureFlag> featureFlags =
                switch (request.getRelation()) {
                    case RelationType.SELF -> (featureFlagRepository
                            .findByOrganizationNodeId(
                                    request.getNodeId(),
                                    request.getLimit(),
                                    request.getOffset()
                            ));
                    case RelationType.ANCESTOR -> (featureFlagRepository
                            .findAllByAncestorsOrganizationNodes(
                                    request.getNodeId(),
                                    request.getLimit(),
                                    request.getOffset()
                            ));
                    case RelationType.DESCENDANT -> (featureFlagRepository
                            .findAllByDescendantsOrganizationNodes(
                                    request.getNodeId(),
                                    request.getLimit(),
                                    request.getOffset()
                            ));
                };

        if (featureFlags.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }

        return toGetLinkedFeatureFlagsResponse(request, featureFlags);
    }

    private GetLinkedFeatureFlagsResponse toGetLinkedFeatureFlagsResponse(GetLinkedFeatureFlagsRequest request, List<FeatureFlag> featureFlags) {
        return GetLinkedFeatureFlagsResponse.builder()
                .nodeId(request.getNodeId())
                .items(featureFlags.stream().map(this::toItem).toList())
                .relation(request.getRelation())
                .limit(request.getLimit())
                .offset(request.getOffset())
                .total(featureFlags.size())
                .build();
    }

    private GetLinkedFeatureFlagsResponse.Item toItem(FeatureFlag featureFlag) {
        OrganizationNode organizationNode = featureFlag.getOrganizationNode();
        return new GetLinkedFeatureFlagsResponse.Item(
                FeatureFlagDTO.builder()
                        .id(featureFlag.getId())
                        .nodeId(organizationNode.getId())
                        .name(featureFlag.getName())
                        .value(featureFlag.getValue())
                        .version(featureFlag.getVersion())
                        .build(),
                OrganizationNodeDTO.builder()
                        .id(organizationNode.getId())
                        .organizationId(organizationNode.getOrganization().getId())
                        .uuid(organizationNode.getUuid())
                        .path(organizationNode.getPath())
                        .name(organizationNode.getName())
                        .isService(organizationNode.getIsService())
                        .version(organizationNode.getVersion())
                        .build()
        );
    }
}
