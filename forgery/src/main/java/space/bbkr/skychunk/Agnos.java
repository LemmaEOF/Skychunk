package space.bbkr.skychunk;

import java.nio.file.Path;
import net.minecraftforge.fml.loading.FMLPaths;

public final class Agnos {

	public static Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}

}
