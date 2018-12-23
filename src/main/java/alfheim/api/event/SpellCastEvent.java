package alfheim.api.event;

import alfheim.api.spell.SpellBase;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.EntityLivingBase;

public abstract class SpellCastEvent extends Event {

	public final SpellBase spell;
	public final EntityLivingBase caster;
	
	public SpellCastEvent(SpellBase spell, EntityLivingBase caster) {
		this.spell = spell;
		this.caster = caster;
	}

	@Cancelable
	public static class Pre extends SpellCastEvent {

		public Pre(SpellBase spell, EntityLivingBase caster) {
			super(spell, caster);
		}
	}
	
	public static class Post extends SpellCastEvent {

		public int cd;
		
		public Post(SpellBase spell, EntityLivingBase caster, int c) {
			super(spell, caster);
			cd = c;
		}
	}
}