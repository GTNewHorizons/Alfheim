package alfheim.common.crafting.recipe

import alfheim.api.lib.LibOreDict
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.api.mana.ILens
import vazkii.botania.common.item.lens.ItemLens
import java.util.*

/**
 * @author WireSegal
 * Created at 8:26 PM on 2/21/16.
 */
class RecipeRainbowLensDye : IRecipe {

    override fun matches(var1: InventoryCrafting, var2: World): Boolean {
        ores = OreDictionary.getOres(LibOreDict.DYES[16])

        var foundLens = false
        var foundDye = false

        for (i in 0 until var1.sizeInventory) {
            val stack = var1.getStackInSlot(i)
            if (stack != null) {
                if (stack.item is ILens && !foundLens) {
                    foundLens = true
                } else {
                    if (foundDye) {
                        return false
                    }

                    if (!isRainbow(stack))
                        return false

                    foundDye = true
                }
            }
        }

        return foundLens && foundDye
    }

    var ores = ArrayList<ItemStack?>()

    fun isRainbow(stack: ItemStack): Boolean {
        for (ore in ores)
            if (OreDictionary.itemMatches(ore, stack, false))
                return true
        return false
    }

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack? {
        var lens: ItemStack? = null

        for (lensCopy in 0 until var1.sizeInventory) {
            val stack = var1.getStackInSlot(lensCopy)
            if (stack != null) {
                if (stack.item is ILens && lens == null) {
                    lens = stack
                }
            }
        }
    
        return if (lens!!.item is ILens) {
            lens.item
            val var6 = lens.copy()
            ItemLens.setLensColor(var6, 16)
            var6
        } else null
    }

    override fun getRecipeSize() = 10

    override fun getRecipeOutput() = null
}
