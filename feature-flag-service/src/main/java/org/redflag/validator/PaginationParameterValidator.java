package org.redflag.validator;

import java.util.Objects;

public class PaginationParameterValidator {
    private static final Integer MAX_LIMIT = 100;
    private static final Integer MIN_LIMIT = 1;
    private static final Integer MIN_OFFSET = 0;

    public static boolean validateLimit(Integer limit) {
        return Objects.nonNull(limit) && limit >= MIN_LIMIT && limit <= MAX_LIMIT;
    }

    public static boolean validateOffset(Integer offset) {
        return Objects.nonNull(offset) && offset >= MIN_OFFSET;
    }
}
