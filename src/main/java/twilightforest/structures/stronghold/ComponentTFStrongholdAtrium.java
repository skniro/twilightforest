package twilightforest.structures.stronghold;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFFeature;
import twilightforest.biomes.TFBiomeDecorator;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdAtrium extends StructureTFStrongholdComponent {

	private boolean enterBottom;

	public ComponentTFStrongholdAtrium(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSAt, nbt);
	}

	public ComponentTFStrongholdAtrium(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(TFStrongholdPieces.TFSAt, feature, i, facing, x, y, z);
	}

	/**
	 * Save to NBT
	 * TODO: See super
	 */
//	@Override
//	protected void writeStructureToNBT(CompoundNBT tagCompound) {
//		super.writeStructureToNBT(tagCompound);
//
//		tagCompound.putBoolean("enterBottom", this.enterBottom);
//	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		this.enterBottom = tagCompound.getBoolean("enterBottom");
	}

	@Override
	public MutableBoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {

		if (y > 17) {
			this.enterBottom = false;
		} else if (y < 11) {
			this.enterBottom = true;
		} else {
			this.enterBottom = (z & 1) == 0;
		}

		if (enterBottom) {
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 18, 14, 18, facing);
		} else {
			// enter on the top
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -8, 0, 18, 14, 18, facing);
		}
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

		if (this.enterBottom) {
			this.addDoor(4, 1, 0);
			addNewComponent(parent, list, random, Rotation.CLOCKWISE_180, 13, 8, -1);
		} else {
			this.addDoor(13, 8, 0);
			addNewComponent(parent, list, random, Rotation.CLOCKWISE_180, 4, 1, -1);
		}

		addNewComponent(parent, list, random, Rotation.NONE, 13, 1, 18);
		addNewComponent(parent, list, random, Rotation.NONE, 4, 8, 18);
	}

	/**
	 * Generate the blocks that go here
	 */
	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 13, 17, rand, deco.randomBlocks);


		// balcony
		this.fillWithRandomizedBlocks(world, sbb, 1, 6, 1, 16, 7, 16, false, rand, deco.randomBlocks);
		this.fillWithBlocks(world, sbb, 5, 8, 5, 12, 8, 12, deco.fenceState, AIR, false);
		this.fillWithAir(world, sbb, 6, 6, 6, 11, 8, 11);

		// balcony pillars
		placeBalconyPillar(world, sbb, Rotation.NONE);
		placeBalconyPillar(world, sbb, Rotation.CLOCKWISE_90);
		placeBalconyPillar(world, sbb, Rotation.CLOCKWISE_180);
		placeBalconyPillar(world, sbb, Rotation.COUNTERCLOCKWISE_90);

		// corner pillars
		this.fillWithRandomizedBlocks(world, sbb, 1, 1, 1, 1, 12, 2, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 2, 1, 1, 2, 12, 1, false, rand, deco.randomBlocks);

		this.fillWithRandomizedBlocks(world, sbb, 16, 1, 1, 16, 12, 2, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 15, 1, 1, 15, 12, 1, false, rand, deco.randomBlocks);

		this.fillWithRandomizedBlocks(world, sbb, 1, 1, 15, 1, 12, 16, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 2, 1, 16, 2, 12, 16, false, rand, deco.randomBlocks);

		this.fillWithRandomizedBlocks(world, sbb, 16, 1, 15, 16, 12, 16, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 15, 1, 16, 15, 12, 16, false, rand, deco.randomBlocks);

		// grass
		BlockState grass = Blocks.GRASS.getDefaultState();
		this.generateMaybeBox(world, sbb, rand, 0.5F, 6, 0, 6, 11, 0, 11, grass, grass, false, false);
		this.fillWithBlocks(world, sbb, 7, 0, 7, 10, 0, 10, grass, AIR, false);

		// tree
		this.spawnATree(world, manager, rand.nextInt(5), 8, 1, 8, sbb);

		// statues
		placeCornerStatue(world, 2, 8, 2, 0, sbb);
		placeCornerStatue(world, 2, 1, 15, 1, sbb);
		placeCornerStatue(world, 15, 1, 2, 2, sbb);
		placeCornerStatue(world, 15, 8, 15, 3, sbb);

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}

	private void spawnATree(ISeedReader world, StructureManager manager, int treeNum, int x, int y, int z, MutableBoundingBox sbb) {
		BlockPos pos = getBlockPosWithOffset(x, y, z);

		if (sbb.isVecInside(pos)) {
			ConfiguredFeature<?,?> treeGen;
			// grow a tree
			int minHeight = 8;

			//TODO: All trees here grab configs from DefaultBiomeFeatures or TFBiomeDecorator, and will not have "minHeight"
			switch (treeNum) {
				case 0:
				default:
					// oak tree
					treeGen = Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG); //TODO: minHeight
					break;
				case 1:
					// jungle tree
					treeGen = Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.JUNGLE_TREE_CONFIG); //TODO: minHeight
					break;
				case 2:
					// birch
					treeGen = Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.BIRCH_TREE_CONFIG); //TODO: minHeight
					break;
				case 3:
					treeGen = Feature.field_236291_c_.withConfiguration(TFBiomeDecorator.OAK_TREE); //TODO: minHeight
					break;
				case 4:
					treeGen = Feature.field_236291_c_.withConfiguration(TFBiomeDecorator.RAINBOAK_TREE);
					break;
			}

			for (int i = 0; i < 100; i++) {
				if (treeGen.func_236265_a_(world, manager, ((ServerWorld) world).getChunkProvider().getChunkGenerator(), world.getRandom(), pos)) {
					break;
				}
			}
		}
	}

	private void placeBalconyPillar(ISeedReader world, MutableBoundingBox sbb, Rotation rotation) {
		this.fillBlocksRotated(world, sbb, 5, 1, 5, 5, 12, 5, deco.pillarState, rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), rotation, false), 5, 1, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), rotation, false), 6, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), rotation, true), 5, 5, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), rotation, true), 6, 5, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), rotation, true), 5, 12, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), rotation, true), 6, 12, 5, rotation, sbb);
	}
}
