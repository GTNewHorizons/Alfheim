package alfheim.common.block

import alexsocol.asjlib.extendables.ItemBlockMetaName
import alfheim.api.ModInfo
import alfheim.api.lib.LibRenderIDs
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*
import net.minecraft.util.IIcon
import kotlin.math.*

open class BlockPaneMeta @JvmOverloads constructor(mat: Material, val subtypes: Int, val texName: String, val folder: String = ""): BlockPane(texName, "${texName}Top", mat, true) {
	
	lateinit var texture: Array<IIcon>
	lateinit var textureTop: Array<IIcon>
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockMetaName::class.java, name)
		return super.setBlockName(name)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		texture = Array(subtypes) {
			reg.registerIcon("${ModInfo.MODID}:$folder$texName$it")
		}
		
		textureTop = Array(subtypes) {
			reg.registerIcon("${ModInfo.MODID}:$folder$texName${it}Top")
		}
	}
	
	override fun getSubBlocks(block: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		texture.indices.mapTo(list) { ItemStack(block, 1, it) }
	}
	
	override fun damageDropped(meta: Int) = meta
	
	override fun getIcon(side: Int, meta: Int) = texture[max(0, min(meta, texture.size - 1))]
	
	fun getTopIcon(meta: Int) = textureTop[max(0, min(meta, texture.size - 1))]
	
	override fun getRenderType() = LibRenderIDs.idShrinePanel
}