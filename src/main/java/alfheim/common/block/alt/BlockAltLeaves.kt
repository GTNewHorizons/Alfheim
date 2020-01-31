package alfheim.common.block.alt

import alfheim.api.lib.LibOreDict.ALT_TYPES
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.core.helper.IconHelper
import alfheim.common.item.block.ItemUniqueSubtypedBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.*
import java.util.*

class BlockAltLeaves(): BlockLeavesMod() {
	
	var icon_norm: Array<IIcon> = emptyArray()
	var icon_opaque: Array<IIcon> = emptyArray()
	
	init {
		setBlockName("altLeaves")
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBlockColor() = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int) = 0xFFFFFF
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess?, x: Int, y: Int, z: Int) = 0xFFFFFF
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemUniqueSubtypedBlockMod::class.java, name)
	}
	
	override fun registerBlockIcons(iconRegister: IIconRegister) {
		icon_norm = Array(6) { i -> IconHelper.forBlock(iconRegister, this, ALT_TYPES[i]) }
		icon_opaque = Array(6) { i -> IconHelper.forBlock(iconRegister, this, "${ALT_TYPES[i]}_opaque") }
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics)
		return if (field_150121_P) icon_norm[meta and decayBit().inv()] else icon_opaque[meta and decayBit().inv()]
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) =
		Item.getItemFromBlock(AlfheimBlocks.irisSapling)!!
	
	override fun quantityDropped(random: Random) = if (random.nextInt(60) == 0) 1 else 0
	
	override fun func_150125_e() = arrayOf("altLeaves")
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>?) {
		if (list != null && item != null)
			for (i in 0..5) {
				list.add(ItemStack(item, 1, i))
			}
	}
	
	override fun decayBit() = 0x8
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.irisSapling
}
