package org.redflag.service.impl;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.ErrorResponse;
import org.redflag.dto.complex.GetOrganizationNodeByUuidRequest;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationDTOMapper;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;

import java.util.Objects;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class GetOrganizationNodeByUuidService extends BaseService<GetOrganizationNodeByUuidRequest, OrganizationNodeDTO> {
    private final OrganizationNodeRepository organizationNodeRepository;
    @Override
    protected void validateRequest(GetOrganizationNodeByUuidRequest request) {
        UUID uuid = request.getNodeUuid();
        if (Objects.isNull(uuid)) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("uuid");
        }
    }

    @Override
    protected OrganizationNodeDTO execute(GetOrganizationNodeByUuidRequest request) {
        OrganizationNode organizationNode = organizationNodeRepository.findByUuid(request.getNodeUuid())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        return OrganizationNodeDTOMapper.toOrganizationNodeDTO(organizationNode);
    }
}
