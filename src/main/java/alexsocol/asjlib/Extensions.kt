package alexsocol.asjlib

import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.block.Block
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.*
import net.minecraft.potion.PotionEffect
import net.minecraft.stats.Achievement
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.oredict.OreDictionary
import kotlin.math.*

fun safeIndex(id: Int, size: Int) = max(0, min(id, size - 1))
fun <T> List<T>.safeGet(id: Int): T = this[safeIndex(id, size)]
fun <T> Array<T>.safeGet(id: Int): T = this[safeIndex(id, size)]

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

fun String.substringEnding(lastNChars: Int): String = this.substring(0, length - lastNChars)

val Number.D get() = this.toDouble()
val Number.F get() = this.toFloat()
val Number.I get() = this.toInt()

// ################ MINECRAFT ####################

fun Int.clamp(min: Int, max: Int) = MathHelper.clamp_int(this, min, max)
fun Float.clamp(min: Float, max: Float) = MathHelper.clamp_float(this, min, max)
fun Double.clamp(min: Double, max: Double) = MathHelper.clamp_double(this, min, max)
fun Double.mfloor() = MathHelper.floor_double(this)

fun Entity.boundingBox(range: Number = 1) = getBoundingBox(posX, posY, posZ).expand(range)

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

fun TileEntity.boundingBox(range: Number = 1) = getBoundingBox(xCoord.D, yCoord.D, zCoord).expand(range)

fun getBoundingBox(x: Number, y: Number, z: Number) = AxisAlignedBB.getBoundingBox(x.D, y.D, z.D, x.D, y.D, z.D)!!

fun AxisAlignedBB.expand(d: Number) = this.expand(d.D, d.D, d.D)!!

fun AxisAlignedBB.offset(d: Number) = this.offset(d.D, d.D, d.D)!!

fun Entity.playSoundAtEntity(sound: String, volume: Float, duration: Float) {
	worldObj.playSoundEffect(posX, posY, posZ, sound, volume, duration)
}

fun EntityLivingBase.getActivePotionEffect(id: Int) = activePotionsMap[id] as PotionEffect?

fun EntityPlayerMP.hasAchievement(a: Achievement?) = if (a == null) false else func_147099_x().hasAchievementUnlocked(a)

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

fun Block.toItem(): Item? = Item.getItemFromBlock(this)
fun Item.toBlock(): Block? = Block.getBlockFromItem(this)
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