package alfheim.common.item.equipment.tool.elementuim;

import java.util.List;

import alfheim.common.item.equipment.tool.manasteel.ItemManasteelHoe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;

public class ItemElementiumHoe extends ItemManasteelHoe {

	public ItemElementiumHoe() {
		super(BotaniaAPI.elementiumToolMaterial, "ElementiumHoe");
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		boolean _do = super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		if (!player.isSneaking() && _do) {
			for (int i = -1; i < 2; i++) {
				for (int k = -1; k < 2; k++) {
					if (i == 0 && k == 0) continue;
					if (!world.isAirBlock(x + i, y, z + k)) super.onItemUse(stack, player, world, x + i, y, z + k, side, hitX, hitY, hitZ);
				}
			}
		}
		return _do;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean extra) {
		info.add(StatCollector.translateToLocal("item.ElementiumHoe.desc"));
	}
}
