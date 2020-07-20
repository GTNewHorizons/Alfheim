package alfmod.common.item

import alfmod.common.item.equipment.armor.*
import alfmod.common.item.equipment.tool.*
import alfmod.common.item.interaction.thaumcraft.*
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
	
	val volcanoMace: Item
	val volcanoHelmet: Item
	val volcanoHelmetRevealing: Item?
	val volcanoChest: Item
	val volcanoLeggings: Item
	val volcanoBoots: Item
	
	init {
		eventResource = ItemEventResource()
		
		snowSword = ItemSnowSword()
		snowHelmet = ItemSnowArmor(0, "SnowHelmet")
		snowHelmetRevealing = if (Botania.thaumcraftLoaded) ItemSnowHelmetRevealing() else null
		snowChest = ItemSnowArmor(1, "SnowChest")
		snowLeggings = ItemSnowArmor(2, "SnowLeggings")
		snowBoots = ItemSnowArmor(3, "SnowBoots")
		
		volcanoMace = ItemVolcanoMace()
		volcanoHelmet = ItemVolcanoArmor(0, "VolcanoHelmet")
		volcanoHelmetRevealing = if (Botania.thaumcraftLoaded) ItemVolcanoHelmetRevealing() else null
		volcanoChest = ItemVolcanoArmor(1, "VolcanoChest")
		volcanoLeggings = ItemVolcanoArmor(2, "VolcanoLeggings")
		volcanoBoots = ItemVolcanoArmor(3, "VolcanoBoots")
	}
}