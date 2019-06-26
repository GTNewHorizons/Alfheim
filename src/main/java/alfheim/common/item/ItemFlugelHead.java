package alfheim.common.item;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.registry.AlfheimBlocks;
import net.minecraft.block.BlockSkull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemFlugelHead extends Item {
	
	public ItemFlugelHead() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setTextureName(ModInfo.MODID + ":FlugelHead");
		setUnlocalizedName("FlugelHead");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (player.getCurrentArmor(3) == null) player.setCurrentItemOrArmor(4, stack.splitStack(1)); 
		return stack;
	}
	
	// > ItemGaiaHead#onItemUse:
	// I couldn't deal with it. ~Vazkii
	// :thinking: ~AlexSocol
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float sideX, float sideY, float sideZ) {
		// The side of the wall the head is being used on.
		ForgeDirection sideDir = ForgeDirection.getOrientation(side);

		// If we can replace the block we're clicking on, then we'll go ahead
		// and replace it (eg, snow).
		if (world.getBlock(x, y, z).isReplaceable(world, x, y, z) && sideDir != ForgeDirection.DOWN) {
			sideDir = ForgeDirection.UP;
			y--;
		}

		// Skulls can't be placed on the bottom side of a block.
		if (sideDir == ForgeDirection.DOWN)
			return false;

		// If the side we're trying to place the skull on isn't solid, then
		// we can't place it either.
		if (!world.isSideSolid(x, y, z, sideDir))
			return false;

		// Figure out where the skull actually goes based on the side we're placing it against.
		switch(sideDir) {
		case UP: y++; break; // If we're placing it on the top, then the skull goes 1 block above.
		case NORTH: z--; break; // Placing it on the north side (Z- axis).
		case SOUTH: z++; break; // Placing it on the south side (Z+ axis).
		case WEST: x--; break; // Placing it on the west side (X- axis).
		case EAST: x++; break; // Placing it on the east side (X+ axis).
		default: return false; // Oops, this shouldn't happen.
		}

		// We can't place blocks as a measly client.
		if(world.isRemote)
			return true;

		// If the skull says no, who are we to argue?
		if (!AlfheimBlocks.flugelHead.canPlaceBlockOnSide(world, x, y, z, side))
			return false;

		// Flugel head, instead of skull
		world.setBlock(x, y, z, AlfheimBlocks.flugelHead, sideDir.ordinal(), 2);
		int headAngle = 0;

		// If we place the skull on top of a block, we should also make it
		// face the player by rotating it.
		if (sideDir == ForgeDirection.UP)
			headAngle = MathHelper.floor_double(player.rotationYaw * 16.0F / 360.0F + 0.5D) & 15;

		// Update the skull's orientation if it lets us.
		TileEntity tileentity = world.getTileEntity(x, y, z);

		if (tileentity instanceof TileEntitySkull) {
			((TileEntitySkull) tileentity).func_145903_a(headAngle);
			((BlockSkull) Blocks.skull).func_149965_a(world, x, y, z, (TileEntitySkull) tileentity);
		}

		// Remove a head from the stack.
		--stack.stackSize;

		// Call it a success and leave.
		return true;
	}
}
