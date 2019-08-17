package alfheim.common.item.rod

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.item.ColorOverrideHelper
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
import vazkii.botania.common.Botania
import java.awt.Color

/**
 * @author WireSegal
 * Created at 9:32 PM on 1/27/16.
 */
open class ItemFlameRod(name: String = "flameRod"): ItemMod(name), IManaUsingItem {
	
	init {
		setMaxStackSize(1)
		if (FMLLaunchHandler.side().isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(par1IconRegister: IIconRegister) = Unit
	
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
	
	override fun onItemRightClick(par1ItemStack: ItemStack, par2World: World?, par3EntityPlayer: EntityPlayer?): ItemStack {
		par3EntityPlayer!!.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack))
		return par1ItemStack
	}
	
	override fun onUsingTick(stack: ItemStack?, player: EntityPlayer?, count: Int) {
		try {
			if (player != null) {
				val world = player.worldObj
				
				val priest = (ItemPriestEmblem.getEmblem(3, player) != null)
				val prowess = IManaProficiencyArmor.Helper.hasProficiency(player)
				
				val power = getDamage(prowess, priest)
				
				val mop = ASJUtilities.getMouseOver(player, power.toDouble(), true)
				
				val hit = if (mop?.hitVec == null)
					Vector3(player.lookVec).normalize().mul(power.toDouble()).add(player.posX, player.posY + player.eyeHeight, player.posZ)
				else
					Vector3(mop.hitVec)
				
				val (x, y, z) = hit
				
				val color = Color(ColorOverrideHelper.getColor(player, 0xF94407))
				val r = color.red / 255f
				val g = color.green / 255f
				val b = color.blue / 255f
				
				Botania.proxy.sparkleFX(world, x, y, z, r, g, b, 1f, 5)
				
				if (count % 20 == 0) {
					val entities = world.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB.getBoundingBox(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5))
					val cost = getCost(prowess, priest)
					for (entity in entities) {
						if (entity is EntityLivingBase && entity != player && entity.health > 0) {
							if (ManaItemHandler.requestManaExactForTool(stack, player, cost, false)) {
								if (entity.attackEntityFrom(EntityDamageSource("onFire", player), power.toFloat())) {
									ManaItemHandler.requestManaExactForTool(stack, player, cost, true)
									entity.setFire(power * 20)
								}
							}
						}
					}
				}
			}
		} catch (e: Throwable) {}
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
