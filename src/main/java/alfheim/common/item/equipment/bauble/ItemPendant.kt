package alfheim.common.item.equipment.bauble

import alfheim.client.core.util.mc
import alfheim.common.core.util.AlfheimTab
import baubles.api.BaubleType
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.item.equipment.bauble.ItemBauble

open class ItemPendant(name: String): ItemBauble(name), IBaubleRender {
	
	lateinit var icon: IIcon
	
	init {
		creativeTab = AlfheimTab
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(par1IconRegister: IIconRegister) {
		super.registerIcons(par1IconRegister)
		icon = IconHelper.forItem(par1IconRegister, this, "Gem")
	}
	
	override fun getBaubleType(stack: ItemStack): BaubleType {
		return BaubleType.AMULET
	}
	
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type == IBaubleRender.RenderType.BODY) {
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			IBaubleRender.Helper.rotateIfSneaking(event.entityPlayer)
			val armor = event.entityPlayer.getCurrentArmor(2) != null
			glPushMatrix()
			glRotated(180.0, 1.0, 0.0, 0.0)
			glTranslated(-0.25, -0.4, if (armor) 0.21 else 0.14)
			glScaled(0.5, 0.5, 0.5)
			ItemRenderer.renderItemIn2D(Tessellator.instance, icon.maxU, icon.minV, icon.minU, icon.maxV, icon.iconWidth, icon.iconHeight, 1f / 32f)
			glPopMatrix()
		}
	}
}