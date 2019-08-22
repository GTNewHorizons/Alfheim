package alfheim.common.item

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.core.helper.IconHelper
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.*
import vazkii.botania.common.Botania
import java.awt.Color
import kotlin.properties.Delegates

open class ItemIridescent(name: String) : Item() {

    companion object {
        const val TYPES = 16
        
        fun rainbowColor() = Color.HSBtoRGB(Botania.proxy.worldElapsedTicks * 2 % 360 / 360F, 1F, 1F)

        fun colorFromItemStack(par1ItemStack: ItemStack): Int {
            if (par1ItemStack.itemDamage == TYPES) {
                return rainbowColor()
            }
            if (par1ItemStack.itemDamage >= EntitySheep.fleeceColorTable.size)
                return 0xFFFFFF

            val color = EntitySheep.fleeceColorTable[par1ItemStack.itemDamage]
            return Color(color[0], color[1], color[2]).rgb
        }

        fun dirtFromMeta(meta: Int): Block? {
            if (meta == TYPES)
                return ShadowFoxBlocks.rainbowDirtBlock
            return ShadowFoxBlocks.coloredDirtBlock
        }

        fun dirtStack(meta: Int): ItemStack? {
            if (meta == TYPES)
                return ItemStack(ShadowFoxBlocks.rainbowDirtBlock)
            return ItemStack(ShadowFoxBlocks.coloredDirtBlock, 1, meta)
        }

        fun isRainbow(meta: Int) = meta == TYPES
    }

    init {
        setHasSubtypes(true)
        creativeTab = AlfheimCore.baTab
        unlocalizedName = name
    }

    var overlayIcon: IIcon by Delegates.notNull()

    override fun requiresMultipleRenderPasses() = true

    override fun setUnlocalizedName(par1Str: String): Item {
        GameRegistry.registerItem(this, par1Str)
        return super.setUnlocalizedName(par1Str)
    }

    override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
        super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.".toRegex(), "item.${ModInfo.MODID}:")

    @SideOnly(Side.CLIENT)
    override fun getIconFromDamageForRenderPass(meta: Int, pass: Int) =
        if (pass == 1) overlayIcon else super.getIconFromDamageForRenderPass(meta, pass)!!

    @SideOnly(Side.CLIENT)
    override fun registerIcons(iconRegister: IIconRegister) {
        itemIcon = IconHelper.forItem(iconRegister, this)
        overlayIcon = IconHelper.forItem(iconRegister, this, "Overlay")
    }

    override fun getColorFromItemStack(par1ItemStack: ItemStack, pass: Int): Int =
        if (pass > 0) 0xFFFFFF else colorFromItemStack(par1ItemStack)

    fun addStringToTooltip(s: String, tooltip: MutableList<Any?>?) {
        tooltip!!.add(s.replace("&".toRegex(), "\u00a7"))
    }

    override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>?, par4: Boolean) {
        if (par1ItemStack == null) return
        addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.color." + par1ItemStack.itemDamage) + "&r", par3List)
    }

    override fun getUnlocalizedName(par1ItemStack: ItemStack?) =
        if (par1ItemStack != null) getUnlocalizedNameLazy(par1ItemStack)!! else ""

    internal fun getUnlocalizedNameLazy(par1ItemStack: ItemStack) = super.getUnlocalizedName(par1ItemStack)
}
