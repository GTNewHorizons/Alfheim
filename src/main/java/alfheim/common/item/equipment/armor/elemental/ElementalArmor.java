package alfheim.common.item.equipment.armor.elemental;

import java.util.List;

import alfheim.Constants;
import alfheim.api.crafting.recipe.AlfheimAPI;
import alfheim.client.model.armor.ModelElementalArmor;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.item.IPixieSpawner;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

public abstract class ElementalArmor extends ItemManasteelArmor implements IPixieSpawner {

	public ElementalArmor(int type, String name) {
		super(type, name, AlfheimAPI.ELEMENTAL);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped provideArmorModelForSlot(ItemStack stack, int slot) {
		models[slot] = new ModelElementalArmor(slot);
		return models[slot];
	}
	
	@Override
	public String getArmorTextureAfterInk(ItemStack stack, int slot) {
		return Constants.MODID + ":textures/model/armor/ElementalArmor_" + (ConfigHandler.enableArmorModels ? "new" : armorType == 2 ? "1" : "0") + ".png";
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 7 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	static ItemStack[] armorset;

	@Override
	public ItemStack[] getArmorSetStacks() {
		if(armorset == null)
			armorset = new ItemStack[] {
				new ItemStack(AlfheimItems.elementalHelmet),
				new ItemStack(AlfheimItems.elementalChestplate),
				new ItemStack(AlfheimItems.elementalLeggings),
				new ItemStack(AlfheimItems.elementalBoots)
		};

		return armorset;
	}

	@Override
	public boolean hasArmorSetItem(EntityPlayer player, int i) {
		ItemStack stack = player.inventory.armorInventory[3 - i];
		if(stack == null)
			return false;

		switch(i) {
			case 0: return stack.getItem() == AlfheimItems.elementalHelmet || stack.getItem() == AlfheimItems.elementalHelmetRevealing;
			case 1: return stack.getItem() == AlfheimItems.elementalChestplate;
			case 2: return stack.getItem() == AlfheimItems.elementalLeggings;
			case 3: return stack.getItem() == AlfheimItems.elementalBoots;
		}

		return false;
	}

	@Override
	public String getArmorSetName() {
		return StatCollector.translateToLocal("alfheim.armorset.elemental.name");
	}

	@Override
	public void addArmorSetDescription(ItemStack stack, List<String> list) {
		super.addArmorSetDescription(stack, list);
		addStringToTooltip(StatCollector.translateToLocal("botania.armorset.elementium.desc"), list);
	}
}