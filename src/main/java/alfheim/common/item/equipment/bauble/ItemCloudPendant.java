package alfheim.common.item.equipment.bauble;

import java.util.UUID;

import alfheim.AlfheimCore;
import alfheim.common.network.Message0d;
import alfheim.common.network.Message0d.m0d;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.IBaubleRender;

public class ItemCloudPendant extends ItemPendant implements IBaubleRender {

	private static int timesJumped;
	private static boolean jumpDown;
	private final int maxJumps;
	
	public ItemCloudPendant(String name, int jumps) {
		super(name);
		maxJumps = jumps;
	}
	
	public ItemCloudPendant() {
		this("CloudPendant", 2);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);
		clientWornTick(stack, player);
	}
	
	@SideOnly(Side.CLIENT)
	public void clientWornTick(ItemStack stack, EntityLivingBase player) {
		if(player instanceof EntityPlayerSP && player == Minecraft.getMinecraft().thePlayer) {
			EntityPlayerSP playerSp = (EntityPlayerSP) player;
			UUID uuid = playerSp.getUniqueID();

			if(playerSp.onGround)
				timesJumped = 0;
			else {
				if(playerSp.movementInput.jump) {
					if(!jumpDown && timesJumped < getMaxAllowedJumps()) {
						playerSp.jump();
						AlfheimCore.network.sendToServer(new Message0d(m0d.JUMP));
						timesJumped++;
					}
					jumpDown = true;
				} else jumpDown = false;
			}
		}
	}
	
	public final int getMaxAllowedJumps() {
		return maxJumps;
	}
}