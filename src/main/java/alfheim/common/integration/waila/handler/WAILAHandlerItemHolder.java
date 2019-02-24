package alfheim.common.integration.waila.handler;

import java.util.List;

import alfheim.common.block.tile.TileItemHolder;
import alfheim.common.core.util.AlfheimConfig;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaItem;

public class WAILAHandlerItemHolder implements IWailaDataProvider {

	public static final String TAG_MANA = "mana";
	public static final String TAG_MAX_MANA = "maxmana";

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound result, World world, int x, int y, int z) {
		if (tile instanceof TileItemHolder) {
			ItemStack stack = ((TileItemHolder) tile).getItem();
			if (stack != null && stack.getItem() instanceof IManaItem) {
				IManaItem mana = (IManaItem) stack.getItem();
				result.setInteger(TAG_MANA, mana.getMana(stack));
				result.setInteger(TAG_MAX_MANA, mana.getMaxMana(stack));
			}
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
		
		if (tag.hasKey(TAG_MANA) && tag.hasKey(TAG_MAX_MANA)) 
			currenttip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted("alfheimmisc.waila.holder.mana", AlfheimConfig.numericalMana ? (tag.getInteger(TAG_MANA) + "/" + tag.getInteger(TAG_MAX_MANA)) : (tag.getInteger(TAG_MANA) * 100 / tag.getInteger(TAG_MAX_MANA) + "%")));
		
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}
}