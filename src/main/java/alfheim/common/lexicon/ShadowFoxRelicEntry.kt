package alfheim.common.lexicon

import alfheim.client.core.util.mc
import net.minecraft.block.Block
import net.minecraft.item.*
import vazkii.botania.api.lexicon.LexiconCategory

/**
 * @author WireSegal
 * Created at 6:22 PM on 2/4/16.
 */
class ShadowFoxRelicEntry : ShadowfoxLexiconEntry {
    constructor(unlocalizedName: String, category: LexiconCategory, stack: ItemStack) : super(unlocalizedName, category, stack)
    
    constructor(unlocalizedName: String, category: LexiconCategory, block: Block) : super(unlocalizedName, category, block)
    
    constructor(unlocalizedName: String, category: LexiconCategory, item: Item) : super(unlocalizedName, category, item)
    
    override fun isVisible(): Boolean = mc.thePlayer.inventory.hasItem(icon.item)
}