package alfheim.common.entity.spell;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.spell.ITimeStopSpecific;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.handler.SpellEffectHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.*;
import net.minecraft.world.World;

import java.util.UUID;

public class EntitySpellHarp extends Entity implements ITimeStopSpecific {

	public EntityLivingBase caster;

	public EntitySpellHarp(World world) {
		super(world);
		setSize(0.1F, 0.1F);
	}

	public EntitySpellHarp(World world, EntityLivingBase caster, double x, double y, double z) {
		this(world);
		setPosition(x, y, z);
		this.caster = caster;
	}

    public void onUpdate() {
    	if (!AlfheimCore.enableMMO || !worldObj.isRemote && (caster == null || caster.isDead)) {
            setDead();
	    } else {
        	if (!ASJUtilities.isServer()) return;
        	super.onUpdate();
        	
            if (ticksExisted >= 600) setDead();
            
            Party pt = PartySystem.getMobParty(caster);
            if (pt == null || pt.count == 0) {
            	setDead();
            	return;
            }
            
            if (worldObj.rand.nextInt() % (20 / pt.count) == 0) {
            	EntityLivingBase mr = pt.get(worldObj.rand.nextInt(pt.count));
            	if (Vector3.entityDistance(this, mr) > 16) return;
            	mr.heal(0.5F);
	            mr = pt.get(worldObj.rand.nextInt(pt.count));
	            for (Object o : mr.getActivePotionEffects()) {
	            	if (Potion.potionTypes[((PotionEffect) o).potionID].isBadEffect) {
	            		mr.removePotionEffect(((PotionEffect) o).potionID);
	            		break;
	            	}
	            }
	            
	            SpellEffectHandler.sendPacket(Spells.NOTE, this);
            }
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