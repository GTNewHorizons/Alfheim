package alfheim.common.block.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.block.ASJTile
import alexsocol.asjlib.math.Vector3
import alfheim.api.AlfheimAPI
import alfheim.common.block.AlfheimBlocks
import net.minecraft.block.Block
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.*
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.api.lexicon.multiblock.*
import vazkii.botania.api.mana.IManaPool
import vazkii.botania.api.mana.spark.*
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.block.*
import vazkii.botania.common.block.tile.mana.TilePool
import kotlin.math.*

class TileManaInfuser: ASJTile(), ISparkAttachable {
	
	var mana = 0
	var manaRequest = 0
	var knownMana = -1
	var result: ItemStack? = null
	
	val v = Vector3()
	
	val items: List<EntityItem>
		get() = worldObj.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB.getBoundingBox(xCoord.D, (yCoord + 1).D, zCoord.D, (xCoord + 1).D, (yCoord + 2).D, (zCoord + 1).D)) as List<EntityItem>
	
	val isReadyToKillGaia: Boolean
		get() = checkPlatform(0, -2, 0, Blocks.beacon, 0) && checkAll(PYLONS, AlfheimBlocks.alfheimPylon, 2)
	
	var deGaiaingTime = 0
	var soulParticlesTime = 0
	
	override fun updateEntity() {
		if (isReadyToKillGaia) {
			if (--soulParticlesTime > 0) soulParticles()
			if (--deGaiaingTime > 0) return
		}
		
		var removeMana = true
		
		if (mana <= 0 && blockMetadata != 0) worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3)
		
		if (hasValidPlatform()) {
			val items = items
			if (areItemsValid(items)) {
				if (DEBUG) println("Mana: " + mana + "\tMana requested: " + manaRequest + "\tResult: " + result!!.toString())
				
				removeMana = false
				val spark = attachedSpark
				if (spark != null) {
					val sparkEntities = SparkHelper.getSparksAround(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5)
					for (otherSpark in sparkEntities) {
						if (spark === otherSpark)
							continue
						
						if (otherSpark.attachedTile != null && otherSpark.attachedTile is IManaPool)
							otherSpark.registerTransfer(spark)
					}
				}
				
				if (mana > 0) {
					doParticles()
					if (blockMetadata != 1) worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3)
				}
				
				if (mana >= manaRequest && !worldObj.isRemote) {
					var first = true
					for (item in items) {
						if (first) {
							item.setEntityItemStack(ItemStack(result!!.item, max(result!!.stackSize, 1), result!!.meta))
							item.entityItem.readFromNBT(result!!.writeToNBT(NBTTagCompound()))
							item.playSoundAtEntity("botania:terrasteelCraft", 1f, 1f)
							first = false
						} else
							worldObj.removeEntity(item)
					}
					
					mana -= manaRequest
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3)
					worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord))
					ASJUtilities.dispatchTEToNearbyPlayers(this)
					result = null
					manaRequest = 0
				}
			} else {
				result = null
				manaRequest = 0
			}
		}
		
		if (removeMana) recieveMana(-1000)
	}
	
	fun doParticles() {
		if (worldObj.isRemote) {
			val ticks = (100.0 * (currentMana.D / manaRequest.D)).I
			
			val totalSpiritCount = 6
			val tickIncrement = 360.0 / totalSpiritCount
			
			val speed = 5
			var wticks = ticks * speed - tickIncrement
			
			val r = sin((ticks - 100) / 10.0) * 2
			val g = sin(wticks * Math.PI / 180 * 0.55)
			
			for (i in 0 until totalSpiritCount) {
				val x = xCoord.D + sin(wticks * Math.PI / 180) * r + 0.5
				val y = yCoord.D + 0.25 + abs(r) * 0.7
				val z = zCoord.D + cos(wticks * Math.PI / 180) * r + 0.5
				
				wticks += tickIncrement
				val colorsfx = floatArrayOf(ticks.F / 100.F, 0f, 1f - ticks.F / 100.F)
				Botania.proxy.wispFX(worldObj, x, y + 1, z, colorsfx[0], colorsfx[1], colorsfx[2], 0.85f, g.F * 0.05f, 0.25f)
				Botania.proxy.wispFX(worldObj, x, y + 1, z, colorsfx[0], colorsfx[1], colorsfx[2], Math.random().F * 0.1f + 0.1f, (Math.random() - 0.5).F * 0.05f, (Math.random() - 0.5).F * 0.05f, (Math.random() - 0.5).F * 0.05f, 0.9f)
				
				if (ticks == 100)
					for (j in 0..14)
						Botania.proxy.wispFX(worldObj, xCoord + 0.5, yCoord + 1.25, zCoord + 0.5, colorsfx[0], colorsfx[1], colorsfx[2], Math.random().F * 0.15f + 0.15f, (Math.random() - 0.5f).F * 0.125f, (Math.random() - 0.5f).F * 0.125f, (Math.random() - 0.5f).F * 0.125f)
			}
		}
	}
	
	fun prepareParticles() {
		Botania.proxy.setSparkleFXNoClip(true)
		for ((ci, c) in PYLONS.withIndex()) {
			for (i in -1..83) {
				v.set(0.0, 0.0, 0.1).rotate((-45 - 90 * ci).D, Vector3.oY).extend(i / 10.0).add(c[0].D, c[1].D, c[2].D)
				Botania.proxy.sparkleFX(worldObj, xCoord.D + v.x + 0.5, yCoord.D + v.y + 0.65, zCoord.D + v.z + 0.5, 1f, 0.01f, 0.01f, 1f, 2)
			}
		}
		Botania.proxy.setSparkleFXNoClip(false)
	}
	
	fun soulParticles() {
		if (soulParticlesTime < 10)
			for (c in PYLONS) {
				v.set((-c[0]).D, (-c[1]).D, (-c[2]).D).normalize().mul(0.4)
				Botania.proxy.wispFX(worldObj, xCoord.D + c[0].D + 0.5, yCoord.D + c[1].D + 0.65, zCoord.D + c[2].D + 0.5, 1f, 0.01f, 0.01f, 0.5f, v.x.F, v.y.F, v.z.F, 0.5f)
			}
		else
			for (c in GAIAS) {
				v.set(c[0].D, 0.0, c[2].D).normalize().mul(0.3)
				val r = Math.random().F * 0.3f
				val g = 0.7f + Math.random().F * 0.3f
				val b = 0.7f + Math.random().F * 0.3f
				v.rotate(107.5, Vector3.oY)
				Botania.proxy.wispFX(worldObj, xCoord.D + c[0].D + 0.5, yCoord.D + c[1].D + 0.65, zCoord.D + c[2].D + 0.5, r, g, b, 0.5f, v.x.F, v.y.F, v.z.F, 0.5f)
				v.rotate(-215.0, Vector3.oY)
				Botania.proxy.wispFX(worldObj, xCoord.D + c[0].D + 0.5, yCoord.D + c[1].D + 0.65, zCoord.D + c[2].D + 0.5, r, g, b, 0.5f, v.x.F, v.y.F, v.z.F, 0.5f)
			}
	}
	
	fun doneParticles() {
		for (i in 0..63) {
			v.set(Math.random() - 0.5, 0.0, Math.random() - 0.5).normalize().mul(Math.random() * 0.2 + 0.1)
			Botania.proxy.wispFX(worldObj, xCoord + 0.5, yCoord.D + 1.65 + Math.random() * 0.2, zCoord + 0.5, 1f, 0.01f, 0.01f, 0.5f, v.x.F, v.y.F, v.z.F, 0.5f)
		}
		for (i in 0..15) {
			v.set(0.0, 1.0, 0.0).mul(Math.random() * 0.2 + 0.1)
			Botania.proxy.wispFX(worldObj, xCoord + 0.5, yCoord.D + 1.0 + Math.random(), zCoord + 0.5, 1f, 0.01f, 0.01f, 0.5f, v.x.F, v.y.F, v.z.F, 0.5f)
		}
		for (i in 0..63) {
			Botania.proxy.sparkleFX(worldObj, xCoord.D + 0.5 + Math.random() * 0.25 - 0.125, (yCoord + 4).D, zCoord.D + 0.5 + Math.random() * 0.25 - 0.125, 1f, 0.01f, 0.01f, 5f, 15)
		}
	}
	
	fun areItemsValid(entities: List<EntityItem>): Boolean {
		//boolean DBG = !worldObj.isRemote;
		if (entities.isEmpty()) return false
		for (recipe in AlfheimAPI.manaInfuserRecipes) {
			if (DEBUG) println("$recipe")
			if (entities.size != recipe.inputs.size) {
				if (DEBUG) println("Incorrect items amount (" + entities.size + "). Skipping this recipe.")
				continue // Odd items will mess up the infusion, less means not enough materials
			}
			
			val equalitylist = BooleanArray(recipe.inputs.size) // this array contains whether required ingredient is inside of AABB of infuser
			
			if (DEBUG) println("Scanning entities...")
			
			for (entity in entities) { // For every item in AABB
				val stack = entity.entityItem
				
				if (stack.tagCompound?.hasNoTags() == true) stack.tagCompound = null
				
				if (DEBUG) println("Entity stack: $stack")
				if (DEBUG) println("Scanning recipe for stack...")
				for (i in 0 until recipe.inputs.size) {
					if (equalitylist[i]) continue
					val ing = recipe.inputs[i]
					if (DEBUG) println("Ingredient: $ing")
					var flag = false
					if (ing is ItemStack) {
						val cing = ing.copy()
						
						if (cing.meta == OreDictionary.WILDCARD_VALUE)
							cing.meta = stack.meta
						
						flag = ASJUtilities.isItemStackEqualCrafting(cing, stack)
						
						flag = flag && cing.stackSize == stack.stackSize
					} else if (ing is String) {
						val ores = OreDictionary.getOres(ing)
						for (ore in ores) {
							val core = ore.copy()
							
							if (core.meta == OreDictionary.WILDCARD_VALUE)
								core.meta = stack.meta
							
							if (ItemStack.areItemStacksEqual(stack, ItemStack(core.item, 1, core.meta))) {
								flag = true
								break
							}
						}
					}
					
					if (flag) {
						if (DEBUG) println("Entity stack matches ingredient stack ($stack == $ing) Continuing scanning.")
						equalitylist[i] = true // Marking true for further processing
						continue
					}
					if (DEBUG) println("Entity stack DON'T match ingredient stack ($stack != $ing) Continuing scanning.")
				}
			}
			
			if (DEBUG) println("Scanning complete. Checking matching")
			
			var flagAllEqual = true // I'm sure everything matches
			for (deflag in equalitylist) { // But let's check
				flagAllEqual = deflag
				if (!flagAllEqual) {
					if (DEBUG) println("Matching error. Breaking cycle!")
					break // Oh no! Something went wrong!
				}
				// Leaving to maybe do something else
			}
			
			if (flagAllEqual) { // I told you everything is fine
				if (DEBUG) println("Everything matches. Sending item and mana cost to tile, returning true.")
				manaRequest = recipe.manaUsage
				result = recipe.output
				return true
			}
		}
		
		if (DEBUG) println("Scanned all recipes, no matching found. Returning false.")
		return false
	}
	
	fun hasValidPlatform(): Boolean {
		return checkAll(QUARTZ_BLOCK, ModFluffBlocks.elfQuartz, 0) && checkAll(ELEMENTIUM_BLOCKS, ModBlocks.storage, 2)
	}
	
	fun checkAll(positions: Array<IntArray>, block: Block, meta: Int) = positions.all { checkPlatform(it[0], it[1], it[2], block, meta) }
	
	fun checkPlatform(xOff: Int, yOff: Int, zOff: Int, block: Block, meta: Int): Boolean {
		return worldObj.getBlock(xCoord + xOff, yCoord + yOff, zCoord + zOff) === block && worldObj.getBlockMetadata(xCoord + xOff, yCoord + yOff, zCoord + zOff) == meta
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		nbt.setInteger(TAG_MANA, mana)
		nbt.setInteger(TAG_MANA_REQUIRED, manaRequest)
		nbt.setInteger(TAG_DEGAIAING, deGaiaingTime)
		nbt.setInteger(TAG_SOUL_EFFECT, soulParticlesTime)
		// nbt.setInteger(TAG_KNOWN_MANA, knownMana)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		mana = nbt.getInteger(TAG_MANA)
		deGaiaingTime = nbt.getInteger(TAG_DEGAIAING)
		soulParticlesTime = nbt.getInteger(TAG_SOUL_EFFECT)
		
		if (nbt.hasKey(TAG_KNOWN_MANA))
			knownMana = nbt.getInteger(TAG_KNOWN_MANA)
	}
	
	override fun getCurrentMana() = mana
	
	override fun isFull() = mana >= MAX_MANA
	
	override fun recieveMana(mana: Int) {
		this.mana = max(0, min(MAX_MANA, this.mana + mana))
		ASJUtilities.dispatchTEToNearbyPlayers(this)
	}
	
	override fun canRecieveManaFromBursts() = hasValidPlatform() && areItemsValid(items)
	
	override fun canAttachSpark(stack: ItemStack) = true
	
	override fun attachSpark(entity: ISparkEntity) = Unit
	
	override fun getAttachedSpark(): ISparkEntity? {
		return worldObj.getEntitiesWithinAABB(ISparkEntity::class.java, boundingBox().offset(0.0, 1.0, 0.0)).safeZeroGet(0) as? ISparkEntity
	}
	
	override fun areIncomingTranfersDone() = !hasValidPlatform() || !areItemsValid(items)
	
	override fun getAvailableSpaceForMana() = max(0, MAX_MANA - mana)
	
	fun onWanded(player: EntityPlayer?) {
		if (player == null)
			return
		
		if (!player.isSneaking) {
			if (!worldObj.isRemote) {
				knownMana = mana
				val nbt = NBTTagCompound()
				writeCustomNBT(nbt)
				nbt.setInteger(TAG_KNOWN_MANA, knownMana)
				if (player is EntityPlayerMP)
					player.playerNetServerHandler.sendPacket(S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbt))
			}
			player.playSoundAtEntity("botania:ding", 0.1f, 1f)
		}
	}
	
	fun renderHUD(res: ScaledResolution) {
		val name = ItemStack(AlfheimBlocks.manaInfuser).displayName
		val color = 0xCC00FF
		HUDHandler.drawSimpleManaHUD(color, knownMana, MAX_MANA, name, res)
		org.lwjgl.opengl.GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
	}
	
	companion object {
		
		const val DEBUG = false
		
		const val MAX_MANA = TilePool.MAX_MANA * 8
		
		val GAIAS = arrayOf(intArrayOf(4, -1, 4), intArrayOf(-4, -1, 4), intArrayOf(-4, -1, -4), intArrayOf(4, -1, -4))
		val PYLONS = arrayOf(intArrayOf(6, -1, 0), intArrayOf(0, -1, 6), intArrayOf(-6, -1, 0), intArrayOf(0, -1, -6))
		val QUARTZ_BLOCK = arrayOf(intArrayOf(1, 0, 0), intArrayOf(-1, 0, 0), intArrayOf(0, 0, 1), intArrayOf(0, 0, -1))
		val ELEMENTIUM_BLOCKS = arrayOf(intArrayOf(1, 0, 1), intArrayOf(1, 0, -1), intArrayOf(-1, 0, 1), intArrayOf(-1, 0, -1))
		
		const val TAG_MANA = "mana"
		const val TAG_MANA_REQUIRED = "manaRequired"
		const val TAG_KNOWN_MANA = "knownMana"
		const val TAG_DEGAIAING = "degaiatimer"
		const val TAG_SOUL_EFFECT = "soulEffect"
		
		fun makeMultiblockSetSoul(): MultiblockSet {
			val mb = Multiblock()
			for (l in PYLONS) mb.addComponent(l[0], 1, l[2], AlfheimBlocks.alfheimPylon, 2)
			mb.addComponent(0, 0, 0, Blocks.beacon, 0)
			mb.addComponent(0, 2, 0, AlfheimBlocks.manaInfuser, 0)
			mb.addComponent(0, 5, 0, ModBlocks.brewery, 0)
			mb.setRenderOffset(0, 0, 0)
			return mb.makeSet()
		}
		
		fun makeMultiblockSetUnknown(): MultiblockSet {
			val mb = Multiblock()
			for (l in QUARTZ_BLOCK) mb.addComponent(l[0], 0, l[2], Blocks.wool, 0)
			for (l in ELEMENTIUM_BLOCKS) mb.addComponent(l[0], 0, l[2], Blocks.wool, 15)
			mb.addComponent(0, 0, 0, AlfheimBlocks.manaInfuser, 0)
			mb.setRenderOffset(0, 1, 0)
			return mb.makeSet()
		}
		
		fun makeMultiblockSet(): MultiblockSet {
			val mb = Multiblock()
			for (l in QUARTZ_BLOCK) mb.addComponent(l[0], 0, l[2], ModFluffBlocks.elfQuartz, 0)
			for (l in ELEMENTIUM_BLOCKS) mb.addComponent(l[0], 0, l[2], ModBlocks.storage, 2)
			mb.addComponent(0, 0, 0, AlfheimBlocks.manaInfuser, 0)
			mb.setRenderOffset(0, 1, 0)
			return mb.makeSet()
		}
	}
}