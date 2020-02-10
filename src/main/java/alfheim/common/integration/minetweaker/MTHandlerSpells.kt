package alfheim.common.integration.minetweaker

import alfheim.api.*
import minetweaker.*
import stanhebben.zenscript.annotations.*

@ZenClass("mods." + ModInfo.MODID + ".Spells")
object MTHandlerSpells {
	
	@ZenMethod
	@JvmStatic
	fun setManaCost(name: String, mana: Int) {
		if (mana < 0) throw IllegalArgumentException("Manacost for $name must not be negative")
		MineTweakerAPI.apply(ManaCost(name, mana))
	}
	
	@ZenMethod
	@JvmStatic
	fun setCooldown(name: String, cd: Int) {
		if (cd < 0) throw IllegalArgumentException("Cooldown for $name must not be negative")
		MineTweakerAPI.apply(Cooldown(name, cd))
	}
	
	@ZenMethod
	@JvmStatic
	fun setCastTime(name: String, time: Int) {
		if (time < 0) throw IllegalArgumentException("Cast time for $name must not be negative")
		MineTweakerAPI.apply(CastTime(name, time))
	}
	
	@ZenMethod
	@JvmStatic
	fun setParams(name: String, damage: Float, duration: Int, efficiency: Double, radius: Double) {
		if (damage < 0 || duration < 0 || efficiency < 0 || radius < 0) throw IllegalArgumentException("Params for $name must not be negative")
		MineTweakerAPI.apply(Params(name, damage, duration, efficiency, radius))
	}
	
	private class ManaCost(name: String, private val newVal: Int): IUndoableAction {
		
		var spell = AlfheimAPI.getSpellInstance(name)!!
		var oldVal = 0
		
		override fun apply() {
			oldVal = spell.setManaCost(newVal)
		}
		
		override fun canUndo() = true
		
		override fun undo() {
			spell.setManaCost(oldVal)
		}
		
		override fun describe() = "Setting manacost to $newVal for ${spell.name}"
		
		override fun describeUndo() = "Resetting manacost for ${spell.name} to old value ($oldVal)"
		
		override fun getOverrideKey() = null
	}
	
	private class Cooldown(name: String, private val newVal: Int): IUndoableAction {
		
		val spell = AlfheimAPI.getSpellInstance(name)!!
		var oldVal = 0
		
		override fun apply() {
			oldVal = spell.setCooldown(newVal)
		}
		
		override fun canUndo() = true
		
		override fun undo() {
			spell.setCooldown(oldVal)
		}
		
		override fun describe() = "Setting cooldown to $newVal for ${spell.name}"
		
		override fun describeUndo() = "Resetting cooldown for ${spell.name} to old value (oldVal)"
		
		override fun getOverrideKey() = null
	}
	
	private class CastTime(name: String, private val newVal: Int): IUndoableAction {
		
		val spell = AlfheimAPI.getSpellInstance(name)!!
		var oldVal = 0
		
		override fun apply() {
			oldVal = spell.setCastTime(newVal)
		}
		
		override fun canUndo() = true
		
		override fun undo() {
			spell.setCastTime(oldVal)
		}
		
		override fun describe() = "Setting cast time to $newVal for ${spell.name}"
		
		override fun describeUndo() = "Resetting cast time for ${spell.name} to old value ($oldVal)"
		
		override fun getOverrideKey() = null
	}
	
	private class Params(name: String, private val damage: Float, private val duration: Int, private val efficiency: Double, private val radius: Double): IUndoableAction {
		
		val spell = AlfheimAPI.getSpellInstance(name)!!
		var oldVals = arrayOf(0f, 0, 0.0, 0.0)
		
		override fun apply() {
			oldVals = arrayOf(spell.damage, spell.duration, spell.efficiency, spell.radius)
			
			spell.damage = damage
			spell.duration = duration
			spell.efficiency = efficiency
			spell.radius = radius
		}
		
		override fun canUndo() = true
		
		override fun undo() {
			spell.damage = oldVals[0] as Float
			spell.duration = oldVals[1] as Int
			spell.efficiency = oldVals[2] as Double
			spell.radius = oldVals[3] as Double
		}
		
		override fun describe() = "Setting damage [$damage], duration [$duration], efficiency [$efficiency], radius [$radius] for ${spell.name}"
		
		override fun describeUndo() = "Resetting damage, duration, efficiency, radius for ${spell.name} to old values (${oldVals.contentToString()})"
		
		override fun getOverrideKey() = null
	}
}