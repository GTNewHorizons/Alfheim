package alfheim.common.items;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.registry.AlfheimBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class Rod extends Item implements IManaUsingItem {
	
	public static final String[] subItems = new String[] { "MuspelheimRod", "NiflheimRod" };
	private IIcon[] texture = new IIcon[subItems.length];
	
	public Rod() {
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("Rod");
	}
	
	public void registerIcons(IIconRegister iconRegister){
		for (int i = 0; i < subItems.length; i++){
			texture[i] = iconRegister.registerIcon(Constants.MODID + ':' + subItems[i]);
		}
	}

    public IIcon getIconFromDamage(int i) {
    	if (i < texture.length) {
        	return texture[i];
    	} else {
    		return texture[0];
    	}
    }

    public String getUnlocalizedName(ItemStack stack) {
    	if (stack.getItemDamage() < subItems.length) {
        	return "item." + subItems[stack.getItemDamage()];
    	} else {
    		return subItems[0];
    	}
    }

    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < subItems.length; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }
    
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote)
			for (int x = -6; x < 7; x++)
				for (int z = -6; z < 7; z++)
					for (int y = -2; y < 3; y++)
						if (4 < Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)) && Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)) < 6) {
							int X = MathHelper.floor_double(player.posX) + x;
							int Y = MathHelper.floor_double(player.posY) + y;
							int Z = MathHelper.floor_double(player.posZ) + z;
							Block block = stack.getItemDamage() == 0 ? AlfheimBlocks.redFlame : stack.getItemDamage() == 1 ? AlfheimBlocks.poisonIce : Blocks.air;
							if (world.isAirBlock(X, Y, Z) && block.canPlaceBlockAt(world, X, Y, Z))
								if (player.capabilities.isCreativeMode || ManaItemHandler.requestManaExactForTool(stack, player, 50, block != Blocks.air)) world.setBlock(X, Y, Z, block);
						}
		return stack;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}
