package alfheim.common.integration.minetweaker.handler;

import static alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig.*;

import alfheim.api.AlfheimAPI;
import alfheim.api.ModInfo;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + ModInfo.MODID + ".TradePortal")
public class MTHandlerTradePortal {

	@ZenMethod
	public static void banRetrade(IItemStack output) {
		MineTweakerAPI.apply(new Ban(getStack(output)));
	}

	private static class Ban implements IUndoableAction {

		private final ItemStack output;
		
		public Ban(ItemStack stack) {
			output = stack;
		}

		@Override
		public void apply() {
			AlfheimAPI.banRetrade(output);
		}

		@Override
		public boolean canUndo() {
			return false;
		}

		@Override
		public void undo() {
			throw new IllegalArgumentException("Don't cheat!");
		}

		@Override
		public String describe() {
			return String.format("Removing %s from Alfheim trade portal", output.getUnlocalizedName());
		}

		@Override
		public String describeUndo() {
			throw new IllegalArgumentException("Don't cheat!");
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}
}
