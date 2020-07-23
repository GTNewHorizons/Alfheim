package alfheim.common.item.relic

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.core.helper.IconHelper
import alfheim.common.entity.EntityMjolnir
import alfheim.common.entity.spell.EntitySpellFenrirStorm
import alfheim.common.item.AlfheimItems
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.command.IEntitySelector
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.*
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.world.World
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.relic.ItemRelic
import java.awt.Color
import java.util.*
import kotlin.math.*
import vazkii.botania.common.core.helper.Vector3 as Bector3

class ItemMjolnir: ItemRelic("Mjolnir") {
	
	init {
		setHasSubtypes(true)
		setFull3D()
	}
	
	// ################ Checker ################
	
	fun isWorthy(player: EntityLivingBase): Boolean {
		if (player !is EntityPlayer) return false
		val inv = PlayerHandler.getPlayerBaubles(player)
		return (inv.getStackInSlot(1)?.item === ModItems.thorRing || inv.getStackInSlot(2)?.item === ModItems.thorRing) && inv.getStackInSlot(0)?.let { it.item === AlfheimItems.priestEmblem && it.meta == 0 } == true
	}
	
	// ################ Left-click ################
	
	override fun onEntitySwing(entity: EntityLivingBase, stack: ItemStack?): Boolean {
		if (entity.isSneaking && isWorthy(entity))
			entity.worldObj.spawnEntityInWorld(EntitySpellFenrirStorm(entity.worldObj, entity).also { it.mjolnir = true })
		
		return super.onEntitySwing(entity, stack)
	}
	
	override fun hitEntity(stack: ItemStack?, entity: EntityLivingBase?, attacker: EntityLivingBase?): Boolean {
		if (entity != null && attacker != null && isWorthy(attacker)) {
			val range = 10
			var dmg = 8f
			
			val alreadyTargetedEntities = ArrayList<EntityLivingBase>()
			val selector = IEntitySelector { e -> e is EntityLivingBase && e is IMob && e !is EntityPlayer && !alreadyTargetedEntities.contains(e) }
			var lightningSource: EntityLivingBase = entity
			
			val lightningSeed = getLong(stack, TAG_LIGHTNING_SEED, 0)
			val rand = Random(lightningSeed)
			
			for (i in 0..5) {
				val entities = entity.worldObj.getEntitiesWithinAABBExcludingEntity(lightningSource, lightningSource.boundingBox(range), selector) as MutableList<EntityLivingBase>
				if (entities.isEmpty()) break
				
				val target = entities[rand.nextInt(entities.size)]
				
				if (attacker is EntityPlayer)
					target.attackEntityFrom(DamageSource.causePlayerDamage(attacker), dmg)
				else
					target.attackEntityFrom(DamageSource.causeMobDamage(attacker), dmg)
				
				var color = 0x0079C4
				if (attacker is EntityPlayer) color = ColorOverrideHelper.getColor(attacker, color)
				
				Botania.proxy.lightningFX(entity.worldObj, Bector3.fromEntityCenter(lightningSource), Bector3.fromEntityCenter(target), 1f, color, Color(color).brighter().brighter().rgb)
				alreadyTargetedEntities.add(target)
				lightningSource = target
				dmg--
			}
			
			if (!entity.worldObj.isRemote)
				setLong(stack, TAG_LIGHTNING_SEED, entity.worldObj.rand.nextLong())
		}
		
		return super.hitEntity(stack, entity, attacker)
	}
	
	override fun getAttributeModifiers(stack: ItemStack?) = super.getAttributeModifiers(stack).apply {
		put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(Item.field_111210_e, "Weapon modifier", 8.0, 0))
	}
	
	// ################ Right-click ################
	
	override fun getMaxItemUseDuration(stack: ItemStack) = 72000
	
	override fun getItemUseAction(stack: ItemStack) = EnumAction.bow
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (stack.cooldown > 0) return stack
		
		if (isWorthy(player) && getInt(stack, TAG_SHAKE_TIMER, 0) <= 0) {
			player.setItemInUse(stack, getMaxItemUseDuration(stack))
		}
		
		return stack
	}
	
	override fun onUsingTick(stack: ItemStack, player: EntityPlayer, coitemInUseCountunt: Int) {
		if (player.worldObj.isRemote) return
		
		if (getCharge(stack) < MAX_CHARGE) {
			val req = min(MAX_CHARGE - getCharge(stack), CHARGE_PER_TICK)
			addCharge(stack, ManaItemHandler.requestMana(stack, player, req, true))
		}
	}
	
	override fun onPlayerStoppedUsing(stack: ItemStack, world: World, player: EntityPlayer, itemInUseCount: Int) {
		if (getCharge(stack) >= MAX_CHARGE) {
			if (player.isSneaking) {
				setCharge(stack, 0)
				if (!world.isRemote)
					world.spawnEntityInWorld(EntityMjolnir(world, player, stack.copy()))
				
				stack.cooldown = 600
				return
			} else if (player is EntityPlayerMP) {
				val mop = ASJUtilities.getSelectedBlock(player, player.theItemInWorldManager.blockReachDistance, true)
				
				if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
					setInt(stack, TAG_SHAKE_TIMER, 16)
					setInt(stack, TAG_SHAKE_X, mop.blockX)
					setInt(stack, TAG_SHAKE_Y, mop.blockY)
					setInt(stack, TAG_SHAKE_Z, mop.blockZ)
					
					val start = Bector3(mop.blockX.D + 0.5, mop.blockY.D + 1.5, mop.blockZ.D + 0.5)
					val end = Bector3()
					val oY = Bector3(0.0, 1.0, 0.0)
					
					val color = ColorOverrideHelper.getColor(player, 0x0079C4)
					
					for (i in 0 until 360 step 5) {
						end.set(5.0, 1.0, 0.0).rotate(i.D, oY).add(start)
						Botania.proxy.lightningFX(world, start, end, 3f, color, Color(color).brighter().brighter().rgb)
					}
				}
			}
		}
		
		setCharge(stack, 0)
	}
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity, slot: Int, inHand: Boolean) {
		if (stack.cooldown > 0) stack.cooldown--
		
		if (entity !is EntityLivingBase) return
		
		val timer = getInt(stack, TAG_SHAKE_TIMER, 0)
		
		if (timer > 0 && timer % 3 == 0) {
			val x = getInt(stack, TAG_SHAKE_X, 0)
			val y = getInt(stack, TAG_SHAKE_Y, 0)
			val z = getInt(stack, TAG_SHAKE_Z, 0)
			
			val radius = 5 - timer / 3 + 1.0
			
			val center = Vector3(x + 0.5, y, z + 0.5)
			val list = world.getEntitiesWithinAABB(EntityLivingBase::class.java, getBoundingBox(x, y + 1, z).offset(0.5).expand(radius)) as MutableList<EntityLivingBase>
			for (e in list) {
				if (e === entity) continue
				
				if (Vector3.vecEntityDistance(center, e) in (radius-1)..(radius+1)) {
					if (e.onGround) {
						e.attackEntityFrom(if (entity is EntityPlayer) DamageSource.causePlayerDamage(entity) else DamageSource.causeMobDamage(entity), 10f)
						e.hurtResistantTime = 0
						e.hurtTime = 0
					}
					
					e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entity, entity), 5f)
				}
			}
			
			val iradius = (radius + 1).I
			for (i in 0 until iradius * 2 + 1) {
				for (j in 0 until iradius * 2 + 1) {
					val xp: Int = x + i - iradius
					val zp: Int = z + j - iradius
					
					if (floor(Vector3.pointDistancePlane(xp.D, zp.D, x.D, z.D)).I == iradius - 1) {
						val block = world.getBlock(xp, y, zp)
						val meta = world.getBlockMetadata(xp, y, zp)
						
						if (world.isRemote) world.spawnEntityInWorld(EntityFallingBlock(world, xp.D + 0.5, y.D, zp.D + 0.5, block, meta).also { it.motionY += 0.5; it.noClip = true })
					}
				}
			}
		}
		
		if (timer > 0) setInt(stack, TAG_SHAKE_TIMER, timer-1)
		
		super.onUpdate(stack, world, entity, slot, inHand)
	}
	
	// ################ Render ################
	
	@SideOnly(Side.CLIENT)
	override fun getColorFromItemStack(stack: ItemStack?, pass: Int): Int {
		return if (pass == 1 && getCharge(stack) >= MAX_CHARGE) Color.HSBtoRGB((200 + (sin(Botania.proxy.worldElapsedTicks / 10.0 % 20) * 20).F) / 360f, 0.5f, 1f) else -0x1
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		icons = Array(2) { IconHelper.forItem(reg, this, it) }
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(stack: ItemStack, pass: Int) = icons[pass]
	
	override fun requiresMultipleRenderPasses() = true
	
	override fun getRenderPasses(meta: Int) = 2
	
	private var ItemStack.cooldown
		get() = getInt(this, TAG_COOLDOWN, 0)
		set(value) = setInt(this, TAG_COOLDOWN, value)
	
	companion object {
		
		const val CHARGE_PER_TICK = 1000
		const val MAX_CHARGE = 10000
		
		const val TAG_COOLDOWN = "cooldown"
		
		const val TAG_CHARGE = "charge"
		const val TAG_LIGHTNING_SEED = "lightningSeed"
		
		const val TAG_SHAKE_TIMER = "shakeTimer"
		const val TAG_SHAKE_X = "shakeX"
		const val TAG_SHAKE_Y = "shakeY"
		const val TAG_SHAKE_Z = "shakeZ"
		
		lateinit var icons: Array<IIcon>
		
		fun addCharge(stack: ItemStack?, charge: Int) {
			setInt(stack!!, TAG_CHARGE, getCharge(stack) + charge)
		}
		
		fun setCharge(stack: ItemStack?, charge: Int) {
			setInt(stack!!, TAG_CHARGE, charge)
		}
		
		fun getCharge(stack: ItemStack?) = getInt(stack, TAG_CHARGE, 0)
	}
}