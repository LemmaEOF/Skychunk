package space.bbkr.skychunk;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CaveCarver;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Skychunk implements ModInitializer {
	public static final String MODID = "skychunk";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Override
	public void onInitialize() {
		//TODO: config
	}
}
