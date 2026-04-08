package org.redflag.services.externalServices;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import org.redflag.dto.CreateOrganizationRequest;
import org.redflag.dto.CreateOrganizationResponse;
import org.redflag.dto.OrganizationNodeDTO;

import java.util.UUID;

@Client(id = "ff-service")
public interface FFServiceClient {

    @Post("/api/v1/organizations/with-root-node")
    CreateOrganizationResponse createOrganization(@Body CreateOrganizationRequest request);

    @Get("/api/v1/find-node")
    OrganizationNodeDTO getOrganizationNodeByUuid(
            @QueryValue("organizationNodeUuid") UUID organizationNodeUuid
    );

}
