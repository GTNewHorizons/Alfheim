package alfheim.common.block

import alexsocol.asjlib.*
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileBarrel
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.*
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.*
import net.minecraftforge.event.entity.living.LivingFallEvent
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent
import vazkii.botania.api.lexicon.ILexiconable

class BlockBarrel: BlockContainerMod(Material.wood), ILexiconable {
	
	init {
		setBlockName("barrel")
		setHardness(1f)
		setLightOpacity(0)
		setStepSound(Block.soundTypeWood)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val ret = onBlockActivated2(world, x, y, z, player)
		
		if (ret && !world.isRemote) {
			world.getTileEntity(x, y, z)?.let { ASJUtilities.dispatchTEToNearbyPlayers(it) }
		}
		
		return ret
	}
	
	fun onBlockActivated2(world: World, x: Int, y: Int, z: Int, player: EntityPlayer): Boolean {
		val tile = world.getTileEntity(x, y, z) as? TileBarrel ?: return false
		val stack = player.heldItem
		
		if (stack == null) {
			if (player.isSneaking) {
				tile.closed = !tile.closed
				return true
			}
			
			return false
		}
		
		if (!tile.closed) {
			
			when (tile.wineStage) {
				0                           -> { // nothing
					if (stack.item is ItemElvenFood && stack.meta == ElvenFoodMetas.WhiteGrapes || stack.meta == ElvenFoodMetas.RedGrapes) {
						tile.wineStage = TileBarrel.WINE_STAGE_GRAPE
						tile.wineType = stack.meta
						tile.wineLevel++
						stack.stackSize--
					}
				}
				
				TileBarrel.WINE_STAGE_GRAPE -> {
					if (stack.item is ItemElvenFood && tile.wineType == stack.meta && tile.wineLevel < TileBarrel.MAX_WINE_LEVEL) {
						tile.wineLevel++
						stack.stackSize--
					}
				}
				
				TileBarrel.WINE_STAGE_MASH  -> {
					if (stack.item is ItemElvenFood && stack.meta == ElvenFoodMetas.Nectar) {
						tile.wineStage = TileBarrel.WINE_STAGE_LIQUID
						tile.timer = TileBarrel.FERMENTATION_TIME
						stack.stackSize--
					}
				}
				
				TileBarrel.WINE_STAGE_READY -> {
					if (stack.item is ItemElvenResource && stack.meta == ElvenResourcesMetas.Jug && tile.wineLevel >= 4) {
						--stack.stackSize
						
						val jug = ItemStack(AlfheimItems.elvenFood, 1, if (tile.wineType == TileBarrel.WINE_TYPE_RED) ElvenFoodMetas.RedWine else ElvenFoodMetas.WhiteWine)
						if (player.inventory.addItemStackToInventory(jug)) {
							player.dropPlayerItemWithRandomChoice(jug, true)
						}
						
						tile.wineLevel -= 4
						
						if (tile.wineLevel == 0) {
							tile.reset()
						}
					}
				}
				
				else                        -> return false
			}
			
			return true
		}
		
		return false
	}
	
	override fun createNewTileEntity(world: World, meta: Int) = TileBarrel()
	override fun registerBlockIcons(reg: IIconRegister) = Unit
	override fun getIcon(side: Int, meta: Int) = Blocks.planks.getIcon(side, meta)!!
	override fun renderAsNormalBlock() = false
	override fun isOpaqueCube() = false
	override fun getRenderType() = LibRenderIDs.idBarrel
	
	override fun setBlockBoundsBasedOnState(world: IBlockAccess?, x: Int, y: Int, z: Int) {
		setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f)
		super.setBlockBoundsBasedOnState(world, x, y, z)
	}
	
	override fun addCollisionBoxesToList(world: World?, x: Int, y: Int, z: Int, aabb: AxisAlignedBB?, list: MutableList<Any?>?, entity: Entity?) {
		val tile = world?.getTileEntity(x, y, z) as? TileBarrel
		
		val f = 2f / 16f
		
		setBlockBounds(0f, 0f, 0f, 1f, f, 1f)
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
		
		if (tile != null && tile.closed) {
			setBlockBounds(0f, 1 - f, 0f, 1f, 1f, 1f)
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
		}
		
		setBlockBounds(0f, 0f, 0f, f, 1f, 1f)
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
		
		setBlockBounds(0f, 0f, 0f, 1f, 1f, f)
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
		
		setBlockBounds(1 - f, 0f, 0f, 1f, 1f, 1f)
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
		
		setBlockBounds(0f, 0f, 1 - f, 1f, 1f, 1f)
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
	}
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = AlfheimLexiconData.winery
	
	companion object {
		
		init {
			this.eventForge()
		}
		
		@SubscribeEvent
		fun onEntityFall(e: LivingFallEvent) {
			onSomeoneFall(e.entity)
		}
		
		@SubscribeEvent
		fun onPlayerFall(e: PlayerFlyableFallEvent) {
			onSomeoneFall(e.entity)
		}
		
		fun onSomeoneFall(entity: Entity) {
			val tile = entity.worldObj.getTileEntity(entity) as? TileBarrel ?: return
			
			if (!tile.closed && tile.wineStage == TileBarrel.WINE_STAGE_GRAPE && tile.wineLevel == TileBarrel.MAX_WINE_LEVEL) {
				if (++tile.stomps == 8) {
					tile.wineStage = TileBarrel.WINE_STAGE_MASH
					ASJUtilities.dispatchTEToNearbyPlayers(tile)
				}
			}
		}
	}
}
