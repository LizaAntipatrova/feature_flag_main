package org.redflag.error;

import io.micronaut.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    UNEXPECTED_ERROR("00-0000", null, ErrorType.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR),

    EMPTY_FIELD("01-0001", "Поле %s не может быть пустым или null", ErrorType.CLIENT_ERROR, HttpStatus.BAD_REQUEST),
    BAD_LIMIT("01-0002", "Некорректный формат limit", ErrorType.CLIENT_ERROR, HttpStatus.BAD_REQUEST),
    BAD_OFFSET("01-0003", "Некорректный формат offset", ErrorType.CLIENT_ERROR, HttpStatus.BAD_REQUEST),
    NO_DATA("01-0004", "Нет данных по переданным параметрам", ErrorType.CLIENT_ERROR, HttpStatus.NOT_FOUND),
    INVALID_JSON("01-0005", "Некорректный JSON", ErrorType.CLIENT_ERROR, HttpStatus.BAD_REQUEST),

    NOT_UNIQUE_ORGANIZATION_NAME("02-0001", "Организация с таким именем уже существует", ErrorType.BUSINESS_ERROR, HttpStatus.CONFLICT),
    NOT_UNIQUE_ORGANIZATION_NODE_NAME_IN_ORGANIZATION("02-0002", "Звено организации с таким именем в этой организации уже существует", ErrorType.BUSINESS_ERROR, HttpStatus.CONFLICT),
    SERVICE_CANNOT_HAVE_DESCENDANTS("02-0003", "Сервис не может иметь потомков", ErrorType.BUSINESS_ERROR, HttpStatus.CONFLICT),
    OPTIMISTIC_LOCK("02-0004", "Устаревшая версия данных", ErrorType.BUSINESS_ERROR, HttpStatus.CONFLICT),
    CYCLE_MOVE("02-0005", "Нельзя переместить узел в дочерний ему или в него самого", ErrorType.BUSINESS_ERROR, HttpStatus.CONFLICT),
    MOVE_ROOT_NODE("02-0006", "Нельзя переместить корневой узел организации", ErrorType.BUSINESS_ERROR, HttpStatus.CONFLICT),
    NOT_UNIQUE_FEATURE_FLAG_NAME_IN_ORGANIZATION("02-0007", "Фича флаг с таким именем в этой организации уже существует", ErrorType.BUSINESS_ERROR, HttpStatus.CONFLICT);


    private final String code;
    private final String message;
    private final ErrorType errorType;
    private final HttpStatus status;

    public FeatureFlagAppException withMessageArgs(Object... messageArgs) {
        return new FeatureFlagAppException(this, this.message.formatted(messageArgs));
    }
    public FeatureFlagAppException getException() {
        return new FeatureFlagAppException(this, this.message);
    }


}
