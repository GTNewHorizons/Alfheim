package alfheim.common.items;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.ModInfo;
import alfheim.common.registry.AlfheimItems;
import alfheim.common.registry.AlfheimRegistry;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

public class ElementalArmor extends ItemArmor implements IManaUsingItem {

	public static final int ONEBLOCKCOST = 10;
	public static final int MANA_PER_DAMAGE = 70;
	
	public ElementalArmor(int armorpart) {
		super(AlfheimRegistry.ELEMENTAL, 0, armorpart);
		this.setCreativeTab(AlfheimCore.alfheimTab);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if (armorType == 0 && player.isInWater()) {
			player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 1, -1));
			player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 3, -1));
		}
		if (armorType == 1) player.addPotionEffect(new PotionEffect(Potion.resistance.id, 1, 1));
		if (armorType == 2) {
			player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 1, -1));
			player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 1, 1));
			if (player.isBurning()) player.extinguish();
		}
		
		if(!world.isRemote && stack.getItemDamage() > 0 && ManaItemHandler.requestManaExact(stack, player, MANA_PER_DAMAGE * 2, true))
			stack.setItemDamage(stack.getItemDamage() - 1);
    }

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		return ModInfo.MODID + ":textures/armor/ElementalArmor_" + ((armorType == 2) ? "1" : "0") + ".png";
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		if (stack.getItem() == AlfheimItems.elementalBoots) list.add(StatCollector.translateToLocal("item.ElementalArmor.desc1"));
		if (stack.getItem() == AlfheimItems.elementalLeggings) list.add(StatCollector.translateToLocal("item.ElementalArmor.desc2"));
		if (stack.getItem() == AlfheimItems.elementalChestplate) list.add(StatCollector.translateToLocal("item.ElementalArmor.desc3"));
		if (stack.getItem() == AlfheimItems.elementalHelmet) list.add(StatCollector.translateToLocal("item.ElementalArmor.desc4"));
	}
	
	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
	
	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event) {
		if (armorType == 3 && event.entityLiving instanceof EntityPlayer && ((EntityPlayer) event.entityLiving).getCurrentArmor(0) != null && ((EntityPlayer) event.entityLiving).getCurrentArmor(0).getItem() == AlfheimItems.elementalBoots && ManaItemHandler.requestManaExact(((EntityPlayer) event.entityLiving).getCurrentArmor(0), (EntityPlayer) event.entityLiving, ONEBLOCKCOST * 10, true)) {
			event.entityLiving.motionY += 0.5;
		}
	}
	
	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event) {
		if (armorType == 3 && event.entityLiving instanceof EntityPlayer && ((EntityPlayer) event.entityLiving).getCurrentArmor(0) != null && ((EntityPlayer) event.entityLiving).getCurrentArmor(0).getItem() == AlfheimItems.elementalBoots) {
			if (event.distance < 4.5063215) event.distance = 0;

			if (event.distance >= 4.5063215) {
				int decrease = ManaItemHandler.requestMana(((EntityPlayer) event.entityLiving).getCurrentArmor(0), (EntityPlayer) event.entityLiving, MathHelper.floor_float(event.distance) * ONEBLOCKCOST, true);
				event.distance -= decrease / ONEBLOCKCOST;
			}
		}
	}
}