package alfheim.common.core.handler

import alfheim.common.block.AlfheimBlocks
import alfheim.common.item.material.ElvenResourcesMetas
import net.minecraft.block.BlockDispenser
import net.minecraft.dispenser.*
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.common.block.ModBlocks

/**
 * @author WireSegal
 * Created at 9:28 PM on 2/15/16.
 */
class BifrostFlowerDispenserHandler: IBehaviorDispenseItem {

    private val defaultBehavior = BehaviorDefaultDispenseItem()

    override fun dispense(block: IBlockSource, stack: ItemStack): ItemStack? {
        if (stack.itemDamage != ElvenResourcesMetas.RainbowDust) return defaultBehavior.dispense(block, stack)

        val facing = ForgeDirection.getOrientation(BlockDispenser.func_149937_b(block.blockMetadata).ordinal)
        val x = block.xInt + facing.offsetX
        val y = block.yInt + facing.offsetY
        val z = block.zInt + facing.offsetZ

        if (block.world.getBlock(x, y, z) == ModBlocks.flower) {
            block.world.setBlock(x, y, z, AlfheimBlocks.rainbowGrass, 1, 3)
            block.world.playSoundEffect(x.toDouble(), y.toDouble(), z.toDouble(), "botania:enchanterEnchant", 1f, 1f)
            stack.stackSize--
            return stack
        }
        return stack
    }
}
