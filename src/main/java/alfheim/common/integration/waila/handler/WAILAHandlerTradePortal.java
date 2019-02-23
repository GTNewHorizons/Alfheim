package alfheim.common.integration.waila.handler;

import java.util.List;

import alfheim.common.block.tile.TileTradePortal;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;

public class WAILAHandlerTradePortal implements IWailaDataProvider {
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound result, World world, int x, int y, int z) {
		if (tile instanceof TileTradePortal) {
			((TileTradePortal) tile).writeCustomNBT(result);
			result.removeTag(TileTradePortal.TAG_TICKS_OPEN);
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
		get: if (tag.hasKey(TileTradePortal.TAG_RECIPE_MULT) && tag.hasKey(TileTradePortal.TAG_RECIPE_NUM)) {
			int count = tag.getInteger(TileTradePortal.TAG_RECIPE_MULT);
			if (count <= 0) break get;
			
			int num = tag.getInteger(TileTradePortal.TAG_RECIPE_NUM);
			if (num < 0) break get;
			
			RecipeElvenTrade recipe = BotaniaAPI.elvenTradeRecipes.get(num);
			currenttip.add(StatCollector.translateToLocal("alfheimmisc.waila.trade.request") + recipe.getOutput().getDisplayName() + " x" + recipe.getOutput().stackSize);
			currenttip.add(StatCollector.translateToLocal("alfheimmisc.waila.trade.results"));
			for (Object o : recipe.getInputs()) {
				if (o instanceof String) currenttip.add(getOreName((String) o)); else
				if (o instanceof ItemStack) currenttip.add(((ItemStack) o).getDisplayName() + " x" + ((ItemStack) o).stackSize);
				else currenttip.add(o.toString());
			}
			currenttip.add(StatCollector.translateToLocalFormatted("alfheimmisc.waila.trade.available", count));
		}
		return currenttip;
	}
	
	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}
	
	private String getOreName(String o) {
		for(ItemStack stack : OreDictionary.getOres(o)) {
			ItemStack cstack = stack.copy();
			if(cstack.getItemDamage() == Short.MAX_VALUE)
				cstack.setItemDamage(0);
	
			return cstack.getDisplayName() + " x" + cstack.stackSize;
		}
		
		return o;
	}
}
