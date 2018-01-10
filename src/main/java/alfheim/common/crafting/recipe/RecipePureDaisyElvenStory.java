package alfheim.common.crafting.recipe;

import alfheim.common.core.utils.AlfheimConfig;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.subtile.SubTileEntity;

public class RecipePureDaisyElvenStory extends RecipePureDaisy {
	
	Object input;
	
	public RecipePureDaisyElvenStory(Object input, Block output, int outputMeta) {
		super(input, output, outputMeta);
		this.input = input;
	}

	public boolean matches(World world, int x, int y, int z, SubTileEntity pureDaisy, Block block, int meta) {
		if(input.equals("logWood") && world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim) return false;
		return super.matches(world, x, y, z, pureDaisy, block, meta);
	}
	
	public boolean set(World world, int x, int y, int z, SubTileEntity pureDaisy) {
		return (world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim) ? false : super.set(world, x, y, z, pureDaisy);
	}
}
