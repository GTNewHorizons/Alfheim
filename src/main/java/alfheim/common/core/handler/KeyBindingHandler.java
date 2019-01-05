package alfheim.common.core.handler;

import static alfheim.api.spell.SpellBase.SpellCastResult.*;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.AlfheimAPI;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.api.event.SpellCastEvent;
import alfheim.api.spell.SpellBase;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.common.core.handler.CardinalSystem.SpellCastingSystem;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.entity.Flight;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.common.MinecraftForge;

public class KeyBindingHandler {
	
	public static void enableFlight(EntityPlayerMP player, boolean boost) {
		if (!AlfheimCore.enableElvenStory) return;
		if (player.capabilities.isCreativeMode || EnumRace.getRace(player) == EnumRace.HUMAN) return;
		player.capabilities.allowFlying = true;
		player.capabilities.isFlying = !player.capabilities.isFlying;
		player.sendPlayerAbilities();
		if (boost) Flight.sub(player, 300);
	}

	public static void atack(EntityPlayerMP player) {
		MovingObjectPosition mop = ASJUtilities.getMouseOver(player, player.theItemInWorldManager.getBlockReachDistance(), true);
		if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit != null) {
			player.attackTargetEntityWithCurrentItem(mop.entityHit);
		}
	}

	public static int cast(EntityPlayerMP caster, int raceID, int spellID) {
		if (!AlfheimCore.enableMMO) return -NOTALLOW.ordinal();
		if (caster.isPotionActive(AlfheimRegistry.leftFlame)) return -NOTALLOW.ordinal();
		SpellBase spell = AlfheimAPI.getSpellByIDs(raceID, spellID);
		if (spell == null) return -DESYNC.ordinal();
		if (SpellCastingSystem.getCoolDown(caster, spell) > 0) return -NOTREADY.ordinal();
		SpellCastResult result = spell.performCast(caster);
		if(result == OK) {
			// SpellBase.say(caster, spell);
			SpellCastEvent.Post e = new SpellCastEvent.Post(spell, caster, spell.getCooldown());
			MinecraftForge.EVENT_BUS.post(e);
			return SpellCastingSystem.setCoolDown(caster, spell, e.cd);
		}
		return -result.ordinal();
	}
}