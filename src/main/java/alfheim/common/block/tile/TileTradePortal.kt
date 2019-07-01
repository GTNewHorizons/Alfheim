package alfheim.common.block.tile

import alfheim.api.AlfheimAPI
import alfheim.common.core.registry.AlfheimBlocks
import alfheim.common.core.util.AlfheimConfig
import com.google.common.base.Function
import net.minecraft.block.Block
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.multiblock.*
import vazkii.botania.api.recipe.RecipeElvenTrade
import vazkii.botania.common.Botania
import vazkii.botania.common.block.*
import vazkii.botania.common.block.tile.TileMod
import vazkii.botania.common.core.handler.ConfigHandler

class TileTradePortal: TileMod() {
	
	internal var tradeRecipe: RecipeElvenTrade? = null
	internal var recipeMult = 0
	internal var recipeNum = -1
	
	var ticksOpen = 0
	private var hasUnloadedParts = false
	
	internal val portalAABB: AxisAlignedBB
		get() = if (getBlockMetadata() == 2) AxisAlignedBB.getBoundingBox(xCoord.toDouble(), (yCoord + 1).toDouble(), (zCoord - 1).toDouble(), (xCoord + 1).toDouble(), (yCoord + 4).toDouble(), (zCoord + 2).toDouble()) else AxisAlignedBB.getBoundingBox((xCoord - 1).toDouble(), (yCoord + 1).toDouble(), zCoord.toDouble(), (xCoord + 2).toDouble(), (yCoord + 4).toDouble(), (zCoord + 1).toDouble())
	
	private val validMetadata: Int
		get() {
			if (this.worldObj.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) return 0
			if (checkConverter(null)) return 1
			return if (checkConverter(CONVERTER_X_Z)) 2 else 0
			
		}
	
	val output: ItemStack
		get() = this.tradeRecipe!!.output
	
	val isTradeOn: Boolean
		get() = tradeRecipe != null && recipeMult > 0
	
	override fun updateEntity() {
		val meta = getBlockMetadata()
		if (meta == 0) {
			ticksOpen = 0
			return
		}
		val newMeta = validMetadata
		
		if (!hasUnloadedParts) {
			ticksOpen++
			
			val aabb = portalAABB
			
			if (ticksOpen > 60) {
				if (ConfigHandler.elfPortalParticlesEnabled)
					blockParticle(meta)
				
				if (worldObj.rand.nextInt(AlfheimConfig.tradePortalRate) == 0 && !worldObj.isRemote) setRandomRecipe()
				
				if (tradeRecipe != null) {
					val items = worldObj.getEntitiesWithinAABB(EntityItem::class.java, aabb)
					if (!worldObj.isRemote) {
						for (item in items) {
							item as EntityItem
							if (item.isDead)
								continue
							
							val stack = item.entityItem
							if (stack != null && isTradeAvailable(stack, tradeRecipe!!.output)) {
								stack.stackSize -= tradeRecipe!!.output.stackSize
								performTrade()
								break
							}
						}
					}
				}
			}
		}
		
		if (newMeta != meta) {
			if (newMeta == 0) for (i in 0..35) blockParticle(meta)
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 1 or 2)
		}
		
		hasUnloadedParts = false
	}
	
	internal fun isTradeAvailable(input: ItemStack, output: ItemStack): Boolean {
		return input.item === output.item && input.itemDamage == output.itemDamage && input.stackSize >= output.stackSize
	}
	
	private fun blockParticle(meta: Int) {
		val i = worldObj.rand.nextInt(AIR_POSITIONS.size)
		var pos: DoubleArray? = doubleArrayOf((AIR_POSITIONS[i][0] + 0.5f).toDouble(), (AIR_POSITIONS[i][1] + 0.5f).toDouble(), (AIR_POSITIONS[i][2] + 0.5f).toDouble())
		if (meta == 2) pos = CONVERTER_X_Z_FP.apply(pos)
		
		val motionMul = 0.2f
		Botania.proxy.wispFX(getWorldObj(), xCoord + pos!![0], yCoord + pos[1], zCoord + pos[2],
							 (Math.random() * 0.5f + 0.5f).toFloat(), (Math.random() * 0.25f + 0.5f).toFloat(), (Math.random() * 0.25f).toFloat(),
							 (Math.random() * 0.15f + 0.1f).toFloat(), (Math.random() - 0.5f).toFloat() * motionMul,
							 (Math.random() - 0.5f).toFloat() * motionMul, (Math.random() - 0.5f).toFloat() * motionMul)
	}
	
	fun onWanded(): Boolean {
		if (getBlockMetadata() == 0 && worldObj.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim) {
			val newMeta = validMetadata
			if (newMeta != 0) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 1 or 2)
				return true
			}
		}
		return false
	}
	
	internal fun setRandomRecipe() {
		val i = this.worldObj.rand.nextInt(BotaniaAPI.elvenTradeRecipes.size)
		val recipe = BotaniaAPI.elvenTradeRecipes[i]
		
		if (AlfheimAPI.isRetradeable(recipe.output)) {
			if (recipe.output.item is ItemBlock && Block.getBlockFromItem(recipe.output.item) is BlockStorage && this.worldObj.rand.nextInt(10) != 0) setRandomRecipe()
			recipeMult = worldObj.rand.nextInt(16) + 1
			setTradeRecipe(recipe)
			recipeNum = i
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
		} else
			setRandomRecipe()
	}
	
	internal fun performTrade() {
		if (recipeMult <= 0) return
		val inputs = tradeRecipe!!.inputs
		for (`in` in inputs) {
			val stack = when (`in`) {
				is String 		-> OreDictionary.getOres(`in`)[0]
				is ItemStack	-> `in`.copy()
				else			-> throw IllegalArgumentException("Invalid input")
			}
			spawnItem(ItemStack(stack.item, 1, stack.itemDamage))
		}
		if (--recipeMult <= 0) setTradeRecipe(null)
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
	}
	
	internal fun spawnItem(stack: ItemStack) {
		val item = EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, stack)
		worldObj.spawnEntityInWorld(item)
	}
	
	fun setTradeRecipe(recipe: RecipeElvenTrade?) {
		tradeRecipe = recipe
		if (worldObj != null) worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType())
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		nbt.setInteger(TAG_TICKS_OPEN, ticksOpen)
		nbt.setInteger(TAG_RECIPE_MULT, this.recipeMult)
		nbt.setInteger(TAG_RECIPE_NUM, this.recipeNum)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		ticksOpen = nbt.getInteger(TAG_TICKS_OPEN)
		recipeMult = nbt.getInteger(TAG_RECIPE_MULT)
		recipeNum = nbt.getInteger(TAG_RECIPE_NUM)
		if (recipeNum != -1) setTradeRecipe(BotaniaAPI.elvenTradeRecipes[recipeNum])
	}
	
	private fun checkConverter(baseConverter: Function<IntArray, IntArray>?): Boolean {
		return checkMultipleConverters(arrayOf(baseConverter)) || checkMultipleConverters(arrayOf(CONVERTER_Z_SWAP, baseConverter))
	}
	
	private fun checkMultipleConverters(converters: Array<Function<IntArray, IntArray>?>?): Boolean {
		if (wrong2DArray(AIR_POSITIONS, Blocks.air, -1, converters)) return false
		if (wrong2DArray(LIVINGROCK_POSITIONS, ModBlocks.livingrock, 0, converters)) return false
		if (wrong2DArray(GLOWSTONE_POSITIONS, Blocks.glowstone, 0, converters)) return false
		if (wrong2DArray(PYLON_POSITIONS, AlfheimBlocks.alfheimPylon, 1, converters)) return false
		
		lightPylons(converters)
		return true
	}
	
	private fun lightPylons(converters: Array<Function<IntArray, IntArray>?>?) {
		if (ticksOpen < 50)
			return
		
		for (pos in PYLON_POSITIONS) {
			var pos = pos
			converters?.forEach { pos = it?.apply(pos) ?: pos }
			
			val tile = worldObj.getTileEntity(xCoord + pos[0], yCoord + pos[1], zCoord + pos[2])
			if (tile is TileAlfheimPylon) {
				tile.activated = true
				tile.centerX = xCoord
				tile.centerY = yCoord
				tile.centerZ = zCoord
			}
		}
	}
	
	private fun wrong2DArray(positions: Array<IntArray>, block: Block, meta: Int, converters: Array<Function<IntArray, IntArray>?>?): Boolean {
		for (pos in positions) {
			var pos = pos
			
			converters?.forEach { pos = it?.apply(pos) ?: pos }
			
			if (!checkPosition(pos, block, meta))
				return true
		}
		return false
	}
	
	private fun checkPosition(pos: IntArray, block: Block, meta: Int): Boolean {
		val x = xCoord + pos[0]
		val y = yCoord + pos[1]
		val z = zCoord + pos[2]
		if (!worldObj.blockExists(x, y, z)) {
			hasUnloadedParts = true
			return true // Don't fuck everything up if there's a chunk unload
		}
		
		val blockat = worldObj.getBlock(x, y, z)
		if (if (block === Blocks.air) blockat.isAir(worldObj, x, y, z) else blockat === block) {
			if (meta == -1)
				return true
			
			val metaat = worldObj.getBlockMetadata(x, y, z)
			return meta == metaat
		}
		
		return false
	}
	
	override fun getRenderBoundingBox(): AxisAlignedBB {
		return TileEntity.INFINITE_EXTENT_AABB
	}
	
	companion object {
		
		private val LIVINGROCK_POSITIONS = arrayOf(intArrayOf(-1, 0, 0), intArrayOf(1, 0, 0), intArrayOf(-2, 1, 0), intArrayOf(2, 1, 0), intArrayOf(-2, 3, 0), intArrayOf(2, 3, 0), intArrayOf(-1, 4, 0), intArrayOf(1, 4, 0))
		
		private val GLOWSTONE_POSITIONS = arrayOf(intArrayOf(-2, 2, 0), intArrayOf(2, 2, 0), intArrayOf(0, 4, 0))
		
		private val PYLON_POSITIONS = arrayOf(intArrayOf(-2, 4, 0), intArrayOf(2, 4, 0))
		
		private val AIR_POSITIONS = arrayOf(intArrayOf(-1, 1, 0), intArrayOf(0, 1, 0), intArrayOf(1, 1, 0), intArrayOf(-1, 2, 0), intArrayOf(0, 2, 0), intArrayOf(1, 2, 0), intArrayOf(-1, 3, 0), intArrayOf(0, 3, 0), intArrayOf(1, 3, 0))
		
		const val TAG_TICKS_OPEN = "ticksOpen"
		const val TAG_RECIPE_MULT = "recipeMult"
		const val TAG_RECIPE_NUM = "recipeNub"
		
		private val CONVERTER_X_Z = Function<IntArray, IntArray> { input -> intArrayOf(input!![2], input[1], input[0]) }
		
		private val CONVERTER_X_Z_FP = Function<DoubleArray, DoubleArray> { input -> doubleArrayOf(input!![2], input[1], input[0]) }
		
		private val CONVERTER_Z_SWAP = Function<IntArray, IntArray> { input -> intArrayOf(input!![0], input[1], -input[2]) }
		
		fun makeMultiblockSet(): MultiblockSet {
			val mb = Multiblock()
			for (l in LIVINGROCK_POSITIONS) mb.addComponent(l[0], l[1] + 1, l[2], ModBlocks.livingrock, 0)
			for (g in GLOWSTONE_POSITIONS) mb.addComponent(g[0], g[1] + 1, g[2], Blocks.glowstone, 0)
			for (p in PYLON_POSITIONS) mb.addComponent(p[0], p[1] + 1, p[2], AlfheimBlocks.alfheimPylon, 1)
			mb.addComponent(0, 1, 0, AlfheimBlocks.tradePortal, 0)
			mb.setRenderOffset(0, -1, 0)
			return mb.makeSet()
		}
	}
}
