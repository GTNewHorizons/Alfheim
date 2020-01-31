package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.api.ModInfo
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.common.item.ModItems
import java.util.*
import kotlin.math.*

class BlockElvenOres: BlockModMeta(Material.rock, 5, ModInfo.MODID, "ElvenOre", AlfheimTab, 2f, harvLvl = 2), ILexiconable {
	
	val metas = intArrayOf(9, 1, 5, 3, ElvenResourcesMetas.IffesalDust)
	val rand = Random()
	
	init {
		setHarvestLevel("pickaxe", 1, 1)
	}
	
	override fun getItemDropped(meta: Int, rand: Random?, fortune: Int): Item? = when (meta) {
			0 -> ModItems.manaResource
			2 -> ModItems.quartz
			4 -> AlfheimItems.elvenResource
			else -> Item.getItemFromBlock(this)
		}
	
	override fun damageDropped(meta: Int) = metas[max(0, min(meta, metas.size - 1))]
	
	override fun getExpDrop(world: IBlockAccess?, meta: Int, fortune: Int) =
		if (Item.getItemFromBlock(this) !== getItemDropped(meta, rand, fortune)) rand.nextInt(5) + 3 else 0
	
	override fun quantityDropped(meta: Int, fortune: Int, random: Random): Int {
		return if (meta == 0 || meta == 2) // Dragonstone and quartz
			max(random.nextInt(fortune + 2) - 1, 0) + 1
		else 1 // everything else
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.ores
}