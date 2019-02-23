package alfheim.common.entity.spell;

import java.util.List;
import java.util.UUID;

import alexsocol.asjlib.math.OrientedBB;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.spell.ITimeStopSpecific;
import alfheim.api.spell.SpellBase;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.core.util.DamageSourceSpell;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;

public class EntitySpellFirewall extends Entity implements ITimeStopSpecific {

	public EntityLivingBase caster;
	public OrientedBB obb;
	
	public EntitySpellFirewall(World world) {
		super(world);
		this.setSize(0, 0);
	}
	
	public EntitySpellFirewall(World world, EntityLivingBase caster) {
		this(world);
		this.caster = caster;
		Vector3 v = new Vector3(caster.getLookVec());
		v.set(v.x, 0, v.z).normalize().mul(5);
		setLocationAndAngles(caster.posX + v.x, caster.posY, caster.posZ + v.z, caster.rotationYaw, caster.rotationPitch);
	}
	
	public EntitySpellFirewall(World world, double x, double y, double z) {
		this(world);
		setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		return false;
	}
	
	public void onUpdate() {
		if (!AlfheimCore.enableMMO || (!worldObj.isRemote && (caster != null && caster.isDead))) {
			setDead();
			return;
		} else {
			//if (!ASJUtilities.isServer()) return;
			super.onUpdate();
			
			if (ticksExisted >= 600) {
				this.setDead();
				return;
			}
			
			if (obb == null) {
				obb = new OrientedBB(AxisAlignedBB.getBoundingBox(-3, -1, -0.5, 3, 4, 0.5)).translate(posX, posY, posZ).rotateOY(rotationYaw);
			}
			
			List<EntityLivingBase> list = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, obb.toAABB());
			for(EntityLivingBase e : list) {
				if (e != caster && obb.intersectsWith(e.boundingBox)) {
					e.attackEntityFrom(DamageSourceSpell.firewall(this, caster), SpellBase.over(caster, 1));
					if (!PartySystem.mobsSameParty(caster, e) || AlfheimConfig.frienldyFire) e.setFire(3);
				}
			}
			
			Vector3 a = obb.a.copy().sub(obb.pos).rotate(rotationYaw * -2, Vector3.oY).add(obb.pos),
					b = obb.b.copy().sub(obb.pos).rotate(rotationYaw * -2, Vector3.oY).add(obb.pos),
					d = obb.d.copy().sub(obb.pos).rotate(rotationYaw * -2, Vector3.oY).add(obb.pos),
					v = d.copy().sub(d.copy().sub(a).mul(0.5));
			
			int sources = 20, power = 5;
			for (int i = 0; i < sources; i++) {
				v.sub(a.copy().sub(b.copy()).mul(1./sources));
				for (int j = 0; j < power; j++) Botania.proxy.wispFX(worldObj, v.x + Math.random() * 0.5, v.y + Math.random() * 2, v.z + Math.random() * 0.5, 1, (float) Math.random() * 0.25F, (float) Math.random() * 0.075F, 0.5F + (float) Math.random() * 0.5F, -0.15F, (float) Math.random() * 1.5F);
			}
		}
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