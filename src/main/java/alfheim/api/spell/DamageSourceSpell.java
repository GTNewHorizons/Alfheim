package alfheim.api.spell;

import alfheim.common.entity.spell.EntitySpellDriftingMine;
import alfheim.common.entity.spell.EntitySpellFenrirStorm;
import alfheim.common.entity.spell.EntitySpellFireball;
import alfheim.common.entity.spell.EntitySpellFirewall;
import alfheim.common.entity.spell.EntitySpellMortar;
import alfheim.common.entity.spell.EntitySpellWindBlade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;

public class DamageSourceSpell extends DamageSource {

	public DamageSourceSpell(String type) {
		super(type);
	}

	public static final DamageSource bleeding	= new DamageSource		("bleeding")	.setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource gravity	= new DamageSourceSpell	("gravity")		.setDamageBypassesArmor().setDifficultyScaled();
	public static final DamageSource mark		= new DamageSourceSpell	("mark")		.setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage();
	public static final DamageSource poison		= new DamageSourceSpell	("poison")		.setDamageBypassesArmor();
	public static final DamageSource possession = new DamageSourceSpell	("possession")	.setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage();
	public static final DamageSource sacrifice	= new DamageSourceSpell	("sacrifice")	.setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage();
	public static final DamageSource soulburn	= new DamageSourceSpell	("soulburn")	.setDamageBypassesArmor().setMagicDamage();

	public static DamageSource blades(EntitySpellWindBlade wb, EntityLivingBase caster) {
		return new EntityDamageSourceIndirectSpell("windblade", caster, wb).setDamageBypassesArmor();
	}
	
	/** Sacrifice type of damage to attack other mobs */
	public static DamageSource darkness(EntityLivingBase attacker) {
		return new EntityDamageSourceSpell("darkness_FF", attacker).setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage();
	}
	
	public static DamageSource explosion(EntitySpellDriftingMine dm, EntityLivingBase caster) {
        return new EntityDamageSource("explosion.player", caster).setDifficultyScaled().setExplosion();
    }
	
    public static DamageSource fireball(EntitySpellFireball fb, EntityLivingBase caster) {
        return new EntityDamageSourceIndirectSpell("fireball", caster, fb).setFireDamage().setExplosion().setProjectile();
    }
	
    public static DamageSource firewall(EntitySpellFirewall fw, EntityLivingBase caster) {
        return new EntityDamageSourceIndirectSpell("firewall", caster, fw).setFireDamage();
    }
    
	/** Fenrir Storm type of damage */
	public static DamageSource lightning(EntitySpellFenrirStorm st, EntityLivingBase caster) {
		return new EntityDamageSourceIndirectSpell("lightning", caster, st).setDamageBypassesArmor().setFireDamage();
	}
	
	public static DamageSource mortar(EntitySpellMortar mt, EntityLivingBase caster) {
		return new EntityDamageSourceIndirectSpell("fallingBlock", caster, mt).setDifficultyScaled().setProjectile();
	}
	
	/** Some water blades (?) type of damage */
	public static DamageSource water(EntityLivingBase caster) {
		return (new EntityDamageSourceSpell("water", caster)).setDamageBypassesArmor();
	}
	
	public static class EntityDamageSourceSpell extends DamageSourceSpell {
	    protected Entity attacker;

	    public EntityDamageSourceSpell(String source, Entity attacker) {
	        super(source);
	        this.attacker = attacker;
	    }

	    public Entity getEntity() {
	        return this.attacker;
	    }

	    public IChatComponent func_151519_b(EntityLivingBase target) {
	        ItemStack itemstack = this.attacker instanceof EntityLivingBase ? ((EntityLivingBase)this.attacker).getHeldItem() : null;
	        String s = "death.attack." + this.damageType;
	        String s1 = s + ".item";
	        return itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1) ? new ChatComponentTranslation(s1, new Object[] {target.func_145748_c_(), this.attacker.func_145748_c_(), itemstack.func_151000_E()}): new ChatComponentTranslation(s, new Object[] {target.func_145748_c_(), this.attacker.func_145748_c_()});
	    }

	    public boolean isDifficultyScaled() {
	        return this.attacker != null && this.attacker instanceof EntityLivingBase && !(this.attacker instanceof EntityPlayer);
	    }
	}
	
	public static class EntityDamageSourceIndirectSpell extends EntityDamageSourceSpell {
	    private Entity indirectEntity;

	    public EntityDamageSourceIndirectSpell(String type, Entity attacker, Entity indirect) {
	        super(type, attacker);
	        this.indirectEntity = indirect;
	    }

	    public Entity getSourceOfDamage() {
	        return this.attacker;
	    }

	    public Entity getEntity() {
	        return this.indirectEntity;
	    }

	    public IChatComponent func_151519_b(EntityLivingBase target) {
	        IChatComponent ichatcomponent = this.indirectEntity == null ? this.attacker.func_145748_c_() : this.indirectEntity.func_145748_c_();
	        ItemStack itemstack = this.indirectEntity instanceof EntityLivingBase ? ((EntityLivingBase)this.indirectEntity).getHeldItem() : null;
	        String s = "death.attack." + this.damageType;
	        String s1 = s + ".item";
	        return itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1) ? new ChatComponentTranslation(s1, new Object[] {target.func_145748_c_(), ichatcomponent, itemstack.func_151000_E()}): new ChatComponentTranslation(s, new Object[] {target.func_145748_c_(), ichatcomponent});
	    }
	}
}