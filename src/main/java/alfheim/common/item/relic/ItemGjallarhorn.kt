package alfheim.common.item.relic

import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.ragnarok.RagnarokHandler
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.PotionEffect
import net.minecraft.server.MinecraftServer
import net.minecraft.world.World
import vazkii.botania.common.item.relic.ItemRelic

class ItemGjallarhorn: ItemRelic("Gjallarhorn") {
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (!RagnarokHandler.ragnarok) return stack
		
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return stack
	}
	
	override fun getMaxItemUseDuration(stack: ItemStack?) = 100
	
	override fun getItemUseAction(stack: ItemStack?) = EnumAction.bow
	
	override fun onEaten(stack: ItemStack, world: World?, player: EntityPlayer?): ItemStack {
		// TODO consume mana?
		MinecraftServer.getServer().worldServers.forEach { ws ->
			ws.playerEntities.forEach { pl ->
				pl as EntityPlayer
				if (ItemPriestEmblem.getEmblem(-1, pl) != null) // TODO blessing effect
					pl.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDBerserk, 100))
			}
		}
		return stack
	}
}