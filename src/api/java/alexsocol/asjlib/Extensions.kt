@file:Suppress("unused")

package alexsocol.asjlib

import alexsocol.asjlib.math.Vector3
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.block.Block
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.entity.*
import net.minecraft.entity.player.*
import net.minecraft.inventory.IInventory
import net.minecraft.item.*
import net.minecraft.potion.PotionEffect
import net.minecraft.stats.Achievement
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.oredict.OreDictionary
import kotlin.math.*

fun convertRange(x: Number, originalStart: Number, originalEnd: Number, targetStart: Number, targetEnd: Number): Double {
	val ratio = x.D / originalEnd.D - originalStart.D
	return ratio * (targetEnd.D - targetStart.D) + targetStart.D
}

fun Int.bidiRange(range: Int) = (this - range)..(this + range)

fun safeIndex(id: Int, size: Int) = max(0, min(id, size - 1))

fun <T> List<T>.safeGet(id: Int): T = this[safeIndex(id, size)]
fun <T> List<T>.safeZeroGet(id: Int): T? = if (size == 0) null else this[safeIndex(id, size)]

fun <T> Array<T>.safeGet(id: Int): T = this[safeIndex(id, size)]
fun <T> Array<T>.safeZeroGet(id: Int): T? = if (size == 0) null else this[safeIndex(id, size)]

fun <T> Array<T>.shuffled(): MutableList<T> = toMutableList().apply { shuffle() }

/**
 * Makes a list of [Pair]s from original [Iterable] (zero to first, second to third, etc)
 *
 * If there are odd amount of elements - then last pair will have either last element to [last] argument or same element in both positions if [last] is null/unprovided
 */
fun <T> Iterable<T>.paired(last: T? = null): List<Pair<T, T>> {
	val pairs = ArrayList<Pair<T, T>>()
	val i = this.iterator()
	while (i.hasNext()) {
		val a = i.next()
		val b = if (i.hasNext()) i.next() else last ?: a
		pairs.add(a to b)
	}
	return pairs
}

fun <T> MutableIterator<T>.onEach(action: MutableIterator<T>.(T) -> Unit): MutableIterator<T> {
	return apply { for (element in this) action(element) }
}

fun <T> MutableCollection<T>.removeRandom(): T {
	return this.random().also { remove(it) }
}

fun <T> Array<T?>.ensureCapacity(min: Int): Array<T?> {
	if (size >= min) return this
	val new = copyOf(min)
	System.arraycopy(this, 0, new, 0, size)
	return new
}

/**
 * Creates a tuple of type [Triple] from elements of this pair adding [third].
 */
infix fun <A, B, C> Pair<A, B>.with(third: C): Triple<A, B, C> = Triple(first, second, third)

fun String.substringEnding(lastNChars: Int): String = this.substring(0, length - lastNChars)

val Number.D get() = toDouble()
val Number.F get() = toFloat()
val Number.I get() = toInt()

operator fun <T> T.plus(s: String) = "$this$s"

/**
 * Tries block and ignores any thrown exceptions
 */
fun try_(try_: () -> Any?) {
	try {
		try_()
	} catch (ignore: Throwable) {}
}

// ################ MINECRAFT ####################

/** Free fall acceleration */
const val g = 0.08 * 0.9800000190734863

fun Int.clamp(min: Int, max: Int) = MathHelper.clamp_int(this, min, max)
fun Float.clamp(min: Float, max: Float) = MathHelper.clamp_float(this, min, max)
fun Double.clamp(min: Double, max: Double) = MathHelper.clamp_double(this, min, max)
fun Double.mfloor() = MathHelper.floor_double(this)
fun Float.mfloor() = MathHelper.floor_float(this)
fun Double.mceil() = MathHelper.ceiling_double_int(this)
fun Float.mceil() = MathHelper.ceiling_float_int(this)

fun Entity.setSize(wid: Double, hei: Double) {
	var f2: Float
	val w = wid.F
	val h = hei.F
	
	if (w != width || h != height) {
		f2 = width
		width = w
		height = h
		boundingBox.maxX = boundingBox.minX + width
		boundingBox.maxZ = boundingBox.minZ + width
		boundingBox.maxY = boundingBox.minY + height
		if (width > f2 && !worldObj.isRemote) moveEntity((f2 - width).D, 0.0, (f2 - width).D)
	}
	
	f2 = w % 2f
	
	myEntitySize = when {
		f2 < 0.375 -> Entity.EnumEntitySize.SIZE_1
		f2 < 0.75  -> Entity.EnumEntitySize.SIZE_2
		f2 < 1.0   -> Entity.EnumEntitySize.SIZE_3
		f2 < 1.375 -> Entity.EnumEntitySize.SIZE_4
		f2 < 1.75  -> Entity.EnumEntitySize.SIZE_5
		else       -> Entity.EnumEntitySize.SIZE_6
	}
}

fun DataWatcher.getWatchableObjectChunkCoordinates(id: Int): ChunkCoordinates {
	return getWatchedObject(id).`object` as ChunkCoordinates? ?: ChunkCoordinates()
}

fun getBoundingBox(x: Number, y: Number, z: Number) = getBoundingBox(x, y, z, x, y, z)

fun getBoundingBox(x1: Number, y1: Number, z1: Number, x2: Number, y2: Number, z2: Number) = AxisAlignedBB.getBoundingBox(x1.D, y1.D, z1.D, x2.D, y2.D, z2.D)!!

fun TileEntity.boundingBox(range: Number = 0) = getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(range)

fun Entity.boundingBox(range: Number = 0) = getBoundingBox(posX, posY, posZ, posX + width, posY + height, posZ + width).offset(width / 2.0, 0.0, width / 2.0).expand(range)

fun AxisAlignedBB.expand(d: Number) = expand(d, d, d)

fun AxisAlignedBB.offset(d: Number) = offset(d, d, d)

fun AxisAlignedBB.expand(x: Number, y: Number, z: Number) = expand(x.D, y.D, z.D)!!

fun AxisAlignedBB.offset(x: Number, y: Number, z: Number) = offset(x.D, y.D, z.D)!!

fun Entity.playSoundAtEntity(sound: String, volume: Float, duration: Float) {
	worldObj.playSoundAtEntity(this, sound, volume, duration)
}

fun Entity.setPosition(e: Entity, oX: Double = 0.0, oY: Double = 0.0, oZ: Double = 0.0) = setPosition(e.posX + oX, e.posY + oY, e.posZ + oZ)

fun Entity.setPosition(c: ChunkCoordinates, oX: Double = 0.0, oY: Double = 0.0, oZ: Double = 0.0) = setPosition(c.posX + oX, c.posY + oY, c.posZ + oZ)

fun Entity.setMotion(x: Double, y: Double = x, z: Double = y) {
	motionX = x
	motionY = y
	motionZ = z
}

fun Entity.spawn(world: World = this.worldObj) = world.spawnEntityInWorld(this)

operator fun ChunkCoordinates.component1() = posX
operator fun ChunkCoordinates.component2() = posY
operator fun ChunkCoordinates.component3() = posZ

operator fun Vec3.component1() = xCoord
operator fun Vec3.component2() = yCoord
operator fun Vec3.component3() = zCoord

fun Vec3(x: Number, y: Number, z: Number): Vec3 = Vec3.createVectorHelper(x.D, y.D, z.D)

fun EntityLivingBase.getActivePotionEffect(id: Int) = activePotionsMap[id] as PotionEffect?

fun Entity.knockback(attacker: Entity, dmg: Float, force: Float) {
	val (mx, _, mz) = Vector3.fromEntity(attacker).sub(this).mul(1, 0, 1).normalize().mul(force)
	isAirBorne = true
	motionX -= mx
	motionY += 0.4
	motionZ -= mz
}

fun EntityPlayerMP.hasAchievement(a: Achievement?) = if (a == null) false else func_147099_x().hasAchievementUnlocked(a)

fun EntityPlayer.hasAchievement(a: Achievement?) = if (this is EntityPlayerMP) hasAchievement(a) else if (this is EntityClientPlayerMP) hasAchievement(a) else false

fun ItemStack.itemEquals(ingredient: Any): Boolean {
	return when (ingredient) {
		is String    -> {
			for (orestack in OreDictionary.getOres(ingredient)) {
				val cstack = orestack.copy()
				
				if (cstack.meta == 32767) cstack.meta = meta
				if (isItemEqual(cstack)) return true
			}
			
			false
		}
		
		is ItemStack -> ASJUtilities.isItemStackEqualCrafting(ingredient, this)
		else         -> false
	}
}

fun ItemStack.areItemStackTagsEqual(stack: ItemStack): Boolean {
	return when {
		stackTagCompound == null || stackTagCompound.hasNoTags()             -> stack.stackTagCompound == null || stack.stackTagCompound.hasNoTags()
		stack.stackTagCompound == null || stack.stackTagCompound.hasNoTags() -> stackTagCompound == null || stackTagCompound.hasNoTags()
		else                                                                 -> stackTagCompound == stack.stackTagCompound
	}
}

var ItemStack.meta
	get() = itemDamage
	set(meta) {
		itemDamage = meta
	}

operator fun IInventory.get(i: Int): ItemStack? = getStackInSlot(i)
operator fun IInventory.set(i: Int, stack: ItemStack?) = setInventorySlotContents(i, stack)

fun Block.toItem(): Item? = Item.getItemFromBlock(this)
fun Item.toBlock(): Block? = Block.getBlockFromItem(this)
val Block.id get() = Block.getIdFromBlock(this)
val Item.id get() = Item.getIdFromItem(this)
val ItemStack.block: Block? get() = item.toBlock()

fun PotionEffectU(id: Int, time: Int, lvl: Int = 0, ambient: Boolean = false) = PotionEffect(id, time, lvl, ambient).apply { curativeItems.clear() }

fun <T> T.eventForge(): T {
	MinecraftForge.EVENT_BUS.register(this)
	return this
}

fun <T> T.eventFML(): T {
	FMLCommonHandler.instance().bus().register(this)
	return this
}

fun World.isBlockDirectlyGettingPowered(x: Int, y: Int, z: Int) = getBlockPowerInput(x, y, z) > 0

fun World.getBlock(e: Entity, x: Int = 0, y: Int = 0, z: Int = 0): Block {
	val (i, j, k) = Vector3.fromEntity(e).mf()
	return getBlock(i + x, j + y, k + z)
}

fun World.getBlockMeta(e: Entity, x: Int = 0, y: Int = 0, z: Int = 0): Int {
	val (i, j, k) = Vector3.fromEntity(e).mf()
	return getBlockMetadata(i + x, j + y, k + z)
}

fun World.getTileEntity(e: Entity, x: Int = 0, y: Int = 0, z: Int = 0): TileEntity? {
	val (i, j, k) = Vector3.fromEntity(e).mf()
	return getTileEntity(i + x, j + y, k + z)
}

fun World.setBlock(e: Entity, block: Block, x: Int = 0, y: Int = 0, z: Int = 0, meta: Int = 0): Boolean {
	val (i, j, k) = Vector3.fromEntity(e).mf()
	return setBlock(i + x, j + y, k + z, block, meta, 3)
}

fun addStringToTooltip(tooltip: MutableList<Any?>, s: String, vararg format: String) {
	tooltip.add(StatCollector.translateToLocalFormatted(s, *format).replace("&".toRegex(), "\u00a7"))
}

fun EntityLivingBase.teleportRandomly(diameter: Double): Boolean {
	val d0 = posX + (rng.nextDouble() - 0.5) * diameter
	val d1 = posY + (rng.nextInt(diameter.I) - (diameter.I) / 2)
	val d2 = posZ + (rng.nextDouble() - 0.5) * diameter
	return teleportTo(d0, d1, d2)
}

fun EntityLivingBase.teleportTo(x: Double, y: Double, z: Double): Boolean {
	val prevX = posX
	val prevY = posY
	val prevZ = posZ
	posX = x
	posY = y
	posZ = z
	var flag = false
	val i = posX.mfloor()
	var j = posY.mfloor()
	val k = posZ.mfloor()
	if (worldObj.blockExists(i, j, k)) {
		var flag1 = false
		while (!flag1 && j > 0) {
			val block: Block = worldObj.getBlock(i, j - 1, k)
			if (block.material.blocksMovement()) {
				flag1 = true
			} else {
				--posY
				--j
			}
		}
		if (flag1) {
			setPosition(posX, posY, posZ)
			if (worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox)) {
				flag = true
			}
		}
	}
	return if (!flag) {
		setPosition(prevX, prevY, prevZ)
		false
	} else {
		val short1: Short = 128
		for (l in 0 until short1) {
			val d6 = l.toDouble() / (short1.toDouble() - 1.0)
			val f: Float = (rng.nextFloat() - 0.5f) * 0.2f
			val f1: Float = (rng.nextFloat() - 0.5f) * 0.2f
			val f2: Float = (rng.nextFloat() - 0.5f) * 0.2f
			val d7: Double = prevX + (posX - prevX) * d6 + (rng.nextDouble() - 0.5) * width * 2.0
			val d8: Double = prevY + (posY - prevY) * d6 + rng.nextDouble() * height
			val d9: Double = prevZ + (posZ - prevZ) * d6 + (rng.nextDouble() - 0.5) * width * 2.0
			worldObj.spawnParticle("portal", d7, d8, d9, f.toDouble(), f1.toDouble(), f2.toDouble())
		}
		worldObj.playSoundEffect(prevX, prevY, prevZ, "mob.endermen.portal", 1.0f, 1.0f)
		playSound("mob.endermen.portal", 1.0f, 1.0f)
		true
	}
}

// backwarn compatibility
fun Entity.setPosition(c: ChunkCoordinates) = setPosition(c, 0.0, 0.0, 0.0)