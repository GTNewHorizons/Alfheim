package alfheim.common.core.util

import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.stats.Achievement
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraftforge.oredict.OreDictionary

val Number.D get() = this.toDouble()
val Number.F get() = this.toFloat()
val Number.I get() = this.toInt()

// ################ MINECRAFT ####################

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

fun Entity.playSoundAtEntity(sound: String, volume: Float, duration: Float) {
    worldObj.playSoundEffect(posX, posY, posZ, sound, volume, duration)
}

fun EntityLivingBase.getActivePotionEffect(id: Int) = activePotionsMap[id] as PotionEffect?

fun EntityPlayerMP.hasAchievement(a: Achievement?) = if(a == null) false else func_147099_x().hasAchievementUnlocked(a)

fun ItemStack.itemEquals(rItem: Any): Boolean {
    if (rItem is String) {
        
        for (stack in OreDictionary.getOres(rItem)) {
            val cstack = stack.copy()
            
            if (cstack.meta == 32767) cstack.meta = meta
            if (isItemEqual(cstack)) return true
        }
        
    } else return rItem is ItemStack && simpleAreStacksEqual(rItem, this)
    return false
}

var ItemStack.meta
    get() = itemDamage
    set(meta) {
        itemDamage = meta
    }

internal fun simpleAreStacksEqual(stack: ItemStack, stack2: ItemStack) = stack.item === stack2.item && stack.meta == stack2.meta