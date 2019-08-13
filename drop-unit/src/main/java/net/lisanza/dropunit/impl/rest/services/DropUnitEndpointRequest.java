package net.lisanza.dropunit.impl.rest.services;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class DropUnitEndpointRequest {

    protected String contentType;

    private List<String> patterns;

    // Getters and Setters

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    // Builders

    public DropUnitEndpointRequest withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public DropUnitEndpointRequest withBody(String body) {
        this.patterns = new ArrayList<>();
        this.patterns.add(body);
        return this;
    }

    public DropUnitEndpointRequest withPatterns(List<String> patterns) {
        this.patterns = new ArrayList<>();
        if (patterns != null) {
            this.patterns.addAll(patterns);
        }
        return this;
    }

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
                .append("DropUnitEndpointRequestPatterns =>\n")
                .append(" contentType = '").append(contentType).append("'\n")
                .append(" patterns= ");
        if (patterns.isEmpty()) {
            stringBuilder.append("''");
        } else {
            for (String patterns : patterns) {
                stringBuilder.append("'").append(patterns).append("' ");
            }
        }
        return stringBuilder.toString();
    }

    // hashCode

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(contentType)
                .append(patterns)
                .toHashCode();
    }
}
