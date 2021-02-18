package alfheim.common.block

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.api.ModInfo
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.*
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.common.item.ModItems
import java.util.*
import kotlin.math.max

class BlockElvenOre: BlockModMeta(Material.rock, 6, ModInfo.MODID, "ElvenOre", AlfheimTab, 2f, harvLvl = 2), ILexiconable {
	
	/**
	 * Meta of dropped item
	 */
	val metas = arrayOf(9, 1, 5, 3, ElvenResourcesMetas.IffesalDust, 4)
	val rand = Random()
	
	init {
		setHarvestLevel("pickaxe", 1, 1)
	}
	
	override fun getItemDropped(meta: Int, rand: Random?, fortune: Int): Item? = when (meta) {
		0    -> ModItems.manaResource
		2    -> ModItems.quartz
		4    -> AlfheimItems.elvenResource
		5    -> Items.dye
		else -> this.toItem()
	}
	
	override fun damageDropped(meta: Int) = metas.safeGet(meta)
	
	override fun getExpDrop(world: IBlockAccess?, meta: Int, fortune: Int) =
		if (this.toItem() !== getItemDropped(meta, rand, fortune)) rand.nextInt(5) + 3 else 0
	
	override fun quantityDropped(meta: Int, fortune: Int, random: Random) = when (meta) {
		0, 2, 5 -> {
			if (fortune > 0) {
				var j = max(0, rand.nextInt(fortune + 2) - 1) + 1
				if (meta == 5) j *= 4 + rand.nextInt(5)
				j
			} else 1
		}
		
		else    -> 1
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.ores
}