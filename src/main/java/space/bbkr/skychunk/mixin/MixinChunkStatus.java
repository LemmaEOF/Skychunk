package space.bbkr.skychunk.mixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import com.mojang.datafixers.util.Either;
import net.minecraft.server.world.ChunkHolder.Unloaded;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.structure.StructureManager;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.random.ChunkRandom;
import net.minecraft.world.gen.random.SimpleRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import space.bbkr.skychunk.Skychunk;

@Mixin(ChunkStatus.class)
public class MixinChunkStatus {
	private static final ChunkRandom skychunk$random = new ChunkRandom(new SimpleRandom(0));

	//lambda for NOISE
	@Inject(method = { "method_38284", "lambda$static$8" }, at = @At("HEAD"), cancellable = true)
	private static void skychunk$cancelNoise(ChunkStatus targetStatus, Executor executor, ServerWorld world,
									ChunkGenerator generator, StructureManager structureManager,
									ServerLightingProvider lightingProvider,
									Function<Chunk, CompletableFuture<Either<Chunk, Unloaded>>> fullChunkConverter,
									List<Chunk> chunks, Chunk chunk, boolean bl,
									CallbackInfoReturnable<CompletableFuture<Either<Chunk, Unloaded>>> cir) {
		if (skychunk$shouldCancel(world, chunk)) {
			cir.setReturnValue(CompletableFuture.completedFuture(Either.left(chunk)));
		}
	}

	//lambda for SURFACE
	@Inject(method = { "method_16569", "lambda$static$9" }, at = @At("HEAD"), cancellable = true)
	private static void skychunk$cancelSurfaceBuilding(ChunkStatus targetStatus, ServerWorld world,
													   ChunkGenerator generator, List<Chunk> chunks,
													   Chunk chunk, CallbackInfo ci) {
		if (skychunk$shouldCancel(world, chunk)) {
			ci.cancel();
		}
	}

	private static boolean skychunk$shouldCancel(ServerWorld world, Chunk chunk) {
		ChunkPos pos = chunk.getPos();
		// Return if we're in a stronghold chunk
		for (ConfiguredStructureFeature<?, ?> feature : chunk.getStructureStarts().keySet()) {
			if (feature.feature == StructureFeature.STRONGHOLD) {
				return false;
			}
		}
		// Or at chunk (0, 0)
		if (pos.equals(ChunkPos.ORIGIN)) return false;

		// Set the seed based on the position and seed to ensure it is deterministic
		skychunk$random.setPopulationSeed(world.getSeed(), pos.x, pos.z);

		int next = skychunk$random.nextInt(100);
		return next < Skychunk.chunkRemovalRate;
	}
}
