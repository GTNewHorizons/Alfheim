package alfheim.common.item.creator

import alexsocol.asjlib.ASJUtilities
import alfheim.common.core.helper.*
import alfheim.common.item.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.*
import net.minecraft.entity.item.EntityFireworkRocket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.item.lens.*

class ItemRoyalStaff: ItemMod("RoyalStaff") {
	
	init {
		creativeTab = null
		setFull3D()
		maxStackSize = 1
		maxDamage = 0
		
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity, slot: Int, equiped: Boolean) {
		if (!ContributorsPrivacyHelper.isCorrect(entity.commandSenderName, "AlexSocol")) {
			if (entity is EntityPlayer)
				while (entity.inventory.consumeInventoryItem(this));
			else
				stack.stackSize = 0
		}
	}
	
	override fun getRarity(itemstack: ItemStack) = BotaniaAPI.rarityRelic!!
	override fun getItemUseAction(par1ItemStack: ItemStack) = EnumAction.bow
	override fun getMaxItemUseDuration(itemstack: ItemStack) = Int.MAX_VALUE
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (!ContributorsPrivacyHelper.isCorrect(player, "AlexSocol")) {
			while (player.inventory.consumeInventoryItem(this));
			return stack
		}
		
		if (!player.isSneaking)
			player.setItemInUse(stack, getMaxItemUseDuration(stack))
		else {
			if (!world.isRemote) {
				world.spawnEntityInWorld(EntityFireworkRocket(world, player.posX, player.posY, player.posZ, (ItemLens.getLens(ItemLens.FIREWORK) as LensFirework).generateFirework(ItemIridescent.rainbowColor())))
			}
		}
		return stack
	}
	
	override fun registerIcons(reg: IIconRegister) {
		orn = IconHelper.forName(reg, "misc/focus_whatever_orn")
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 1)
			dep = InterpolatedIconHelper.forName(event.map, "misc/focus_warding_depth")
	}
	
	override fun hitEntity(stack: ItemStack?, target: EntityLivingBase?, attacker: EntityLivingBase?): Boolean {
		target?.hurtResistantTime = 0
		target?.hurtTime = 0
		
		return super.hitEntity(stack, target, attacker)
	}
	
	companion object {
		lateinit var orn: IIcon
		var dep: IIcon? = null
	}
}
