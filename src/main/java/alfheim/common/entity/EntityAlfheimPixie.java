package alfheim.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityFlyingCreature;

public class EntityAlfheimPixie extends EntityFlyingCreature {

	public EntityAlfheimPixie(World world) {
		super(world);
		setSize(1.0F, 1.0F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(20, 0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0);
	}

	@Override
	protected void updateEntityActionState() {
		renderYawOffset = rotationYaw = -((float)Math.atan2(motionX, motionZ)) * 180.0F / (float)Math.PI;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if(worldObj.isRemote)
			for(int i = 0; i < 4; i++)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 0.1F + (float) Math.random() * 0.25F, 12);
	}

	@Override
	public void setDead() {
		super.setDead();
		if(worldObj.isRemote)
			for(int i = 0; i < 12; i++)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 1F + (float) Math.random() * 0.25F, 5);
	}
}
