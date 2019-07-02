package alfheim.common.crafting.recipe

import alexsocol.asjlib.ASJUtilities
import alfheim.api.lib.LibOreDict.Companion.ARUNE
import alfheim.api.lib.LibOreDict.Companion.ELVORIUM_INGOT
import alfheim.api.lib.LibOreDict.Companion.MAUFTRIUM_INGOT
import alfheim.common.core.registry.AlfheimItems
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.lib.LibOreDict.DREAMWOOD_TWIG

class RecipeHelmetElvorium: IRecipe {
	
	private val slots = arrayOf(false, false, false, false, false, false, false, false, false)
	
	override fun matches(var1: InventoryCrafting, var2: World): Boolean {
		slots.fill(false)
		slots[0] = ASJUtilities.isOre(var1.getStackInSlot(0), DREAMWOOD_TWIG)
		slots[1] = ASJUtilities.isOre(var1.getStackInSlot(1), ARUNE[0])
		slots[2] = ASJUtilities.isOre(var1.getStackInSlot(2), DREAMWOOD_TWIG)
		slots[3] = ASJUtilities.isOre(var1.getStackInSlot(3), ELVORIUM_INGOT)
		slots[4] = checkHelm(var1.getStackInSlot(4))
		slots[5] = ASJUtilities.isOre(var1.getStackInSlot(5), ELVORIUM_INGOT)
		slots[6] = var1.getStackInSlot(6)?.item === AlfheimItems.elvenResource
				&& var1.getStackInSlot(6)?.itemDamage == AlfheimItems.ElvenResourcesMetas.ManaInfusionCore
		slots[7] = ASJUtilities.isOre(var1.getStackInSlot(7), MAUFTRIUM_INGOT)
		slots[8] = var1.getStackInSlot(8)?.item === AlfheimItems.elvenResource
				&& var1.getStackInSlot(8)?.itemDamage == AlfheimItems.ElvenResourcesMetas.ManaInfusionCore
			return slots.all { it }
	}
	
	override fun getCraftingResult(var1: InventoryCrafting): ItemStack? {
		val helmCopy = var1.getStackInSlot(4)?.copy()
		
		val newHelm = when (helmCopy?.item) {
			ModItems.terrasteelHelm -> ItemStack(AlfheimItems.elvoriumHelmet)
			ModItems.terrasteelHelmRevealing -> ItemStack(AlfheimItems.elvoriumHelmetRevealing)
			else -> return null
		}
		
		// Copy Ancient Wills
		for (i in 0..5)
			if (ItemNBTHelper.getBoolean(helmCopy, "AncientWill$i", false))
				ItemNBTHelper.setBoolean(newHelm, "AncientWill$i", true)
		
		// Copy Enchantments
		/*val enchList = ItemNBTHelper.getList(helmCopy, "ench", 10, true)
		if (enchList != null)
			ItemNBTHelper.setList(newHelm, "ench", enchList)*/
		
		// Copy Runic Hardening
		/*val runicHardening = ItemNBTHelper.getByte(helmCopy, "RS.HARDEN", 0.toByte())
		ItemNBTHelper.setByte(newHelm, "RS.HARDEN", runicHardening)*/
		
		return newHelm
	}
	
	override fun getRecipeSize(): Int {
		return 10
	}
	
	override fun getRecipeOutput(): ItemStack {
		return ItemStack(AlfheimItems.elvoriumHelmet)
	}
	
	private fun checkHelm(helmStack: ItemStack?): Boolean {
		if (helmStack == null) return false
		val helmItem = helmStack.item
		return helmItem === ModItems.terrasteelHelm || helmItem === ModItems.terrasteelHelmRevealing
	}
}
