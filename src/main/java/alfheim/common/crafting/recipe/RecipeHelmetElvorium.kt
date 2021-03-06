package alfheim.common.crafting.recipe

import alexsocol.asjlib.get
import alfheim.api.lib.LibOreDict.ARUNE
import alfheim.api.lib.LibOreDict.ELVORIUM_INGOT
import alfheim.api.lib.LibOreDict.INFUSED_DREAM_TWIG
import alfheim.api.lib.LibOreDict.MAUFTRIUM_INGOT
import alfheim.common.item.AlfheimItems
import alfheim.common.item.AlfheimItems.elvenResource
import alfheim.common.item.material.ElvenResourcesMetas.ManaInfusionCore
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.*
import net.minecraftforge.oredict.ShapedOreRecipe
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ModItems

class RecipeHelmetElvorium(out: Item, helm: Item): ShapedOreRecipe(out, "TRT", "EHE", "CMC", 'T', INFUSED_DREAM_TWIG, 'R', ARUNE[0], 'E', ELVORIUM_INGOT, 'H', helm, 'C', ItemStack(elvenResource, 1, ManaInfusionCore), 'M', MAUFTRIUM_INGOT) {
	
	override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
		val helmCopy = inv[4]?.copy()
		
		val newHelm = when (helmCopy?.item) {
			ModItems.terrasteelHelm          -> ItemStack(AlfheimItems.elvoriumHelmet)
			ModItems.terrasteelHelmRevealing -> ItemStack(AlfheimItems.elvoriumHelmetRevealing)
			else                             -> return null
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
}
