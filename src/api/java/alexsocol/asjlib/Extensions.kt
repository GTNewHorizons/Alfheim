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

fun Int.bidiRange(range: Int) = (this - range)..(this + range)

fun safeIndex(id: Int, size: Int) = max(0, min(id, size - 1))
fun <T> List<T>.safeGet(id: Int): T = this[safeIndex(id, size)]
fun <T> Array<T>.safeGet(id: Int): T = this[safeIndex(id, size)]
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

val Number.D get() = this.toDouble()
val Number.F get() = this.toFloat()
val Number.I get() = this.toInt()

operator fun <T> T.plus(s: String) = "$this$s"

// ################ MINECRAFT ####################

/** Free fall acceleration */
const val g = 0.08 * 0.9800000190734863

fun Int.clamp(min: Int, max: Int) = MathHelper.clamp_int(this, min, max)
fun Float.clamp(min: Float, max: Float) = MathHelper.clamp_float(this, min, max)
fun Double.clamp(min: Double, max: Double) = MathHelper.clamp_double(this, min, max)
fun Double.mfloor() = MathHelper.floor_double(this)

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
		if (width > f2 && !worldObj.isRemote)
			moveEntity((f2 - width).D, 0.0, (f2 - width).D)
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

fun getBoundingBox(x: Number, y: Number, z: Number) = AxisAlignedBB.getBoundingBox(x.D, y.D, z.D, x.D, y.D, z.D)!!

fun TileEntity.boundingBox(range: Number = 0) = AxisAlignedBB.getBoundingBox(xCoord.D, yCoord.D, zCoord.D, xCoord.D + 1, yCoord.D + 1, zCoord.D + 1).expand(range)

fun Entity.boundingBox(range: Number = 0) = AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + width, posY + height, posZ + width).expand(range)

fun AxisAlignedBB.expand(d: Number) = this.expand(d.D, d.D, d.D)!!

fun AxisAlignedBB.offset(d: Number) = this.offset(d.D, d.D, d.D)!!

fun Entity.playSoundAtEntity(sound: String, volume: Float, duration: Float) {
	worldObj.playSoundAtEntity(this, sound, volume, duration)
}

fun Entity.setPosition(e: Entity, oX: Double = 0.0, oY: Double = 0.0, oZ: Double = 0.0) = setPosition(e.posX + oX, e.posY + oY, e.posZ + oZ)

fun Entity.setPosition(c: ChunkCoordinates) = setPosition(c.posX.D, c.posY.D, c.posZ.D)

fun Entity.setMotion(x: Double, y: Double = x, z: Double = y) {
	motionX = x
	motionY = y
	motionZ = z
}

operator fun ChunkCoordinates.component1() = posX
operator fun ChunkCoordinates.component2() = posY
operator fun ChunkCoordinates.component3() = posZ

operator fun Vec3.component1() = xCoord
operator fun Vec3.component2() = yCoord
operator fun Vec3.component3() = zCoord

fun EntityLivingBase.getActivePotionEffect(id: Int) = activePotionsMap[id] as PotionEffect?

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
		stackTagCompound == null           -> stack.stackTagCompound == null || stack.stackTagCompound.hasNoTags()
		stackTagCompound.hasNoTags()       -> stack.stackTagCompound == null || stack.stackTagCompound.hasNoTags()
		stack.stackTagCompound == null     -> stackTagCompound == null || stackTagCompound.hasNoTags()
		stack.stackTagCompound.hasNoTags() -> stackTagCompound == null || stackTagCompound.hasNoTags()
		else                               -> stackTagCompound == stack.stackTagCompound
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