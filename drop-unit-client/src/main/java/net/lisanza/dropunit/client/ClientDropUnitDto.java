package net.lisanza.dropunit.client;

import net.lisanza.dropunit.impl.rest.DropUnitDto;

public class ClientDropUnitDto {

    private String id;

    private int count;

    private DropUnitDto dropUnitDto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientDropUnitDto withId(String id) {
        this.id = id;
        return this;
    }

    public DropUnitDto getDropUnitDto() {
        return dropUnitDto;
    }

    public void setDropUnitDto(DropUnitDto dropUnitDto) {
        this.dropUnitDto = dropUnitDto;
    }

    public ClientDropUnitDto withDropUnitDto(DropUnitDto dropUnitDto) {
        this.dropUnitDto = dropUnitDto;
        return this;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ClientDropUnitDto withCount(int count) {
        this.count = count;
        return this;
    }

    //

    public String getUrl() {
        return getDropUnitDto().getUrl();
    }

    public String getResponseBody() {
        return getDropUnitDto().getResponseBody();
    }

    @Override
    public String toString() {
        return "ClientDropUnitDto =>\n" +
                " id         = '" + id + "'\n" +
                " url        = '" + dropUnitDto.getUrl() + "'\n" +
                " count      = " + count + "\n";
    }
}