package net.lisanza.dropunit.simulator;

import net.lisanza.dropunit.impl.DropUnitApplication;
import net.lisanza.dropunit.simulator.config.yml.AppConfiguration;

public class App extends DropUnitApplication<AppConfiguration> {

    public static void main(String[] args) throws Exception {
        // Start the application
        new DropUnitApplication().run(args);
    }
}
