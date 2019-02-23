package alfheim.common.entity.spell;

import java.util.List;
import java.util.UUID;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.OrientedBB;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.spell.ITimeStopSpecific;
import alfheim.api.spell.SpellBase;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntitySpellFenrirStorm extends Entity implements ITimeStopSpecific {
	
	public OrientedBB area;
	public EntityLivingBase caster;
	
	public EntitySpellFenrirStorm(World world) {
		super(world);
		setSize(0.1F, 0.1F);
		area = new OrientedBB(AxisAlignedBB.getBoundingBox(-0.5, -0.5, -8, 0.5, 0.5, 8));
		renderDistanceWeight = 4;
	}
	
	public EntitySpellFenrirStorm(World world, EntityLivingBase caster) {
		this(world);
		this.caster = caster;
		Vector3 l = new Vector3(caster.getLookVec()).mul(0.1);
		setPositionAndRotation(caster.posX + l.x, caster.posY + caster.getEyeHeight() + l.y, caster.posZ + l.z, caster.rotationYaw, caster.rotationPitch);
		
		area.translate(caster.posX, caster.posY + caster.getEyeHeight(), caster.posZ);
		area.rotateOX(caster.rotationPitch);
		area.rotateOY(-caster.rotationYaw);
		
		Vector3 v = new Vector3(caster.getLookVec()).mul(8.5);
		area.translate(v.x, v.y, v.z);
	}
	
	@Override
	public void onEntityUpdate() {
		if (!AlfheimCore.enableMMO || (!worldObj.isRemote && (caster == null || area == null || ticksExisted > 12))) {
			setDead();
			return;
		}
		if (this.isDead || !ASJUtilities.isServer()) return;
		
		if (ticksExisted == 4) {
			List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, area.toAABB());
			for (EntityLivingBase e : l) if (e != caster && area.intersectsWith(e.boundingBox)) e.attackEntityFrom(DamageSourceSpell.lightning(this, caster), SpellBase.over(caster, 10));
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
	}
	
	public void writeEntityToNBT(NBTTagCompound nbt) {
		if (caster instanceof EntityPlayer) nbt.setString("castername", caster.getCommandSenderName());
	}
}