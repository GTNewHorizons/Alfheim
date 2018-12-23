package alfheim.common.spell.darkness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellPoisonRoots extends SpellBase {

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		Party pt = caster instanceof EntityPlayer ? PartySystem.getParty((EntityPlayer) caster) : PartySystem.getMobParty(caster);
		if (pt == null) return SpellCastResult.NOTARGET;
		boolean flagBadEffs = false, flagNotParty = false;
		EntityLivingBase member;	
		
		scanpt: for (int i = 0; i < pt.count; i++) {
			member = pt.get(i);
			if (member == null || Vector3.entityDistance(caster, member) > 32) continue;
			for (Object o : member.getActivePotionEffects()) {
				if (Potion.potionTypes[((PotionEffect) o).getPotionID()].isBadEffect) {
					flagBadEffs = true;
					break scanpt;
				}
			}
		}
		
		if (!flagBadEffs) return SpellCastResult.WRONGTGT;
		
		List<EntityLivingBase> l = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, caster.boundingBox.expand(16, 16, 16));
		for (EntityLivingBase e : l) {
			if (pt.isMember(e)) {
				flagNotParty = true;
				break;
			}
		}
		
		if (!flagNotParty) return SpellCastResult.NOTARGET;
		
		SpellCastResult result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		ArrayList<PotionEffect> remove = new ArrayList<PotionEffect>();
		Iterator<EntityLivingBase> mobs = l.iterator();
		EntityLivingBase target = mobs.next();
		PotionEffect pe;
		
		for (int i = 0; i < pt.count; i++) {
			member = pt.get(i);
			for (Object o : member.getActivePotionEffects()) {
				pe = (PotionEffect) o;
				
				while (pt.isMember(target) && mobs.hasNext()) target = mobs.next();
				if (pt.isMember(target)) return SpellCastResult.NOTARGET;			// Some desync, sorry for your mana :(
				
				if (Potion.potionTypes[pe.getPotionID()].isBadEffect) {
					target.addPotionEffect(new PotionEffect(pe.potionID, pe.duration, pe.amplifier, pe.isAmbient));
					remove.add(pe);
					if (mobs.hasNext()) target = mobs.next();
					else mobs = l.iterator();
				}
			}
			
			for (PotionEffect r : remove) member.removePotionEffect(r.potionID);
			remove.clear();
		}
		
		for (EntityLivingBase e : l) if (!pt.isMember(e)) e.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 300, 4, true));
		
		return result;
	}

	@Override
	public EnumRace getRace() {
		return EnumRace.IMP;
	}

	@Override
	public String getName() {
		return "poisonroots";
	}

	@Override
	public int getManaCost() {
		return 60000;
	}

	@Override
	public int getCooldown() {
		return 6000;
	}

	@Override
	public int castTime() {
		return 30;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public void render(EntityLivingBase caster) {}
}