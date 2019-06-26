package alfheim.common.integration.minetweaker;

import alfheim.common.integration.minetweaker.handler.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;

public class MinetweakerAlfheimConfig {

	public static void loadConfig() {
		MineTweakerAPI.registerClass(MTHandlerAnyavil.class);
		MineTweakerAPI.registerClass(MTHandlerManaInfuser.class);
		MineTweakerAPI.registerClass(MTHandlerSpells.class);
		MineTweakerAPI.registerClass(MTHandlerTradePortal.class);
	}
	
	public static ItemStack getStack(IItemStack istack) {
		return MineTweakerMC.getItemStack(istack);
	} 
	
	/// #### original code from modtweaker2.helpers.InputHelper ####
	
	public static Object getObject(IIngredient iStack) {
		if (iStack == null)
			return null;
		else {
			if (iStack instanceof IOreDictEntry) {
				return getString((IOreDictEntry) iStack);
			} else if (iStack instanceof IItemStack) {
				return getStack((IItemStack) iStack);
			} else
				return null;
		}
	}

	public static Object[] getObjects(IIngredient[] ingredient) {
		if (ingredient == null)
			return null;
		else {
			Object[] output = new Object[ingredient.length];
			for (int i = 0; i < ingredient.length; i++) {
				if (ingredient[i] != null) {
					output[i] = getObject(ingredient[i]);
				} else
					output[i] = "";
			}

			return output;
		}
	}
	
	public static String getString(IOreDictEntry entry) {
		return entry.getName();
	}
}