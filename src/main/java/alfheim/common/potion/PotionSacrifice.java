package alfheim.common.potion;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.client.render.world.SpellEffectHandlerClient;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.common.Botania;

public class PotionSacrifice extends PotionAlfheim {

	public int timeQueued;
	
	public PotionSacrifice() {
		super(AlfheimConfig.potionIDSacrifice, "sacrifice", false, 0);
	}
	
	@Override
	public boolean isReady(int time, int mod) {
		timeQueued = time;
		return AlfheimCore.enableMMO && timeQueued <= 32; 
	}
	
	@Override
	public void performEffect(EntityLivingBase target, int mod) {
		if (!AlfheimCore.enableMMO) return;
		if (timeQueued == 32) for (int i = 0; i < 8; i++) target.worldObj.playSoundEffect(target.posX, target.posY, target.posZ, ModInfo.MODID + ":redexp", 10000.0F, 0.8F + target.worldObj.rand.nextFloat() * 0.2F);
		else particles(target, 32 - timeQueued);
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
		super.removeAttributesModifiersFromEntity(target, attributes, ampl);
		if (!AlfheimCore.enableMMO) return;
		List<EntityLivingBase> l = target.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, target.boundingBox.copy().expand(32, 32, 32));
		DamageSource dmg;
		for (EntityLivingBase e : l) {
			if (e instanceof IBossDisplayData) continue;
			dmg = e == target ? DamageSourceSpell.sacrifice : DamageSourceSpell.darkness(target);
			e.attackEntityFrom(dmg, Integer.MAX_VALUE);
		}
	}
	
	public void particles(EntityLivingBase target, int time) {
		Vector3 v = new Vector3();
		for (int i = 0; i < 128 ; i++) {
			v.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(time);
			Botania.proxy.wispFX(target.worldObj, target.posX + v.x, target.posY + v.y, target.posZ + v.z, 1, (float) Math.random() * 0.5F, (float) Math.random() * 0.075F, (float) (Math.random() * time + 1), 0, (float) (Math.random() * 3. + 2));
		}
	}
}