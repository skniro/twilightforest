package twilightforest.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

/**
 * Follows similar structure to HugeTreeFeatureConfig
 */
public class TFTreeFeatureConfig implements IFeatureConfig {
	public static final Codec<TFTreeFeatureConfig> codecTFTreeConfig = RecordCodecBuilder.create((instance) ->
			instance.group(
					BlockStateProvider.field_236796_a_.fieldOf("trunk_provider").forGetter((obj) -> obj.trunkProvider),
					BlockStateProvider.field_236796_a_.fieldOf("leaves_provider").forGetter((obj) -> obj.leavesProvider),
					BlockStateProvider.field_236796_a_.fieldOf("branch_provider").forGetter((obj) -> obj.branchProvider),
					BlockStateProvider.field_236796_a_.fieldOf("roots_provider").forGetter((obj) -> obj.rootsProvider),
					Codec.INT.fieldOf("minimum_size").withDefault(20).forGetter((obj) -> obj.minHeight),
					Codec.INT.fieldOf("add_first_five_chance").withDefault(-1).forGetter((obj) -> obj.chanceAddFiveFirst),
					Codec.INT.fieldOf("add_second_five_chance").withDefault(-1).forGetter((obj) -> obj.chanceAddFiveSecond),
					Codec.BOOL.fieldOf("has_leaves").withDefault(true).forGetter((obj) -> obj.hasLeaves),
					Codec.BOOL.fieldOf("check_water").withDefault(false).forGetter((obj) -> obj.checkWater),
					BlockStateProvider.field_236796_a_.fieldOf("sapling").withDefault(new SimpleBlockStateProvider(Blocks.OAK_SAPLING.getDefaultState())).forGetter((obj) -> obj.sapling))
					.apply(instance, TFTreeFeatureConfig::new));

	public final BlockStateProvider trunkProvider;
	public final BlockStateProvider leavesProvider;
	public final BlockStateProvider branchProvider;
	public final BlockStateProvider rootsProvider;
	public final int minHeight;
	public final int chanceAddFiveFirst;
	public final int chanceAddFiveSecond;
	public final boolean hasLeaves;
	public final boolean checkWater;
	public final BlockStateProvider sapling;
	public transient boolean forcePlacement;

	public TFTreeFeatureConfig(BlockStateProvider trunk, BlockStateProvider leaves, BlockStateProvider branch, BlockStateProvider roots, int height, int chanceFiveFirst, int chanceFiveSecond, boolean hasLeaves, boolean checkWater, BlockStateProvider sapling) {
		this.trunkProvider = trunk;
		this.leavesProvider = leaves;
		this.branchProvider = branch;
		this.rootsProvider = roots;
		this.minHeight = height;
		this.chanceAddFiveFirst = chanceFiveFirst;
		this.chanceAddFiveSecond = chanceFiveSecond;
		this.hasLeaves = hasLeaves;
		this.checkWater = checkWater;
		this.sapling = sapling;
	}

	public void forcePlacement() {
		this.forcePlacement = true;
	}

	public IPlantable getSapling(Random rand, BlockPos pos) {
		return (IPlantable) sapling.getBlockState(rand, pos).getBlock();
	}

	public static class Builder {
		private BlockStateProvider trunkProvider;
		private BlockStateProvider leavesProvider;
		private BlockStateProvider branchProvider;
		private BlockStateProvider rootsProvider;
		private int baseHeight;
		private int chanceFirstFive;
		private int chanceSecondFive;
		private boolean hasLeaves;
		private boolean checkWater;
		private BlockStateProvider sapling;

		public Builder(BlockStateProvider trunk, BlockStateProvider leaves, BlockStateProvider branch, BlockStateProvider roots) {
			this.trunkProvider = trunk;
			this.leavesProvider = leaves;
			this.branchProvider = branch;
			this.rootsProvider = roots;
		}

		public TFTreeFeatureConfig.Builder minHeight(int height) {
			this.baseHeight = height;
			return this;
		}

		public TFTreeFeatureConfig.Builder chanceFirstFive(int chance) {
			this.chanceFirstFive = chance;
			return this;
		}

		public TFTreeFeatureConfig.Builder chanceSecondFive(int chance) {
			this.chanceSecondFive = chance;
			return this;
		}

		public TFTreeFeatureConfig.Builder noLeaves() {
			this.hasLeaves = false;
			return this;
		}

		public TFTreeFeatureConfig.Builder checksWater() {
			this.checkWater = true;
			return this;
		}

		public TFTreeFeatureConfig.Builder setSapling(SaplingBlock plant) {
			this.sapling = new SimpleBlockStateProvider(plant.getDefaultState());
			return this;
		}

		public TFTreeFeatureConfig build() {
			return new TFTreeFeatureConfig(trunkProvider, leavesProvider, branchProvider, rootsProvider, baseHeight, chanceFirstFive, chanceSecondFive, hasLeaves, checkWater, sapling);
		}
	}
}
