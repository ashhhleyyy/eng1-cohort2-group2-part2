package group1.unisim.headless;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import group1.unisim.Main;

/**
 * Launches the headless application. Can be converted into a utilities project or a server application.
 */
public class HeadlessLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    @SuppressWarnings("UnusedReturnValue")
    private static Application createApplication() {
        // Note: you can use a custom ApplicationListener implementation for the headless project instead of Main.
        return new HeadlessApplication(new Main(), getDefaultConfiguration());
    }

    private static HeadlessApplicationConfiguration getDefaultConfiguration() {
        HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
        configuration.updatesPerSecond = -1; // When this value is negative, Main#render() is never called.
        return configuration;
    }
}
