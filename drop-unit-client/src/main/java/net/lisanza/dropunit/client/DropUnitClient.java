package net.lisanza.dropunit.client;

public class DropUnitClient {

    private final BaseDropUnitClient baseClient;

    public DropUnitClient(String baseUrl) {
        this.baseClient = new BaseDropUnitClient(baseUrl);
    }
}
