package de.main;

import io.quarkus.runtime.Quarkus;

/**
 * //todo kurz erklären!
 *
 * @author Franz Klose
 */
public class Main {

    public static void main(String[] args) {
        Quarkus.run();
        LowServer server = new LowServer();

    }

}
