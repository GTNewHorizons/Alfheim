package alfheim.common.entity.spell;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.spell.*;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.SpellEffectHandler;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.*;

public class EntitySpellAcidMyst extends Entity implements ITimeStopSpecific {
	
	public EntityLivingBase caster;
	
	public EntitySpellAcidMyst(World world) {
		super(world);
		setSize(1, 1);
	}
	
	public EntitySpellAcidMyst(World world, EntityLivingBase caster) {
		this(world);
		this.caster = caster;
		setPosition(caster.posX, caster.posY, caster.posZ);
		SpellEffectHandler.sendPacket(Spells.ACID, this);
	}
	
	@Override
	public void onEntityUpdate() {
		if (!AlfheimCore.enableMMO || (caster == null || caster.isDead || ticksExisted > 50)) {
			setDead();
			return;
		}
		if (this.isDead || !ASJUtilities.isServer()) return;
		
		List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX, posY, posZ).expand(4.5, 4.5, 4.5));
		l.remove(caster);
		for (EntityLivingBase e : l) if (!PartySystem.mobsSameParty(caster, e) && Vector3.entityDistance(caster, e) < 9) e.attackEntityFrom(DamageSourceSpell.poison, SpellBase.over(caster, 1)); 
	}
	
	public int getTopBlock(World world, int x, int z) {
		int y = 255;
		while (y > 0 && world.isAirBlock(x, y, z)) --y;
		return y;
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