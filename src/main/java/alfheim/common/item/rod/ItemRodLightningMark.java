package alfheim.common.item.rod;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.entity.EntityLightningMark;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import vazkii.botania.api.mana.ManaItemHandler;

public class ItemRodLightningMark extends ItemRodBase {

	public ItemRodLightningMark() {
		super("RodLightningMark");
	}
	
	@Override
	public void cast(ItemStack stack, World world, EntityPlayer player) {
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(player, 1, 256, true); 
    	if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK && ManaItemHandler.requestManaExactForTool(stack, player, MANA_PER_CAST, world.isRemote)) if (!world.isRemote) world.spawnEntityInWorld(new EntityLightningMark(world, mop.blockX + 0.5, mop.blockY + 1, mop.blockZ + 0.5));
	}
}