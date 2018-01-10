package alfheim.common.item.equipment.baubles;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.core.utils.AlfheimConfig;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class ItemManaStorage extends Item implements IManaItem, IManaTooltipDisplay, IBauble {
	
	public static BaubleType type;
	public int MAX_MANA;
	public static final String TAG_MANA = "mana";
	
	public ItemManaStorage(String name, double maxManaCap, BaubleType type) {
		this.type = type;
		MAX_MANA = (int) (TilePool.MAX_MANA * maxManaCap);
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setMaxDamage(1000);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setTextureName(Constants.MODID + ':' + name);
		this.setUnlocalizedName(name);
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 1000));
		ItemStack full = new ItemStack(par1, 1, 1);
		setMana(full, MAX_MANA);
		par3List.add(full);
	}

	@Override
	public int getDamage(ItemStack stack) {
		float mana = getMana(stack);
		return 1000 - (int) (mana / getMaxMana(stack) * 1000);
	}

	@Override
	public int getDisplayDamage(ItemStack stack) {
		return getDamage(stack);
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	public static void setMana(ItemStack stack, int mana) {
		ItemNBTHelper.setInt(stack, TAG_MANA, mana);
	}

	@Override
	public int getMana(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return MAX_MANA;
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		setMana(stack, Math.min(getMana(stack) + mana, getMaxMana(stack)));
		stack.setItemDamage(getDamage(stack));
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
		return true;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return true;
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

	@Override
	public float getManaFractionForDisplay(ItemStack stack) {
		return (float) getMana(stack) / (float) getMaxMana(stack);
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return this.type;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		// NO-OP
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase entity) {
		// NO-OP
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
		// NO-OP	
	}

	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase entity) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack stack, EntityLivingBase entity) {
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		list.add(StatCollector.translateToLocalFormatted("item.manastorage.desc0", this.MAX_MANA / TilePool.MAX_MANA));
		if (AlfheimConfig.showNumbersInTooltip) list.add(StatCollector.translateToLocalFormatted("item.manastorage.desc1", getMana(stack), getMaxMana(stack)));
	}
}
