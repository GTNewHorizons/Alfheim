package alfheim.common.utils;

import alexsocol.asjlib.ASJUtilities;
import alfheim.Constants;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class KeyBindingsUtils {
	
	public static void enableFlight(EntityPlayerMP player) {
		if (player.capabilities.isCreativeMode || player.getEntityAttribute(Constants.RACE).getAttributeValue() == 0) return;
		player.capabilities.allowFlying = true;
		player.capabilities.isFlying = !player.capabilities.isFlying;
		player.sendPlayerAbilities();
	}

	public static void atack(EntityPlayerMP player) {
		MovingObjectPosition mop = ASJUtilities.getMouseOver(player, 1.0F, player.theItemInWorldManager.getBlockReachDistance(), true);
		if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit != null) {
			player.attackTargetEntityWithCurrentItem(mop.entityHit);
		}
	} 
}
