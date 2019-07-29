package alfheim.common.item

import alfheim.common.entity.EntityThrowableItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemFireGrenade : ItemMod("fireGrenade") {

    override fun onItemRightClick(stack: ItemStack?, world: World?, player: EntityPlayer?): ItemStack? {
        if (stack != null && world != null && player != null) {
            if (!world.isRemote) {
                val potion = EntityThrowableItem(player)
                world.spawnEntityInWorld(potion)
                stack.stackSize--
            }
        }

        return stack
    }

    override fun getIcon(stack: ItemStack, pass: Int) = Items.fire_charge.getIconFromDamage(0)!!
}
