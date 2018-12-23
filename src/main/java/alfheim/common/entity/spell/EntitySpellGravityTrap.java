package alfheim.common.entity.spell;

import java.util.List;
import java.util.UUID;

import alexsocol.asjlib.math.Vector3;
import alfheim.api.spell.DamageSourceSpell;
import alfheim.api.spell.ITimeStopSpecific;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntitySpellGravityTrap extends Entity implements ITimeStopSpecific {

    public EntityLivingBase caster;
	
	public EntitySpellGravityTrap(World world) {
		this(world, null, 0, 0, 0);
	}
	
	public EntitySpellGravityTrap(World world, EntityLivingBase caster, double x, double y, double z) {
		super(world);
		setPosition(x, y, z);
		setSize(8F, 0.01F);
		this.caster = caster;
	}

	@Override
	public void onEntityUpdate() {
		if (ticksExisted > 200) {
			setDead();
			return;
		}
		if (this.isDead || ticksExisted < 20 || worldObj.isRemote) return;

		List<Entity> l = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(posX, posY + 8, posZ, posX, posY + 8, posZ).expand(4, 8, 4));
		for (Entity e : l) {
			if (e == this || e == caster || (e instanceof EntityLivingBase && PartySystem.mobsSameParty(caster, (EntityLivingBase) e) && !AlfheimConfig.frienldyFire) || (e instanceof EntityPlayer && ((EntityPlayer) e).capabilities.isCreativeMode)) continue;
			Vector3 dist = Vector3.fromEntity(e).sub(Vector3.fromEntity(this));
			if (Vector3.entityDistancePlane(e, this) <= 4.0) {
				e.motionY -= 1;
				e.motionX -= dist.x / 5;
				e.motionZ -= dist.z / 5;
				e.attackEntityFrom(DamageSourceSpell.gravity, 0.5F);
			}
		}
		
		if (worldObj.rand.nextBoolean()) {
			Vector3 p = new Vector3(posX, posY, posZ).add(Math.random() * 8.0 - 4.0, Math.random() * 3.5, Math.random() * 8.0 - 4.0);
			Vector3 m = Vector3.fromEntity(this).sub(p).mul(0.05);
			SpellEffectHandler.sendPacket(Spells.GRAVITY, dimension, p.x, p.y, p.z, m.x, m.y, m.z);
		}
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
		ticksExisted = nbt.getInteger("ticksExisted");
	}
	
	public void writeEntityToNBT(NBTTagCompound nbt) {
		if (caster instanceof EntityPlayer) nbt.setString("castername", caster.getCommandSenderName());
		nbt.setInteger("ticksExisted", ticksExisted);
	}
}