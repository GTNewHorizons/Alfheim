package alexsocol.asjlib.extendables.block

import alexsocol.asjlib.extendables.ItemBlockMetaName
import alfheim.api.ModInfo
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.World
import kotlin.math.*

open class BlockModMeta @JvmOverloads constructor(mat: Material, val subtypes: Int, val folder: String? = null): Block(mat) {
	
	lateinit var name: String
	lateinit var texture: Array<IIcon>
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockMetaName::class.java, name)
		this.name = name
		return super.setBlockName(name)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		texture = Array(subtypes) {
			reg.registerIcon("${ModInfo.MODID}:${if (folder != null) "$folder/" else ""}$name$it")
		}
	}
	
	override fun getSubBlocks(block: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		texture.indices.mapTo(list) { ItemStack(block, 1, it) }
	}
	
	override fun getIcon(side: Int, meta: Int) = texture[max(0, min(meta, texture.size - 1))]
	
	override fun damageDropped(meta: Int) = meta
	
	override fun getDamageValue(world: World, x: Int, y: Int, z: Int) = world.getBlockMetadata(x, y, z)
}
