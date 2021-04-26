package code.lordofwar.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import code.lordofwar.main.LOW;

import static code.lordofwar.backend.Constants.WORLD_HEIGHT_PIXEL;
import static code.lordofwar.backend.Constants.WORLD_WIDTH_PIXEL;


/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {

	public static void main(String[] args) {
		createApplication();
	}

	private static Lwjgl3Application createApplication() {
		return new Lwjgl3Application(new LOW(), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("LordOfWar");
		configuration.setWindowedMode(WORLD_WIDTH_PIXEL,WORLD_HEIGHT_PIXEL);
		configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
		return configuration;
	}
}