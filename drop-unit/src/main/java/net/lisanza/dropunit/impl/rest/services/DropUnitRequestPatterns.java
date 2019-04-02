package net.lisanza.dropunit.impl.rest.services;

import java.util.List;

public class DropUnitRequestPatterns extends AbstractDropUnitRequest {

    private List<String> patterns;

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    public DropUnitRequestPatterns withPatterns(List<String> patterns) {
        this.patterns = patterns;
        return this;
    }

    @Override
    public boolean doesRequestMatch(String body) {
        for (String pattern : patterns) {
            if (!body.contains(pattern)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("DropUnitRequestPatterns =>\n")
                .append(" contentType = '").append(contentType).append("'\n")
                .append(" patterns= ");
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