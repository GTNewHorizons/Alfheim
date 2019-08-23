package alfheim.client.integration.nei

import alfheim.AlfheimCore
import alfheim.client.integration.nei.recipes.*
import alfheim.common.block.AlfheimBlocks
import codechicken.nei.api.*
import net.minecraft.item.ItemStack
import vazkii.botania.common.block.ModBlocks

class NEIAlfheimConfig: IConfigureNEI {
	
	override fun loadConfig() {
		API.registerRecipeHandler(RecipeHandlerManaInfuser())
		API.registerUsageHandler(RecipeHandlerManaInfuser())
		API.registerRecipeHandler(RecipeHandlerTradePortal())
		API.registerUsageHandler(RecipeHandlerTradePortal())
		API.registerRecipeHandler(RecipeHandlerTreeCrafting())
		API.registerUsageHandler(RecipeHandlerTreeCrafting())
		
		API.hideItem(ItemStack(ModBlocks.gaiaHead))
		API.hideItem(ItemStack(AlfheimBlocks.anomaly))
		API.hideItem(ItemStack(AlfheimBlocks.flugelHeadBlock))
		API.hideItem(ItemStack(AlfheimBlocks.flugelHead2Block))
		API.hideItem(ItemStack(AlfheimBlocks.poisonIce))
		API.hideItem(ItemStack(AlfheimBlocks.redFlame))
	}
	
	override fun getName() = AlfheimCore.meta.name!!
	
	override fun getVersion() = AlfheimCore.meta.version!!
}