package alfheim.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class EntityElf extends EntityCreature implements IMerchant, INpc {

    public int attackTimer;
    
	public EntityElf(World world) {
		super(world);
		this.setSize(0.6F, 1.8F);
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, IMob.mobSelector));
	}

	public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25 + this.getRNG().nextInt(15));
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    }
	
	public boolean isAIEnabled() {
        return true;
    }
	
	public void collideWithEntity(Entity entity) {
		if (entity instanceof IMob && this.getRNG().nextInt(20) == 0) {
			this.setAttackTarget((EntityLivingBase) entity);
		}

		super.collideWithEntity(entity);
	}
	
	public boolean attackEntityAsMob(Entity entity) {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(3 + this.rand.nextInt(5)));
        return flag;
    }
	
	@Override
	public void setCustomer(EntityPlayer p_70932_1_) {
		
	}

	@Override
	public EntityPlayer getCustomer() {
		return null;
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
		return null;
	}

	@Override
	public void setRecipes(MerchantRecipeList p_70930_1_) {
		
	}

	@Override
	public void useRecipe(MerchantRecipe p_70933_1_) {
		
	}

	@Override
	public void func_110297_a_(ItemStack p_110297_1_) {
		
	}
}