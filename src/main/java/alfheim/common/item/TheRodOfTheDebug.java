package alfheim.common.item;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem;
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.tiles.TileNode;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class TheRodOfTheDebug extends Item {
	
	public TheRodOfTheDebug() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setMaxStackSize(1);
		setTextureName(ModInfo.MODID + ":TheRodOfTheDebug");
		setUnlocalizedName("TheRodOfTheDebug");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		try {
			if (true/*!world.isRemote*/) {
				if (!player.isSneaking()) {
					if (!world.isRemote) {
						PartySystem.setParty(player, new Party(player));
						PartySystem.getParty(player).add(TargetingSystem.getTarget(player).target);
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
					EnumRace.setRaceID(player, (EnumRace.getRace(player).ordinal() + 1) % 11);
					ASJUtilities.chatLog(EnumRace.getRace(player).ordinal() + " - " + EnumRace.getRace(player), player);
				}
			}
		} catch (Throwable e) {
			ASJUtilities.log("Oops!");
			e.printStackTrace();
		}
		
		return stack;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		try {
			if (world.isRemote) return false;
			
			TileEntity te = world.getTileEntity(x, y, z);
			if (te != null) {
//				NBTTagCompound nbt = new NBTTagCompound();
//				te.writeToNBT(nbt);
//				for (String s : ASJUtilities.toString(nbt).split("\n")) ASJUtilities.chatLog(s);
				
//				if (te instanceof TileAnomaly) ((TileAnomaly) te).addSubTile("Lightning");
				
//				if (te instanceof TileNode) ((TileNode) te).setAspects(new AspectList().add(Aspect.WATER, 500).add(Aspect.AIR, 500).add(Aspect.FIRE, 500).add(Aspect.EARTH, 500).add(Aspect.ORDER, 500).add(Aspect.ENTROPY, 500));
			}
		} catch (Throwable e) {
			ASJUtilities.log("Oops!");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void royalStaff(EntityPlayer player) {
		if (!Botania.thaumcraftLoaded) return;
		
		ItemStack stk = new ItemStack(GameRegistry.findItem("Thaumcraft", "WandCasting"), 1, 148);
		ItemNBTHelper.setBoolean(stk, "sceptre", true);
		ItemNBTHelper.setString(stk, "cap", ThaumcraftAlfheimModule.capMauftriumName);
		ItemNBTHelper.setString(stk, "rod", "primal_staff");
		ItemNBTHelper.setInt(stk, "aer", 37500);
		ItemNBTHelper.setInt(stk, "terra", 37500);
		ItemNBTHelper.setInt(stk, "ignis", 37500);
		ItemNBTHelper.setInt(stk, "aqua", 37500);
		ItemNBTHelper.setInt(stk, "ordo", 37500);
		ItemNBTHelper.setInt(stk, "perditio", 37500);
		NBTTagCompound focus = new NBTTagCompound();
		focus.setShort("id", (short) 4109);
		focus.setShort("Damage", (short) 0);
		focus.setBoolean("Count", true);
		stk.stackTagCompound.setTag("focus", focus);
		player.inventory.addItemStackToInventory(stk);
	}
}