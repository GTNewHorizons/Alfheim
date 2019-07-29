package alfheim.common.block.colored

import alfheim.AlfheimCore
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.base.IDoublePlant
import alfheim.common.item.block.*
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.stats.StatList
import net.minecraft.util.IIcon
import net.minecraft.world.*
import alfheim.common.lexicon.ShadowFoxLexiconData
import alfheim.common.core.helper.IconHelper
import vazkii.botania.api.lexicon.ILexiconable
import java.awt.Color
import java.util.*
import kotlin.properties.Delegates

class BlockColoredDoubleGrass(var colorSet: Int): BlockDoublePlant(), IDoublePlant, ILexiconable {
	
	val name = "irisDoubleGrass$colorSet"
	val TYPES: Int = 8
	var topIcon: IIcon by Delegates.notNull()
	var bottomIcon: IIcon by Delegates.notNull()
	
	init {
		setCreativeTab(AlfheimCore.baTab)
		setStepSound(Block.soundTypeGrass)
		setBlockNameSafe(name)
	}
	
	override fun func_149851_a(world: World, x: Int, y: Int, z: Int, isRemote: Boolean) = false
	
	override fun func_149853_b(world: World, random: Random, x: Int, y: Int, z: Int) = Unit
	
	fun isTop(meta: Int) = (meta and 8) != 0
	
	internal fun register(name: String) {
		when (colorSet) {
			1    -> GameRegistry.registerBlock(this, ItemIridescentTallGrassMod1::class.java, name)
			else -> GameRegistry.registerBlock(this, ItemIridescentTallGrassMod0::class.java, name)
		}
	}
	
	fun setBlockNameSafe(par1Str: String): Block {
		register(par1Str)
		return super.setBlockName(par1Str)
	}
	
	override fun setBlockName(par1Str: String) = null
	
	@SideOnly(Side.CLIENT)
	override fun getBlockColor() = 0xFFFFFF
	
	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int): Int {
		if (meta >= TYPES)
			return 0xFFFFFF
		
		val color = EntitySheep.fleeceColorTable[meta + TYPES * colorSet]
		return Color(color[0], color[1], color[2]).rgb
	}
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(access: IBlockAccess?, x: Int, y: Int, z: Int): Int {
		val meta = access!!.getBlockMetadata(x, y, z)
		return getRenderColor(meta)
	}
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>?) {
		if (list != null && item != null)
			for (i in 0 until TYPES) {
				list.add(ItemStack(item, 1, i))
			}
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(iconRegister: IIconRegister) {
		topIcon = IconHelper.forName(iconRegister, "irisDoubleGrassTop")
		bottomIcon = IconHelper.forName(iconRegister, "irisDoubleGrass")
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(side: Int, meta: Int) = topIcon
	
	@SideOnly(Side.CLIENT)
	override fun func_149888_a(top: Boolean, index: Int) = if (top) topIcon else bottomIcon
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.pastoralSeeds
	
	fun dropBlock(world: World, x: Int, y: Int, z: Int, meta: Int, player: EntityPlayer): Boolean {
		return if (isTop(meta)) false
		else {
			player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1)
			val b0 = TYPES * colorSet + meta
			this.dropBlockAsItem(world, x, y, z, ItemStack(ShadowFoxBlocks.irisGrass, 2, b0))
			true
		}
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = null
	
	override fun onSheared(item: ItemStack, world: IBlockAccess, x: Int, y: Int, z: Int, fortune: Int): ArrayList<ItemStack> {
		val ret = ArrayList<ItemStack>()
		val meta = world.getBlockMetadata(x, y, z)
		if (isTop(meta)) {
			if (y > 0 && world.getBlock(x, y - 1, z) == this) {
				val downMeta = world.getBlockMetadata(x, y - 1, z)
				val b0 = TYPES * colorSet + downMeta
				ret.add(ItemStack(ShadowFoxBlocks.irisGrass, 2, b0))
			}
		} else {
			val b0 = TYPES * colorSet + meta
			ret.add(ItemStack(ShadowFoxBlocks.irisGrass, 2, b0))
		}
		return ret
	}
	
	override fun getRenderType() = LibRenderIDs.idDoubleFlower
	
	override fun isShearable(item: ItemStack, world: IBlockAccess, x: Int, y: Int, z: Int): Boolean {
		val meta = world.getBlockMetadata(x, y, z)
		return !isTop(meta)
	}
	
	override fun getBottomIcon(lowerMeta: Int) = bottomIcon
	
	override fun getTopIcon(lowerMeta: Int) = topIcon
	
}
