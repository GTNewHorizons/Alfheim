package alfheim.common.entity.spell;

import java.util.List;
import java.util.UUID;

import alexsocol.asjlib.math.Vector3;
import alfheim.api.spell.ITimeStopSpecific;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.util.DamageSourceSpell;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;

public class EntitySpellFireball extends Entity implements ITimeStopSpecific {

    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public EntityLivingBase caster;
    
	public EntitySpellFireball(World world) {
		super(world);
		setSize(0, 0);
	}
	
	public EntitySpellFireball(World world, double x, double y, double z, double accX, double accY, double accZ) {
		this(world);
		setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
        double d = MathHelper.sqrt_double(accX * accX + accY * accY + accZ * accZ);
        accelerationX = accX / d * 0.1D;
        accelerationY = accY / d * 0.1D;
        accelerationZ = accZ / d * 0.1D;
	}

	public EntitySpellFireball(World world, EntityLivingBase shooter) {
		this(world, shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.getLookVec().xCoord, shooter.getLookVec().yCoord, shooter.getLookVec().zCoord);
		this.caster = shooter;
        setRotation(shooter.rotationYaw, shooter.rotationPitch);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		return false;
	}
	
    public void onImpact(MovingObjectPosition mop) {
        if (!worldObj.isRemote) {
            if (mop != null && mop.entityHit != null) mop.entityHit.attackEntityFrom(DamageSourceSpell.fireball(this, caster), 6.0F);
            for (Object o : worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX, posY, posZ).expand(2, 2, 2))) {
            	EntityLivingBase e = (EntityLivingBase) o;
            	if (!PartySystem.mobsSameParty(e, caster)) e.attackEntityFrom(DamageSourceSpell.fireball(this, caster), 6.0F);
            }
        	worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        	SpellEffectHandler.sendPacket(Spells.EXPL, this);
            setDead();
        }
    }
    
    @Override
    public void onUpdate() {
        if (!worldObj.isRemote && (caster != null && caster.isDead || !worldObj.blockExists((int)posX, (int)posY, (int)posZ))) {
            setDead();
        } else {
            super.onUpdate();

            if (ticksExisted == 600) onImpact(null);

            Vec3 vec3 = Vec3.createVectorHelper(posX, posY, posZ);
            Vec3 vec31 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3, vec31);

            if (movingobjectposition == null) {
            	List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox.addCoord(motionX, motionY, motionZ).expand(0.1, 0.1, 0.1));
		        l.remove(caster);
		            
		        for (EntityLivingBase e : l) if (e.canBeCollidedWith() && !PartySystem.mobsSameParty(caster, e))  {
		          	movingobjectposition = new MovingObjectPosition(e);
		          	break;
		        }
            }
	        
	        if (movingobjectposition != null) onImpact(movingobjectposition);
            
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            rotationYaw = (float)(Math.atan2(motionZ, motionX) * 180.0D / Math.PI) + 90.0F;

            for (rotationPitch = (float)(Math.atan2((double)f1, motionY) * 180.0D / Math.PI) - 90.0F; rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F);
            while (rotationPitch - prevRotationPitch >= 180.0F) prevRotationPitch += 360.0F;
            while (rotationYaw - prevRotationYaw < -180.0F) prevRotationYaw -= 360.0F;
            while (rotationYaw - prevRotationYaw >= 180.0F) prevRotationYaw += 360.0F;

            rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
            rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
            float f2 = 0.95F;

            if (isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    float f3 = 0.25F;
                    worldObj.spawnParticle("bubble", posX - motionX * (double)f3, posY - motionY * (double)f3, posZ - motionZ * (double)f3, motionX, motionY, motionZ);
                }

                f2 = 0.8F;
            }

            motionX += accelerationX;
            motionY += accelerationY;
            motionZ += accelerationZ;
            motionX *= (double)f2;
            motionY *= (double)f2;
            motionZ *= (double)f2;
            setPosition(posX, posY, posZ);
            
            for(int i = 0; i < 5; i++) {
            	Vector3 v = new Vector3(motionX, motionY, motionZ);//.normalize().multiply(0.05);
				Botania.proxy.wispFX(worldObj, posX, posY - 0.2, posZ, 1, (float) Math.random() * 0.25F, (float) Math.random() * 0.075F, 0.65F + (float) Math.random() * 0.45F, (float) v.x, (float) v.y, (float) v.z, 0.1F);
            	
				Botania.proxy.wispFX(worldObj, posX + Math.random() * 0.5 - 0.25, posY + Math.random() * 0.5 - 0.25, posZ + Math.random() * 0.5 - 0.25, 1, (float) Math.random() * 0.25F, (float) Math.random() * 0.075F, (float) Math.random() * 0.25F, 0, 0.5F);
				// smoke
				float gs = (float) Math.random() * 0.15F;
				Botania.proxy.wispFX(worldObj, posX, posY - 0.25, posZ, gs, gs, gs, 2, -0.15F);
			}
        }
	}
    
    public boolean canBeCollidedWith() {
        return true;
    }

    public float getCollisionBorderSize() {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

	public boolean isImmune() {
		return false;
	}

	public boolean affectedBy(UUID uuid) {
		return !caster.getUniqueID().equals(uuid);
	}
	
	public void entityInit() {}

	public void readEntityFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("castername")) caster = worldObj.getPlayerEntityByName(nbt.getString("castername")); else setDead();
		if (caster == null) setDead();
	}
	
	public void writeEntityToNBT(NBTTagCompound nbt) {
		if (caster instanceof EntityPlayer) nbt.setString("castername", caster.getCommandSenderName());
	}
}