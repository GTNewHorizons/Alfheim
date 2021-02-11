package alfheim.client.core.helper

import alfheim.api.ModInfo
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.item.Item
import net.minecraft.util.IIcon

object IconHelper {
	
	fun forName(ir: IIconRegister, name: String): IIcon = ir.registerIcon("${ModInfo.MODID}:$name")
	
	fun forName(ir: IIconRegister, name: String, dir: String): IIcon = ir.registerIcon("${ModInfo.MODID}:$dir/$name")
	
	fun forBlock(ir: IIconRegister, block: Block): IIcon = forName(ir, block.unlocalizedName.replace("tile\\.".toRegex(), ""))
	
	fun forBlock(ir: IIconRegister, block: Block, i: Int): IIcon = forBlock(ir, block, "$i")
	
	fun forBlock(ir: IIconRegister, block: Block, i: Int, dir: String): IIcon = forBlock(ir, block, "$i", dir)
	
	fun forBlock(ir: IIconRegister, block: Block, s: String): IIcon = forName(ir, block.unlocalizedName.replace("tile\\.".toRegex(), "") + s)
	
	fun forBlock(ir: IIconRegister, block: Block, s: String, dir: String): IIcon = forName(ir, block.unlocalizedName.replace("tile\\.".toRegex(), "") + s, dir)
	
	fun forItem(ir: IIconRegister, item: Item): IIcon = forName(ir, item.unlocalizedName.replace("item\\.".toRegex(), ""))
	
	fun forItem(ir: IIconRegister, item: Item, i: Int): IIcon = forItem(ir, item, "$i")
	
	fun forItem(ir: IIconRegister, item: Item, i: Int, dir: String): IIcon = forItem(ir, item, "$i", dir)
	
	fun forItem(ir: IIconRegister, item: Item, s: String): IIcon = forName(ir, item.unlocalizedName.replace("item\\.".toRegex(), "") + s)
	
	fun forItem(ir: IIconRegister, item: Item, s: String, dir: String): IIcon = forName(ir, item.unlocalizedName.replace("item\\.".toRegex(), "") + s, dir)
}
