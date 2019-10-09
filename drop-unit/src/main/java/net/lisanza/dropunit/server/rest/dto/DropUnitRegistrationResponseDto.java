package net.lisanza.dropunit.server.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitRegistrationResponseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("result")
    private String result;

    @JsonProperty("count")
    private int count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DropUnitRegistrationResponseDto withId(String id) {
        this.id = id;
        return this;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DropUnitRegistrationResponseDto withResult(String result) {
        this.result = result;
        return this;
    }

    public int getCount() {
        return count;
    }

    public DropUnitRegistrationResponseDto withCount(int count) {
        this.count = count;
        return this;
    }

    @Override
    public String toString() {
        return "EndpointRegistration =>\n" +
                " id          = '" + id + "'\n" +
                " result      = '" + result + "'\n" +
                " count = '" + count + "'";
    }
}