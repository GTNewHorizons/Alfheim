package alfheim.common.integration.minetweaker.handler;

import static alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig.getStack;

import alfheim.api.AlfheimAPI;
import alfheim.api.ModInfo;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + ModInfo.MODID + ".Anyavil") 
public class MTHandlerAnyavil {

	@ZenMethod
	public static void pinkify(IItemStack input, int pinkness) {
		MineTweakerAPI.apply(new Pinkifier(getStack(input), pinkness));
	}
	
	private static class Pinkifier implements IUndoableAction {

		private final ItemStack output;
		private final int pinkness;
		int old = 0;
		
		public Pinkifier(ItemStack stack, int pink) {
			output = stack;
			pinkness = pink;
		}

		@Override
		public void apply() {
			Integer i = AlfheimAPI.addPink(output, pinkness);
			if (i != null) old = i;
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			AlfheimAPI.pinkness.put(output, old);
		}

		@Override
		public String describe() {
			return String.format("Mapping new (%d) pinkness weight for %s", pinkness, output.getUnlocalizedName());
		}

		@Override
		public String describeUndo() {
			return String.format("Mapping previous (%d) pinkness weight for %s", old, output.getUnlocalizedName());
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}
}