package alfheim.common.item.block;

import static alfheim.common.block.tile.TileAnomaly.*;
import static vazkii.botania.common.core.helper.ItemNBTHelper.*;

import alfheim.common.block.BlockAnomaly;
import alfheim.common.block.tile.TileAnomaly;
import alfheim.common.core.registry.AlfheimBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.internal.VanillaPacketDispatcher;

public class ItemBlockAnomaly extends ItemBlock {
	
	public static final String TYPE_UNDEFINED = "undefined";
	
	public ItemBlockAnomaly(Block block) {
		super(block);
		setMaxStackSize(1);
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return BlockAnomaly.iconUndefined;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "tile.Anomaly." + getString(stack, TAG_SUBTILE_MAIN, TYPE_UNDEFINED);
	}
	
	@Override
	public int getMetadata(int meta) {
		return meta;
	}
	
	public static String getType(ItemStack stack) {
		return detectNBT(stack) ? getString(stack, TAG_SUBTILE_MAIN, TYPE_UNDEFINED) : TYPE_UNDEFINED;
	}
	
	public static ItemStack ofType(String type) {
		return ofType(new ItemStack(AlfheimBlocks.anomaly), type);
	}
	
	public static ItemStack ofType(ItemStack stack, String type) {
		if (type == null || type.isEmpty()) type = TYPE_UNDEFINED;
		setString(stack, TAG_SUBTILE_MAIN, type);
		setInt(stack, TAG_SUBTILE_COUNT, 1);
		setString(stack, TAG_SUBTILE_NAME + "1", type);
		
		return stack;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		if(placed) {
			TileEntity te = world.getTileEntity(x, y, z);
			if(te instanceof TileAnomaly) {
				TileAnomaly tile = (TileAnomaly) te;
				tile.readCustomNBT(getNBT(stack));
				
				if(!world.isRemote) {
					world.markBlockForUpdate(x, y, z);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
				}
			}
		}
		
		return placed;
	}
}
