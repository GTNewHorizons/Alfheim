package alfheim.client.core.helper

import alfheim.api.ModInfo
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.item.Item
import net.minecraft.util.IIcon
import vazkii.botania.client.render.block.InterpolatedIcon

object InterpolatedIconHelper {
	
	fun forName(map: TextureMap, name: String): IIcon? {
		val localIcon = InterpolatedIcon("${ModInfo.MODID}:$name")
		if (map.setTextureEntry("${ModInfo.MODID}:$name", localIcon)) {
			return localIcon
		}
		return null
	}
	
	fun forName(map: TextureMap, name: String, dir: String): IIcon? = forName(map, "$dir/$name")
	
	fun forBlock(map: TextureMap, block: Block): IIcon? = forName(map, block.unlocalizedName.replace("tile\\.".toRegex(), ""))
	
	fun forBlock(map: TextureMap, block: Block, i: Int): IIcon? = forBlock(map, block, "$i")
	
	fun forBlock(map: TextureMap, block: Block, i: Int, dir: String): IIcon? = forBlock(map, block, "$i", dir)
	
	fun forBlock(map: TextureMap, block: Block, s: String): IIcon? = forName(map, block.unlocalizedName.replace("tile\\.".toRegex(), "") + s)
	
	fun forBlock(map: TextureMap, block: Block, s: String, dir: String): IIcon? = forName(map, block.unlocalizedName.replace("tile\\.".toRegex(), "") + s, dir)
	
	fun forItem(map: TextureMap, item: Item): IIcon? = forName(map, item.unlocalizedName.replace("item\\.".toRegex(), ""))
	
	fun forItem(map: TextureMap, item: Item, i: Int): IIcon? = forItem(map, item, "$i")
	
	fun forItem(map: TextureMap, item: Item, i: Int, dir: String): IIcon? = forItem(map, item, "$i", dir)
	
	fun forItem(map: TextureMap, item: Item, s: String): IIcon? = forName(map, item.unlocalizedName.replace("item\\.".toRegex(), "") + s)
	
	fun forItem(map: TextureMap, item: Item, s: String, dir: String): IIcon? = forName(map, item.unlocalizedName.replace("item\\.".toRegex(), "") + s, dir)
}
