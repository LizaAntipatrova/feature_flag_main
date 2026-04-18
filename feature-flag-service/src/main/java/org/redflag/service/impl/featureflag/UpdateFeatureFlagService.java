package org.redflag.service.impl.featureflag;

import io.micronaut.json.JsonMapper;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.redflag.dto.featureflag.FeatureFlagDTO;
import org.redflag.dto.featureflag.update.UpdateFeatureFlagRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.kafka.KafkaFeatureFlagEventDTO;
import org.redflag.kafka.KafkaProducer;
import org.redflag.kafka.KafkaTopicNameUtil;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.FeatureFlagDTOMapper;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.EntityVersionValidator;
import org.redflag.service.validator.LinkedEntityValidator;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Singleton
@Slf4j
@RequiredArgsConstructor
public class UpdateFeatureFlagService extends BaseService<UpdateFeatureFlagRequest, FeatureFlagDTO> {
    private final FeatureFlagRepository featureFlagRepository;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final FeatureFlagDTOMapper featureFlagDTOMapper;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;
    private final EntityVersionValidator entityVersionValidator;
    private final KafkaProducer kafkaProducer;
    private final KafkaTopicNameUtil kafkaTopicNameUtil;
    private final JsonMapper jsonMapper;


    @Override
    protected void validateRequest(UpdateFeatureFlagRequest request) {
        if (Objects.isNull(request.getValue())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("value");
        }
        if (Objects.isNull(request.getVersion())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("version");
        }
    }

    @Override
    protected void validateState(UpdateFeatureFlagRequest request) {
        authRightsToNodeValidator.checkIsAuthNodeIsParentToRequestNode(request.getNodeId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
        linkedEntityValidator.checkIsFeatureFlagInNode(request.getFeatureFlagId(), request.getNodeId());
    }

    @Override
    @Transactional
    protected FeatureFlagDTO execute(UpdateFeatureFlagRequest request) {
        FeatureFlag featureFlag = featureFlagRepository.findById(request.getFeatureFlagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        entityVersionValidator.checkVersionMatch(featureFlag.getVersion(), request.getVersion());

        featureFlag.setValue(request.getValue());
        FeatureFlag newFeatureFlag;
        try {
            newFeatureFlag = featureFlagRepository.update(featureFlag);
            featureFlagRepository.flush();
        } catch (OptimisticLockException e) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }
        KafkaFeatureFlagEventDTO event = KafkaFeatureFlagEventDTO.builder()
                .flagName(featureFlag.getName())
                .value(featureFlag.getValue()).build();
        String jsonEvent;
        try {
            jsonEvent = jsonMapper.writeValueAsString(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        List<UUID> serviceNodeUuids = organizationNodeRepository
                .getNodeUuidsServiceDescendants(featureFlag.getOrganizationNode().getId());

        serviceNodeUuids
                .forEach(uuid -> {
                    String topicName = kafkaTopicNameUtil.getTopicNameByNodeUuid(uuid);
                    kafkaProducer.sendEvent(
                            topicName,
                            jsonEvent
                    ).thenRun(() -> log.info("Сообщение отправлено в топик {}", topicName));
                });
        return featureFlagDTOMapper.toFeatureFlagDTO(newFeatureFlag);
    }
}
