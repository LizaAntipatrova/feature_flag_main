package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.get.GetLinkedFeatureFlagsRequest;
import org.redflag.dto.featureflag.get.GetLinkedFeatureFlagsResponse;
import org.redflag.dto.featureflag.get.RelationType;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.AbstractService;

import java.util.List;
import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class GetLinkedFeatureFlagsService extends AbstractService<GetLinkedFeatureFlagsRequest, GetLinkedFeatureFlagsResponse> {
    private final FeatureFlagRepository featureFlagRepository;

    @Override
    protected void validateRequest(GetLinkedFeatureFlagsRequest request) {
        Integer limit = request.limit();
        Integer offset = request.offset();
        //TODO: вынести максимальный лимит и прописать в api
        if (Objects.isNull(limit) || limit <= 0 || limit > 100) {
            throw ErrorCatalog.BAD_LIMIT.getException();
        }
        if (Objects.isNull(offset) || offset < 0) {
            throw ErrorCatalog.BAD_OFFSET.getException();
        }
        if (Objects.isNull(request.relation())){
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("relation");
        }
    }

    @Override
    protected GetLinkedFeatureFlagsResponse logic(GetLinkedFeatureFlagsRequest request) {
        List<FeatureFlag> featureFlags =
        switch (request.relation()) {
            case RelationType.SELF -> (featureFlagRepository
                    .findByOrganizationNode_Id(request.nodeId(), request.limit(), request.offset()));
            case RelationType.ANCESTOR -> (featureFlagRepository
                    .findAllByAncestorsOrganizationNodes(request.nodeId(), request.limit(), request.offset()));
            case RelationType.DESCENDANT -> (featureFlagRepository
                    .findAllByDescendantsOrganizationNodes(request.nodeId(), request.limit(), request.offset()));
        };
        if (featureFlags.isEmpty()){
            throw ErrorCatalog.NO_DATA.getException();
        }
        return new GetLinkedFeatureFlagsResponse(request.nodeId(),
                request.relation(),
                featureFlags.stream().map(this::toItem).toList(),
                request.limit(),
                request.offset(),
                featureFlags.size());
    }

    private GetLinkedFeatureFlagsResponse.Item toItem(FeatureFlag featureFlag) {
        OrganizationNode  organizationNode = featureFlag.getOrganizationNode();
        return new GetLinkedFeatureFlagsResponse.Item(
                new GetLinkedFeatureFlagsResponse.Item.FeatureFlag(
                        featureFlag.getId(),
                        organizationNode.getId(),
                        featureFlag.getName(),
                        featureFlag.getValue(),
                        featureFlag.getVersion()
                ),
                new GetLinkedFeatureFlagsResponse.Item.BelongsToNode(
                        organizationNode.getId(),
                        organizationNode.getOrganization().getId(),
                        organizationNode.getUuid(),
                        organizationNode.getPath(),
                        organizationNode.getName(),
                        organizationNode.getIsService(),
                        organizationNode.getVersion()
                )
        );
    }
}
