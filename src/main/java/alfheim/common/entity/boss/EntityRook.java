package alfheim.common.entity.boss;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

import java.awt.Rectangle;

import alexsocol.asjlib.math.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.client.core.handler.BossBarHandler;

public class EntityRook extends EntityCreature implements IBotaniaBoss { // EntityFlugel, EntityIronGolem, EntityWither

	private static final double MAX_HP = 1000;

	public EntityRook(World world) {
		super(world);
		setSize(3F, 5F);
		isImmuneToFire = true;
		
		getNavigator().setAvoidsWater(true);
		getNavigator().setCanSwim(false);
		
		tasks.addTask(1, new EntityAIAttackOnCollide(this, 1, true));
        tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9, 32));
        tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1));
        tasks.addTask(6, new EntityAIWander(this, 0.5));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, new IEntitySelector() {@Override public boolean isEntityApplicable(Entity e) {return e instanceof EntityLivingBase;}}));
	}

	public static void spawn(World world, int x, int y, int z) {
		if (!world.isRemote) {
			EntityRook rook = new EntityRook(world);
			rook.setPositionAndRotation(x, y, z, 0, 0);
			world.spawnEntityInWorld(rook);
		}
	}
	
	public void onLivingUpdate() {
		super.onLivingUpdate();

		heal(0.1F);
		
		if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.5E-7D && this.rand.nextInt(5) == 0) {
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY - 0.2D - (double) this.yOffset);
			int k = MathHelper.floor_double(this.posZ);
			Block block = this.worldObj.getBlock(i, j, k);

			if (block.getMaterial() != Material.air) this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + this.worldObj.getBlockMetadata(i, j, k), this.posX + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, this.boundingBox.minY + 0.1D, this.posZ + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, 4.0D * ((double) this.rand.nextFloat() - 0.5D), 0.5D, ((double) this.rand.nextFloat() - 0.5D) * 4.0D);
		}
	}
	
	/*	================================	AI and Data STUFF	================================	*/
	
	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HP);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
	}
	
	@Override
	public boolean canDespawn() {
		return false;
	}
	
	@Override
	public boolean isAIEnabled() {
		return true;
	}
	
	@Override
	public void entityInit() {
		super.entityInit();
	}
	
	public int decreaseAirSupply(int air) {
        return air;
    }
	
	@Override
	public void collideWithEntity(Entity collided) {
		if (collided instanceof EntityLivingBase && this.getRNG().nextInt(20) == 0) 
			setAttackTarget((EntityLivingBase) collided);
		super.collideWithEntity(collided);
    }
	
	public boolean attackEntityAsMob(Entity entity) {
		this.worldObj.setEntityState(this, (byte) 4);
		boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) (12 + this.rand.nextInt(6)));

		if (flag) {
			Vector3 zis = Vector3.fromEntity(this);
			Vector3 zat = Vector3.fromEntity(entity);
			zis.sub(zat).set(zis.x, 0, zis.z).normalize().mul(0.2);
			entity.motionX = zis.x;
			entity.motionZ = zis.z;
		}

		playSound("mob.irongolem.throw", 1.0F, 1.0F);
		return flag;
	}
	
	public boolean canAttackClass(Class clazz) {
        return true;
    }

    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.boundingBox;
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public boolean canBePushed() {
    	return false;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getBossBarTexture() {
		return BossBarHandler.defaultBossBar;
	}
	
	@SideOnly(Side.CLIENT)
	private static Rectangle barRect;
	@SideOnly(Side.CLIENT)
	private static Rectangle hpBarRect;

	@Override
	@SideOnly(Side.CLIENT)
	public Rectangle getBossBarTextureRect() {
		if(barRect == null)
			barRect = new Rectangle(0, 0, 185, 15);
		return barRect;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Rectangle getBossBarHPTextureRect() {
		if(hpBarRect == null)
			hpBarRect = new Rectangle(0, barRect.y + barRect.height, 181, 7);
		return hpBarRect;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void bossBarRenderCallback(ScaledResolution res, int x, int y) {
		// NO-OP for now
		/*glPushMatrix();
		int px = x + 160;
		int py = y + 12;

		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = new ItemStack(Items.skull, 1, 3);
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		glEnable(GL_RESCALE_NORMAL);
		RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, stack, px, py);
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

		boolean unicode = mc.fontRenderer.getUnicodeFlag();
		mc.fontRenderer.setUnicodeFlag(true);
		mc.fontRenderer.drawStringWithShadow("" + getPlayerCount(), px + 15, py + 4, 0xFFFFFF);
		mc.fontRenderer.setUnicodeFlag(unicode);
		glPopMatrix();*/
	}
}