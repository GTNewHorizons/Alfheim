package alexsocol.asjlib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemPattern extends Item {
	
	public ItemPattern(String modid, String name, CreativeTabs tab, int stackSize) {
        this.setCreativeTab(tab);
        this.setMaxStackSize(stackSize);
        this.setTextureName(modid + ":" + name);
        this.setUnlocalizedName(name);
    }
}
