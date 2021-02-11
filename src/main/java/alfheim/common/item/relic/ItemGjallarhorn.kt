package alfheim.common.item.relic

import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.ragnarok.RagnarokHandler
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import net.minecraft.entity.player.*
import net.minecraft.item.*
import net.minecraft.potion.PotionEffect
import net.minecraft.server.MinecraftServer
import net.minecraft.world.World
import vazkii.botania.common.item.relic.ItemRelic

class ItemGjallarhorn: ItemRelic("Gjallarhorn") {
	
	init {
		setFull3D()
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (!AlfheimCore.ENABLE_RAGNAROK) return stack
		if (!RagnarokHandler.ragnarok) return stack
		
		// TODO play woo-oo sound
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return stack
	}
	
	override fun getMaxItemUseDuration(stack: ItemStack?) = 100
	
	override fun getItemUseAction(stack: ItemStack?) = EnumAction.bow
	
	override fun onEaten(stack: ItemStack, world: World, player: EntityPlayer?): ItemStack {
		if (world.isRemote) return stack
		
		// TODO consume mana?
		MinecraftServer.getServer().configurationManager.playerEntityList.forEach { pl ->
			pl as EntityPlayerMP
			if (ItemPriestEmblem.getEmblem(-1, pl) != null) // TODO blessing effect
				pl.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDBerserk, 100))
		}
		
		return stack
	}
}