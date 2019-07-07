package alfheim.common.integration.minetweaker.handler

import alfheim.api.*
import minetweaker.*
import stanhebben.zenscript.annotations.*

@ZenClass("mods." + ModInfo.MODID + ".Spells")
object MTHandlerSpells {
	
	@ZenMethod
	@JvmStatic
	fun setManaCost(name: String, mana: Int) {
		MineTweakerAPI.apply(ManaCost(name, mana))
	}
	
	@ZenMethod
	@JvmStatic
	fun setCooldown(name: String, cd: Int) {
		MineTweakerAPI.apply(Cooldown(name, cd))
	}
	
	@ZenMethod
	@JvmStatic
	fun setCastTime(name: String, time: Int) {
		MineTweakerAPI.apply(CastTime(name, time))
	}
	
	private class ManaCost(name: String, private val newVal: Int): IUndoableAction {
		
		private var spell = AlfheimAPI.getSpellInstance(name)
		internal var oldVal = 0
		
		override fun apply() {
			oldVal = spell!!.setManaCost(newVal)
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			spell!!.setManaCost(oldVal)
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
		
		private val spell = AlfheimAPI.getSpellInstance(name)
		internal var oldVal = 0
		
		override fun apply() {
			oldVal = spell!!.setCooldown(newVal)
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			spell!!.setCooldown(oldVal)
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
		
		private val spell = AlfheimAPI.getSpellInstance(name)
		internal var oldVal = 0
		
		override fun apply() {
			oldVal = spell!!.setCastTime(newVal)
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			spell!!.setCastTime(oldVal)
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