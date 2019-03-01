package alfheim.common.item.compat.thaumcraft;

import java.util.List;

import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemAlfheimWandRod extends Item {
	
	public static IIcon[] textures = new IIcon[3];
	
	public ItemAlfheimWandRod() {
		setCreativeTab(ThaumcraftAlfheimModule.tcnTab);
		setHasSubtypes(true);
		setUnlocalizedName("AlfheimRod");
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		for (int i = 0; i < textures.length; i++) 
			textures[i] = reg.registerIcon("thaumcraft:AlfRod" + i);
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return textures[Math.max(0, Math.min(meta, textures.length-1))];
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < textures.length; i++)
			list.add(new ItemStack(this, 1, i));
	}
	
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
	}
}
