package alfheim.common.item;

import alfheim.common.block.BlockElvenOres;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockElvenOres extends ItemBlock {
	
	public ItemBlockElvenOres(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int i = stack.getItemDamage();
		if (i < 0 || i >= BlockElvenOres.names.length) i = 0;
		return "tile." + BlockElvenOres.names[i] + super.getUnlocalizedName().substring(5);
	}
	
	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
