package alfheim.common.item.block;

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
		return "tile." + BlockElvenOres.names[Math.max(0, Math.min(stack.getItemDamage(), BlockElvenOres.names.length-1))] + super.getUnlocalizedName().substring(5);
	}
	
	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
