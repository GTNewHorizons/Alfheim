package alfheim.common.block

import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.AlfheimItems
import alfheim.common.item.block.ItemBlockGrapeRed
import alfheim.common.item.material.*
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import java.util.*

class BlockGrapeRed(val stage: Int): BlockVine(), IGrowable {
	
	init {
		setBlockName("RedGrape${stage+1}")
		setCreativeTab(if (stage == 0) AlfheimTab else null)
		setHardness(0.2f)
		setLightOpacity(0)
		setStepSound(soundTypeGrass)
	}
	
	override fun getItemDropped(meta: Int, random: Random?, fortune: Int) = AlfheimItems.elvenResource
	override fun quantityDropped(rand: Random) = if (rand.nextInt(20) == 0) 1 else 0
	override fun damageDropped(meta: Int) = ElvenResourcesMetas.GrapeLeaf
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockGrapeRed::class.java, name)
		return super.setBlockName(name)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		blockIcon = IconHelper.forBlock(reg, this)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (!world.isRemote && stage == 2 && player.heldItem == null) {
			player.dropPlayerItemWithRandomChoice(ItemStack(AlfheimItems.elvenFood, world.rand.nextInt(2) + 1, ElvenFoodMetas.RedGrapes), true)?.delayBeforeCanPickup = 0
			world.setBlock(x, y, z, AlfheimBlocks.grapesRed[0], world.getBlockMetadata(x, y, z), 3)
			return true
		}
		
		return false
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
		if (stage == 0) super.updateTick(world, x, y, z, random)
		
		if (random.nextInt(if (stage == 0) 50 else 10) == 0 && stage != 2) return func_149853_b(world, null, x, y, z)
	}
	
	// can be bonemealed at all? If true will consume one item
	override fun func_149851_a(world: World?, x: Int, y: Int, z: Int, isRemote: Boolean) = stage < 2
	
	// can "do bonemealing" function be called?
	override fun func_149852_a(world: World?, random: Random, x: Int, y: Int, z: Int) = random.nextInt(3) == 0
	
	// "do bonemealing" function
	override fun func_149853_b(world: World, random: Random?, x: Int, y: Int, z: Int) {
		world.setBlock(x, y, z, AlfheimBlocks.grapesRed[stage + 1], world.getBlockMetadata(x, y, z), 3)
	}
	
	override fun onSheared(item: ItemStack?, world: IBlockAccess?, x: Int, y: Int, z: Int, fortune: Int) = arrayListOf(ItemStack(AlfheimBlocks.grapesRed[0]))
	
	@SideOnly(Side.CLIENT)
	override fun getBlockColor() = 0xFFFFFF
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int) = 0xFFFFFF
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(world: IBlockAccess, x: Int, y: Int, z: Int) = 0xFFFFFF
}
