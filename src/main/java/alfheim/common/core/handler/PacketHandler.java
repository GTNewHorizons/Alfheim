package alfheim.common.core.handler;

import java.lang.reflect.Field;

import alexsocol.asjlib.ASJReflectionHelper;
import alfheim.common.item.equipment.bauble.ItemCloudPendant;
import alfheim.common.item.equipment.bauble.ItemDodgeRing;
import alfheim.common.network.Message0d;
import alfheim.common.network.Message0d.m0d;
import baubles.api.BaublesApi;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ChatComponentTranslation;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;

public class PacketHandler {

	public static void handle(Message0d packet, MessageContext ctx) {
		switch (m0d.values()[packet.type]) {
			case DODGE: DOGIE(ctx.getServerHandler()); break;
			case JUMP: jump(ctx.getServerHandler().playerEntity); break;
		}
	}
	
	private static void DOGIE(NetHandlerPlayServer sh) {
		EntityPlayerMP player = sh.playerEntity;
		
		player.worldObj.playSoundAtEntity(player, "botania:dash", 1F, 1F);

		IInventory baublesInv = BaublesApi.getBaubles(player);
		ItemStack ringStack = baublesInv.getStackInSlot(1);

		if(ringStack == null|| !(ringStack.getItem() instanceof ItemDodgeRing)) {
			ringStack = baublesInv.getStackInSlot(2);
			if(ringStack == null || !(ringStack.getItem() instanceof ItemDodgeRing)) {
				sh.netManager.closeChannel(new ChatComponentTranslation("alfheimmisc.invalidDodge"));
				return;
			}
		}

		if (ItemNBTHelper.getInt(ringStack, ItemDodgeRing.TAG_DODGE_COOLDOWN, ItemDodgeRing.MAX_CD) > 0) 
			sh.netManager.closeChannel(new ChatComponentTranslation("alfheimmisc.invalidDodge"));
		
		player.addExhaustion(0.3F);
		ItemNBTHelper.setInt(ringStack, ItemDodgeRing.TAG_DODGE_COOLDOWN, ItemDodgeRing.MAX_CD);
	}

	public static Field fallBuffer;
	
	static {
		fallBuffer = ASJReflectionHelper.getField(ItemTravelBelt.class, "fallBuffer");
		fallBuffer.setAccessible(true);
	}
	
	private static void jump(EntityPlayerMP player) {
		IInventory baublesInv = BaublesApi.getBaubles(player);
		ItemStack amuletStack = baublesInv.getStackInSlot(0);

		if(amuletStack != null && amuletStack.getItem() instanceof ItemCloudPendant) {
			player.addExhaustion(0.3F);
			player.fallDistance = 0;
				
			ItemStack belt = baublesInv.getStackInSlot(3);

			if(belt != null && belt.getItem() instanceof ItemTravelBelt) {
				float val = ASJReflectionHelper.getValue(fallBuffer, (ItemTravelBelt) belt.getItem(), false);
				player.fallDistance = -val * ((ItemCloudPendant) amuletStack.getItem()).getMaxAllowedJumps();
			}
		}
	}
}