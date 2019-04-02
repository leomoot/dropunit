package net.lisanza.dropunit.impl.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DropUnitRequestPatternsDto {

    @JsonProperty("requestContentType")
    protected String requestContentType;

    @JsonProperty("requestBodyPatterns")
    private List<String> patterns;

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("DropUnitRequestPatternsDto =>\n")
                .append(" req-ContentType = '").append(requestContentType).append("'\n")
                .append(" req-Body-patterns= ");
        if (patterns.isEmpty()) {
            stringBuilder.append("''");
        } else {
            for (String patterns : patterns) {
                stringBuilder.append("'").append(patterns).append("'");
            }
        }
        return stringBuilder.toString();
    }
}