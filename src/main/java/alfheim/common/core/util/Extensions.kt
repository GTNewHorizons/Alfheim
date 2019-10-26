package alfheim.common.core.util

import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.common.core.helper.Vector3

fun Double.mfloor() = MathHelper.floor_double(this)

fun Entity.boundingBox(range: Int = 1) =
    AxisAlignedBB.getBoundingBox(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range)!!

fun TileEntity.boundingBox(range: Int = 1): AxisAlignedBB {
    return AxisAlignedBB.getBoundingBox((xCoord - range).toDouble(),
                                        (yCoord - range).toDouble(), (zCoord - range).toDouble(),
                                        (xCoord + range).toDouble(), (yCoord + range).toDouble(), (zCoord + range).toDouble())
}

fun Entity.playSoundAtEntity(sound: String, volume: Float, duration: Float) {
    worldObj.playSoundEffect(posX, posY, posZ, sound, volume, duration)
}

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

internal fun simpleAreStacksEqual(stack: ItemStack, stack2: ItemStack) = stack.item === stack2.item && stack.itemDamage == stack2.itemDamage