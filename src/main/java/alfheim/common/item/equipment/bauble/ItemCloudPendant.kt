package alfheim.common.item.equipment.bauble

import alfheim.AlfheimCore
import alfheim.common.network.Message0dS
import alfheim.common.network.Message0dS.m0ds
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import vazkii.botania.api.item.IBaubleRender

class ItemCloudPendant @JvmOverloads constructor(name: String = "CloudPendant", val maxAllowedJumps: Int = 2): ItemPendant(name), IBaubleRender {
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		super.onWornTick(stack, player)
		if (player.worldObj.isRemote) clientWornTick(stack, player)
	}
	
	@SideOnly(Side.CLIENT)
	fun clientWornTick(stack: ItemStack, player: EntityLivingBase) {
		if (player is EntityPlayerSP && player === Minecraft.getMinecraft().thePlayer) {

			if (player.onGround)
				timesJumped = 0
			else {
				jumpDown = if (player.movementInput.jump) {
					if (!jumpDown && timesJumped < maxAllowedJumps) {
						player.jump()
						AlfheimCore.network.sendToServer(Message0dS(m0ds.JUMP))
						timesJumped++
					}
					true
				} else
					false
			}
		}
	}
	
	companion object {
		private var timesJumped: Int = 0
		private var jumpDown: Boolean = false
	}
}