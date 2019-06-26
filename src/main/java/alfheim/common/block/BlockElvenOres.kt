package alfheim.common.block

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.registry.AlfheimItems
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.*
import vazkii.botania.api.lexicon.*
import vazkii.botania.common.item.ModItems
import java.util.*
import kotlin.math.*

class BlockElvenOres: Block(Material.rock), ILexiconable {
	val textures = arrayOfNulls<IIcon>(names.size)
	val drops = arrayOf(ModItems.manaResource, null, ModItems.quartz, null, AlfheimItems.elvenResource)
	val metas = intArrayOf(9, 1, 5, 3, ElvenResourcesMetas.IffesalDust)
	val rand = Random()
	
	init {
		this.setBlockName("ElvenOre")
		this.setCreativeTab(AlfheimCore.alfheimTab)
		this.setHardness(2f)
		this.setHarvestLevel("pickaxe", 2)
		this.setHarvestLevel("pickaxe", 1, 1)
		this.setResistance(5.0f)
		this.setStepSound(soundTypeStone)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		for (i in names.indices)
			textures[i] = reg.registerIcon(ModInfo.MODID + ':'.toString() + names[i] + "OreElven")
	}
	
	override fun getSubBlocks(block: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in names.indices)
			list.add(ItemStack(block, 1, i))
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		return textures[max(0, min(meta, textures.size - 1))]!!
	}
	
	override fun getItemDropped(meta: Int, rand: Random?, fortune: Int): Item {
		if (meta < 0 || drops.size <= meta) return drops[0]!!
		return if (drops[meta] == null) Item.getItemFromBlock(this) else drops[meta]!!
	}
	
	override fun damageDropped(meta: Int): Int {
		return metas[max(0, min(meta, metas.size - 1))]
	}
	
	override fun getDamageValue(world: World, x: Int, y: Int, z: Int): Int {
		// how can damageDropped even be here if it isn't related to dropping stuff? -_-
		return world.getBlockMetadata(x, y, z)
	}
	
	override fun getExpDrop(world: IBlockAccess?, meta: Int, fortune: Int): Int {
		return if (Item.getItemFromBlock(this) !== this.getItemDropped(meta, Random(), fortune)) rand.nextInt(5) + 3 else 0
	}
	
	override fun quantityDropped(meta: Int, fortune: Int, random: Random): Int {
		return if (meta == 0 || meta == 2)
		// Dragonstone and quartz
			max(random.nextInt(fortune + 2) - 1, 0) + 1
		else
			1                            // everything else
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		return AlfheimLexiconData.ores
	}
	
	companion object {
		
		val names = arrayOf("Dragonstone", "Elementium", "Quartz", "Gold", "Iffesal")
	}
}