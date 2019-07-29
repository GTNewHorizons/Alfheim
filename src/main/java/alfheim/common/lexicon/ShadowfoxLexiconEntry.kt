package alfheim.common.lexicon

import alfheim.api.ModInfo
import net.minecraft.block.Block
import net.minecraft.item.*
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.*

open class ShadowfoxLexiconEntry: LexiconEntry, IAddonEntry {
	
	constructor(unlocalizedName: String, category: LexiconCategory, stack: ItemStack?): super(unlocalizedName, category) {
		if (stack != null) icon = stack
		BotaniaAPI.addEntry(this, category)
	}
	
	constructor(unlocalizedName: String, category: LexiconCategory, block: Block): this(unlocalizedName, category, ItemStack(block))
    
    constructor(unlocalizedName: String, category: LexiconCategory, item: Item): this(unlocalizedName, category, ItemStack(item))
    
    constructor(unlocalizedName: String, category: LexiconCategory): this(unlocalizedName, category, null as ItemStack?)
    
    override fun setLexiconPages(vararg pages: LexiconPage): LexiconEntry {
		for (page in pages) {
			page.unlocalizedName = "${ModInfo.MODID}.page." + getLazyUnlocalizedName() + page.unlocalizedName
			if (page is ITwoNamedPage) {
				page.secondUnlocalizedName = "${ModInfo.MODID}.page." + getLazyUnlocalizedName() + page.secondUnlocalizedName
			}
		}
		
		return super.setLexiconPages(*pages)
	}
	
	override fun getUnlocalizedName() = "${ModInfo.MODID}.entry.${super.getUnlocalizedName()}"
	
	override fun getTagline() = "${ModInfo.MODID}.tagline.${super.getUnlocalizedName()}"
	
	override fun getSubtitle() = "[Botanical Addons]"
	
	fun getLazyUnlocalizedName() = super.getUnlocalizedName()!!
}
