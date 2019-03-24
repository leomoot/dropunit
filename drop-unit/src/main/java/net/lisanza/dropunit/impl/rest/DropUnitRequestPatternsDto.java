package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DropUnitRequestPatternsDto extends AbstractDropUnitRequestDto {

    @JsonProperty("requestBodyPatterns")
    private List<String> patterns;

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("DropUnitDto =>\n")
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