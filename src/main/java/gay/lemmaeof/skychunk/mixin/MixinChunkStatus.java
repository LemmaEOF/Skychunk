package gay.lemmaeof.skychunk.mixin;

import java.util.concurrent.CompletableFuture;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.chunk.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import gay.lemmaeof.skychunk.Skychunk;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerating.class)
public class MixinChunkStatus {
	@Inject(method = "populateNoise", at = @At("HEAD"), cancellable = true)
	private static void cancelNoise(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture<Chunk>> info) {
		if (shouldCancel(context.world(), chunk, context.generator())) {
			info.setReturnValue(CompletableFuture.completedFuture(chunk));
		}
	}

	@Inject(method = "buildSurface", at = @At("HEAD"), cancellable = true)
	private static void cancelSurfaceBuilding(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture<Chunk>> info) {
		if (shouldCancel(context.world(), chunk, context.generator())) {
			info.setReturnValue(CompletableFuture.completedFuture(chunk));
		}
	}

	//lambda for LIQUID_CARVERS
//	@Inject(method = {"method_16569", "lambda$static$8"}, at = @At("HEAD"), cancellable = true)
//	private static void cancelLiquidCarvers(ServerWorld world, ChunkGenerator generator, List<Chunk> list, Chunk chunk, CallbackInfo info) {
//		// Water caves make a mess of the world so we just turn them off
//		info.cancel();
//	}

	private static boolean shouldCancel(ServerWorld world, Chunk chunk, ChunkGenerator generator) {
		//TODO: this seems like some messy random churn...
		ChunkRandom random = new ChunkRandom(world.getRandom());
		ChunkPos pos = chunk.getPos();
		// Return if we're at 0, 0 or a stronghold chunk
		RegistryEntry<StructureSet> strongholdSet = world.getRegistryManager().get(RegistryKeys.STRUCTURE_SET).getEntry(StructureSetKeys.STRONGHOLDS).orElse(null);
		if (world.getChunkManager().getStructurePlacementCalculator().canGenerate(strongholdSet, pos.getCenterX(), pos.getCenterZ(), 1) || pos.equals(new ChunkPos(0, 0))) return false;

		// Set the seed based on the position and seed to ensure it is deterministic
		random.setPopulationSeed(world.getSeed(), pos.x, pos.z);

		int next = random.nextInt(100);
		return next < Skychunk.chunkRemovalRate;
	}
}
