package alfheim.client.integration.nei

import alfheim.AlfheimCore
import alfheim.client.integration.nei.recipes.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.item.AlfheimItems
import codechicken.nei.api.*
import net.minecraft.client.Minecraft
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
		API.hideItem(ItemStack(AlfheimBlocks.invisibleFlame))
		API.hideItem(ItemStack(AlfheimBlocks.powerStone, 1, 0))
		API.hideItem(ItemStack(AlfheimBlocks.rainbowFlame))
		API.hideItem(ItemStack(AlfheimBlocks.starBlock))
		API.hideItem(ItemStack(AlfheimBlocks.starBlock2))
		API.hideItem(ItemStack(AlfheimItems.flugelDisc2))
		API.hideItem(ItemStack(AlfheimItems.flugelHead2))
		if (Minecraft.getMinecraft().session.username != "AlexSocol")
			API.hideItem(ItemStack(AlfheimItems.royalStaff))
	}
	
	override fun getName() = AlfheimCore.meta.name!!
	
	override fun getVersion() = AlfheimCore.meta.version!!
}