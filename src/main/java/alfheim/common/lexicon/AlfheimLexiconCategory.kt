package alfheim.common.lexicon

import alfheim.api.ModInfo
import net.minecraft.util.ResourceLocation
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.LexiconCategory

class AlfheimLexiconCategory(unlocalizedName: String, priority: Int): LexiconCategory("${ModInfo.MODID}.category.$unlocalizedName") {
	
	init {
		icon = ResourceLocation("${ModInfo.MODID}:textures/gui/categories/$unlocalizedName.png")
		setPriority(priority)
		BotaniaAPI.addCategory(this)
	}
}
