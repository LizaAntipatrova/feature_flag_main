package org.redflag.service.validator;

import jakarta.inject.Singleton;
import org.redflag.error.ErrorCatalog;

import java.util.Objects;

@Singleton
public class PaginationParameterValidator {
    private static final Integer MAX_LIMIT = 100;
    private static final Integer MIN_LIMIT = 1;
    private static final Integer MIN_OFFSET = 0;

    public void validateLimit(Integer limit) {
        if (Objects.isNull(limit) || limit < MIN_LIMIT || limit > MAX_LIMIT) {
            throw ErrorCatalog.BAD_LIMIT.getException();
        }
    }

    public void validateOffset(Integer offset) {
        if (Objects.isNull(offset) || offset < MIN_OFFSET) {
            throw ErrorCatalog.BAD_OFFSET.getException();
        }

    }
}
