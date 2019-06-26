package alfheim.common.item.relic;

import static vazkii.botania.common.core.helper.ItemNBTHelper.*;

import java.awt.Color;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.relic.ItemRelic;

@Deprecated
public class ItemMjolnir extends ItemRelic {

	public static final String TAG_CHARGE = "charge", TAG_CREATIVE = "creative";
	public static final int MAX_CHARGE = 10000, CHARGE_PER_TICK = 1000;
	public static final IIcon[] icons = new IIcon[2];
	
	public ItemMjolnir() {
		super("Mjolnir");
		setCreativeTab(AlfheimCore.alfheimTab);
		setHasSubtypes(true);
		setFull3D();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		pass = pass == 1 && getCharge(stack) >= MAX_CHARGE ? 1 : 0;
		return pass == 1 ? Color.HSBtoRGB((200 + (float)(Math.sin(Botania.proxy.getWorldElapsedTicks() / 10.0 % 20) * 20)) / 360F, 0.5F, 1F) : 0xFFFFFFFF;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		for (int i = 0; i < icons.length; i++) icons[i] = IconHelper.forItem(reg, this, i);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		return icons[pass];
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public int getRenderPasses(int meta) {
		return 2;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item));
		
		ItemStack creative = new ItemStack(item);
		setBoolean(creative, TAG_CREATIVE, true);
		setCharge(creative, MAX_CHARGE);
		list.add(creative);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int itemInUseCount) {
		if (getCharge(stack) >= MAX_CHARGE && !world.isRemote) {
			MovingObjectPosition mop = ASJUtilities.getSelectedBlock(player, 256, true); 
			if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) world.addWeatherEffect(new EntityLightningBolt(world, mop.blockX, mop.blockY + 1, mop.blockZ));
		}
		if (!getBoolean(stack, TAG_CREATIVE, false)) setCharge(stack, 0);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int coitemInUseCountunt) {
		if (player.worldObj.isRemote) return;
		if (getCharge(stack) < MAX_CHARGE) addCharge(stack, player.capabilities.isCreativeMode ? CHARGE_PER_TICK : ManaItemHandler.requestMana(stack, player, CHARGE_PER_TICK, true));
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.bow;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (player.capabilities.isCreativeMode || ManaItemHandler.requestManaExact(stack, player, MAX_CHARGE, false)) player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		return stack;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotID, boolean inHand) {
		super.onUpdate(stack, world, entity, slotID, inHand);
		if (entity instanceof EntityPlayer && !world.isRemote && (getCharge(stack) >= MAX_CHARGE && !getBoolean(stack, TAG_CREATIVE, false) && !inHand) || (getBoolean(stack, TAG_CREATIVE, false) && inHand && stack.getDisplayName().toLowerCase().trim().equals("banhammer"))) onPlayerStoppedUsing(stack, world, (EntityPlayer) entity, 0);
	}
	
	public static void addCharge(ItemStack stack, int charge) {
		setInt(stack, TAG_CHARGE, getCharge(stack) + charge);
	}
	
	public static void setCharge(ItemStack stack, int charge) {
		setInt(stack, TAG_CHARGE, charge);
	}
	
	public static int getCharge(ItemStack stack) {
		return getInt(stack, TAG_CHARGE, 0);
	}
}