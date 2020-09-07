package alfheim.common.block.tile

import alexsocol.asjlib.*
import alfheim.api.*
import alfheim.api.crafting.recipe.RecipeTreeCrafting
import alfheim.common.block.AlfheimBlocks
import alfheim.common.lexicon.MultiblockComponentRainbow
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import org.lwjgl.opengl.*
import vazkii.botania.api.lexicon.multiblock.*
import vazkii.botania.api.mana.IManaPool
import vazkii.botania.api.mana.spark.*
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.client.core.helper.RenderHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.tile.TileMod
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.util.*
import kotlin.math.*

class TileTreeCrafter: TileMod(), ISparkAttachable {
	
	companion object {
		const val TAG_SUFF_TILE = "SuffusedTile"
		
		val ITEMDISPLAY_LOCATIONS = arrayOf(Pos(-3, 1, 3), Pos(-4, 1, 0), Pos(0, 1, 4), Pos(-3, 1, -3), Pos(0, 1, -4), Pos(3, 1, -3), Pos(4, 1, 0), Pos(3, 1, 3))
		val COLOREDWOOD_LOCATIONS = arrayOf(Pos(2, 0, 2), Pos(2, 0, 1), Pos(2, 0, -1), Pos(2, 0, -2), Pos(1, 0, 2), Pos(1, 0, -2), Pos(-1, 0, 2), Pos(-1, 0, -2), Pos(-2, 0, 2), Pos(-2, 0, 1), Pos(-2, 0, -1), Pos(-2, 0, -2))
		val OBSIDIAN_LOCATIONS = arrayOf(Pos(3, 0, 2), Pos(3, 0, 1), Pos(3, 0, 0), Pos(3, 0, -1), Pos(3, 0, -2), Pos(2, 0, 3), Pos(2, 0, 0), Pos(2, 0, -3), Pos(1, 0, 3), Pos(1, 0, 0), Pos(1, 0, -3), Pos(0, 0, 3), Pos(0, 0, 2), Pos(0, 0, 1), Pos(0, 0, -1), Pos(0, 0, -2), Pos(0, 0, -3), Pos(-1, 0, 3), Pos(-1, 0, 0), Pos(-1, 0, -3), Pos(-2, 0, 3), Pos(-2, 0, 0), Pos(-2, 0, -3), Pos(-3, 0, 2), Pos(-3, 0, 1), Pos(-3, 0, 0), Pos(-3, 0, -1), Pos(-3, 0, -2), Pos(1, 0, 1), Pos(1, 0, -1), Pos(-1, 0, 1), Pos(-1, 0, -1))
		
		fun makeMultiblockSet(): MultiblockSet {
			val mb = Multiblock()
			
			for (i in ITEMDISPLAY_LOCATIONS) {
				mb.addComponent(i.x, i.y + 2, i.z, ModBlocks.pylon, 0)
				mb.addComponent(MultiblockComponentRainbow(ChunkCoordinates(i.x, i.y, i.z), AlfheimBlocks.itemDisplay))
			}
			
			for (i in OBSIDIAN_LOCATIONS) {
				mb.addComponent(i.x, i.y, i.z, Blocks.obsidian, 0)
			}
			
			for (i in COLOREDWOOD_LOCATIONS) {
				mb.addComponent(MultiblockComponentRainbow(ChunkCoordinates(i.x, i.y, i.z), AlfheimBlocks.irisPlanks, AlfheimBlocks.rainbowPlanks, AlfheimBlocks.auroraPlanks))
			}
			
			mb.addComponent(MultiblockComponentRainbow(ChunkCoordinates(0, 0, 0), AlfheimBlocks.irisDirt, AlfheimBlocks.rainbowDirt, AlfheimBlocks.auroraDirt))
			mb.addComponent(MultiblockComponentRainbow(ChunkCoordinates(0, 4, 0), AlfheimBlocks.irisPlanks, AlfheimBlocks.rainbowPlanks, AlfheimBlocks.auroraPlanks))
			
			return mb.makeSet()
		}
		
		fun canEnchanterExist(world: World, x: Int, y: Int, z: Int): Boolean {
			
			val y0 = y - 4
			
			for (i in ITEMDISPLAY_LOCATIONS) {
				if (!i.isBlock(world, ModBlocks.pylon, x, y0 + 2, z)) {
					// FMLLog.log(Level.INFO, "Pylon at $i")
					return false
				}
				if (!i.isBlock(world, AlfheimBlocks.itemDisplay, x, y0, z)) {
					// FMLLog.log(Level.INFO, "Item Display at $i")
					return false
				}
			}
			
			for (i in OBSIDIAN_LOCATIONS) {
				if (!i.isBlock(world, Blocks.obsidian, x, y0, z)) {
					// FMLLog.log(Level.INFO, "Obsidian at $i")
					return false
				}
			}
			
			for (i in COLOREDWOOD_LOCATIONS) {
				if (!i.isBlock(world, AlfheimBlocks.irisPlanks, x, y0, z) &&
					!i.isBlock(world, AlfheimBlocks.rainbowPlanks, x, y0, z) &&
					!i.isBlock(world, AlfheimBlocks.auroraPlanks, x, y0, z)) {
					// FMLLog.log(Level.INFO, "Colored Wood at $i")
					return false
				}
			}
			
			if (world.getBlock(x, y0 + 4, z) !== AlfheimBlocks.treeCrafterBlock &&
				world.getBlock(x, y0 + 4, z) !== AlfheimBlocks.treeCrafterBlockRB &&
				world.getBlock(x, y0 + 4, z) !== AlfheimBlocks.treeCrafterBlockAU &&
				world.getBlock(x, y0 + 4, z) !== AlfheimBlocks.irisPlanks &&
				world.getBlock(x, y0 + 4, z) !== AlfheimBlocks.rainbowPlanks &&
				world.getBlock(x, y0 + 4, z) !== AlfheimBlocks.auroraPlanks) {
				// FMLLog.log(Level.INFO, "Core Block")
				return false
			}
			
			if (world.getBlock(x, y0, z) !== AlfheimBlocks.irisDirt &&
				world.getBlock(x, y0, z) !== AlfheimBlocks.rainbowDirt &&
				world.getBlock(x, y0, z) !== AlfheimBlocks.auroraDirt) {
				// FMLLog.log(Level.INFO, "Dirt Block")
				return false
			}
			
			return true
		}
	}
	
	class Pos(val x: Int, val y: Int, val z: Int) {
		fun getBlock(world: World, x0: Int = 0, y0: Int = 0, z0: Int = 0): Block = world.getBlock(x + x0, y + y0, z + z0)
		fun isBlock(world: World, block: Block, x0: Int = 0, y0: Int = 0, z0: Int = 0): Boolean = world.getBlock(x + x0, y + y0, z + z0) === block
		
		override fun toString() = "X: $x Y: $y Z: $z"
	}
	
	private var ticksAlive: Int = 0
	
	private var mana: Int = 0
	private var manaRequired: Int = 0
	private var stage: Int = 0
	private var stageTicks: Int = 0
	var signal = 0
	
	override fun updateEntity() {
		if (!canEnchanterExist(worldObj, xCoord, yCoord, zCoord)) {
			val block = worldObj.getBlock(xCoord, yCoord, zCoord)
			
			when {
				block === AlfheimBlocks.treeCrafterBlock   -> worldObj.setBlock(xCoord, yCoord, zCoord, AlfheimBlocks.irisPlanks, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), 3)
				block === AlfheimBlocks.treeCrafterBlockRB -> worldObj.setBlock(xCoord, yCoord, zCoord, AlfheimBlocks.rainbowPlanks, 0, 3)
				block === AlfheimBlocks.treeCrafterBlockAU -> worldObj.setBlock(xCoord, yCoord, zCoord, AlfheimBlocks.auroraPlanks, 0, 3)
			}
			
			for (var11 in 0..49) {
				val var13 = Math.random().F
				val var16 = Math.random().F
				val var19 = Math.random().F
				Botania.proxy.wispFX(worldObj, xCoord.D + 0.5, yCoord.D + 0.5, zCoord.D + 0.5, var13, var16, var19, Math.random().F * 0.15f + 0.15f, (Math.random() - 0.5).F * 0.25f, (Math.random() - 0.5).F * 0.25f, (Math.random() - 0.5).F * 0.25f)
			}
			
			worldObj.playSoundEffect(xCoord.D, yCoord.D, zCoord.D, "botania:enchanterBlock", 0.5F, 10F)
			
			return //finishes execution just in case
		}
		
		val recipe = getRecipe()
		var recipeItems = ArrayList<Any?>()
		if (recipe != null) recipeItems = ArrayList(recipe.inputs)
		
		itemDisplays {
			val stack = it.get(0)
			if (stack != null) {
				
				val s = 0.2f + Math.random().F * 0.1f
				val m = 0.03f + Math.random().F * 0.015f
				
				for (rItem in recipeItems) {
					if (rItem != null)
						if (stack.itemEquals(rItem)) {
							if (mana > 0) {
								if (stack.item is ItemBlock) worldObj.spawnParticle("blockcrack_${stack.item.id}_${stack.meta}", it.xCoord.D + .5, it.yCoord + 1.0, it.zCoord.D + .5, (xCoord.D - it.xCoord.D) * 8.0, 0.1, (zCoord.D - it.zCoord.D) * 8.0)
								else worldObj.spawnParticle("iconcrack_${stack.item.id}_${stack.meta}", it.xCoord.D + .5, it.yCoord + 1.0, it.zCoord.D + .5, (xCoord.D - it.xCoord.D) / 8.0, 0.1, (zCoord.D - it.zCoord.D) / 8.0)
								Botania.proxy.wispFX(worldObj, it.xCoord.D + .5, it.yCoord + 3.D + .5, it.zCoord.D + .5, 1f, 1f, 1f, s, -m)
							}
							recipeItems.remove(rItem)
							break
						}
				}
			}
		}
		
		if (getRecipe() == null) stage = 0
		
		if (stage == 0) {
			manaRequired = 0; mana = 0
		}
		
		when (stage) {
			0    -> {
				signal = 0
				forRecipe {
					mana = 0
					manaRequired = it.manaUsage
					advanceStage()
				}
			}
			
			1    -> {
				stageTicks++
				signal = 1
				workingFanciness()
				
				if (mana >= manaRequired) {
					manaRequired = 0
					advanceStage()
				} else {
					if (attachedSpark != null) {
						val sparks = SparkHelper.getSparksAround(worldObj, xCoord.D + 0.5, yCoord.D + 0.5, zCoord.D + 0.5)
						if (sparks != null) for (spark in sparks)
							if (spark != null && attachedSpark !== spark && spark.attachedTile != null && spark.attachedTile is IManaPool)
								spark.registerTransfer(attachedSpark)
					}
				}
			}
			
			2    -> {
				forRecipe {
					craftingFanciness(it)
				}
			}
			
			else -> {
				stage = 0
			}
		}
		
		ticksAlive++
	}
	
	fun workingFanciness() {
		10.tickDelay {
			for (i in 0..359) {
				val radian = (i.D * 3.141592653589793 / 180.0).F
				val xp = xCoord.D + cos(radian.D) * 3
				val zp = zCoord.D + sin(radian.D) * 3
				Botania.proxy.wispFX(worldObj, xp + 0.5, yCoord - 3.0, zp + 0.5, 0f, 1f, 1f, 0.3f, -0.01f)
			}
		}
	}
	
	fun getCore(): ItemStack? {
		val core = worldObj.getBlock(xCoord, yCoord - 3, zCoord)
		if (core === Blocks.air) return null
		
		val stack = ItemStack(core, 1, worldObj.getBlockMetadata(xCoord, yCoord - 3, zCoord) )
		val tile = worldObj.getTileEntity(xCoord, yCoord - 3, zCoord) ?: return stack
		stack.tagCompound = NBTTagCompound().also { tile.writeToNBT(it); it.removeTag("x"); it.removeTag("y"); it.removeTag("z") }
		return stack
	}
	
	fun getRecipe() = getCore()?.let { core -> AlfheimAPI.treeRecipes.firstOrNull { it.matches(getRecipeInputs(), core) } }
	
	fun forRecipe(action: (RecipeTreeCrafting) -> Unit) {
		val recipe = getRecipe()
		if (recipe != null) {
			action.invoke(recipe)
		}
	}
	
	fun Int.tickDelay(lambda: () -> Any) {
		if (ticksAlive % this == 0) {
			lambda.invoke()
		}
	}
	
	fun itemDisplays(do_this: (display: TileItemDisplay) -> Unit) {
		for (i in ITEMDISPLAY_LOCATIONS) {
			val block = worldObj.getTileEntity(i.x + xCoord, i.y + yCoord - 4, i.z + zCoord)
			if (block is TileItemDisplay) {
				do_this.invoke(block)
			}
		}
	}
	
	fun renderHUD(mc: Minecraft, res: ScaledResolution) {
		val xc = res.scaledWidth / 2
		val yc = res.scaledHeight / 2
		var angle = -90f
		val radius = 24
		val recipe = getRecipe()
		val items = getRecipeInputs()
		if (recipe != null && (mana == 0 || manaRequired > 0)) {
			GL11.glEnable(GL11.GL_BLEND)
			GL11.glEnable(GL12.GL_RESCALE_NORMAL)
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
			val progress = mana.F / manaRequired.F
			mc.renderEngine.bindTexture(HUDHandler.manaBar)
			GL11.glColor4f(1f, 1f, 1f, 1f)
			RenderHelper.drawTexturedModalRect(xc + radius + 9, yc - 8, 0f, 0, 8, 22, 15)
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting()
			RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, recipe.core, xc + radius + 16, yc + 8)
			RenderHelper.renderProgressPie(xc + radius + 32, yc - 8, progress, recipe.out)
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting()
			mc.fontRenderer.drawStringWithShadow("+", xc + radius + 14, yc + 12, 0xFFFFFF)
		}
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting()
		val anglePer = 360f / items.size
		for (i in items) {
			val xPos = xc + cos(angle * Math.PI / 180.0) * radius - 8
			val yPos = yc + sin(angle * Math.PI / 180.0) * radius - 8
			GL11.glTranslated(xPos, yPos, 0.0)
			RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, i, 0, 0)
			GL11.glTranslated(-xPos, -yPos, 0.0)
			angle += anglePer
		}
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting()
	}
	
	fun getRecipeInputs(): ArrayList<ItemStack> {
		val items = ArrayList<ItemStack>()
		
		for (i in ITEMDISPLAY_LOCATIONS) {
			val block = worldObj.getTileEntity(i.x + xCoord, i.y + yCoord - 4, i.z + zCoord)
			
			if (block is TileItemDisplay) {
				val item = block.get(0)
				
				if (item != null) items.add(item)
			}
			
		}
		
		return items
	}
	
	override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
		nbttagcompound.setInteger("mana", mana)
		nbttagcompound.setInteger("manaRequired", manaRequired)
		nbttagcompound.setInteger("stage", stage)
		nbttagcompound.setInteger("stageTicks", stageTicks)
	}
	
	override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
		mana = nbttagcompound.getInteger("mana")
		manaRequired = nbttagcompound.getInteger("manaRequired")
		stage = nbttagcompound.getInteger("stage")
		stageTicks = nbttagcompound.getInteger("stageTicks")
	}
	
	fun advanceStage() {
		++stage
		stageTicks = 0
		sync()
	}
	
	fun sync() {
		ASJUtilities.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord)
	}
	
	fun craftingFanciness(recipe: RecipeTreeCrafting) {
		stage = 0
		
		worldObj.setBlockToAir(xCoord, yCoord - 3, zCoord)
		worldObj.setBlock(xCoord, yCoord - 3, zCoord, recipe.out.block, recipe.out.meta, 3)
		
		if (ItemNBTHelper.getBoolean(recipe.out, TAG_SUFF_TILE, false)) run tile@ {
			val copy = recipe.out.copy()
			copy.tagCompound.removeTag(TAG_SUFF_TILE)
			
			val tile = TileEntity.createAndLoadEntity(copy.tagCompound) ?: return@tile
			
			tile.xCoord = xCoord
			tile.yCoord = yCoord - 3
			tile.zCoord = zCoord
			
			worldObj.setTileEntity(xCoord, yCoord - 3, zCoord, tile)
		}
		
		worldObj.playSoundEffect(xCoord.D, yCoord.D, zCoord.D, "botania:enchanterEnchant", 1f, 1f)
		
		for (i in 0..24) {
			val red = Math.random().F
			val green = Math.random().F
			val blue = Math.random().F
			Botania.proxy.sparkleFX(worldObj, xCoord.D + 0.5 + Math.random() * 0.4 - 0.2, (yCoord + 1).D, zCoord.D + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, Math.random().F, 10)
		}
		
		val recipeItems = ArrayList(recipe.inputs)
		
		itemDisplays {
			for (rItem: Any? in recipeItems) {
				if (rItem != null)
					if (it.get(0)?.itemEquals(rItem) == true) {
						it.apply {
							setInventorySlotContents(0, null)
							invalidate()
						}
						recipeItems.remove(rItem)
						break
					}
			}
		}
	}
	
	/**
	 * ISparkAttachable overrides
	 */
	
	override fun canAttachSpark(p0: ItemStack?) = true
	
	override fun areIncomingTranfersDone() = stage != 1
	
	override fun getAvailableSpaceForMana(): Int {
		val recipe = getRecipe()
		val space = max(0, manaRequired - mana)
		if (recipe != null && recipe.throttle > 0)
			return min(recipe.throttle, space)
		return space
	}
	
	override fun isFull() = (mana >= manaRequired)
	override fun canRecieveManaFromBursts() = manaRequired > 0
	override fun getCurrentMana() = mana
	
	override fun recieveMana(mana: Int) {
		this.mana = min(manaRequired, this.mana + mana)
	}
	
	override fun attachSpark(entity: ISparkEntity) = Unit /*Pass*/
	
	override fun getAttachedSpark(): ISparkEntity? {
		val sparks = worldObj.getEntitiesWithinAABB(ISparkEntity::class.java, AxisAlignedBB.getBoundingBox(xCoord.D, (yCoord + 1).D, zCoord.D, (xCoord + 1).D, (yCoord + 2).D, (zCoord + 1).D))
		return if (sparks.size == 1) {
			val e = sparks[0] as Entity
			e as ISparkEntity
		} else {
			null
		}
	}
}
