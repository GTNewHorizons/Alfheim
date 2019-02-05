package alfheim.common.item.block;

import alfheim.api.block.tile.SubTileEntity;
import alfheim.common.block.tile.TileAnomaly;
import alfheim.common.core.registry.AlfheimBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class ItemBlockAnomaly extends ItemBlock {

	public ItemBlockAnomaly(Block block) {
		super(block);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "tile.Anomaly." + ItemNBTHelper.getString(stack, SubTileEntity.TAG_TYPE, "undefined");
	}
	
	@Override
	public int getMetadata(int meta) {
		return meta;
	}
	
	public static String getType(ItemStack stack) {
		return ItemNBTHelper.detectNBT(stack) ? ItemNBTHelper.getString(stack, SubTileEntity.TAG_TYPE, "") : "";
	}
	
	public static ItemStack ofType(String type) {
		return ofType(new ItemStack(AlfheimBlocks.anomaly), type);
	}

	public static ItemStack ofType(ItemStack stack, String type) {
		ItemNBTHelper.setString(stack, SubTileEntity.TAG_TYPE, type);
		return stack;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		if(placed) {
			String type = getType(stack);
			TileEntity te = world.getTileEntity(x, y, z);
			if(te instanceof TileAnomaly) {
				/*TileAnomaly tile = */
				((TileAnomaly) te).setSubTile(type);
//				tile.onBlockAdded(world, x, y, z);
//				tile.onBlockPlacedBy(world, x, y, z, player, stack);
				if(!world.isRemote)
					world.markBlockForUpdate(x, y, z);
			}
		}

		return placed;
	}
}
