package alfheim.common.integration.waila.handler;

import java.util.List;

import alfheim.common.block.tile.TileAnyavil;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WAILAHandlerAnyavil implements IWailaDataProvider {
	
	private static final String TAG_ITEM = "waila:item";
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound result, World world, int x, int y, int z) {
		if (tile instanceof TileAnyavil) {
			ItemStack stack = ((TileAnyavil) tile).getItem();
			if (stack != null)
				result.setString(TAG_ITEM, String.format("%s (%d/%d)", stack.getDisplayName(), stack.getMaxDamage() - stack.getItemDamage(), stack.getMaxDamage()));
			else result.removeTag(TAG_ITEM);
		}
		return result;
	}
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}
	
	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}
	
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		NBTTagCompound tag = accessor.getNBTData();
		
		if (tag.hasKey(TAG_ITEM)) 
			currenttip.add(tag.getString(TAG_ITEM));
		
		return currenttip;
	}
	
	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}
}