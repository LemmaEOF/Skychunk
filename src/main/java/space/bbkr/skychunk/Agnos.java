package space.bbkr.skychunk;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;

public final class Agnos {

	public static Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

}
