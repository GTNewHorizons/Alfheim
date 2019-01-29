package alfheim.common.integration.minetweaker.handler;

import alfheim.api.AlfheimAPI;
import alfheim.api.ModInfo;
import alfheim.api.spell.SpellBase;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + ModInfo.MODID + ".Spells")
public class MTHandlerSpells {

	@ZenMethod
	public static void setManaCost(String name, int mana) {
		MineTweakerAPI.apply(new ManaCost(name, mana));
	}

	@ZenMethod
	public static void setCooldown(String name, int mana) {
		MineTweakerAPI.apply(new Cooldown(name, mana));
	}

	@ZenMethod
	public static void setCastTime(String name, int mana) {
		MineTweakerAPI.apply(new CastTime(name, mana));
	}

	private static class ManaCost implements IUndoableAction {

		private final SpellBase spell;
		private final int newVal;
		int oldVal = 0;
		
		public ManaCost(String name, int mana) {
			spell = AlfheimAPI.getSpellInstance(name);
			newVal = mana;
		}

		@Override
		public void apply() {
			oldVal = spell.setManaCost(newVal);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			spell.setManaCost(oldVal);
		}

		@Override
		public String describe() {
			return String.format("Setting manacost to %d for %s", newVal, spell.name);
		}

		@Override
		public String describeUndo() {
			return String.format("Resetting manacost for %s to old value (%d)", spell.name, oldVal);
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}

	private static class Cooldown implements IUndoableAction {

		private final SpellBase spell;
		private final int newVal;
		int oldVal = 0;
		
		public Cooldown(String name, int mana) {
			spell = AlfheimAPI.getSpellInstance(name);
			newVal = mana;
		}

		@Override
		public void apply() {
			oldVal = spell.setCooldown(newVal);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			spell.setCooldown(oldVal);
		}

		@Override
		public String describe() {
			return String.format("Setting cooldown to %d for %s", newVal, spell.name);
		}

		@Override
		public String describeUndo() {
			return String.format("Resetting cooldown for %s to old value (%d)", spell.name, oldVal);
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}

	private static class CastTime implements IUndoableAction {

		private final SpellBase spell;
		private final int newVal;
		int oldVal = 0;
		
		public CastTime(String name, int mana) {
			spell = AlfheimAPI.getSpellInstance(name);
			newVal = mana;
		}

		@Override
		public void apply() {
			oldVal = spell.setCastTime(newVal);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			spell.setCastTime(oldVal);
		}

		@Override
		public String describe() {
			return String.format("Setting cast time to %d for %s", newVal, spell.name);
		}

		@Override
		public String describeUndo() {
			return String.format("Resetting cast time for %s to old value (%d)", spell.name, oldVal);
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}
}