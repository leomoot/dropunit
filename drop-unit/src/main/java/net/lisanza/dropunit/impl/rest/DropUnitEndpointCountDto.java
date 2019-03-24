package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitEndpointCountDto {

    @JsonProperty("count")
    private int count;

    public int getCount() {
        return count;
    }

    public DropUnitEndpointCountDto withCount(int count) {
        this.count = count;
        return this;
    }

    @Override
    public String toString() {
        return "EndpointCount =>\n" +
                " count = '" + count + "'";
    }
}