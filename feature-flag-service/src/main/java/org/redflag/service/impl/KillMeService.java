//package org.redflag.service.impl;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.inject.Singleton;
//import lombok.RequiredArgsConstructor;
//import org.redflag.dto.featureflag.update.UpdateFeatureFlagRequest;
//import org.redflag.dto.featureflag.update.UpdateFeatureFlagResponse;
//import org.redflag.error.ErrorCatalog;
//import org.redflag.model.FeatureFlag;
//import org.redflag.model.Organization;
//import org.redflag.model.OrganizationNode;
//import org.redflag.repository.FeatureFlagRepository;
//import org.redflag.repository.OrganizationNodeRepository;
//import org.redflag.repository.OrganizationRepository;
//import org.redflag.service.AbstractService;
//
//import java.util.UUID;
//
//@Singleton
//@RequiredArgsConstructor
//public class KillMeService extends AbstractService<UpdateFeatureFlagRequest, UpdateFeatureFlagResponse> {
//
//    private final OrganizationRepository organizationRepository;
//    private final OrganizationNodeRepository organizationNodeRepository;
//    private final FeatureFlagRepository featureFlagRepository;
//
//
//    @PostConstruct
//    public void init() {
//        Organization organization = new Organization();
//        organization.setName("jjjj");
//        organizationRepository.save(organization);
//
//        OrganizationNode organizationNode = new OrganizationNode();
//        organizationNode.setName("jjjj");
//        organizationNode.setOrganization(organization);
//        organizationNode.setUuid(UUID.randomUUID());
//        organizationNode.setIsService(false);
//        organizationNodeRepository.saveAndFlush(organizationNode);
//
//        OrganizationNode organizationNodeCopy = organizationNodeRepository.findByName("jjjj");
//        organizationNodeCopy.setPath(organizationNodeCopy.getId().toString());
//        organizationNodeRepository.update(organizationNodeCopy);
//
//        FeatureFlag featureFlag = new FeatureFlag()
//                .setName("pup")
//                .setOrganizationNode(organizationNode)
//                .setValue(true);
//        featureFlagRepository.save(featureFlag);
//
//    }
//
//    @Override
//    protected void validateRequest(UpdateFeatureFlagRequest request) {
//        if (request.getValue() == null) {
//            throw ErrorCatalog.NOT_EMPTY.withMessage("value");
//        }
//    }
//
//    @Override
//    protected UpdateFeatureFlagResponse logic(UpdateFeatureFlagRequest updateFeatureFlagRequest) {
//        return null;
//    }
//}
