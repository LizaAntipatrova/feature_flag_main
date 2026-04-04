package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.get.GetOrganizationNodesRequest;
import org.redflag.dto.node.get.GetOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;
import org.redflag.service.validator.PaginationParameterValidator;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetOrganizationNodesService extends BaseService<GetOrganizationNodesRequest, GetOrganizationNodesResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;
    private final PaginationParameterValidator paginationParameterValidator;

    @Override
    protected void validateRequest(GetOrganizationNodesRequest request) {
        paginationParameterValidator.validateLimit(request.getLimit());
        paginationParameterValidator.validateOffset(request.getOffset());
    }

    @Override
    protected void validateState(GetOrganizationNodesRequest request) {
        authRightsToNodeValidator.checkIsAuthNodeInOrganization(request.getOrganizationId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
    }

    @Override
    protected GetOrganizationNodesResponse execute(GetOrganizationNodesRequest request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository.findAllByOrganizationIdAndParentId(
                request.getOrganizationId(),
                request.getNodeId(),
                request.getLimit(),
                request.getOffset());

        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return toGetOrganizationNodesResponse(request, organizationNodes);
    }

    private GetOrganizationNodesResponse toGetOrganizationNodesResponse(GetOrganizationNodesRequest request, List<OrganizationNode> organizationNodes) {
        return GetOrganizationNodesResponse.builder()
                .items(organizationNodes.stream()
                        .map(organizationNodeDTOMapper::toOrganizationNodeDTO)
                        .toList())
                .limit(request.getLimit())
                .offset(request.getOffset())
                .total(organizationNodes.size())
                .build();
    }

}
