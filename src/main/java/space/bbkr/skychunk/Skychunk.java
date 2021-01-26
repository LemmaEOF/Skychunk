package space.bbkr.skychunk;

import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("skychunk")
public class Skychunk {
	public static final String MODID = "skychunk";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public Skychunk() {
		try {
			Class.forName("org.spongepowered.asm.mixin.Mixins");
		} catch (Throwable t) {
			throw new LoaderException("SkyChunk requires MixinBootstrap on versions earlier than Forge 32.0.72");
		}
	}
}
