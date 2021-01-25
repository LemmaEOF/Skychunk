package space.bbkr.skychunk.mixin;

import net.fabricmc.fabric.mixin.structure.StructureFeatureAccessor;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Mixin(NoiseChunkGenerator.class)
public abstract class MixinNoiseChunkGenerator extends ChunkGenerator {

	@Shadow @Final private long seed;

	private final Random skychunk$random = new Random(seed);

	public MixinNoiseChunkGenerator(BiomeSource biomeSource, StructuresConfig structuresConfig) {
		super(biomeSource, structuresConfig);
	}

	private final Set<ChunkPos> skychunk$ignoredChunks = new HashSet<>();

	@Inject(method = "buildSurface", at = @At("HEAD"), cancellable = true)
	private void cancelSurfaceBuild(ChunkRegion region, Chunk chunk, CallbackInfo info) {
		if (skychunk$ignoredChunks.contains(chunk.getPos())) info.cancel();
	}

	@Inject(method = "populateNoise", at = @At("HEAD"), cancellable = true)
	private void cancelNoise(WorldAccess world, StructureAccessor structures, Chunk chunk, CallbackInfo info) {
		if (isStrongholdStartingChunk(chunk.getPos()) || chunk.getPos().equals(new ChunkPos(0, 0))) return;
		int next = this.skychunk$random.nextInt(100);
		if (next < 95) {
			skychunk$ignoredChunks.add(chunk.getPos());
			info.cancel();
		}
	}

}
