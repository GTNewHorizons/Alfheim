package alfheim.common.item;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.entity.EnumRace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class TheRodOfTheDebug extends Item {

	public TheRodOfTheDebug() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setTextureName(ModInfo.MODID + ":TheRodOfTheDebug");
		setUnlocalizedName("TheRodOfTheDebug");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (/*!world.isRemote */true) {
			if (!player.isSneaking()) {
				/*int m = 100;
				for (int x = -m; x < m; x++) for (int y = -m; y < m; y++) for (int z = -m; z < m; z++) {
					Block b = player.worldObj.getBlock((int) player.posX + x, (int) player.posY + y, (int) player.posZ + z);
					if (b == ModBlocks.livingrock || b.getMaterial().equals(Material.water)) {
						world.setBlockToAir((int) player.posX + x, (int) player.posY + y, (int) player.posZ + z);
					} else if (b == Blocks.air) world.setBlock((int) player.posX + x, (int) player.posY + y, (int) player.posZ + z, Blocks.stone);
				} */
				
//				MovingObjectPosition mop = ASJUtilities.getSelectedBlock(player, 1.0F, 64, true);
//				int x = mop.blockX, y = mop.blockY, z = mop.blockZ;
//				ASJUtilities.chatLog("meta " + world.getBlockMetadata(x, y, z), world);

//				EnumRace.setRaceID(player, (EnumRace.getRace(player).ordinal() + 1) % 11);
//				ASJUtilities.chatLog(EnumRace.getRace(player).ordinal() + " - " + EnumRace.getRace(player).toString(), player);
				
//				ASJUtilities.sendToDimensionWithoutPortal(player, 0, player.posX, 228, player.posZ);
				
				/*TileEntity t = world.getTileEntity(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ));
				if (t != null) {
					for (int i = 0; i < 1000; i++ )t.updateEntity();
				}*/
			} else {
				player.addChatComponentMessage(new ChatComponentText("Current dimension id: " + player.dimension));
			}
		}
		return stack;
	}
}
