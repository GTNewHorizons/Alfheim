package alfheim.common.item.equipment.armor.elvoruim;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.api.crafting.recipe.AlfheimAPI;
import alfheim.client.model.armor.ModelElvoriumArmor;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.IVisDiscountGear", striprefs = true)
public class ElvoriumArmor extends ItemManasteelArmor implements IManaDiscountArmor, IManaProficiencyArmor, IVisDiscountGear {

	public ElvoriumArmor(int type, String name) {
		super(type, name, AlfheimAPI.ELVORIUM);
		this.setCreativeTab(AlfheimCore.alfheimTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped provideArmorModelForSlot(ItemStack stack, int slot) {
		models[slot] = new ModelElvoriumArmor(slot);
		return models[slot];
	}
	
	@Override
	public String getArmorTextureAfterInk(ItemStack stack, int slot) {
		return Constants.MODID + ":textures/model/armor/ElvoriumArmor.png";
	}

	@Override
	public boolean getIsRepairable(ItemStack armor, ItemStack material) {
		return material.getItem() == AlfheimItems.elvenResource && material.getItemDamage() == ElvenResourcesMetas.ElvoriumIngot ? true : super.getIsRepairable(armor, material);
	}

	@Override
	public Multimap getItemAttributeModifiers() {
		Multimap multimap = HashMultimap.create();
		UUID uuid = new UUID(getUnlocalizedName().hashCode(), 0);
		multimap.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(uuid, "Terrasteel modifier " + type, (double) getArmorDisplay(null, new ItemStack(this), type) / 20, 0));
		return multimap;
	}

	static ItemStack[] armorset;

	@Override
	public ItemStack[] getArmorSetStacks() {
		if(armorset == null)
			armorset = new ItemStack[] {
				new ItemStack(AlfheimItems.elvoriumHelmet),
				new ItemStack(AlfheimItems.elvoriumChestplate),
				new ItemStack(AlfheimItems.elvoriumLeggings),
				new ItemStack(AlfheimItems.elvoriumBoots)
		};

		return armorset;
	}
	
	@Override
	public boolean hasArmorSetItem(EntityPlayer player, int i) {
		ItemStack stack = player.inventory.armorInventory[3 - i];
		if(stack == null)
			return false;

		switch(i) {
			case 0: return stack.getItem() == AlfheimItems.elvoriumHelmet || stack.getItem() == AlfheimItems.elvoriumHelmetRevealing;
			case 1: return stack.getItem() == AlfheimItems.elvoriumChestplate;
			case 2: return stack.getItem() == AlfheimItems.elvoriumLeggings;
			case 3: return stack.getItem() == AlfheimItems.elvoriumBoots;
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		itemIcon = reg.registerIcon(Constants.MODID + ':' + this.getUnlocalizedName().substring(5));
	}
	
	@Override
	public String getUnlocalizedNameInefficiently(ItemStack stack) {
		 String s = this.getUnlocalizedName(stack);
	     return s == null ? "" : StatCollector.translateToLocal(s);
	}
	
	@Override
	public String getArmorSetName() {
		return StatCollector.translateToLocal("alfheim.armorset.elvorium.name");
	}

	@Override
	public void addArmorSetDescription(ItemStack stack, List<String> list) {
		addStringToTooltip(StatCollector.translateToLocal("alfheim.armorset.elvorium.desc0"), list);	// -30% mana cost
		addStringToTooltip(StatCollector.translateToLocal("alfheim.armorset.elvorium.desc1"), list);	// Powerful rods
		if (Loader.isModLoaded("Thaumcraft")) addStringToTooltip(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("alfheim.armorset.elvorium.desc2"), list);	// -20% vis discount
		if (Loader.isModLoaded("Thaumcraft")) addStringToTooltip(EnumChatFormatting.GOLD		+ StatCollector.translateToLocal("alfheim.armorset.elvorium.desc3"), list);	// 8 pts of runic shield
		addStringToTooltip(StatCollector.translateToLocal("botania.armorset.terrasteel.desc1"), list);	// Regen w/o full hungerbar
		addStringToTooltip(StatCollector.translateToLocal("botania.armorset.terrasteel.desc2"), list);	// Passive mana regen
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotID, boolean inHand) {
		if(entity instanceof EntityPlayer)
			onArmorTick(world, (EntityPlayer) entity, stack);
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		super.onArmorTick(world, player, stack);
		if (!stack.hasTagCompound()) stack.stackTagCompound = new NBTTagCompound();
		if (player instanceof EntityPlayer) stack.stackTagCompound.setBoolean("SET", hasArmorSet((EntityPlayer) player));
	}
	
	@Override
	@Optional.Method(modid = "Thaumcraft")
	public int getRunicCharge(ItemStack stack) {
		if (!stack.hasTagCompound()) stack.stackTagCompound = new NBTTagCompound();
		return stack.stackTagCompound.getBoolean("SET") ? 2 : 0;
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, EntityPlayer player) {
		return hasArmorSet(player) ? (0.3F/4.0F) : 0F;
	}

	@Override
	public boolean shouldGiveProficiency(ItemStack stack, int slot, EntityPlayer player) {
		return hasArmorSet(player);
	}

	@Override
	public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) {
		return hasArmorSet(player) ? 5 : 0;
	}
}
