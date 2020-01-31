package alfheim.api.event

import alfheim.api.spell.SpellBase
import cpw.mods.fml.common.eventhandler.*
import net.minecraft.entity.EntityLivingBase

abstract class SpellCastEvent(val spell: SpellBase, val caster: EntityLivingBase): Event() {
	
	@Cancelable
	class Pre(spell: SpellBase, caster: EntityLivingBase): SpellCastEvent(spell, caster)
	
	class Post(spell: SpellBase, caster: EntityLivingBase, var cd: Int): SpellCastEvent(spell, caster)
}