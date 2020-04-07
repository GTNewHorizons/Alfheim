package alfheim.common.item.rod

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.common.block.AlfheimBlocks
import alfheim.common.item.ItemIridescent
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import net.minecraft.block.Block
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.*
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.item.*
import vazkii.botania.api.mana.*
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import java.awt.Color

class ItemRodIridescent(name: String = "rodColorfulSkyDirt"): ItemIridescent(name), IAvatarWieldable, IManaUsingItem, IBlockProvider {
	
	private val avatarOverlay = ResourceLocation("${ModInfo.MODID}:textures/model/avatar/avatarDirtRainbow.png")
	
	val COST = 150
	
	companion object {
		fun place(par1ItemStack: ItemStack, par2EntityPlayer: EntityPlayer, par3World: World,
				  par4: Int, par5: Int, par6: Int, par7: Int, par8: Float, par9: Float,
				  par10: Float, toPlace: ItemStack?, cost: Int, r: Float, g: Float, b: Float): Boolean {
			
			if (ManaItemHandler.requestManaExactForTool(par1ItemStack, par2EntityPlayer, cost, false)) {
				val dir = ForgeDirection.getOrientation(par7)
				
				val aabb = AxisAlignedBB.getBoundingBox((par4 + dir.offsetX).D,
														(par5 + dir.offsetY).D, (par6 + dir.offsetZ).D,
														(par4 + dir.offsetX + 1).D, (par5 + dir.offsetY + 1).D, (par6 + dir.offsetZ + 1).D)
				val entities = par3World.getEntitiesWithinAABB(EntityLivingBase::class.java, aabb).size
				
				if (entities == 0) {
					toPlace!!.tryPlaceItemIntoWorld(par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10)
					
					if (toPlace.stackSize == 0) {
						ManaItemHandler.requestManaExactForTool(par1ItemStack, par2EntityPlayer, cost, true)
						for (i in 0..6)
							Botania.proxy.sparkleFX(par3World, par4 + dir.offsetX + Math.random(), par5 + dir.offsetY + Math.random(), par6 + dir.offsetZ + Math.random(), r, g, b, 1F, 5)
					}
				}
			}
			
			return true
		}
	}
	
	init {
		maxStackSize = 1
	}
	
	override fun onItemUse(par1ItemStack: ItemStack, par2EntityPlayer: EntityPlayer, par3World: World, par4: Int, par5: Int, par6: Int, par7: Int, par8: Float, par9: Float, par10: Float) =
		place(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, dirtStack(par1ItemStack.meta), COST, 0.35F, 0.2F, 0.05F)
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		var blockstack = dirtStack(stack.meta)
		
		/*val beltStack = ItemToolbelt.getEquippedBelt(player)
		if (beltStack != null && ItemToolbelt.isEquipped(beltStack))
			return stack*/
		
		if (player.isSneaking) {
			var damage = stack.meta
			if (!world.isRemote) {
				if (stack.meta >= 17) stack.meta = 0 else stack.meta++
				damage = stack.meta
			} else if (damage >= 17) damage = 0 else damage++
			world.playSoundAtEntity(player, "botania:ding", 0.1F, 1F)
			blockstack = dirtStack(damage)
			blockstack!!.setStackDisplayName(StatCollector.translateToLocal("misc.${ModInfo.MODID}.color.$damage"))
			ItemsRemainingRenderHandler.set(blockstack, -2)
		} else if (!world.isRemote && ManaItemHandler.requestManaExactForTool(stack, player, COST * 2, false)) {
			
			val color = Color(getColorFromItemStack(stack, 0))
			val r = color.red / 255F
			val g = color.green / 255F
			val b = color.blue / 255F
			
			val sif = (ItemPriestEmblem.getEmblem(1, player) != null)
			var basePlayerRange = 5.0
			if (player is EntityPlayerMP)
				basePlayerRange = player.theItemInWorldManager.blockReachDistance
			val distmultiplier = if (sif) basePlayerRange - 1 else 3.0
			
			val playerVec = Vector3.fromEntityCenter(player)
			val lookVec = Vector3(player.lookVec).multiply(distmultiplier)
			val placeVec = playerVec.copy().add(lookVec)
			
			val x = placeVec.x.mfloor()
			val y = placeVec.y.mfloor() + 1
			val z = placeVec.z.mfloor()
			
			val entities = world.getEntitiesWithinAABB(EntityLivingBase::class.java,
													   AxisAlignedBB.getBoundingBox(x.D, y.D, z.D, (x + 1).D,
																					(y + 1).D, (z + 1).D)).size
			
			if (entities == 0) {
				blockstack!!.tryPlaceItemIntoWorld(player, world, x, y, z, 0, 0F, 0F, 0F)
				
				if (blockstack.stackSize == 0) {
					ManaItemHandler.requestManaExactForTool(stack, player, COST * 2, true)
					for (i in 0..6)
						Botania.proxy.sparkleFX(world, x + Math.random(), y + Math.random(), z + Math.random(),
												r, g, b, 1F, 5)
				}
			}
		}
		if (world.isRemote)
			player.swingItem()
		
		return stack
	}
	
	override fun onAvatarUpdate(tile: IAvatarTile, stack: ItemStack) {
		val te = tile as TileEntity
		val world = te.worldObj
		val x = te.xCoord
		val y = te.yCoord
		val z = te.zCoord
		
		var xl = 0
		var zl = 0
		
		when (te.getBlockMetadata() - 2) {
			0 -> zl = -2
			1 -> zl = 2
			2 -> xl = -2
			3 -> xl = 2
		}
		
		val block = world.getBlock(x + xl, y, z + zl)
		
		val color = Color(getColorFromItemStack(stack, 0))
		val r = color.red / 255F
		val g = color.green / 255F
		val b = color.blue / 255F
		
		if (tile.currentMana >= COST && block.isAir(world, x + xl, y, z + zl) && tile.elapsedFunctionalTicks % 50 == 0 && tile.isEnabled) {
			world.setBlock(x + xl, y, z + zl, dirtFromMeta(stack.meta), stack.meta, 1 or 2)
			tile.recieveMana(-COST)
			for (i in 0..6)
				Botania.proxy.sparkleFX(world, x + xl + Math.random(), y + Math.random(), z + zl + Math.random(),
										r, g, b, 1F, 5)
			if (stack.meta == TYPES)
				world.playAuxSFX(2001, x + xl, y, z + zl, Block.getIdFromBlock(AlfheimBlocks.rainbowDirt))
			else
				world.playAuxSFX(2001, x + xl, y, z + zl, Block.getIdFromBlock(AlfheimBlocks.irisDirt) + (stack.meta shl 12))
			
		}
	}
	
	override fun getOverlayResource(tile: IAvatarTile, stack: ItemStack) = avatarOverlay
	
	override fun isFull3D() = true
	
	override fun usesMana(stack: ItemStack) = true
	
	override fun provideBlock(player: EntityPlayer, requestor: ItemStack, stack: ItemStack, block: Block, meta: Int, doit: Boolean): Boolean {
		if (block == AlfheimBlocks.irisDirt || block == AlfheimBlocks.rainbowDirt)
			return !doit || ManaItemHandler.requestManaExactForTool(requestor, player, COST, true)
		return false
	}
	
	override fun getBlockCount(player: EntityPlayer, requestor: ItemStack, stack: ItemStack, block: Block, meta: Int): Int {
		if (block == AlfheimBlocks.irisDirt || block == AlfheimBlocks.rainbowDirt)
			return -1
		return 0
	}
}
