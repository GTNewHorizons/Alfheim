package alfheim.common.core.util

import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.stats.Achievement
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraftforge.oredict.OreDictionary

fun Double.mfloor() = MathHelper.floor_double(this)

val Number.D get() = this.toDouble()

fun Entity.boundingBox(range: Number = 1) = getBoundingBox(posX, posY, posZ).expand(range)

fun TileEntity.boundingBox(range: Number = 1) = getBoundingBox(xCoord.D, yCoord.D, zCoord).expand(range)

fun getBoundingBox(x: Number, y: Number, z: Number) = AxisAlignedBB.getBoundingBox(x.D, y.D, z.D, x.D, y.D, z.D)!!

fun AxisAlignedBB.expand(d: Number) = this.expand(d.D, d.D, d.D)!!

fun Entity.playSoundAtEntity(sound: String, volume: Float, duration: Float) {
    worldObj.playSoundEffect(posX, posY, posZ, sound, volume, duration)
}

fun EntityLivingBase.getActivePotionEffect(id: Int) = activePotionsMap[id] as PotionEffect?

fun EntityPlayerMP.hasAchievement(a: Achievement?) = if(a == null) false else this.func_147099_x().hasAchievementUnlocked(a)

fun ItemStack.itemEquals(rItem: Any): Boolean {
    if (rItem is String) {
        
        for (stack in OreDictionary.getOres(rItem)) {
            val cstack = stack.copy()
            
            if (cstack.itemDamage == 32767) cstack.itemDamage = itemDamage
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

internal fun simpleAreStacksEqual(stack: ItemStack, stack2: ItemStack) = stack.item === stack2.item && stack.itemDamage == stack2.itemDamage