package alfheim.common.entity;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.core.registry.AlfheimItems;
import baubles.api.BaublesApi;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityFlyingCreature;
import vazkii.botania.common.item.ModItems;

public class EntityAlfheimPixie extends EntityFlyingCreature {

	/** Coordinates of where the pixie spawned. */
	private ChunkCoordinates spawnPosition;
	
	public EntityAlfheimPixie(World world) {
		super(world);
		setSize(0.25F, 0.25F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void collideWithEntity(Entity p_82167_1_) {}

	@Override
	public void collideWithNearbyEntities() {}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void fall(float damage) {}

	@Override
	protected void updateFallState(double distance, boolean isOnGround) {}

	@Override
	public boolean doesEntityNotTriggerPressurePlate() {
		return true;
	}
	
	protected Item getDropItem() {
        return null;
    }

	@Override 
	protected void dropFewItems(boolean hit, int looting) { 
		this.entityDropItem(new ItemStack(ModItems.manaResource, 1, 8), 0.0F); 
	}
	
	@Override
	protected void updateEntityActionState() {
		renderYawOffset = rotationYaw = -((float)Math.atan2(motionX, motionZ)) * 180.0F / (float)Math.PI;
	}

	@Override
	public void onEntityUpdate() {
		if(worldObj.isRemote)
			for(int i = 0; i < 4; i++)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 0.1F + (float) Math.random() * 0.25F, 12);
		
		EntityPlayer player = ASJUtilities.getClosestVulnerablePlayerToEntity(this, 4);
		if (player != null && BaublesApi.getBaubles(player) != null && BaublesApi.getBaubles(player).getStackInSlot(0) != null && BaublesApi.getBaubles(player).getStackInSlot(0).getItem() == AlfheimItems.pixieAttractor && ManaItemHandler.requestManaExact(BaublesApi.getBaubles(player).getStackInSlot(0), player, 1, true)) {
			Vec3 vec = player.getLook(1.0F);
			this.motionX = (player.posX + vec.xCoord - this.posX) / 8.0F;
			this.motionY = (player.posY + vec.yCoord + 1.5 - this.posY) / 8.0F;
			this.motionZ = (player.posZ + vec.zCoord - this.posZ) / 8.0F;
			super.onEntityUpdate();
			return;
		}
		
		this.motionY *= 0.6;
		if (this.worldObj.rand.nextInt(600) == 0) this.motionY -= 5;
		
		super.onEntityUpdate();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
	protected void updateAITasks() {
		//super.updateAITasks();
		if (this.spawnPosition != null && (!this.worldObj.isAirBlock(this.spawnPosition.posX, this.spawnPosition.posY, this.spawnPosition.posZ) || this.spawnPosition.posY < 1)) {
			this.spawnPosition = null;
		}

		if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.getDistanceSquared((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0F) {
			this.spawnPosition = new ChunkCoordinates((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
		}

		double d0 = (double)this.spawnPosition.posX + 0.5D - this.posX;
		double d1 = (double)this.spawnPosition.posY + 0.1D - this.posY;
		double d2 = (double)this.spawnPosition.posZ + 0.5D - this.posZ;
		this.motionX += (Math.signum(d0) * 0.5 - this.motionX) * 0.1;
		this.motionY += (Math.signum(d1) * 0.7 - this.motionY) * 0.1;
		this.motionZ += (Math.signum(d2) * 0.5 - this.motionZ) * 0.1;
		float f = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
		float f1 = MathHelper.wrapAngleTo180_float(f - this.rotationYaw);
		this.moveForward = 0.5F;
		this.rotationYaw += f1;
	}
	
	@Override
	public void setDead() {
        this.isDead = true;
        if(worldObj.isRemote)
			for(int i = 0; i < 12; i++)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 1F + (float) Math.random() * 0.25F, 5);
	}

	@Override
	public boolean getCanSpawnHere() {
		boolean timeFlag = (0 < this.worldObj.getTotalWorldTime() % 24000 && this.worldObj.getTotalWorldTime() % 24000 < 13333) || (22666 < this.worldObj.getTotalWorldTime() % 24000 && this.worldObj.getTotalWorldTime() % 24000 < 24000);
		
		return super.getCanSpawnHere();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return super.isInRangeToRenderDist(distance / 16.0);
    }
}
