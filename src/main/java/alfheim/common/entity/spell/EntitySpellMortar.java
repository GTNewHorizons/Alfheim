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
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntitySpellMortar extends Entity implements ITimeStopSpecific {

	public EntityLivingBase caster;
	
	public EntitySpellMortar(World world) {
		super(world);
		setSize(1, 1);
	}	
	public EntitySpellMortar(World world, EntityLivingBase shooter) {
		this(world);
		this.caster = shooter;
        setPositionAndRotation(caster.posX, caster.posY + caster.height * 0.75, caster.posZ, caster.rotationYaw, caster.rotationPitch);
        Vector3 m = new Vector3(caster.getLookVec()).mul(2);
        motionX = m.x;
        motionY = m.y;
        motionZ = m.z;
	}
	
    public void onImpact(MovingObjectPosition mop) {
        if (!worldObj.isRemote) {
           	if (mop != null && mop.entityHit != null && mop.entityHit instanceof EntityLivingBase && !PartySystem.mobsSameParty((EntityLivingBase) mop.entityHit, caster)) {
	           	mop.entityHit.attackEntityFrom(DamageSource.fallingBlock, 8);
	           	if (mop.entityHit instanceof EntityPlayer) ((EntityPlayer) mop.entityHit).inventory.damageArmor(20);
           	}
           	List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX, posY, posZ).expand(2, 2, 2));
            for (EntityLivingBase e : l) if (!PartySystem.mobsSameParty(e, caster)) e.attackEntityFrom(DamageSourceSpell.mortar(this, caster), 5);
            
            setDead();
        }
    }
    
    @Override
    public void onUpdate() {
        if (!worldObj.isRemote && (caster != null && caster.isDead || !worldObj.blockExists((int)posX, (int)posY, (int)posZ))) {
            setDead();
        } else {
            super.onUpdate();

            if (ticksExisted == 100) onImpact(null);

            Vec3 vec3 = Vec3.createVectorHelper(posX, posY, posZ);
            Vec3 vec31 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3, vec31);
            
            if (movingobjectposition == null) {
            	List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox.addCoord(motionX, motionY, motionZ));
		        l.remove(caster);
		            
		        for (EntityLivingBase e : l) if (e.canBeCollidedWith() && !PartySystem.mobsSameParty(caster, e))  {
		          	movingobjectposition = new MovingObjectPosition(e);
		          	break;
		        }
            }
	        
	        if (movingobjectposition != null) onImpact(movingobjectposition);

	        motionY -= 0.00981;
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
            
            setPosition(posX, posY, posZ);
        }
	}
    
    public boolean canBeCollidedWith() {
        return true;
    }

    public float getCollisionBorderSize() {
        return 5.0F;
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