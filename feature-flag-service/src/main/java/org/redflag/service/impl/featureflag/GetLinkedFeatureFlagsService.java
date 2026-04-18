package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.get.GetLinkedFeatureFlagsRequest;
import org.redflag.dto.featureflag.get.GetLinkedFeatureFlagsResponse;
import org.redflag.dto.featureflag.get.RelationType;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.FeatureFlagDTOMapper;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;
import org.redflag.service.validator.PaginationParameterValidator;

import java.util.List;
import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class GetLinkedFeatureFlagsService extends BaseService<GetLinkedFeatureFlagsRequest, GetLinkedFeatureFlagsResponse> {
    private final FeatureFlagRepository featureFlagRepository;
    private final PaginationParameterValidator paginationParameterValidator;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;
    private final FeatureFlagDTOMapper featureFlagDTOMapper;

    @Override
    protected void validateRequest(GetLinkedFeatureFlagsRequest request) {
        paginationParameterValidator.validateLimit(request.getLimit());
        paginationParameterValidator.validateOffset(request.getOffset());
        if (Objects.isNull(request.getRelation())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("relation");
        }
    }

    @Override
    protected void validateState(GetLinkedFeatureFlagsRequest request) {
        authRightsToNodeValidator.checkIsAuthNodeInOrganization(request.getOrganizationId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
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
                featureFlagDTOMapper.toFeatureFlagDTO(featureFlag),
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
