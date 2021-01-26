package space.bbkr.skychunk.mixin;

import java.util.List;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

@Mixin(ChunkStatus.class)
public class MixinChunkStatus {
	private static final SharedSeedRandom skychunk$random = new SharedSeedRandom();

	//lambda for NOISE
	@Inject(method = "lambda$static$5", at = @At("HEAD"), cancellable = true)
	private static void cancelNoise(ServerWorld world, ChunkGenerator generator, List<Chunk> list, Chunk chunk, CallbackInfo info) {
		if (shouldCancel(world, chunk, generator)) {
			info.cancel();
		}
	}

	//lambda for SURFACE
	@Inject(method = "lambda$static$6", at = @At("HEAD"), cancellable = true)
	private static void cancelSurfaceBuilding(ServerWorld world, ChunkGenerator generator, List<Chunk> list, Chunk chunk, CallbackInfo info) {
		if (shouldCancel(world, chunk, generator)) {
			info.cancel();
		}
	}

	//lambda for LIQUID_CARVERS
	@Inject(method = "lambda$static$8", at = @At("HEAD"), cancellable = true)
	private static void cancelLiquidCarvers(ServerWorld world, ChunkGenerator generator, List<Chunk> list, Chunk chunk, CallbackInfo info) {
		// Water caves make a mess of the world so we just turn them off
		info.cancel();
	}

	private static boolean shouldCancel(ServerWorld world, Chunk chunk, ChunkGenerator generator) {
		ChunkPos pos = chunk.getPos();
		// Return if we're at 0, 0 or a stronghold chunk
		if (generator.func_235952_a_(pos) || pos.equals(new ChunkPos(0, 0))) return false;

		// Set the seed based on the position and seed to ensure it is deterministic
		skychunk$random.setFeatureSeed(world.getSeed(), pos.x, pos.z);

		int next = skychunk$random.nextInt(100);
		return next < 95;
	}
}
