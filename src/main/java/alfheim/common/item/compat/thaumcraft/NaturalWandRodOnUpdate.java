package alfheim.common.item.compat.thaumcraft;

import java.util.ArrayList;

import alfheim.api.ModInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.items.wands.ItemWandCasting;
import vazkii.botania.api.mana.ManaItemHandler;

public class NaturalWandRodOnUpdate implements IWandRodOnUpdate {
	
	public static final ArrayList<Aspect> primals = Aspect.getPrimalAspects();
	
	@Override
	public void onUpdate(ItemStack stack, EntityPlayer player) {
		if (player.ticksExisted % 20 != 0) return;
		
		ItemWandCasting wand = (ItemWandCasting) stack.getItem();
		boolean forTool = wand.getCap(stack).getTag().startsWith(ModInfo.MODID);
		
		for (Aspect primal : primals) 
			if (wand.getVis(stack, primal) < wand.getMaxVis(stack) && (forTool ? ManaItemHandler.requestManaExactForTool(stack, player, 100, true) : ManaItemHandler.requestManaExact(stack, player, 100, true)))
				wand.addVis(stack, primal, 1, true);
	}
}