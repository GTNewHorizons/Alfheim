package alfheim.common.block.colored.rainbow

import alexsocol.asjlib.ASJUtilities
import alfheim.client.core.helper.InterpolatedIconHelper
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.*
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.common.block.decor.quartz.*

/**
 * @author WireSegal
 * Created at 7:59 PM on 2/13/16.
 */
class BlockShimmerQuartz: BlockSpecialQuartz("Shimmer") {
	
	init {
		setCreativeTab(AlfheimTab)
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun registerIcons(e: TextureStitchEvent.Pre) {
		if (e.map.textureType == 0) {
			this.specialQuartzIcons = arrayOfNulls(this.iconNames.size)
			
			for (i in this.specialQuartzIcons.indices) {
				if (this.iconNames[i] == null)
					this.specialQuartzIcons[i] = this.specialQuartzIcons[i - 1]
				else
					this.specialQuartzIcons[i] = InterpolatedIconHelper.forName(e.map, this.iconNames[i].replace("decor/", ""), "decor")
			}
			
			this.specialQuartzTopIcon = this.specialQuartzIcons[0]
			this.chiseledSpecialQuartzIcon = InterpolatedIconHelper.forName(e.map, "chiseled${type}Quartz1", "decor")
			this.pillarSpecialQuartzIcon = InterpolatedIconHelper.forName(e.map, "pillar${type}Quartz1", "decor")
		}
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(par1IconRegister: IIconRegister) = Unit
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = AlfheimLexiconData.shimmer
}

class BlockShimmerQuartzSlab(val block: BlockShimmerQuartz, val full: Boolean): BlockSpecialQuartzSlab(block, full) {
	
	init {
		setCreativeTab(AlfheimTab)
	}
	
	override fun getSingleBlock() = AlfheimBlocks.shimmerQuartzSlab as BlockSlab
	
	override fun getFullBlock() = AlfheimBlocks.shimmerQuartzSlabFull as BlockSlab
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = block.getEntry(world, x, y, z, player, lexicon)
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemShimmerSlabMod::class.java, "quartzSlab${block.type}${if (full) "Full" else "Half"}")
	}
}

class BlockShimmerQuartzStairs(val block: BlockShimmerQuartz): BlockSpecialQuartzStairs(block) {
	
	lateinit var unlocName: String
	
	init {
		setCreativeTab(AlfheimTab)
	}
	
	override fun setBlockName(par1Str: String): Block? {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, par1Str)
		unlocName = "tile.$par1Str"
		return this
	}
	
	override fun getUnlocalizedName() = unlocName
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = block.getEntry(world, x, y, z, player, lexicon)
}


