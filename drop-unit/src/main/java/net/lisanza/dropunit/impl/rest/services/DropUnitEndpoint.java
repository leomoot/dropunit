package net.lisanza.dropunit.impl.rest.services;

import net.lisanza.dropunit.impl.rest.DropUnitDto;

public class DropUnitEndpoint {

    private String id;

    private DropUnitDto dropUnitDto;

    private int count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DropUnitEndpoint withId(String id) {
        this.id = id;
        return this;
    }

    public DropUnitDto getDropUnitDto() {
        return dropUnitDto;
    }

    public void setDropUnitDto(DropUnitDto dropUnitDto) {
        this.dropUnitDto = dropUnitDto;
    }

    public DropUnitEndpoint withDropUnitDto(DropUnitDto dropUnitDto) {
        this.dropUnitDto = dropUnitDto;
        return this;
    }

    public void incr() {
        count++;
    }

    public int getCount() {
        return count;
    }


    @Override
    public String toString() {
        return "DropUnitDto =>\n" +
                " id          = '" + id + "'\n" +
                " url         = '" + dropUnitDto.getUrl() + "'\n" +
                " count       = '" + count + "'";
    }

}
