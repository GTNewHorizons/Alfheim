package alfheim.common.item

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.api.entity.*
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.entity.boss.EntityFenrir
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper

class TheRodOfTheDebug: ItemMod("TheRodOfTheDebug") {
	
	init {
		maxStackSize = 1
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (ModInfo.OBF) return stack
		
		try {
			if (!player.isSneaking) {
				if (!world.isRemote) {
					//CardinalSystem.PartySystem.setParty(player, CardinalSystem.PartySystem.Party(player))
					CardinalSystem.PartySystem.getParty(player).add(CardinalSystem.TargetingSystem.getTarget(player).target)
				}
				
				// for (o in world.loadedEntityList) if (o is Entity && o !is EntityPlayer) o.setDead()
			} else {
				player.raceID = (player.race.ordinal + 1) % 11
				ASJUtilities.chatLog("${player.race.ordinal} - ${player.race}", player)
			}
		} catch (e: Throwable) {
			ASJUtilities.log("Oops!")
			e.printStackTrace()
		}
		
		return stack
	}
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (ModInfo.OBF) return false
		
		try {
//			if (!world.isRemote) world.getBlock(x, y, z).updateTick(world, x, y, z, world.rand)
			
			val te = world.getTileEntity(x, y, z)
			if (te != null) {
				val nbt = NBTTagCompound()
				te.writeToNBT(nbt)
				for (s in ASJUtilities.toString(nbt).split("\n")) ASJUtilities.chatLog(s, world)
				
				//				if (te instanceof TileAnomaly) ((TileAnomaly) te).addSubTile("Lightning");
				
				//				if (te instanceof TileNode) ((TileNode) te).setAspects(new AspectList().add(Aspect.WATER, 500).add(Aspect.AIR, 500).add(Aspect.FIRE, 500).add(Aspect.EARTH, 500).add(Aspect.ORDER, 500).add(Aspect.ENTROPY, 500));
			}
		} catch (e: Throwable) {
			ASJUtilities.log("Oops!")
			e.printStackTrace()
		}
		
		return false
	}
	
	companion object {
		
		fun royalStaff(player: EntityPlayer) {
			if (!Botania.thaumcraftLoaded) return
			
			val stk = ItemStack(GameRegistry.findItem("Thaumcraft", "WandCasting"), 1, 148)
			ItemNBTHelper.setBoolean(stk, "sceptre", true)
			ItemNBTHelper.setString(stk, "cap", ThaumcraftAlfheimModule.capMauftriumName)
			ItemNBTHelper.setString(stk, "rod", "primal_staff")
			ItemNBTHelper.setInt(stk, "aer", 37500)
			ItemNBTHelper.setInt(stk, "terra", 37500)
			ItemNBTHelper.setInt(stk, "ignis", 37500)
			ItemNBTHelper.setInt(stk, "aqua", 37500)
			ItemNBTHelper.setInt(stk, "ordo", 37500)
			ItemNBTHelper.setInt(stk, "perditio", 37500)
			val focus = NBTTagCompound()
			focus.setShort("id", 4109.toShort())
			focus.setShort("Damage", 0.toShort())
			focus.setBoolean("Count", true)
			stk.stackTagCompound.setTag("focus", focus)
			player.inventory.addItemStackToInventory(stk)
		}
	}
}