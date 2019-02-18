package alfheim.common.item;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
					
//					world.setBlock(0, 5, -50, AlfheimBlocks.anomaly);
//					((TileAnomaly) world.getTileEntity(0, 5, -50)).addSubTile("Gravity").addSubTile("Lightning").addSubTile("ManaVoid");
					
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
			return stack;
		} catch (Throwable e) {
			ASJUtilities.log("Oops!");
			return stack;
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return false;
		
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null) {
//			NBTTagCompound nbt = new NBTTagCompound();
//			te.writeToNBT(nbt);
//			for (String s : ASJUtilities.toString(nbt).split("\n")) ASJUtilities.chatLog(s);
			
//			if (te instanceof TileAnomaly) ((TileAnomaly) te).addSubTile("Lightning");
		}
		return false;
	}
}