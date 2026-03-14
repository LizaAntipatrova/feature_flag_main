package org.redflag.error;

import lombok.Getter;

@Getter
public class FeatureFlagAppException extends RuntimeException {

    private final ErrorCatalog error;

    public FeatureFlagAppException(ErrorCatalog error, String message) {
        super(message);
        this.error = error;
    }
}
