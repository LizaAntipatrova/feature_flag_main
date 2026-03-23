package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.FeatureFlagDTO;
import org.redflag.dto.featureflag.get.GetFeatureFlagByNameRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.FeatureFlagDTOMapper;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class GetFeatureFlagByNameService extends BaseService<GetFeatureFlagByNameRequest, FeatureFlagDTO> {
    private final FeatureFlagRepository featureFlagRepository;

    @Override
    protected void validateRequest(GetFeatureFlagByNameRequest request) {
        String name = request.getFlagName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("flagName");
        }
    }

    @Override
    protected FeatureFlagDTO execute(GetFeatureFlagByNameRequest request) {
        FeatureFlag featureFlag = featureFlagRepository.findByName(request.getFlagName())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        return FeatureFlagDTOMapper.toFeatureFlagDTO(featureFlag);
    }
}
