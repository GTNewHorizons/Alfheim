package alexsocol.asjlib.extendables.block

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.ItemBlockMetaName
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.World
import kotlin.math.max

open class BlockModMeta @JvmOverloads constructor(mat: Material, val subtypes: Int, val modid: String, val name: String, tab: CreativeTabs? = null, hard: Float = 1f, harvTool: String = "pickaxe", harvLvl: Int = 1, resist: Float = 5f, val folder: String = ""): Block(mat) {
	
	lateinit var icons: Array<IIcon>
	
	init {
		setBlockName(name)
		setCreativeTab(tab)
		setHardness(hard)
		setHarvestLevel(harvTool, harvLvl)
		setResistance(max(resist, hard * 5f))
		setStepSound(ASJUtilities.soundFromMaterial(mat))
	}
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockMetaName::class.java, name)
		return super.setBlockName(name)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		icons = Array(subtypes) { reg.registerIcon("$modid:$folder$name${if (subtypes > 1) it else ""}") }
	}
	
	override fun getSubBlocks(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in 0 until subtypes) list.add(ItemStack(item, 1, i))
	}
	
	override fun getIcon(side: Int, meta: Int) = icons.safeGet(meta)
	
	override fun damageDropped(meta: Int) = meta
	
	override fun getDamageValue(world: World, x: Int, y: Int, z: Int) = world.getBlockMetadata(x, y, z)
}
