package alfheim.common.entity.spell;

import java.util.List;
import java.util.UUID;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.spell.ITimeStopSpecific;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;

public class EntitySpellWindBlade extends Entity implements ITimeStopSpecific {

	private EntityLivingBase caster;

	public EntitySpellWindBlade(World world) {
		super(world);
		setSize(3, 0.1F);
	}

	public EntitySpellWindBlade(World world, EntityLivingBase caster) {
		this(world);
		this.caster = caster;
        setPositionAndRotation(caster.posX, caster.posY + caster.height * 0.75, caster.posZ, caster.rotationYaw, caster.rotationPitch);
	}

	@Override
	public void onEntityUpdate() {
		if (!AlfheimCore.enableMMO || (!worldObj.isRemote && (caster == null || caster.isDead || ticksExisted > 20))) {
			setDead();
			return;
		}
		
		if (!ASJUtilities.isServer()) {
			Botania.proxy.wispFX(worldObj, posX + Math.random() * 2 - 1, posY, posZ + Math.random() * 2 - 1, (float) Math.random() * 0.1F + 0.8F, (float) Math.random() * 0.1F + 0.9F, (float) Math.random() * 0.1F + 0.8F, (float) Math.random() * 0.3F + 0.2F, (float) motionX / -10F, (float) motionY / -10F, (float) motionZ / -10F, 0.5F);
			return;
		}
		
		if (isCollidedHorizontally) setDead();
		
		if (this.isDead) return;
		
		Vector3 m = new Vector3(ASJUtilities.getLookVec(this));
		moveEntity(m.x, 0, m.z);
		
		List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox);
		l.remove(caster);
		for (EntityLivingBase e : l) if (!PartySystem.mobsSameParty(caster, e)) e.attackEntityFrom(DamageSourceSpell.blades(this, caster), 6);
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