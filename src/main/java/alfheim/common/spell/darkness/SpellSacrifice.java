package alfheim.common.spell.darkness;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL12;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.client.render.world.SpellVisualizations;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.network.MessageEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class SpellSacrifice extends SpellBase {

	public SpellSacrifice() {
		super("sacrifice", EnumRace.IMP, 256000, 75000, 100, true);
	}

	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		SpellCastResult result = checkCast(caster);
		if (result == SpellCastResult.OK) {
			caster.addPotionEffect(new PotionEffect(AlfheimRegistry.sacrifice.id, 32, 0, false));
			AlfheimCore.network.sendToAll(new MessageEffect(caster.getEntityId(), AlfheimRegistry.sacrifice.id, 32, 0));
		}
		return result;
	}
	
	@Override
	public void render(EntityLivingBase caster) {
		SpellVisualizations.negateSphere(caster, 1);
	}
}