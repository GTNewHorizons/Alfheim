package alfheim.common.block.tile

import alfheim.api.ShadowFoxAPI
import alfheim.api.crafting.recipe.RecipeTreeCrafting
import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.core.util.itemEquals
import alfheim.common.lexicon.MultiblockComponentRainbow
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import org.lwjgl.opengl.*
import vazkii.botania.api.internal.VanillaPacketDispatcher
import vazkii.botania.api.lexicon.multiblock.*
import vazkii.botania.api.mana.IManaPool
import vazkii.botania.api.mana.spark.*
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.client.core.helper.RenderHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.tile.TileMod
import java.util.*
import kotlin.math.*

class TileTreeCrafter: TileMod(), ISparkAttachable {
	
	companion object {
		val ITEMDISPLAY_LOCATIONS = arrayOf(Pos(-3, 1, 3), Pos(-4, 1, 0), Pos(0, 1, 4), Pos(-3, 1, -3), Pos(0, 1, -4), Pos(3, 1, -3), Pos(4, 1, 0), Pos(3, 1, 3))
		val COLOREDWOOD_LOCATIONS = arrayOf(Pos(2, 0, 2), Pos(2, 0, 1), Pos(2, 0, -1), Pos(2, 0, -2), Pos(1, 0, 2), Pos(1, 0, -2), Pos(-1, 0, 2), Pos(-1, 0, -2), Pos(-2, 0, 2), Pos(-2, 0, 1), Pos(-2, 0, -1), Pos(-2, 0, -2))
		val OBSIDIAN_LOCATIONS = arrayOf(Pos(3, 0, 2), Pos(3, 0, 1), Pos(3, 0, 0), Pos(3, 0, -1), Pos(3, 0, -2), Pos(2, 0, 3), Pos(2, 0, 0), Pos(2, 0, -3), Pos(1, 0, 3), Pos(1, 0, 0), Pos(1, 0, -3), Pos(0, 0, 3), Pos(0, 0, 2), Pos(0, 0, 1), Pos(0, 0, -1), Pos(0, 0, -2), Pos(0, 0, -3), Pos(-1, 0, 3), Pos(-1, 0, 0), Pos(-1, 0, -3), Pos(-2, 0, 3), Pos(-2, 0, 0), Pos(-2, 0, -3), Pos(-3, 0, 2), Pos(-3, 0, 1), Pos(-3, 0, 0), Pos(-3, 0, -1), Pos(-3, 0, -2), Pos(1, 0, 1), Pos(1, 0, -1), Pos(-1, 0, 1), Pos(-1, 0, -1))
		
		fun makeMultiblockSet(): MultiblockSet {
			val mb = Multiblock()
			
			for (i in ITEMDISPLAY_LOCATIONS) {
				mb.addComponent(i.x, i.y + 2, i.z, ModBlocks.pylon, 0)
				mb.addComponent(MultiblockComponentRainbow(ChunkCoordinates(i.x, i.y, i.z), ShadowFoxBlocks.itemDisplay))
			}
			
			for (i in OBSIDIAN_LOCATIONS) {
				mb.addComponent(i.x, i.y, i.z, Blocks.obsidian, 0)
			}
			
			for (i in COLOREDWOOD_LOCATIONS) {
				mb.addComponent(MultiblockComponentRainbow(ChunkCoordinates(i.x, i.y, i.z), ShadowFoxBlocks.coloredPlanks, ShadowFoxBlocks.rainbowPlanks))
			}
			
			mb.addComponent(MultiblockComponentRainbow(ChunkCoordinates(0, 0, 0), ShadowFoxBlocks.coloredDirtBlock, ShadowFoxBlocks.rainbowDirtBlock))
			mb.addComponent(MultiblockComponentRainbow(ChunkCoordinates(0, 4, 0), ShadowFoxBlocks.coloredPlanks, ShadowFoxBlocks.rainbowPlanks))
			
			return mb.makeSet()
		}
		
		fun canEnchanterExist(world: World, x: Int, y: Int, z: Int): Boolean {
			
			val y0 = y - 4
			
			for (i in ITEMDISPLAY_LOCATIONS) {
				if (!i.isBlock(world, ModBlocks.pylon, x0 = x, y0 = y0 + 2, z0 = z)) {
					// FMLLog.log(Level.INFO, "Pylon at $i")
					return false
				}
				if (!i.isBlock(world, ShadowFoxBlocks.itemDisplay, x0 = x, y0 = y0, z0 = z)) {
					// FMLLog.log(Level.INFO, "Item Display at $i")
					return false
				}
			}
			
			for (i in OBSIDIAN_LOCATIONS) {
				if (!i.isBlock(world, Blocks.obsidian, x0 = x, y0 = y0, z0 = z)) {
					// FMLLog.log(Level.INFO, "Obsidian at $i")
					return false
				}
			}
			
			for (i in COLOREDWOOD_LOCATIONS) {
				if (!i.isBlock(world, ShadowFoxBlocks.coloredPlanks, x0 = x, y0 = y0, z0 = z) &&
					!i.isBlock(world, ShadowFoxBlocks.rainbowPlanks, x0 = x, y0 = y0, z0 = z)) {
					// FMLLog.log(Level.INFO, "Colored Wood at $i")
					return false
				}
			}
			
			if (world.getBlock(x, y0 + 4, z) !== ShadowFoxBlocks.treeCrafterBlock &&
				world.getBlock(x, y0 + 4, z) !== ShadowFoxBlocks.treeCrafterBlockRB &&
				world.getBlock(x, y0 + 4, z) !== ShadowFoxBlocks.coloredPlanks &&
				world.getBlock(x, y0 + 4, z) !== ShadowFoxBlocks.rainbowPlanks) {
				// FMLLog.log(Level.INFO, "Core Block")
				return false
			}
			
			if (world.getBlock(x, y0, z) !== ShadowFoxBlocks.coloredDirtBlock &&
				world.getBlock(x, y0, z) !== ShadowFoxBlocks.rainbowDirtBlock) {
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
			val meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord)
			
			if (block === ShadowFoxBlocks.treeCrafterBlock)
				worldObj.setBlock(xCoord, yCoord, zCoord, ShadowFoxBlocks.coloredPlanks, meta, 3)
			else worldObj.setBlock(xCoord, yCoord, zCoord, ShadowFoxBlocks.rainbowPlanks, 0, 3)
			
			for (var11 in 0..49) {
				val var13 = Math.random().toFloat()
				val var16 = Math.random().toFloat()
				val var19 = Math.random().toFloat()
				Botania.proxy.wispFX(worldObj, xCoord.toDouble() + 0.5, yCoord.toDouble() + 0.5, zCoord.toDouble() + 0.5, var13, var16, var19, Math.random().toFloat() * 0.15f + 0.15f, (Math.random() - 0.5).toFloat() * 0.25f, (Math.random() - 0.5).toFloat() * 0.25f, (Math.random() - 0.5).toFloat() * 0.25f)
			}
			
			worldObj.playSoundEffect(xCoord.toDouble(), yCoord.toDouble(), zCoord.toDouble(), "botania:enchanterBlock", 0.5F, 10F)
			
			return //finishes execution just in case
		}
		
		val recipe = getValidRecipe()
		var recipeItems = ArrayList<Any?>()
		if (recipe != null) recipeItems = ArrayList(recipe.inputs)
		
		itemDisplays {
			val stack = it.getStackInSlot(0)
			if (stack != null) {
				
				val s = 0.2f + Math.random().toFloat() * 0.1f
				val m = 0.03f + Math.random().toFloat() * 0.015f
				
				for (rItem in recipeItems) {
					if (rItem != null)
						if (stack.itemEquals(rItem)) {
							if (mana > 0) {
								if (stack.item is ItemBlock) worldObj.spawnParticle("blockcrack_${Item.getIdFromItem(stack.item)}_${stack.itemDamage}", it.xCoord.toDouble() + .5, it.yCoord + 1.0, it.zCoord.toDouble() + .5, (xCoord.toDouble() - it.xCoord.toDouble()) * 8.0, 0.1, (zCoord.toDouble() - it.zCoord.toDouble()) * 8.0)
								else worldObj.spawnParticle("iconcrack_${Item.getIdFromItem(stack.item)}_${stack.itemDamage}", it.xCoord.toDouble() + .5, it.yCoord + 1.0, it.zCoord.toDouble() + .5, (xCoord.toDouble() - it.xCoord.toDouble()) / 8.0, 0.1, (zCoord.toDouble() - it.zCoord.toDouble()) / 8.0)
								Botania.proxy.wispFX(worldObj, it.xCoord.toDouble() + .5, it.yCoord + 3.toDouble() + .5, it.zCoord.toDouble() + .5, 1.0f, 1.0f, 1.0f, s, -m)
							}
							recipeItems.remove(rItem)
							break
						}
				}
			}
		}
		
		if (getValidRecipe() == null) stage = 0
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
						val sparks = SparkHelper.getSparksAround(worldObj, xCoord.toDouble() + 0.5, yCoord.toDouble() + 0.5, zCoord.toDouble() + 0.5)
						if (sparks != null) for (spark in sparks)
							if (spark != null && attachedSpark !== spark && spark.attachedTile != null && spark.attachedTile is IManaPool)
								spark.registerTransfer(attachedSpark)
					}
				}
			}
			
			2    -> {
				forRecipe { craftingFanciness(it) }
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
				val radian = (i.toDouble() * 3.141592653589793 / 180.0).toFloat()
				val xp = xCoord.toDouble() + cos(radian.toDouble()) * 3
				val zp = zCoord.toDouble() + sin(radian.toDouble()) * 3
				Botania.proxy.wispFX(worldObj, xp + 0.5, yCoord - 3.0, zp + 0.5, 0.0f, 1.0f, 1.0f, 0.3f, -0.01f)
			}
		}
	}
	
	fun getValidRecipe(): RecipeTreeCrafting? {
		if (worldObj.getBlock(xCoord, yCoord - 3, zCoord) === ShadowFoxBlocks.irisSapling) {
			return getRecipe()
		}
		return null
	}
	
	fun getRecipe() = ShadowFoxAPI.treeRecipes.firstOrNull { it.matches(getRecipeInputs()) }
	
	fun forRecipe(action: (RecipeTreeCrafting) -> Unit) {
		val recipe = getValidRecipe()
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
			val sapling = worldObj.getBlock(xCoord, yCoord - 3, zCoord) == ShadowFoxBlocks.irisSapling
			GL11.glEnable(GL11.GL_BLEND)
			GL11.glEnable(GL12.GL_RESCALE_NORMAL)
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
			val progress = mana.toFloat() / manaRequired.toFloat()
			mc.renderEngine.bindTexture(HUDHandler.manaBar)
			GL11.glColor4f(1f, 1f, 1f, 1f)
			RenderHelper.drawTexturedModalRect(xc + radius + 9, yc - 8, 0f, if (sapling) 0 else 22, 8, 22, 15)
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting()
			if (!sapling) RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, ItemStack(ShadowFoxBlocks.irisSapling), xc + radius + 16, yc + 8)
			RenderHelper.renderProgressPie(xc + radius + 32, yc - 8, progress, recipe.output)
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting()
			if (!sapling) mc.fontRenderer.drawStringWithShadow("+", xc + radius + 14, yc + 12, 0xFFFFFF)
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
				val item = block.getStackInSlot(0)
				
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
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord)
	}
	
	fun craftingFanciness(recipe: RecipeTreeCrafting) {
		stage = 0
		
		worldObj.setBlockToAir(xCoord, yCoord - 3, zCoord)
		worldObj.setBlock(xCoord, yCoord - 3, zCoord, recipe.outputBlock, recipe.meta, 3)
		
		worldObj.playSoundEffect(xCoord.toDouble(), yCoord.toDouble(), zCoord.toDouble(), "botania:enchanterEnchant", 1.0f, 1.0f)
		
		for (i in 0..24) {
			val red = Math.random().toFloat()
			val green = Math.random().toFloat()
			val blue = Math.random().toFloat()
			Botania.proxy.sparkleFX(worldObj, xCoord.toDouble() + 0.5 + Math.random() * 0.4 - 0.2, (yCoord + 1).toDouble(), zCoord.toDouble() + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, Math.random().toFloat(), 10)
		}
		
		val recipeItems = ArrayList(recipe.inputs)
		
		itemDisplays {
			for (rItem: Any? in recipeItems) {
				if (rItem != null)
					if (it.getStackInSlot(0)?.itemEquals(rItem) == true) {
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
		val sparks = worldObj.getEntitiesWithinAABB(ISparkEntity::class.java, AxisAlignedBB.getBoundingBox(xCoord.toDouble(), (yCoord + 1).toDouble(), zCoord.toDouble(), (xCoord + 1).toDouble(), (yCoord + 2).toDouble(), (zCoord + 1).toDouble()))
		return if (sparks.size == 1) {
			val e = sparks[0] as Entity
			e as ISparkEntity
		} else {
			null
		}
	}
}
