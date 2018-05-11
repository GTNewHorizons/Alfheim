package alfheim.common.entity.boss.ai;

import java.awt.Color;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.ModItems;

public class AIRays extends AIBase {

	private static final String TAG_ATTACKER_USERNAME = "attackerUsername";
	private static final int MANA_PER_DAMAGE = 1;
	
	public AIRays(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}

	@Override
	public void startExecuting() {
		flugel.setAITaskTimer(20);
		EntityPlayer player = ASJUtilities.getClosestVulnerablePlayerToEntity(flugel, flugel.RANGE * 2);
		if (player != null) flugel.setPosition(flugel.posX, player.posY, flugel.posZ);
		for (int i = 0; i < 360; i += 15) {
			flugel.worldObj.spawnEntityInWorld(getBurst(flugel, i));
		}
		flugel.worldObj.playSoundAtEntity(flugel, "botania:terraBlade", 0.4F, 1.4F);
	}
	
	public EntityManaBurst getBurst(EntityFlugel flugel, int i) {
		EntityManaBurst burst = new EntityManaBurst(flugel.worldObj);

		float motionModifier = 15F;

		burst.setColor(new Color(179, 77, 179).getRGB());
		burst.setMana(1);
		burst.setStartingMana(1);
		burst.setMinManaLoss(600);
		burst.setManaLossPerTick(4F);
		burst.setGravity(0F);
		burst.setMotion(burst.motionX * motionModifier, burst.motionY * motionModifier, burst.motionZ * motionModifier);

		ItemStack lens = new ItemStack(ModItems.terraSword, 1, 0);
		ItemNBTHelper.setString(lens, TAG_ATTACKER_USERNAME, flugel.getCommandSenderName());
		burst.setSourceLens(lens);
		
		burst.setBurstSourceCoords(0, -1, 0);
		burst.setLocationAndAngles(flugel.posX, flugel.posY + flugel.getEyeHeight(), flugel.posZ, i, -flugel.rotationPitch);

		burst.posX -= MathHelper.cos((i) / 180.0F * (float) Math.PI) / 2.0;
		burst.posY -= 0.1;
		burst.posZ -= MathHelper.sin((i) / 180.0F * (float) Math.PI) / 2.0;

		burst.setPosition(burst.posX, burst.posY, burst.posZ);
		burst.yOffset = 0.0F;
		float f = 0.4F;
		double mx = MathHelper.sin(burst.rotationYaw / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(MathHelper.cos(burst.rotationYaw / 180.0F * (float) Math.PI) * f) / 2D;
		double my = 0;
		burst.setMotion(mx, my, mz);
		
		return burst;
	}

	@Override
	public boolean continueExecuting() {
		return canContinue();
	}
}