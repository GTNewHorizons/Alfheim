package alfheim.common.block.alt

import alexsocol.asjlib.render.*
import alfheim.api.lib.LibOreDict.ALT_TYPES
import alfheim.client.core.util.mc
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.*
import alfheim.common.item.block.ItemUniqueSubtypedBlockMod
import alfheim.common.lexicon.*
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.*
import vazkii.botania.api.lexicon.LexiconEntry
import java.util.*

class BlockAltLeaves: BlockLeavesMod(), IGlowingLayerBlock {
	
	companion object {
		lateinit var textures: Array<Array<IIcon>>
		lateinit var glowIcon: IIcon
		val yggMeta = ALT_TYPES.indexOf("Wisdom")
	}
	
	init {
		setBlockName("altLeaves")
	}
	
	override fun getBlockHardness(world: World, x: Int, y: Int, z: Int) = if (world.getBlockMetadata(x, y, z) % 8 == yggMeta) -1f else super.getBlockHardness(world, x, y, z)
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemUniqueSubtypedBlockMod::class.java, name)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		textures = arrayOf(
			Array(ALT_TYPES.size) { i -> IconHelper.forBlock(reg, this, ALT_TYPES[i]) },
			Array(ALT_TYPES.size) { i -> IconHelper.forBlock(reg, this, "${ALT_TYPES[i]}_opaque") }
		)
		
		glowIcon = IconHelper.forBlock(reg, this, "DreamwoodGlow")
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		setGraphicsLevel(mc.gameSettings.fancyGraphics)
		return textures[field_150127_b].safeGet(meta and decayBit().inv())
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = if (meta % 8 == yggMeta) null else if (meta % 8 == yggMeta+1) AlfheimBlocks.dreamSapling.toItem() else AlfheimBlocks.irisSapling.toItem()
	
	override fun func_150123_b(meta: Int) = if (meta == yggMeta) 0 else if (meta == yggMeta + 1) 100 else 60
	
	override fun quantityDropped(random: Random) = if (random.nextInt(func_150123_b(0)) == 0) 1 else 0
	
	override fun func_150125_e() = arrayOf("altLeaves")
	
	override fun getSubBlocks(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in ALT_TYPES.indices) {
			list.add(ItemStack(item, 1, i))
		}
	}
	
	override fun decayBit() = 0b1000
	
	override fun canDecay(meta: Int) = if (meta % 8 == yggMeta) false else super.canDecay(meta)
	
	override fun isLeaves(world: IBlockAccess, x: Int, y: Int, z: Int) = if (world.getBlockMetadata(x, y, z) % 8 == yggMeta) false else super.isLeaves(world, x, y, z)
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?): LexiconEntry? {
		val meta = world.getBlockMetadata(x, y, z)
		return when {
			meta % 8 == yggMeta + 1 -> AlfheimLexiconData.worldgen
			meta % 8 == yggMeta     -> null
			else                    -> ShadowFoxLexiconData.irisSapling
		}
	}
	
	override fun getRenderType() = RenderGlowingLayerBlock.glowBlockID
	
	override fun getGlowIcon(side: Int, meta: Int) = if (meta % 8 == 7) glowIcon else null
}
