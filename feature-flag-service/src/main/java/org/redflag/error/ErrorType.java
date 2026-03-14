package org.redflag.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorType {
    INTEGRATION_ERROR("Ошибка интеграции"),
    BUSINESS_ERROR("Ошибка бизнес правил"),           //422, 409
    CLIENT_ERROR("Некорректный запрос"),               //ошибки валидации и авторизации?
    UNEXPECTED_ERROR("Неожидаемая ошибка выполнения");

    private final String value;
}
