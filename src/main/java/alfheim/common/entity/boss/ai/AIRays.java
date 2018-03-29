package alfheim.common.entity.boss.ai;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;

public class AIRays extends AIBase {

	private static final String TAG_ATTACKER_USERNAME = "attackerUsername";
	private static final int MANA_PER_DAMAGE = 100;
	
	public AIRays(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}

	@Override
	public void startExecuting() { // TODO TEST!
		flugel.setAITaskTimer(5);
		EntityPlayer player = ASJUtilities.getClosestVulnerablePlayerToEntity(flugel, flugel.RANGE * 2);
		if (player != null) flugel.setPosition(flugel.posX, player.posY, flugel.posZ);
		for (int i = 0; i < 360; i += 15) {
			flugel.worldObj.spawnEntityInWorld(getBurst(flugel, i));
			flugel.worldObj.playSoundAtEntity(flugel, "botania:terraBlade", 0.4F, 1.4F);
		}
	}
	
	public EntityManaBurst getBurst(EntityFlugel flugel, int i) {
		EntityManaBurst burst = new EntityManaBurst(flugel.worldObj);

		float motionModifier = 7F;

		burst.setColor(0xFFFF20);
		burst.setMana(MANA_PER_DAMAGE);
		burst.setStartingMana(MANA_PER_DAMAGE);
		burst.setMinManaLoss(40);
		burst.setManaLossPerTick(4F);
		burst.setGravity(0F);
		burst.setMotion(burst.motionX * motionModifier, burst.motionY * motionModifier, burst.motionZ * motionModifier);

		ItemStack lens = new ItemStack(ModItems.terraSword, 1, 0);
		ItemNBTHelper.setString(lens, TAG_ATTACKER_USERNAME, flugel.getCommandSenderName());
		burst.setSourceLens(lens);
		
		burst.setBurstSourceCoords(0, -1, 0);
		burst.setLocationAndAngles(flugel.posX, flugel.posY + flugel.getEyeHeight(), flugel.posZ, i, -flugel.rotationPitch);

		burst.posX -= MathHelper.cos((i) / 180.0F * (float) Math.PI) * 1.5F;
		burst.posY -= 0.10000000149011612D;
		burst.posZ -= MathHelper.sin((i) / 180.0F * (float) Math.PI) * 1.5F;

		burst.setPosition(burst.posX, burst.posY, burst.posZ);
		burst.yOffset = 0.0F;
		float f = 0.4F;
		double mx = MathHelper.sin(burst.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(burst.rotationPitch / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(MathHelper.cos(burst.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(burst.rotationPitch / 180.0F * (float) Math.PI) * f) / 2D;
		double my = MathHelper.sin((burst.rotationPitch) / 180.0F * (float) Math.PI) * f / 2D;
		burst.setMotion(mx, my, mz);
		
		return burst;
	}

	@Override
	public boolean continueExecuting() {
		return canContinue();
	}
}