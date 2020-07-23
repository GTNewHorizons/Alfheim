package alfheim.common.item.rod

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.item.ColorOverrideHelper
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.core.helper.InterpolatedIconHelper
import alfheim.common.item.ItemMod
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.api.item.IManaProficiencyArmor
import vazkii.botania.api.mana.*
import java.awt.Color

/**
 * @author WireSegal
 * Created at 9:32 PM on 1/27/16.
 */
class ItemRodFlameStar(name: String = "rodFlameStar"): ItemMod(name), IManaUsingItem {
	
	init {
		maxStackSize = 1
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) = Unit
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 1) {
			itemIcon = InterpolatedIconHelper.forItem(event.map, this)
		}
	}
	
	override fun getItemUseAction(par1ItemStack: ItemStack?) = EnumAction.bow
	
	override fun getMaxItemUseDuration(par1ItemStack: ItemStack?) = 72000
	
	override fun isFull3D() = true
	
	override fun usesMana(stack: ItemStack) = true
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return stack
	}
	
	override fun onUsingTick(stack: ItemStack, player: EntityPlayer, count: Int) {
		val world = player.worldObj
		
		val priest = (ItemPriestEmblem.getEmblem(3, player) != null)
		val prowess = IManaProficiencyArmor.Helper.hasProficiency(player)
		
		val cost = getCost(prowess, priest)
		if (!ManaItemHandler.requestManaExactForTool(stack, player, cost, false)) return
		
		val power = getDamage(prowess, priest)
		
		val mop = ASJUtilities.getMouseOver(player, power.D, true)
		
		val hit = if (mop?.hitVec == null)
			Vector3(player.lookVec).normalize().mul(power.D).add(player.posX, player.posY + player.eyeHeight, player.posZ)
		else
			Vector3(mop.hitVec)
		
		val (x, y, z) = hit
		
		val color = Color(ColorOverrideHelper.getColor(player, 0xF94407))
		val r = color.red / 255f
		val g = color.green / 255f
		val b = color.blue / 255f
		
		VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.FLAMESTAR, world.provider.dimensionId, x, y, z, r.D, g.D, b.D)
		
		if (count % 20 == 0) {
			val entities = world.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB.getBoundingBox(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5))
			
			for (entity in entities) {
				if (entity is EntityLivingBase && entity != player && entity.health > 0) {
					if (entity.attackEntityFrom(EntityDamageSource("onFire", player), power.F)) {
						ManaItemHandler.requestManaExactForTool(stack, player, cost, true)
						entity.setFire(power * 20)
					}
				}
			}
		}
	}
	
	val COST = 10
	val PROWESS_COST = -2
	val PRIEST_COST = 3
	
	val DAMAGE = 2
	val PROWESS_DAMAGE = 1
	val PRIEST_DAMAGE = 7
	
	fun getCost(prowess: Boolean, priest: Boolean): Int {
		var d = COST
		if (prowess) d += PROWESS_COST
		if (priest) d += PRIEST_COST
		return d
	}
	
	fun getDamage(prowess: Boolean, priest: Boolean): Int {
		var d = DAMAGE
		if (prowess) d += PROWESS_DAMAGE
		if (priest) d += PRIEST_DAMAGE
		return d
	}
}
