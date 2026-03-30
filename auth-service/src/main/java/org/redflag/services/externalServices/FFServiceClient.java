package org.redflag.services.externalServices;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import org.redflag.dto.CreateOrganizationRequest;
import org.redflag.dto.CreateOrganizationResponse;

@Client("${redflag.services.ff-service.url}")
public interface FFServiceClient {

    @Post("/api/v1/organizations/with-root-node")
    CreateOrganizationResponse createOrganization(@Body CreateOrganizationRequest request);

}
