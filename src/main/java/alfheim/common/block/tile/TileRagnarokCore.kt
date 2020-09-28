package alfheim.common.block.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.ASJTile
import alfheim.api.ModInfo
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.VisualEffectHandler
import alfmod.common.item.AlfheimModularItems
import alfmod.common.item.material.EventResourcesMetas
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import vazkii.botania.api.mana.IManaPool
import vazkii.botania.api.mana.spark.*
import vazkii.botania.common.block.tile.mana.TilePool
import kotlin.math.*

class TileRagnarokCore: ASJTile(), ISparkAttachable {
	
	var mana = 0
	var prevMana = mana
	var lastManaTick = 0L
	
	var started = false
	var isWinter = false
	var isSummer = false
	
	// TODO add valid positions set and delete block if it is in manually built hand
	// TODO do nothing if winter/summer/ragnarok is active
	override fun updateEntity() {
		if (!started)
			return
		
		if (!checkStructure(true)) {
			started = false
			mana = 0
			prevMana = 0
		}
		
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
		
//		if (!worldObj.isRemote) ASJUtilities.chatLog("Got ${mana - prevMana} mana")
		
		if (mana >= MAX_MANA)
			return startEvent()
		
		destroyCrystal()
	}
	
	fun checkStructure(stackOnly: Boolean): Boolean {
		var relic: ItemStack? =
		
				(worldObj.getTileEntity(xCoord - 5, yCoord, zCoord) as? TileItemDisplay)?.get(0)
		if (relic?.item === AlfheimModularItems.eventResource && relic.meta == EventResourcesMetas.SnowRelic) {
			if (stackOnly) return true
			
			return SchemaUtils.checkStructure(worldObj, xCoord, yCoord + 1, zCoord, structureWinter) { x, y, z ->
				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, worldObj.provider.dimensionId, x + 0.5, y + 0.5, z + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
			}.also { if (it) isWinter = true }
		}
		
		relic = (worldObj.getTileEntity(xCoord + 5, yCoord, zCoord) as? TileItemDisplay)?.get(0)
		if (relic?.item === AlfheimModularItems.eventResource && relic.meta == EventResourcesMetas.VolcanoRelic) {
			if (stackOnly) return true
			
			return SchemaUtils.checkStructure(worldObj, xCoord, yCoord + 1, zCoord, structureSummer) { x, y, z ->
				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, worldObj.provider.dimensionId, x + 0.5, y + 0.5, z + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
			}.also { if (it) isSummer = true }
		}
		
		return false
	}
	
	val crystalPositions: MutableList<Array<Int>> = mutableListOf(
		arrayOf(1, 3, 0),
		arrayOf(-1, 3, 0),
		arrayOf(0, 3, 1),
		arrayOf(0, 3, -1),
		arrayOf(1, 7, 0),
		arrayOf(-1, 7, 0),
		arrayOf(0, 7, 1),
		arrayOf(0, 7, -1)
	)
	
	val destroyablePositions = ArrayList<Array<Int>>()
	
	init {
		arrayOf(4, 5, 6).forEach { y ->
			for (x in -1..1)
				for (z in -1..1) {
					if (x == 0 && z == 0) continue
					destroyablePositions.add(arrayOf(x, y, z))
				}
		}
	}
	
	fun destroyCrystal() {
		var destroyChance = abs(prevMana + 9000 - mana) / 5000.0
		if (prevMana + 9000 - mana < 0) destroyChance *= 5
		if (destroyChance == 0.0 || !ASJUtilities.chance(destroyChance)) return
		
		if (ASJUtilities.chance(0.1)) {
			worldObj.setBlockToAir(xCoord, yCoord + 5, zCoord)
			return
		}
		
		crystalPositions.forEach {
			if (!worldObj.isAirBlock(xCoord + it[0], yCoord + it[1], zCoord + it[2]))
				destroyablePositions.add(it)
		}
		
		val (x, y, z) = destroyablePositions.random()
		worldObj.setBlockToAir(xCoord + x, yCoord + y, zCoord + z)
		destroyablePositions.clear()
	}
	
	fun startEvent() {
		val success = isSuccessful()
		if (success)
			started = false
		
		// TODO add consequences
	}
	
	fun isSuccessful(): Boolean {
		if (!checkStructure(false))
			return false
		
		if (isWinter) {
			(worldObj.getTileEntity(xCoord - 5, yCoord, zCoord) as TileItemDisplay)[0] = null
			if (!worldObj.isRemote) ASJUtilities.sayToAllOnline("alfheimmisc.ragnarok.startedWinter")
			// TODO start winter in RagnarokHandler
			return true
		}
		
		if (isSummer) {
			(worldObj.getTileEntity(xCoord + 5, yCoord, zCoord) as TileItemDisplay)[0] = null
			if (!worldObj.isRemote) ASJUtilities.sayToAllOnline("alfheimmisc.ragnarok.startedSummer")
			// TODO start summer in RagnarokHandler
			return true
		}
		
		// theoretically unreachable
		return false
	}
	
	override fun writeToNBT(nbt: NBTTagCompound) {
		nbt.setInteger(TAG_MANA, mana)
		nbt.setInteger(TAG_PREV_MANA, prevMana)
		nbt.setLong(TAG_LAST_MANA_TICK, lastManaTick)
		nbt.setBoolean(TAG_STARTED, started)
	}
	
	override fun readFromNBT(nbt: NBTTagCompound) {
		mana = nbt.getInteger(TAG_MANA)
		prevMana = nbt.getInteger(TAG_PREV_MANA)
		lastManaTick = nbt.getLong(TAG_LAST_MANA_TICK)
		started = nbt.getBoolean(TAG_STARTED)
	}
	
	override fun canAttachSpark(stack: ItemStack?) = true
	
	override fun areIncomingTranfersDone() = !started
	
	override fun attachSpark(entity: ISparkEntity?) = Unit
	
	override fun recieveMana(mana: Int) {
		if (lastManaTick != worldObj.totalWorldTime) {
			prevMana = this.mana
			lastManaTick = worldObj.totalWorldTime
			
			if (isWinter)
				worldObj.spawnParticle("iconcrack_${AlfheimModularItems.eventResource.id}_${EventResourcesMetas.SnowRelic}", xCoord - 4.5, yCoord + 1.0, zCoord + .5, 0.625, 0.2, 0.0)
			
			if (isSummer)
				worldObj.spawnParticle("iconcrack_${AlfheimModularItems.eventResource.id}_${EventResourcesMetas.VolcanoRelic}", xCoord + 5.5, yCoord + 1.0, zCoord + .5, -0.625, 0.1, 0.0)
		}
		
		this.mana = max(0, min(MAX_MANA, this.mana + mana))
	}
	
	override fun getAttachedSpark(): ISparkEntity? {
		return worldObj.getEntitiesWithinAABB(ISparkEntity::class.java, boundingBox().offset(0.0, 1.0, 0.0)).safeZeroGet(0) as? ISparkEntity
	}
	
	override fun getAvailableSpaceForMana() = max(0, MAX_MANA - mana)
	
	override fun isFull() = mana >= MAX_MANA
	
	override fun getCurrentMana() = mana
	
	override fun canRecieveManaFromBursts() = false
	
	companion object {
		
		const val MAX_MANA = TilePool.MAX_MANA * 12 // suffer, mwa-ha-ha
		
		const val TAG_MANA = "mana"
		const val TAG_PREV_MANA = "prevMana"
		const val TAG_LAST_MANA_TICK = "lastManaTick"
		const val TAG_STARTED = "started"
		
		val structureWinter = javaClass.getResourceAsStream("/assets/${ModInfo.MODID}/schemas/WinterHandGem").use {
			it.readBytes().toString(Charsets.UTF_8)
		}
		
		val structureSummer = javaClass.getResourceAsStream("/assets/${ModInfo.MODID}/schemas/SummerHandGem").use {
			it.readBytes().toString(Charsets.UTF_8)
		}
	}
}