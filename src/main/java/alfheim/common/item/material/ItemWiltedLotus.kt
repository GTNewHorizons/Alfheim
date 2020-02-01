package alfheim.common.item.material

import alfheim.api.ModInfo
import alfheim.common.core.util.meta
import alfheim.common.item.ItemMod
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.StatCollector

class ItemWiltedLotus : ItemMod("wiltedLotus") {

    init {
        setHasSubtypes(true)
    }

    override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
        for (i in 0..1) list.add(ItemStack(item, 1, i))
    }

    override fun hasEffect(par1ItemStack: ItemStack, pass: Int) = par1ItemStack.meta > 0

    override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>, adv: Boolean) {
        list.add(StatCollector.translateToLocal("misc.${ModInfo.MODID}:lotusDesc"))
    }

    override fun getUnlocalizedNameInefficiently(stack: ItemStack) =
        super.getUnlocalizedNameInefficiently(stack) + stack.meta
}
