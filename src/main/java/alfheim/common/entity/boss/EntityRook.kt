package alfheim.common.entity.boss

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.boss.IBotaniaBossWithName
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.command.IEntitySelector
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import vazkii.botania.client.core.handler.BossBarHandler
import java.awt.Rectangle

class EntityRook(world: World): EntityCreature(world), IBotaniaBossWithName { // EntityFlugel, EntityIronGolem, EntityWither
	
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
		
		if (motionX * motionX + motionZ * motionZ > 2.5E-7 && rand.nextInt(5) == 0) {
			val i = posX.mfloor()
			val j = (posY - 0.2 - yOffset).mfloor()
			val k = posZ.mfloor()
			val block = worldObj.getBlock(i, j, k)
			
			if (block.material !== Material.air) worldObj.spawnParticle("blockcrack_${block.id}_" + worldObj.getBlockMetadata(i, j, k), posX + (rand.nextFloat().D - 0.5) * width.D, boundingBox.minY + 0.1, posZ + (rand.nextFloat().D - 0.5) * width.D, 4.0 * (rand.nextFloat().D - 0.5), 0.5, (rand.nextFloat().D - 0.5) * 4.0)
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
	
	public override fun canDespawn() = false
	public override fun isAIEnabled() = true
	
	fun tickAttackTimer() {
		var attackTimer = attackTimer
		if (attackTimer > 0) dataWatcher.updateObject(21, --attackTimer)
	}
	
	public override fun decreaseAirSupply(air: Int) = air
	
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
		val flag = target.attackEntityFrom(DamageSource.causeMobDamage(this), (12 + rand.nextInt(6)).F)
		
		if (flag) {
			val zis = Vector3.fromEntity(this)
			val zat = Vector3.fromEntity(target)
			zis.sub(zat).set(zis.x, 0.0, zis.z).normalize().mul(0.2)
			target.motionX = zis.x
			target.motionZ = zis.z
		}
		
		playSound("mob.irongolem.throw", 1f, 1f)
		return flag
	}
	
	override fun canAttackClass(clazz: Class<*>?) = true
	override fun getCollisionBox(entity: Entity) = entity.boundingBox!!
	override fun getBoundingBox() = boundingBox!!
	override fun canBePushed() = false
	
	/*	================================	HEALTHBAR STUFF	================================	*/
	
	@SideOnly(Side.CLIENT)
	override fun getNameColor() = 0x8B6042
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarTexture() = BossBarHandler.defaultBossBar!!
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarTextureRect(): Rectangle {
		if (barRect == null)
			barRect = Rectangle(0, 66, 185, 15)
		return barRect!!
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarHPTextureRect(): Rectangle {
		if (hpBarRect == null)
			hpBarRect = Rectangle(0, barRect!!.y + barRect!!.height, 181, 7)
		return hpBarRect!!
	}
	
	@SideOnly(Side.CLIENT)
	override fun bossBarRenderCallback(res: ScaledResolution, x: Int, y: Int) {
		// NO-OP for now
		/*glPushMatrix();
		int px = x + 160;
		int py = y + 12;
		
		Minecraft mc = mc;
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
		
		private const val MAX_HP = 1000.0
		
		fun spawn(world: World, x: Int, y: Int, z: Int) {
			if (!world.isRemote) {
				val rook = EntityRook(world)
				rook.setPositionAndRotation(x.D, y.D, z.D, 0f, 0f)
				world.spawnEntityInWorld(rook)
			}
		}
		
		@SideOnly(Side.CLIENT)
		private var barRect: Rectangle? = null
		
		@SideOnly(Side.CLIENT)
		private var hpBarRect: Rectangle? = null
	}
}