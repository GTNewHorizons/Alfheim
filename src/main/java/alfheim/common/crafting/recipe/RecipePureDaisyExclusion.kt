package alfheim.common.crafting.recipe

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.api.recipe.RecipePureDaisy
import vazkii.botania.api.subtile.SubTileEntity

class RecipePureDaisyExclusion(input: Any, output: Block, outputMeta: Int) : RecipePureDaisy(input, output, outputMeta) {

    override fun matches(world: World, x: Int, y: Int, z: Int, pureDaisy: SubTileEntity, block: Block, meta: Int): Boolean {
        if (input is Block)
            return block == input

        val stack = ItemStack(block, 1, meta)
        val oredict = input as String
        return isOreDict(stack, oredict) && block != output
    }

}
