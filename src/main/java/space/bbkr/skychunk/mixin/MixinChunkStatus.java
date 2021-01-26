package space.bbkr.skychunk.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(ChunkStatus.class)
public class MixinChunkStatus {
	private static final ChunkRandom skychunk$random = new ChunkRandom();

	//lambda for NOISE
	@Inject(method = "method_16564", at = @At("HEAD"), cancellable = true)
	private static void cancelNoise(ServerWorld world, ChunkGenerator generator, List<Chunk> list, Chunk chunk, CallbackInfo info) {
		if (shouldCancel(world, chunk, generator)) {
			info.cancel();
		}
	}

	//lambda for SURFACE
	@Inject(method = "method_16567", at = @At("HEAD"), cancellable = true)
	private static void cancelSurfaceBuilding(ServerWorld world, ChunkGenerator generator, List<Chunk> list, Chunk chunk, CallbackInfo info) {
		if (shouldCancel(world, chunk, generator)) {
			info.cancel();
		}
	}

	//lambda for LIQUID_CARVERS
	@Inject(method = "method_16569", at = @At("HEAD"), cancellable = true)
	private static void cancelLiquidCarvers(ServerWorld world, ChunkGenerator generator, List<Chunk> list, Chunk chunk, CallbackInfo info) {
		// Water caves make a mess of the world so we just turn them off
		info.cancel();
	}

	private static boolean shouldCancel(ServerWorld world, Chunk chunk, ChunkGenerator generator) {
		ChunkPos pos = chunk.getPos();
		// Return if we're at 0, 0 or a stronghold chunk
		if (generator.isStrongholdStartingChunk(pos) || pos.equals(new ChunkPos(0, 0))) return false;

		// Set the seed based on the position and seed to ensure it is deterministic
		skychunk$random.setPopulationSeed(world.getSeed(), pos.x, pos.z);

		int next = skychunk$random.nextInt(100);
		return next < 95;
	}
}
