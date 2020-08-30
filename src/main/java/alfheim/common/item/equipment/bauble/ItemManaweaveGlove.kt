package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.get
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.item.equipment.bauble.IManaDiscountBauble
import alfheim.common.core.util.AlfheimTab
import alfheim.common.integration.travellersgear.*
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.Optional
import cpw.mods.fml.relauncher.*
import net.minecraft.client.model.ModelBiped
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import vazkii.botania.common.item.equipment.bauble.ItemBauble

@Optional.Interface(modid = "TravellersGear", iface = "alfheim.common.integration.travellersgear.ITravellersGearSynced", striprefs = true)
class ItemManaweaveGlove: ItemBauble("ManaweaveGlove" + if (AlfheimCore.TravellersGearLoaded) "s" else ""), IManaDiscountBauble, ITravellersGearSynced {
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun getDiscount(stack: ItemStack, slot: Int, player: EntityPlayer) = 0.075f * if (AlfheimCore.TravellersGearLoaded) 2 else 1
	
	override fun getBaubleType(stack: ItemStack) =
		if (AlfheimCore.TravellersGearLoaded) null else BaubleType.RING
	
	override fun getSlot(stack: ItemStack?) = 2
	
	override fun onTravelGearEquip(player: EntityPlayer, stack: ItemStack) {
		super.onTravelGearEquip(player, stack)
		onEquippedOrLoadedIntoWorld(stack, player)
	}
	
	override fun onTravelGearTickSynced(player: EntityPlayer, stack: ItemStack) {
		onWornTick(stack, player)
	}
	
	override fun onTravelGearUnequip(player: EntityPlayer, stack: ItemStack) {
		super.onTravelGearUnequip(player, stack)
		onUnequipped(stack, player)
	}
	
	override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<Any?>, adv: Boolean) {
		TravellerBaubleTooltipHandler.addHiddenTooltip(this, stack, tooltip)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getArmorTexture(stack: ItemStack, entity: Entity?, slot: Int, type: String?) = "${ModInfo.MODID}:textures/model/armor/ManaweaveGloves.png"
	
	@SideOnly(Side.CLIENT)
	override fun getArmorModel(entity: EntityLivingBase?, stack: ItemStack, armorSlot: Int): ModelBiped? {
		val mod = model ?: ModelBiped(0.01f, 0.0f, 64, 32).apply {
			model = this
			
			bipedHead.showModel = false
			bipedHeadwear.showModel = false
			bipedBody.showModel = false
			bipedLeftLeg.showModel = false
			bipedRightLeg.showModel = false
			bipedBody.showModel = false
		}
		
		if (entity is EntityPlayer) run {
			val baubles = PlayerHandler.getPlayerBaubles(entity)
			mod.bipedRightArm.showModel = baubles[1]?.item === this@ItemManaweaveGlove || AlfheimCore.TravellersGearLoaded
			mod.bipedLeftArm.showModel = baubles[2]?.item === this@ItemManaweaveGlove || AlfheimCore.TravellersGearLoaded
		}
		
		return mod
	}
	
	companion object {
		@field:SideOnly(Side.CLIENT)
		var model: ModelBiped? = null
		@SideOnly(Side.CLIENT)
		get() = field
		@SideOnly(Side.CLIENT)
		set(value) { field = value }
	}
}