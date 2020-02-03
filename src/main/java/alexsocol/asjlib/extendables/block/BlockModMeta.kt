package alexsocol.asjlib.extendables.block

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.extendables.ItemBlockMetaName
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.World
import kotlin.math.*

open class BlockModMeta @JvmOverloads constructor(mat: Material, val subtypes: Int, val modid: String, val name: String, tab: CreativeTabs? = null, hard: Float = 1f, harvTool: String = "pickaxe", harvLvl: Int = 1, resist: Float = 5f, val folder: String = ""): Block(mat) {
	
	lateinit var texture: Array<IIcon>
	
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
		texture = Array(subtypes) {
			reg.registerIcon("$modid:$folder$name$it")
		}
	}
	
	override fun getSubBlocks(block: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in 0 until subtypes) list.add(ItemStack(block, 1, i))
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon? = texture[max(0, min(meta, texture.size - 1))]
	
	override fun damageDropped(meta: Int) = meta
	
	override fun getDamageValue(world: World, x: Int, y: Int, z: Int) = world.getBlockMetadata(x, y, z)
}