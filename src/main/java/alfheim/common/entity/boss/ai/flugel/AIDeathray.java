package alfheim.common.entity.boss.ai.flugel;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.ModInfo;
import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityFallingStar;

import java.util.*;

public class AIDeathray extends AIBase {

	public AIDeathray(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}
	
	@Override
	public boolean isInterruptible() {
		return false;
	}
	
	@Override
	public void startExecuting() {
		flugel.setAITaskTimer(EntityFlugel.DEATHRAY_TICKS);
	}

	@Override
	public boolean continueExecuting() {
		int deathray = flugel.getAITaskTimer();
		ChunkCoordinates source = flugel.getSource();
		float range = EntityFlugel.RANGE;
		if (ModInfo.DEV) if (!flugel.worldObj.isRemote) for (EntityPlayer player : flugel.getPlayersAround()) ASJUtilities.chatLog("Deathray in " + deathray, player);
		flugel.setPosition(source.posX + 0.5, source.posY + 3, source.posZ + 0.5);
		flugel.motionX = 0;
		flugel.motionY = 0;
		flugel.motionZ = 0;
		if (deathray > 10) flugel.spawnPatyklz(true);
		
		if (deathray == 1) {
			List<EntityFallingStar> stars = new ArrayList<EntityFallingStar>(16);
			int rang = (int) Math.ceil(range);
			for (int l = 0; l < 4; l++) {
				for (int i = 0; i < 16;) {
					int x = flugel.worldObj.rand.nextInt(rang * 2 + 1) - rang;
					int z = flugel.worldObj.rand.nextInt(rang * 2 + 1) - rang;
					if (vazkii.botania.common.core.helper.MathHelper.pointDistancePlane(x, z, 0, 0) <= range) {
						Vector3 posVec = new Vector3(source.posX + x, source.posY + l * 20 + 10, source.posZ + z);
						Vector3 motVec = new Vector3((Math.random() - 0.5) * 18, 24, (Math.random() - 0.5) * 18);
						posVec.add(motVec);
						motVec.normalize().negate().mul(1.5);
							
						EntityFallingStar star = new EntityFallingStar(flugel.worldObj, flugel);
						star.setPosition(posVec.x, posVec.y, posVec.z);
						star.motionX = motVec.x;
						star.motionY = motVec.y;
						star.motionZ = motVec.z;
						stars.add(star);
						i++;
					}
				}
				
				List<EntityPlayer> players = flugel.getPlayersAround();
				for (EntityPlayer player : players) {
					Vector3 posVec = new Vector3(player.posX, player.posY + l * 10 * 2 + 10, player.posZ);
					Vector3 motVec = new Vector3((Math.random() - 0.5) * 18, 24, (Math.random() - 0.5) * 18);
					posVec.add(motVec);
					motVec.normalize().negate().mul(1.5);
						
					EntityFallingStar star = new EntityFallingStar(flugel.worldObj, flugel);
					star.setPosition(posVec.x, posVec.y, posVec.z);
					star.motionX = motVec.x;
					star.motionY = motVec.y;
					star.motionZ = motVec.z;
					stars.add(star);
				}
			}
			
			for (EntityFallingStar star : stars) flugel.worldObj.spawnEntityInWorld(star);
			if (flugel.worldObj.isRemote) {
				for (int i = 0; i < 360; i++) {
					float r = 0.2F + (float) Math.random() * 0.3F;
					float g = (float) Math.random() * 0.3F;
					float b = 0.2F + (float) Math.random() * 0.3F;
					Botania.proxy.wispFX(flugel.worldObj, flugel.posX, flugel.posY + 1, flugel.posZ, r, g, b, 0.5F, (float) Math.cos(i) * 0.4F, 0, (float) Math.sin(i) * 0.4F);
					Botania.proxy.wispFX(flugel.worldObj, flugel.posX, flugel.posY + 1, flugel.posZ, r, g, b, 0.5F, (float) Math.cos(i) * 0.3F, 0, (float) Math.sin(i) * 0.3F);
					Botania.proxy.wispFX(flugel.worldObj, flugel.posX, flugel.posY + 1, flugel.posZ, r, g, b, 0.5F, (float) Math.cos(i) * 0.2F, 0, (float) Math.sin(i) * 0.2F);
					Botania.proxy.wispFX(flugel.worldObj, flugel.posX, flugel.posY + 1, flugel.posZ, r, g, b, 0.5F, (float) Math.cos(i) * 0.1F, 0, (float) Math.sin(i) * 0.1F);
				}
			}
		}
		
		return canContinue();
	}
	
	@Override
	public void resetTask() {
		flugel.setStage(EntityFlugel.STAGE_DEATHRAY);
		flugel.setAITaskTimer(0);
		flugel.setAITask(AITask.REGEN);
	}
}