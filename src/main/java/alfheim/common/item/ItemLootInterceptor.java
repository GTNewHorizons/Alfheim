package alfheim.common.item;

import static vazkii.botania.common.core.helper.ItemNBTHelper.*;

import org.apache.commons.lang3.ArrayUtils;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.common.block.tile.mana.TilePool;

public class ItemLootInterceptor extends Item implements IManaItem, IManaTooltipDisplay {

	public static final String TAG_IDS = "ids";
	public static final String TAG_METAS = "metas";
	public static final String TAG_MANA = "mana";
	public static final int[] EMPTY = new int[0];
	public static final int PER_ITEM = 60;
	
	
	public ItemLootInterceptor() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setMaxStackSize(1);
		setTextureName(ModInfo.MODID + ":LootInterceptor");
		setUnlocalizedName("LootInterceptor");
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity holder, int inSlot, boolean inHand) {
		if (holder instanceof EntityPlayer) {
			
			EntityPlayer player = (EntityPlayer) holder;
			ItemStack slot;
			int[] ids = getIDs(stack);
			int[] metas = getMetas(stack);
			
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				slot = player.inventory.getStackInSlot(i);
				
				if (slot != null) {
					int cid = Item.getIdFromItem(slot.getItem());
					int pos = -1;
					
					for (int id : ids) {
						++pos;
						
						if (id == cid) {
							if (metas[pos] == slot.getItemDamage()) {
								int size = slot.stackSize;
								player.inventory.setInventorySlotContents(i, null);
								addMana(stack, PER_ITEM * size);
							}
						}
					}
				}
			}
		}
	}
	
	public static void add(ItemStack stack, int id, int meta) {
		setIDs(stack, ArrayUtils.add(getIDs(stack), id));
		setMetas(stack, ArrayUtils.add(getMetas(stack), meta));
	}
	
	public static void setIDs(ItemStack stack, int[] ids) {
		setIntArray(stack, TAG_IDS, ids);
	}
	
	public static void setMetas(ItemStack stack, int[] metas) {
		setIntArray(stack, TAG_METAS, metas);
	}
	
	public static void setIntArray(ItemStack stack, String tag, int[] array) {
		getNBT(stack).setIntArray(tag, array);
	}
	
	public static int[] getIDs(ItemStack stack) {
		return getIntArray(stack, TAG_IDS, EMPTY);
	}
	
	public static int[] getMetas(ItemStack stack) {
		return getIntArray(stack, TAG_METAS, EMPTY);
	}
	
	public static int[] getIntArray(ItemStack stack, String tag, int[] defaultExpected) {
		return verifyExistance(stack, tag) ? getNBT(stack).getIntArray(tag) : defaultExpected;
	}

	@Override
	public float getManaFractionForDisplay(ItemStack stack) {
		return (float) getMana(stack) / (float) getMaxMana(stack);
	}

	@Override
	public int getMana(ItemStack stack) {
		return getInt(stack, TAG_MANA, 0);
	}

	public static void setMana(ItemStack stack, int mana) {
		setInt(stack, TAG_MANA, mana);
	}
	
	@Override
	public int getMaxMana(ItemStack stack) {
		return TilePool.MAX_MANA;
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		setMana(stack, Math.min(getMana(stack) + mana, getMaxMana(stack)));
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
		return false;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
		return true;
	}

	@Override
	public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
		return true;
	}

	@Override
	public boolean isNoExport(ItemStack stack) {
		return false;
	}
}
