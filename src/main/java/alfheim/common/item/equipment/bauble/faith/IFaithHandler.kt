package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.MathHelper
import vazkii.botania.common.core.helper.Vector3

interface IFaithHandler {
	
	fun onEquipped(stack: ItemStack, player: EntityPlayer, type: FaithBauble) = Unit
	fun onWornTick(stack: ItemStack, player: EntityPlayer, type: FaithBauble) = Unit
	fun onUnequipped(stack: ItemStack, player: EntityPlayer, type: FaithBauble) = Unit
	
	fun getGodPowerLevel(player: EntityPlayer): Int
	
	@SideOnly(Side.CLIENT)
	fun doParticles(stack: ItemStack, player: EntityPlayer)
	
	fun getHeadOrientation(entity: EntityLivingBase): Vector3 {
		val f1 = MathHelper.cos(-entity.rotationYaw * 0.017453292F - Math.PI.F)
		val f2 = MathHelper.sin(-entity.rotationYaw * 0.017453292F - Math.PI.F)
		val f3 = -MathHelper.cos(-(entity.rotationPitch - 90) * 0.017453292F)
		val f4 = MathHelper.sin(-(entity.rotationPitch - 90) * 0.017453292F)
		return Vector3((f2 * f3).D, f4.D, (f1 * f3).D)
	}
	
	companion object {
		fun getFaithHandler(stack: ItemStack): IFaithHandler {
			return when (stack.meta) {
				0    -> FaithHandlerThor
				1    -> FaithHandlerSif
				2    -> FaithHandlerNjord
				3    -> FaithHandlerLoki
				4    -> FaithHandlerHeimdall
				else -> DummyFaithHandler
			}
		}
	}
	
	enum class FaithBauble {
		EMBLEM, CLOAK
	}
	
	object DummyFaithHandler: IFaithHandler {
		override fun getGodPowerLevel(player: EntityPlayer) = 0
		override fun doParticles(stack: ItemStack, player: EntityPlayer) = Unit
	}
}