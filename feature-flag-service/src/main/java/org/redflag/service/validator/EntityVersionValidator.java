package org.redflag.service.validator;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.error.ErrorCatalog;

@Singleton
@RequiredArgsConstructor
public class EntityVersionValidator {
    public void checkVersionMatch(Long entityVersion, Long requestVersion) {
        if (!entityVersion.equals(requestVersion)) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }
    }
}
