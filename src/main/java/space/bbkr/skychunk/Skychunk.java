package space.bbkr.skychunk;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Skychunk implements ModInitializer {
	public static final String MODID = "skychunk";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static int chunkRemovalRate = 95;

	@Override
	public void onInitialize() {
		try {
			File configFile = Agnos.getConfigDir().resolve("skychunk.ini").toFile();
			if (!configFile.exists()) configFile.createNewFile();
			Properties config = new Properties();
			config.load(new FileInputStream(configFile));
			if (!config.contains("chunkRemovalRate")) config.setProperty("chunkRemovalRate", "95");
			chunkRemovalRate = Integer.parseInt(config.getProperty("chunkRemovalRate"));
			config.store(new FileOutputStream(configFile), "Edit the chunk removal rate here, as a number from 0 to 100. Default: 95");
		} catch (IOException e) {
			LOGGER.error("Could not load Skychunk config! Defaulting to 95% chunk removal.");
		}
	}
}
