package net.lisanza.dropunit.server.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DropUnitCount")
public class DropUnitCount {

    @XmlElement(name = "GET")
    @JsonProperty(value = "GET")
    private int httpGet;

    @XmlElement(name = "POST")
    @JsonProperty("POST")
    private int httpPost;

    @XmlElement(name = "PUT")
    @JsonProperty(value = "PUT")
    private int httpPut;

    @XmlElement(name = "DELETE")
    @JsonProperty(value = "DELETE")
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
