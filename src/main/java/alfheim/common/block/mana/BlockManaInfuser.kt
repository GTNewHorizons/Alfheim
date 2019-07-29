package alfheim.common.block.mana

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.tile.TileManaInfuser
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.wand.*
import vazkii.botania.common.block.*

class BlockManaInfuser: BlockContainer(Material.rock), ILexiconable, IWandHUD, IWandable {
	init {
		setBlockName("ManaInfuser")
		setBlockTextureName(ModInfo.MODID + ":ManaInfuser")
		setCreativeTab(AlfheimCore.alfheimTab)
		setHardness(3f)
		setHarvestLevel("pickaxe", 1)
		setResistance(60f)
		setStepSound(soundTypeStone)
	}
	
	override fun createNewTileEntity(world: World, meta: Int) = TileManaInfuser()
	
	override fun registerBlockIcons(reg: IIconRegister) {
		textures[0] = reg.registerIcon(getTextureName() + "Bottom")
		textures[1] = reg.registerIcon(getTextureName() + "Top")
		textures[2] = reg.registerIcon(getTextureName() + "Top_Active")
		textures[3] = reg.registerIcon(getTextureName() + "Side")
		textures[4] = reg.registerIcon(getTextureName() + "BottomDark")
		textures[5] = reg.registerIcon(getTextureName() + "TopDark")
		textures[6] = reg.registerIcon(getTextureName() + "SideDark")
	}
	
	override fun getIcon(side: Int, meta: Int) =
		(if (side == 0) if (meta == 2) textures[4] else textures[0] else if (side == 1) if (meta == 2) textures[5] else if (meta == 1) textures[2] else textures[1] else if (meta == 2) textures[6] else textures[3])!!
	
	override fun onBlockActivated(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (ModInfo.DEV && !world!!.isRemote && player!!.isSneaking) {
			player.addChatComponentMessage(ChatComponentText(EnumChatFormatting.AQUA.toString() + "Mana: " + (world.getTileEntity(x, y, z) as TileManaInfuser).currentMana))
			return true
		}
		return false
	}
	
	override fun onBlockPlacedBy(world: World?, x: Int, y: Int, z: Int, placer: EntityLivingBase?, stack: ItemStack?) {
		super.onBlockPlacedBy(world, x, y, z, placer, stack)
		if (placer is EntityPlayer) {
			if (world!!.getBlock(x + 1, y, z) === ModFluffBlocks.elfQuartz &&
				world!!.getBlock(x - 1, y, z) === ModFluffBlocks.elfQuartz &&
				world!!.getBlock(x, y, z + 1) === ModFluffBlocks.elfQuartz &&
				world!!.getBlock(x, y, z - 1) === ModFluffBlocks.elfQuartz &&
				world!!.getBlock(x + 1, y, z + 1) === ModBlocks.storage && world!!.getBlockMetadata(x + 1, y, z + 1) == 2 &&
				world.getBlock(x + 1, y, z - 1) === ModBlocks.storage && world.getBlockMetadata(x + 1, y, z - 1) == 2 &&
				world.getBlock(x - 1, y, z + 1) === ModBlocks.storage && world.getBlockMetadata(x - 1, y, z + 1) == 2 &&
				world.getBlock(x - 1, y, z - 1) === ModBlocks.storage && world.getBlockMetadata(x - 1, y, z - 1) == 2)
				
				placer.triggerAchievement(AlfheimAchievements.infuser)
		}
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.infuser
	
	override fun renderHUD(mc: Minecraft, res: ScaledResolution, world: World, x: Int, y: Int, z: Int) {
		(world.getTileEntity(x, y, z) as TileManaInfuser).renderHUD(res)
	}
	
	override fun onUsedByWand(player: EntityPlayer, stack: ItemStack, world: World, x: Int, y: Int, z: Int, side: Int): Boolean {
		(world.getTileEntity(x, y, z) as TileManaInfuser).onWanded(player)
		return true
	}
	
	companion object {
		val textures = arrayOfNulls<IIcon>(7)
	}
}