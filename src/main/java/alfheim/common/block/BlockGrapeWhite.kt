package alfheim.common.block

import alexsocol.asjlib.safeGet
import alfheim.api.lib.LibRenderIDs
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.AlfheimItems
import alfheim.common.item.block.ItemGrapeWhite
import alfheim.common.item.material.ElvenFoodMetas
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityBoat
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import java.util.*

class BlockGrapeWhite: BlockBush() {
	
	lateinit var iconsBush: Array<IIcon>
	
	init {
		val f = 0.5f
		val f1 = 0.015625f
		setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f1, 0.5f + f)
		setBlockName("WhiteGrape")
		setCreativeTab(AlfheimTab)
		setHardness(0.2f)
		setStepSound(soundTypeGrass)
	}
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemGrapeWhite::class.java, name)
		return super.setBlockName(name)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		blockIcon = IconHelper.forBlock(reg, this, "Bottom")
		iconsBush = Array(3) { IconHelper.forBlock(reg, this, it + 1) }
	}
	
	override fun getIcon(side: Int, meta: Int) = if (side == 0) iconsBush.safeGet(meta) else blockIcon!!
	override fun getRenderType() = LibRenderIDs.idGrapeWhite
	override fun addCollisionBoxesToList(world: World?, x: Int, y: Int, z: Int, aabb: AxisAlignedBB?, list: List<Any?>?, entity: Entity?) = if (entity !is EntityBoat) super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity) else Unit
	override fun getCollisionBoundingBoxFromPool(world: World?, x: Int, y: Int, z: Int) = AxisAlignedBB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ)!!
	override fun canPlaceBlockOn(block: Block) = block === Blocks.water
	override fun canBlockStay(world: World, x: Int, y: Int, z: Int) = if (y in 0..255) world.getBlock(x, y - 1, z).material === Material.water && world.getBlockMetadata(x, y - 1, z) == 0 else false
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
		super.updateTick(world, x, y, z, random)
		
		val meta = world.getBlockMetadata(x, y, z)
		if (meta < 2 && random.nextInt(if (meta == 0) 50 else 10) == 0) world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (world.getBlockMetadata(x, y, z) >= 2 && player.heldItem == null) {
			player.inventory.addItemStackToInventory(ItemStack(AlfheimItems.elvenFood, world.rand.nextInt(2) + 1, ElvenFoodMetas.WhiteGrapes))
			world.setBlockMetadataWithNotify(x, y, z, 0, 3)
			return true
		}
		
		return false
	}
}
