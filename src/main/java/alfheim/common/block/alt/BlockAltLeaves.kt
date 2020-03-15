package alfheim.common.block.alt

import alfheim.api.lib.LibOreDict.ALT_TYPES
import alfheim.client.core.util.mc
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.safeGet
import alfheim.common.item.block.ItemUniqueSubtypedBlockMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.*
import java.util.*

class BlockAltLeaves: BlockLeavesMod() {
	
	companion object {
		lateinit var textures: Array<Array<IIcon>>
		val yggMeta = ALT_TYPES.indexOf("Wisdom")
	}
	
	init {
		setBlockName("altLeaves")
	}
	
	override fun getBlockHardness(world: World, x: Int, y: Int, z: Int) =
		if (world.getBlockMetadata(x, y, z) == yggMeta) -1f else super.getBlockHardness(world, x, y ,z)
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemUniqueSubtypedBlockMod::class.java, name)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		textures = arrayOf(Array(ALT_TYPES.size) { i -> IconHelper.forBlock(reg, this, ALT_TYPES[i]) }, Array(ALT_TYPES.size) { i -> IconHelper.forBlock(reg, this, "${ALT_TYPES[i]}_opaque") })
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		setGraphicsLevel(mc.gameSettings.fancyGraphics)
		return textures[field_150127_b].safeGet(meta and decayBit().inv())
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) =
		Item.getItemFromBlock(AlfheimBlocks.irisSapling)!!
	
	override fun quantityDropped(random: Random) = if (random.nextInt(60) == 0) 1 else 0
	
	override fun func_150125_e() = arrayOf("altLeaves")
	
	override fun getSubBlocks(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in ALT_TYPES.indices) {
			list.add(ItemStack(item, 1, i))
		}
	}
	
	override fun decayBit() = 0x8
	
	override fun canDecay(meta: Int) = if (meta == yggMeta) false else super.canDecay(meta)
	
	override fun getEntry(p0: World, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = if (p0.getBlockMetadata(p1, p2, p3) == yggMeta) null else ShadowFoxLexiconData.irisSapling
}
