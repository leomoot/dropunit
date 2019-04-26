package net.lisanza.dropunit.simulator;

import net.lisanza.dropunit.impl.DropUnitApplication;
import net.lisanza.dropunit.impl.config.yml.DropUnitConfiguration;

public class App extends DropUnitApplication<DropUnitConfiguration> {

    public static void main(String[] args) throws Exception {
        // Start the application
        new DropUnitApplication().run(args);
    }
}
