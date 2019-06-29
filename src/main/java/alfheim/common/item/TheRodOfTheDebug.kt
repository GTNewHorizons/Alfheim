package alfheim.common.item

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.entity.EnumRace
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.Potion
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper

class TheRodOfTheDebug: Item() {
	init {
		creativeTab = AlfheimCore.alfheimTab
		setMaxStackSize(1)
		setTextureName(ModInfo.MODID + ":TheRodOfTheDebug")
		unlocalizedName = "TheRodOfTheDebug"
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		try {
			if (true/*!world.isRemote*/) {
				if (!player.isSneaking) {
					if (!world.isRemote) {
//						CardinalSystem.PartySystem.setParty(player, Party(player))
//						CardinalSystem.PartySystem.getParty(player)!!.add(CardinalSystem.TargetingSystem.getTarget(player).target)
					}
					
					//					for (Object o : world.loadedEntityList) if (o instanceof Entity && !(o instanceof EntityPlayer)) ((Entity) o).setDead();
					
					//					int r = 12;
					//					for (int x = -r; x < r; x++) {
					//						for (int z = -r; z < r; z++) {
					//							for (int y = 1; y < 4; y++) {
					//								world.setBlock(x, y, z + 50, Blocks.grass);
					//							}
					//						}
					//					}
					
					//					ASJUtilities.sendToDimensionWithoutPortal(player, 0, player.posX, 228, player.posZ);
				} else {
					EnumRace.setRaceID(player, ((EnumRace.getRace(player).ordinal + 1) % 11).toDouble())
					ASJUtilities.chatLog(EnumRace.getRace(player).ordinal.toString() + " - " + EnumRace.getRace(player), player)
				}
			}
		} catch (e: Throwable) {
			ASJUtilities.log("Oops!")
			e.printStackTrace()
		}
		
		return stack
	}
	
	override fun onItemUse(stack: ItemStack?, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		try {
			if (world!!.isRemote) return false
			
			val te = world.getTileEntity(x, y, z)
			if (te != null) {
				//				NBTTagCompound nbt = new NBTTagCompound();
				//				te.writeToNBT(nbt);
				//				for (String s : ASJUtilities.toString(nbt).split("\n")) ASJUtilities.chatLog(s);
				
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