package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DropUnitCount")
public class DropUnitCount {

    @XmlElement(name = "get")
    @JsonProperty(value = "get")
    private int httpGet;

    @XmlElement(name = "post")
    @JsonProperty("post")
    private int httpPost;

    @XmlElement(name = "put")
    @JsonProperty(value = "put")
    private int httpPut;

    @XmlElement(name = "delete")
    @JsonProperty(value = "delete")
    private int httpDelete;

    public int getHttpGet() {
        return httpGet;
    }

    public int getHttpPost() {
        return httpPost;
    }

    public int getHttpPut() {
        return httpPut;
    }

    public int getHttpDelete() {
        return httpDelete;
    }

    public void incrHttpGet() {
        httpGet++;
    }

    public void incrHttpPost() {
        httpPost++;
    }

    public void incrHttpPut() {
        httpPut++;
    }

    public void incrHttpDelete() {
        httpDelete++;
    }
}
