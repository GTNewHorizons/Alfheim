package alfheim.common.item.rod;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.core.registry.AlfheimBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

public class ItemRod extends Item implements IManaUsingItem {
	
	private Block barrier;
	
	public ItemRod(String name, Block barrier) {
		this.barrier = barrier;
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setFull3D();
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
		this.setTextureName(Constants.MODID + ':' + name);
		this.setUnlocalizedName(name);
	}
    
    @Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (stack.getItemDamage() > 0) return stack;
		if (!world.isRemote) {
			for (int x = -6; x < 7; x++)
				for (int z = -6; z < 7; z++)
					for (int y = -2; y < 3; y++)
						if (3 < Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)) && Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)) < 6) {
							int X = MathHelper.floor_double(player.posX) + x;
							int Y = MathHelper.floor_double(player.posY) + y;
							int Z = MathHelper.floor_double(player.posZ) + z;
							if (world.isAirBlock(X, Y, Z) && barrier.canPlaceBlockAt(world, X, Y, Z) &&
							   (player.capabilities.isCreativeMode || ManaItemHandler.requestManaExactForTool(stack, player, 50, true)))
								world.setBlock(X, Y, Z, barrier);
						}
			stack.setItemDamage(this.getMaxDamage());
		}
		return stack;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotID, boolean inHand) {
		if (stack.getItemDamage() > 0) stack.setItemDamage(stack.getItemDamage() - 1);
	}
	
	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}