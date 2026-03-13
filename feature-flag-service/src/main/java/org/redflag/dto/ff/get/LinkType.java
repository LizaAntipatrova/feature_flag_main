package org.redflag.dto.ff.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Serdeable
@RequiredArgsConstructor
public enum LinkType {
    SELF("self"),
    ANCESTOR("ancestor"),
    DESCENDANT("descendant");

    private final String value;

    @JsonCreator
    public static List<LinkType> parseIncludeQuery(String includeQuery) {
        Objects.requireNonNull(includeQuery);
        return Arrays.stream(includeQuery.split(",")).map((linkType) -> switch (linkType) {
            case "self" -> SELF;
            case "ancestor" -> ANCESTOR;
            case "descendant" -> DESCENDANT;
            default -> throw new IllegalArgumentException();
        }).toList();
    }
    @JsonValue
    public String getValue() {
        return value;
    }
}