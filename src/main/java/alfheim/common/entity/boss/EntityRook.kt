package alfheim.common.entity.boss

import alexsocol.asjlib.math.Vector3
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.command.IEntitySelector
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.boss.IBotaniaBoss
import vazkii.botania.client.core.handler.BossBarHandler

import java.awt.*

class EntityRook(world: World): EntityCreature(world), IBotaniaBoss { // EntityFlugel, EntityIronGolem, EntityWither
	
	var attackTimer: Int
		get() = dataWatcher.getWatchableObjectInt(21)
		set(timer) = dataWatcher.updateObject(21, timer)
	
	init {
		setSize(3f, 5f)
		isImmuneToFire = true
		
		navigator.avoidsWater = true
		navigator.setCanSwim(false)
		
		tasks.addTask(1, EntityAIAttackOnCollide(this, 1.0, true))
		tasks.addTask(2, EntityAIMoveTowardsTarget(this, 0.9, 32f))
		tasks.addTask(4, EntityAIMoveTowardsRestriction(this, 1.0))
		tasks.addTask(6, EntityAIWander(this, 0.5))
		tasks.addTask(7, EntityAIWatchClosest(this, EntityPlayer::class.java, 6f))
		tasks.addTask(8, EntityAILookIdle(this))
		targetTasks.addTask(2, EntityAIHurtByTarget(this, false))
		targetTasks.addTask(3, EntityAINearestAttackableTarget(this, EntityLiving::class.java, 0, false, true, IEntitySelector { e -> e is EntityLivingBase }))
	}
	
	override fun onLivingUpdate() {
		super.onLivingUpdate()
		
		heal(0.1f)
		tickAttackTimer()
		
		if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.5E-7 && this.rand.nextInt(5) == 0) {
			val i = MathHelper.floor_double(this.posX)
			val j = MathHelper.floor_double(this.posY - 0.2 - this.yOffset.toDouble())
			val k = MathHelper.floor_double(this.posZ)
			val block = this.worldObj.getBlock(i, j, k)
			
			if (block.material !== Material.air) this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + this.worldObj.getBlockMetadata(i, j, k), this.posX + (this.rand.nextFloat().toDouble() - 0.5) * this.width.toDouble(), this.boundingBox.minY + 0.1, this.posZ + (this.rand.nextFloat().toDouble() - 0.5) * this.width.toDouble(), 4.0 * (this.rand.nextFloat().toDouble() - 0.5), 0.5, (this.rand.nextFloat().toDouble() - 0.5) * 4.0)
		}
	}
	
	/*	================================	AI and Data STUFF	================================	*/
	
	public override fun entityInit() {
		super.entityInit()
		dataWatcher.addObject(21, 0)    // Attack Timer
	}
	
	public override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = 0.2
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = MAX_HP
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).baseValue = 1.0
	}
	
	public override fun canDespawn(): Boolean {
		return false
	}
	
	public override fun isAIEnabled(): Boolean {
		return true
	}
	
	fun tickAttackTimer() {
		var attackTimer = attackTimer
		if (attackTimer > 0) dataWatcher.updateObject(21, --attackTimer)
	}
	
	public override fun decreaseAirSupply(air: Int): Int {
		return air
	}
	
	public override fun collideWithEntity(collided: Entity) {
		super.collideWithEntity(collided)
		
		// if (rand.nextInt(20) != 0) return;
		if (collided is EntityPlayer && collided.capabilities.disableDamage) return
		if (collided is EntityLivingBase && collided.isEntityInvulnerable()) return
		
		if (collided is EntityLivingBase) attackTarget = collided
	}
	
	override fun attackEntityAsMob(target: Entity): Boolean {
		if (target is EntityPlayer && target.capabilities.disableDamage) return false
		if (target is EntityLivingBase && target.isEntityInvulnerable()) return false
		if (attackTimer > 0) return false
		
		attackTimer = 20
		worldObj.setEntityState(this, 4.toByte())
		val flag = target.attackEntityFrom(DamageSource.causeMobDamage(this), (12 + this.rand.nextInt(6)).toFloat())
		
		if (flag) {
			val zis = Vector3.fromEntity(this)
			val zat = Vector3.fromEntity(target)
			zis.sub(zat).set(zis.x, 0.0, zis.z).normalize().mul(0.2)
			target.motionX = zis.x
			target.motionZ = zis.z
		}
		
		playSound("mob.irongolem.throw", 1.0f, 1.0f)
		return flag
	}
	
	override fun canAttackClass(clazz: Class<*>?): Boolean {
		return true
	}
	
	override fun getCollisionBox(entity: Entity): AxisAlignedBB {
		return entity.boundingBox
	}
	
	override fun getBoundingBox(): AxisAlignedBB {
		return boundingBox
	}
	
	override fun canBePushed(): Boolean {
		return false
	}
	
	/*	================================	HEALTHBAR STUFF	================================	*/
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarTexture(): ResourceLocation {
		return BossBarHandler.defaultBossBar
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarTextureRect(): Rectangle {
		if (barRect == null)
			barRect = Rectangle(0, 0, 185, 15)
		return barRect
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarHPTextureRect(): Rectangle {
		if (hpBarRect == null)
			hpBarRect = Rectangle(0, barRect!!.y + barRect!!.height, 181, 7)
		return hpBarRect
	}
	
	@SideOnly(Side.CLIENT)
	override fun bossBarRenderCallback(res: ScaledResolution, x: Int, y: Int) {
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
	
	companion object {
		
		private val MAX_HP = 1000.0
		
		fun spawn(world: World, x: Int, y: Int, z: Int) {
			if (!world.isRemote) {
				val rook = EntityRook(world)
				rook.setPositionAndRotation(x.toDouble(), y.toDouble(), z.toDouble(), 0f, 0f)
				world.spawnEntityInWorld(rook)
			}
		}
		
		@SideOnly(Side.CLIENT)
		private var barRect: Rectangle? = null
		@SideOnly(Side.CLIENT)
		private var hpBarRect: Rectangle? = null
	}
}