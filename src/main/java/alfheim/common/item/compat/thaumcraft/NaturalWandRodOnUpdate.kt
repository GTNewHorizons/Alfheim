package alfheim.common.item.compat.thaumcraft

import alfheim.api.ModInfo
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.wands.IWandRodOnUpdate
import thaumcraft.common.items.wands.ItemWandCasting
import vazkii.botania.api.mana.ManaItemHandler

class NaturalWandRodOnUpdate: IWandRodOnUpdate {
	
	override fun onUpdate(stack: ItemStack, player: EntityPlayer) {
		if (player.ticksExisted % 20 != 0) return
		
		val wand = stack.item as ItemWandCasting
		val forTool = wand.getCap(stack).tag.startsWith(ModInfo.MODID)
		
		for (primal in primals)
			if (wand.getVis(stack, primal) < wand.getMaxVis(stack) && (if (forTool) ManaItemHandler.requestManaExactForTool(stack, player, 100, true) else ManaItemHandler.requestManaExact(stack, player, 100, true)))
				wand.addVis(stack, primal, 1, true)
	}
	
	companion object {
		
		val primals = Aspect.getPrimalAspects()
	}
}