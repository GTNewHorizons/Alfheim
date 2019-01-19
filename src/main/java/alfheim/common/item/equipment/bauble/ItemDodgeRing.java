package alfheim.common.item.equipment.bauble;

import org.lwjgl.opengl.GL11;

import alfheim.AlfheimCore;
import alfheim.common.network.Message0d;
import alfheim.common.network.Message0d.m0d;
import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class ItemDodgeRing extends ItemBauble {

	public static final String TAG_DODGE_COOLDOWN = "dodgeCooldown";
	public static final int MAX_CD = 20;

	private static boolean oldLeftDown, oldRightDown;
	private static int leftDown, rightDown;

	public ItemDodgeRing() {
		super("DodgeRing");
		setCreativeTab(AlfheimCore.alfheimTab);
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeyDown(KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();

		IInventory baublesInv = BaublesApi.getBaubles(mc.thePlayer);
		ItemStack ringStack = baublesInv.getStackInSlot(1);
		if(ringStack == null|| !(ringStack.getItem() instanceof ItemDodgeRing)) {
			ringStack = baublesInv.getStackInSlot(2);
			if(ringStack == null || !(ringStack.getItem() instanceof ItemDodgeRing))
				return;
		}

		if(ItemNBTHelper.getInt(ringStack, TAG_DODGE_COOLDOWN, 0) > 0) return;

		int threshold = 4;
		if(mc.gameSettings.keyBindLeft.getIsKeyPressed() && !oldLeftDown) {
			int oldLeft = leftDown;
			leftDown = ClientTickHandler.ticksInGame;

			if(leftDown - oldLeft < threshold) dodge(mc.thePlayer, true);
		} else if(mc.gameSettings.keyBindRight.getIsKeyPressed() && !oldRightDown) {
			int oldRight = rightDown;
			rightDown = ClientTickHandler.ticksInGame;

			if(rightDown - oldRight < threshold) dodge(mc.thePlayer, false);
		}

		oldLeftDown = mc.gameSettings.keyBindLeft.getIsKeyPressed();
		oldRightDown = mc.gameSettings.keyBindRight.getIsKeyPressed();
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
		if(cd > 0) ItemNBTHelper.setInt(stack, TAG_DODGE_COOLDOWN, cd - 1);
	}

	private static void dodge(EntityPlayer player, boolean left) {
		if(player.capabilities.isFlying || !player.onGround || player.moveForward > 0.2 || player.moveForward < -0.2) return;

		float yaw = player.rotationYaw;
		float x = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
		float z = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
		Vector3 lookVec = new Vector3(x, 0, z);
		Vector3 sideVec = lookVec.crossProduct(new Vector3(0, left ? 1 : -1, 0)).multiply(1.25);

		player.motionX = sideVec.x;
		player.motionY = sideVec.y;
		player.motionZ = sideVec.z;

		AlfheimCore.network.sendToServer(new Message0d(m0d.DODGE));
	}

	@SideOnly(Side.CLIENT)
	public static void renderHUD(ScaledResolution resolution, EntityPlayer player, ItemStack stack, float pticks) {
		int xo = resolution.getScaledWidth() / 2 - 20;
		int y = resolution.getScaledHeight() / 2 + 20;

		if(!player.capabilities.isFlying) {
			int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
			int width = Math.min((int) ((cd - pticks) * 2), 40);
			GL11.glColor4d(1, 1, 1, 1);
			if(width > 0) {
				Gui.drawRect(xo, y - 2, xo + 40, y - 1, 0x88000000);
				Gui.drawRect(xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
			}
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4d(1, 1, 1, 1);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}
}