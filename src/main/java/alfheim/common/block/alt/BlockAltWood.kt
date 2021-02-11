package alfheim.common.block.alt

import alexsocol.asjlib.*
import alfheim.api.lib.LibOreDict.ALT_TYPES
import alfheim.client.core.helper.*
import alfheim.common.block.base.BlockModRotatedPillar
import alfheim.common.item.block.ItemUniqueSubtypedBlockMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.*
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.api.lexicon.LexiconEntry
import java.util.*

class BlockAltWood(val set: Int): BlockModRotatedPillar(Material.wood), IFuelHandler {
	
	lateinit var icons: Array<Array<IIcon?>>
	
	init {
		setBlockName("altWood$set")
		isBlockContainer = true
		blockHardness = 2F
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
		
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun getBlockHardness(world: World, x: Int, y: Int, z: Int) = if (set == 1 && world.getBlockMetadata(x, y, z) % 4 == 2) -1f else super.getBlockHardness(world, x, y, z)
	
	override fun canSustainLeaves(world: IBlockAccess, x: Int, y: Int, z: Int) = !(set == 1 && world.getBlockMetadata(x, y, z) % 4 == 2)
	
	override fun isWood(world: IBlockAccess, x: Int, y: Int, z: Int) = !(set == 1 && world.getBlockMetadata(x, y, z) % 4 == 2)
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, fortune: Int) {
		val meta = world.getBlockMetadata(x, y, z)
		if (set == 1 && meta % 4 == 2)
			return super.breakBlock(world, x, y, z, block, fortune)
		
		val range = if (set == 1 && meta % 4 == 3) 12 else 4
		val chunkRange = range + 1
		
		if (world.checkChunksExist(x - chunkRange, y - chunkRange, z - chunkRange, x + chunkRange, y + chunkRange, z + chunkRange)) {
			for (j1 in -range..range) for (k1 in -range..range)
				for (l1 in -range..range) {
					val blockInWorld: Block = world.getBlock(x + j1, y + k1, z + l1)
					if (blockInWorld.isLeaves(world, x + j1, y + k1, z + l1)) {
						blockInWorld.beginLeavesDecay(world, x + j1, y + k1, z + l1)
					}
				}
		}
		super.breakBlock(world, x, y, z, block, fortune)
	}
	
	override fun damageDropped(meta: Int) = meta and 0b0011
	
	override fun quantityDropped(random: Random) = 1
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemUniqueSubtypedBlockMod::class.java, name)
	}
	
	override fun getTopIcon(meta: Int) = icons[0].safeGet(meta % 4)
	
	override fun getSideIcon(meta: Int) = icons[1].safeGet(meta % 4)
	
	override fun getSubBlocks(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in 0..3)
			list.add(ItemStack(this, 1, i))
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		icons = arrayOf(
			Array(4) { i -> if (set == 0 && i == 3) null else IconHelper.forName(reg, "altOak${ALT_TYPES[(set * 4) + i]}Top") },
			Array(4) { i -> if (set == 0 && i == 3) null else IconHelper.forName(reg, "altOak${ALT_TYPES[(set * 4) + i]}Side") }
		)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	override fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 0 && set == 0) {
			icons[0][3] = InterpolatedIconHelper.forName(event.map, "altOak${ALT_TYPES[3]}Top")
			icons[1][3] = InterpolatedIconHelper.forName(event.map, "altOak${ALT_TYPES[3]}Side")
		}
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?): LexiconEntry? {
		val meta = world.getBlockMetadata(x, y, z)
		return when {
			set == 1 && meta % 8 == BlockAltLeaves.yggMeta - 4 + 1 -> AlfheimLexiconData.worldgen
			set == 1 && meta % 8 == BlockAltLeaves.yggMeta - 4     -> null
			else                                                   -> AlfheimLexiconData.irisSapling
		}
	}
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item === this.toItem()) if (set == 1 && fuel.meta % 8 == BlockAltLeaves.yggMeta - 4) Int.MAX_VALUE / 13 else 300 else 0
}
