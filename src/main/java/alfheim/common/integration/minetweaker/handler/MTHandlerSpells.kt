package alfheim.common.integration.minetweaker.handler

import alfheim.api.AlfheimAPI
import alfheim.api.ModInfo
import alfheim.api.spell.SpellBase
import minetweaker.IUndoableAction
import minetweaker.MineTweakerAPI
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenClass("mods." + ModInfo.MODID + ".Spells")
object MTHandlerSpells {
	
	@ZenMethod
	fun setManaCost(name: String, mana: Int) {
		MineTweakerAPI.apply(ManaCost(name, mana))
	}
	
	@ZenMethod
	fun setCooldown(name: String, mana: Int) {
		MineTweakerAPI.apply(Cooldown(name, mana))
	}
	
	@ZenMethod
	fun setCastTime(name: String, mana: Int) {
		MineTweakerAPI.apply(CastTime(name, mana))
	}
	
	private class ManaCost(name: String, private val newVal: Int): IUndoableAction {
		
		private val spell: SpellBase?
		internal var oldVal = 0
		
		init {
			spell = AlfheimAPI.getSpellInstance(name)
		}
		
		override fun apply() {
			oldVal = spell!!.setManaCost(newVal)
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			spell!!.manaCost = oldVal
		}
		
		override fun describe(): String {
			return String.format("Setting manacost to %d for %s", newVal, spell!!.name)
		}
		
		override fun describeUndo(): String {
			return String.format("Resetting manacost for %s to old value (%d)", spell!!.name, oldVal)
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
	
	private class Cooldown(name: String, private val newVal: Int): IUndoableAction {
		
		private val spell: SpellBase?
		internal var oldVal = 0
		
		init {
			spell = AlfheimAPI.getSpellInstance(name)
		}
		
		override fun apply() {
			oldVal = spell!!.setCooldown(newVal)
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			spell!!.cooldown = oldVal
		}
		
		override fun describe(): String {
			return String.format("Setting cooldown to %d for %s", newVal, spell!!.name)
		}
		
		override fun describeUndo(): String {
			return String.format("Resetting cooldown for %s to old value (%d)", spell!!.name, oldVal)
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
	
	private class CastTime(name: String, private val newVal: Int): IUndoableAction {
		
		private val spell: SpellBase?
		internal var oldVal = 0
		
		init {
			spell = AlfheimAPI.getSpellInstance(name)
		}
		
		override fun apply() {
			oldVal = spell!!.setCastTime(newVal)
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			spell!!.castTime = oldVal
		}
		
		override fun describe(): String {
			return String.format("Setting cast time to %d for %s", newVal, spell!!.name)
		}
		
		override fun describeUndo(): String {
			return String.format("Resetting cast time for %s to old value (%d)", spell!!.name, oldVal)
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
}