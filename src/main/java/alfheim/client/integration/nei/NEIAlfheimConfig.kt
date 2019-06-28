package alfheim.client.integration.nei

import alfheim.api.ModInfo
import alfheim.client.integration.nei.recipes.*
import alfheim.common.core.registry.AlfheimBlocks
import codechicken.nei.api.*
import net.minecraft.item.ItemStack
import vazkii.botania.common.block.ModBlocks

class NEIAlfheimConfig: IConfigureNEI {
	
	override fun loadConfig() {
		API.registerRecipeHandler(RecipeHandlerManaInfuser())
		API.registerUsageHandler(RecipeHandlerManaInfuser())
		API.registerRecipeHandler(RecipeHandlerTradePortal())
		API.registerUsageHandler(RecipeHandlerTradePortal())
		
		API.hideItem(ItemStack(ModBlocks.gaiaHead))
		API.hideItem(ItemStack(AlfheimBlocks.anomaly))
		API.hideItem(ItemStack(AlfheimBlocks.flugelHead))
		API.hideItem(ItemStack(AlfheimBlocks.poisonIce))
		API.hideItem(ItemStack(AlfheimBlocks.redFlame))
	}
	
	override fun getName(): String {
		return ModInfo.MODID
	}
	
	override fun getVersion(): String {
		return ModInfo.VERSION
	}
}