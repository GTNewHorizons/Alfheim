package alfmod.common.lexicon

import alfmod.AlfheimModularCore
import alfmod.common.item.AlfheimModularItems
import alfmod.common.item.material.EventResourcesMetas
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.*
import vazkii.botania.common.lexicon.WLexiconEntry
import vazkii.botania.common.lexicon.page.PageText

@Suppress("JoinDeclarationAndAssignment")
object AlfheimModularLexiconData {
	
	var categoryModular: LexiconCategory
	
	var HV: LexiconEntry
	var WOTW: LexiconEntry
	
	init {
		categoryModular = object: LexiconCategory("alfmod.category.AlfMod") {
			init {
				setPriority(4)
				icon = ResourceLocation(AlfheimModularCore.MODID, "textures/gui/category/AlfMod.png")
			}
		}
		
		BotaniaAPI.addCategory(categoryModular)
		
		HV = AMLexiconEntry("HV", categoryModular)
		WOTW = AMLexiconEntry("WOTW", categoryModular)
		
		HV.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"), PageText("4"))
			.setKnowledgeType(BotaniaAPI.elvenKnowledge)
			.icon = ItemStack(AlfheimModularItems.eventResource, 1, EventResourcesMetas.VolcanoRelic)
		
		WOTW.setLexiconPages(PageText("0"), PageText("1"), PageText("2"))
			.setKnowledgeType(BotaniaAPI.elvenKnowledge)
			.icon = ItemStack(AlfheimModularItems.eventResource, 1, EventResourcesMetas.SnowRelic)
	}
}

class AMLexiconEntry(unlocalizedName: String, category: LexiconCategory): LexiconEntry(unlocalizedName, category) {
	
	init {
		BotaniaAPI.addEntry(this, category)
	}
	
	override fun setLexiconPages(vararg pages: LexiconPage): LexiconEntry {
		for (element in pages) {
			element.unlocalizedName = "alfmod.page." + lazyUnlocalizedName + element.unlocalizedName
			if (element is ITwoNamedPage) {
				val dou = element as ITwoNamedPage
				dou.secondUnlocalizedName = "alfmod.page." + lazyUnlocalizedName + dou.secondUnlocalizedName
			}
		}
		
		return super.setLexiconPages(*pages)
	}
	
	override fun getUnlocalizedName(): String {
		return "alfmod.entry." + super.getUnlocalizedName()
	}
	
	override fun getTagline(): String {
		return "alfmod.tagline." + super.getUnlocalizedName()
	}
	
	val lazyUnlocalizedName: String
		get() = super.getUnlocalizedName()
	
	override fun getWebLink() = "https://www.google.com"
	
	override fun compareTo(other: LexiconEntry): Int {
		return if (other is WLexiconEntry) 1 else super.compareTo(other)
	}
}
