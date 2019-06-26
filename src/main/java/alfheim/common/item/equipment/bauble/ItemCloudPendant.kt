package alfheim.common.item.equipment.bauble

import java.util.UUID

import alfheim.AlfheimCore
import alfheim.common.network.Message0d
import alfheim.common.network.Message0d.m0d
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import vazkii.botania.api.item.IBaubleRender

class ItemCloudPendant @JvmOverloads constructor(name: String = "CloudPendant", val maxAllowedJumps: Int = 2): ItemPendant(name), IBaubleRender {
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		super.onWornTick(stack, player)
		clientWornTick(stack, player)
	}
	
	@SideOnly(Side.CLIENT)
	fun clientWornTick(stack: ItemStack, player: EntityLivingBase) {
		if (player is EntityPlayerSP && player === Minecraft.getMinecraft().thePlayer) {
			val playerSp = player as EntityPlayerSP
			val uuid = playerSp.uniqueID
			
			if (playerSp.onGround)
				timesJumped = 0
			else {
				if (playerSp.movementInput.jump) {
					if (!jumpDown && timesJumped < maxAllowedJumps) {
						playerSp.jump()
						AlfheimCore.network.sendToServer(Message0d(m0d.JUMP))
						timesJumped++
					}
					jumpDown = true
				} else
					jumpDown = false
			}
		}
	}
	
	companion object {
		
		private var timesJumped: Int = 0
		private var jumpDown: Boolean = false
	}
}