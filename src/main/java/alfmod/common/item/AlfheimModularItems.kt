package alfmod.common.item

import alfmod.common.item.equipment.armor.ItemSnowArmor
import alfmod.common.item.equipment.tool.ItemSnowSword
import alfmod.common.item.interaction.thaumcraft.ItemSnowHelmetRevealing
import alfmod.common.item.material.ItemEventResource
import net.minecraft.item.Item
import vazkii.botania.common.Botania

object AlfheimModularItems {
	
	val eventResource: Item
	
	val snowSword: Item
	val snowHelmet: Item
	val snowHelmetRevealing: Item?
	val snowChest: Item
	val snowLeggings: Item
	val snowBoots: Item
	
	init {
		eventResource = ItemEventResource()
		
		snowSword = ItemSnowSword()
		snowHelmet = ItemSnowArmor(0, "SnowHelmet")
		snowHelmetRevealing = if (Botania.thaumcraftLoaded) ItemSnowHelmetRevealing() else null
		snowChest = ItemSnowArmor(1, "SnowChest")
		snowLeggings = ItemSnowArmor(2, "SnowLeggings")
		snowBoots = ItemSnowArmor(3, "SnowBoots")
	}
}