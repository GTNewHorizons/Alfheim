package alfheim.client.gui;

import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public final class ItemsRemainingRenderHandler {

	private static final int maxTicks = 30;
	private static final int leaveTicks = 20;

	private static ItemStack stack = new ItemStack(Blocks.stone);
	private static String customString;
	private static int ticks, count;

	@SideOnly(Side.CLIENT)
	public static void render(ScaledResolution resolution, float partTicks) {
		if(ticks > 0 && !isEmpty(stack)) {
			int pos = maxTicks - ticks;
			Minecraft mc = Minecraft.getMinecraft();
			int x = resolution.getScaledWidth() / 2 + 10 + Math.max(0, pos - leaveTicks);
			int y = resolution.getScaledHeight() / 2;

			int start = maxTicks - leaveTicks;
			float alpha = ticks + partTicks > start ? 1F : (ticks + partTicks) / start;

			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glColor4f(1F, 1F, 1F, alpha);
			RenderHelper.enableGUIStandardItemLighting();
			int xp = x + (int) (16F * (1F - alpha));
			GL11.glTranslatef(xp, y, 0F);
			GL11.glScalef(alpha, 1F, 1F);
			RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, 0);
			GL11.glScalef(1F / alpha,1F, 1F);
			GL11.glTranslatef(-xp, -y, 0F);
			RenderHelper.disableStandardItemLighting();
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glEnable(GL11.GL_BLEND);

			String text = "";

			if(customString == null) {
				if(!isEmpty(stack)) {
					text = EnumChatFormatting.GREEN + stack.getDisplayName();
					if(count >= 0) {
						int max = stack.getMaxStackSize();
						int stacks = count / max;
						int rem = count % max;

						if(stacks == 0)
							text = "" + count;
						else text = count + " (" + EnumChatFormatting.AQUA + stacks + EnumChatFormatting.RESET + "*" + EnumChatFormatting.GRAY + max + EnumChatFormatting.RESET + "+" + EnumChatFormatting.YELLOW + rem + EnumChatFormatting.RESET + ")";
					} else if(count == -1)
						text = "\u221E";
				}
			} else text = customString;

			int color = 0x00FFFFFF | (int) (alpha * 0xFF) << 24;
			mc.fontRenderer.drawStringWithShadow(text, x + 20, y + 6, color);

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void tick() {
		if(ticks > 0)
			--ticks;
	}

	public static void set(ItemStack stack, String str) {
		set(stack, 0, str);
	}

	public static void set(ItemStack stack, int count) {
		set(stack, count, null);
	}

	public static void set(ItemStack stack, int count, String str) {
		ItemsRemainingRenderHandler.stack = stack;
		ItemsRemainingRenderHandler.count = count;
		ItemsRemainingRenderHandler.customString = str;
		ticks = stack.getItem() == Item.getItemFromBlock(Blocks.air) ? 0 : maxTicks;
	}

	public static void set(EntityPlayer player, ItemStack displayStack, Pattern pattern) {
		int count = 0;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(!isEmpty(stack) && pattern.matcher(stack.getDisplayName()).find())
				count += stack.stackSize;
		}

		set(displayStack, count);
	}

	public static boolean isEmpty(ItemStack stack) {
		return stack.getItem() == Item.getItemFromBlock(Blocks.air);
	}
}