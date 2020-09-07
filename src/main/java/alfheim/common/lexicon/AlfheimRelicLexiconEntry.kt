package alfheim.common.lexicon

import alexsocol.asjlib.mc
import net.minecraft.block.Block
import net.minecraft.item.*
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.LexiconCategory

/**
 * @author WireSegal
 * Created at 6:22 PM on 2/4/16.
 */
class AlfheimRelicLexiconEntry : AlfheimLexiconEntry {
    
    constructor(unlocalizedName: String, category: LexiconCategory, stack: ItemStack) : super(unlocalizedName, category, stack)
    
    constructor(unlocalizedName: String, category: LexiconCategory, block: Block) : super(unlocalizedName, category, block)
    
    constructor(unlocalizedName: String, category: LexiconCategory, item: Item) : super(unlocalizedName, category, item)
    
    init {
    	knowledgeType = BotaniaAPI.relicKnowledge
    }
    
    override fun isVisible(): Boolean = mc.thePlayer.capabilities.isCreativeMode || mc.thePlayer.inventory.hasItem(icon.item)
}
