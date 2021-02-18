package alfheim.common.item.equipment.bauble

import baubles.api.BaubleType
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import org.lwjgl.input.Keyboard
import vazkii.botania.api.mana.*
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.equipment.bauble.ItemBauble
import kotlin.math.max

class ItemSpiderRing: ItemBauble("SpiderRing"), IManaUsingItem {
	
	override fun getBaubleType(stack: ItemStack?) = BaubleType.RING
	
	override fun onWornTick(stack: ItemStack?, entity: EntityLivingBase?) {
		super.onWornTick(stack, entity)
		if (entity !is EntityPlayer) return
		
		entity.fallDistance = 0f
		
		ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, ManaItemHandler.requestManaExact(stack, entity, 1, true))
		
		if (entity.worldObj.isRemote)
			clientWornTick(stack, entity)
	}
	
	@SideOnly(Side.CLIENT)
	fun clientWornTick(stack: ItemStack?, player: EntityLivingBase) {
		if (!ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false)) return
		
		if (player is EntityPlayerSP && player === Minecraft.getMinecraft().thePlayer) {
			if (player.isCollidedHorizontally) {
				if (Minecraft.getMinecraft().gameSettings.keyBindForward.keyCode.let { Keyboard.isKeyDown(it) })
					player.motionY = max(player.motionY, 0.11)
			}
		}
	}
	
	override fun usesMana(stack: ItemStack?) = true
	
	companion object {
		
		const val TAG_ACTIVE = "active"
	}
}
