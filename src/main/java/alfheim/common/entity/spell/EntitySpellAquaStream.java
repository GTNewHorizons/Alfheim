package alfheim.common.entity.spell;

import java.util.UUID;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.spell.ITimeStopSpecific;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class EntitySpellAquaStream extends Entity implements ITimeStopSpecific {
	
    public EntityLivingBase caster;
    
	public EntitySpellAquaStream(World world) {
		super(world);
		setSize(1, 1);
	}

	public EntitySpellAquaStream(World world, EntityLivingBase caster) {
		this(world);
		this.caster = caster;
		setPosition(caster.posX, caster.posY, caster.posZ);
	}
	
	@Override
	public void onEntityUpdate() {
		if (!AlfheimCore.enableMMO || (caster == null || caster.isDead || (caster.posX != posX || caster.posY != posY || caster.posZ != posZ) || ticksExisted > 50)) {
			setDead();
			return;
		}
		if (this.isDead || !ASJUtilities.isServer()) return;
		
		MovingObjectPosition mop = ASJUtilities.getMouseOver(caster, 16, true);
		if (mop == null) mop = ASJUtilities.getSelectedBlock(caster, 16, true);

		Vector3 hp, look = new Vector3(caster.getLookVec());
		if (mop != null && mop.hitVec != null) {
			hp = new Vector3(mop.hitVec);
			if (mop.typeOfHit == MovingObjectType.ENTITY) {
				mop.entityHit.attackEntityFrom(DamageSourceSpell.water(caster), 1);
			}
		} else {
			hp = look.copy().extend(15).add(Vector3.fromEntity(caster)).add(0, caster.getEyeHeight(), 0);
		}
		
		double d = 0.75;
		SpellEffectHandler.sendPacket(Spells.AQUASTREAM, dimension, look.x + caster.posX, look.y + caster.posY + caster.getEyeHeight(), look.z + caster.posZ, look.x / d, look.y / d, look.z / d);
		SpellEffectHandler.sendPacket(Spells.AQUASTREAM_HIT, dimension, hp.x, hp.y, hp.z);
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