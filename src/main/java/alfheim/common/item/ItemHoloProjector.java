package alfheim.common.item;

import org.lwjgl.opengl.GL11;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.client.model.entity.ModelBipedNew;
import alfheim.client.render.tile.RenderTileFlugelHead;
import alfheim.common.core.command.CommandRace;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class ItemHoloProjector extends Item {

	private static final int RACES = 9;
	private static final ModelHolder FALLBACK_MODEL = new ModelHolder(0, true);
	
	private static final String TAG_MODEL_PREFIX = "model";
	private static final String TAG_GENDER = "gender";
	private static final String TAG_ROTATION = "rotation";
	private static final String TAG_EQUIPPED = "equipped";
	private static final String TAG_FIRST_TICK = "firstTick";
	
	public ItemHoloProjector() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setTextureName(ModInfo.MODID + ":HoloProjector");
		setUnlocalizedName("HoloProjector");
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	// Shift+RMB - select race; RMB - rotate clockwise
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		int seg = getSegmentLookedAt(stack, player);
		
		if(player.isSneaking() && !world.isRemote) (new CommandRace()).processCommand(player, new String[] {EnumRace.values()[seg+1].toString()});
		else {
			ModelHolder pos = getModel(stack, seg);
			setModel(stack, seg, pos.rotation + 2 % 360, pos.isMale);
		}
		return stack;
	}

	// Shift+LMB - switch gender; LMB - rotate anti-clockwise
	@Override
	public boolean onEntitySwing(EntityLivingBase player, ItemStack stack) {
		if(player instanceof EntityPlayer) {
			int seg = getSegmentLookedAt(stack, player);
			ModelHolder pos = getModel(stack, seg);
			if (player.isSneaking()) setModel(stack, seg, pos.rotation, !pos.isMale);
			else setModel(stack, seg, pos.rotation - 2 % 360, pos.isMale);
			return true;
		}

		return false;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int pos, boolean equipped) {
		super.onUpdate(stack, world, entity, pos, equipped);
		boolean eqLastTick = wasEquipped(stack);
		boolean firstTick = isFirstTick(stack);
		if(eqLastTick != equipped) setEquipped(stack, equipped);

		if((!equipped || firstTick) && entity instanceof EntityLivingBase) {
			int angles = 360;
			int segAngles = angles / RACES;
			float shift = segAngles / 2;
			if(firstTick) tickFirst(stack);
		}
	}

	private static int getSegmentLookedAt(ItemStack stack, EntityLivingBase player) {
		float yaw = getCheckingAngle(player, 360F);

		int angles = 360;
		int segAngles = angles / RACES;
		for(int seg = 0; seg < RACES; seg++) {
			float calcAngle = (float) seg * segAngles;
			if(yaw >= calcAngle && yaw < calcAngle + segAngles) return seg;
		}
		return 0;
	}

	// Screw the way minecraft handles rotation
	// Really...
	private static float getCheckingAngle(EntityLivingBase player, float base) {
		float yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw) + 90F;
		int angles = 360;
		int segAngles = angles / RACES;
		float shift = segAngles / 2;

		if(yaw < 0) yaw = 180F + (180F + yaw);
		yaw -= 360F - base;
		float angle = 360F - yaw + shift;

		if(angle < 0) angle = 360F + angle;

		return angle;
	}

	public static boolean isFirstTick(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_FIRST_TICK, true);
	}

	public static void tickFirst(ItemStack stack) {
		ItemNBTHelper.setBoolean(stack, TAG_FIRST_TICK, false);
	}

	public static boolean wasEquipped(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_EQUIPPED, false);
	}

	public static void setEquipped(ItemStack stack, boolean equipped) {
		ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, equipped);
	}

	public static ModelHolder getModel(ItemStack stack, int seg) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_MODEL_PREFIX + seg, true);
		if(cmp == null) return FALLBACK_MODEL;
		return new ModelHolder(cmp.getInteger(TAG_ROTATION), cmp.getBoolean(TAG_GENDER));
	}
	
	public static void setModel(ItemStack stack, int seg, int rot, boolean gen) {
		NBTTagCompound cmp = new NBTTagCompound();
		cmp.setInteger(TAG_ROTATION, rot);
		cmp.setBoolean(TAG_GENDER, gen);
		ItemNBTHelper.setCompound(stack, TAG_MODEL_PREFIX + seg, cmp);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null && stack.getItem() == this) render(stack, player, event.partialTicks);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onDrawScreenPost(RenderGameOverlayEvent.Post e) {
		if(e.type != ElementType.ALL) return;
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null && stack.getItem() == this) renderHUD(e.resolution, player, stack);
	}
	
	@SideOnly(Side.CLIENT)
	public void render(ItemStack stack, EntityPlayer player, float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		Tessellator tess = Tessellator.instance;
		Tessellator.renderingWorldRenderer = false;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float alpha = ((float) Math.sin((ClientTickHandler.ticksInGame + partialTicks) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;

		double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		GL11.glTranslated(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);

		int angles = 360;
		int segAngles = angles / RACES;
		float shift = 360-segAngles / 2;

		float u = 1F;
		float v = 0.25F;

		float s = 3F;
		float m = 0.8F;
		float y = v * s * 2;
		float y0 = 0;

		int segmentLookedAt = getSegmentLookedAt(stack, player);

		for(int seg = 0; seg < RACES; seg++) {
			boolean inside = false;
			float rotationAngle = (seg + 0.5F) * segAngles + shift;
			if(segmentLookedAt == seg) inside = true;

			GL11.glPushMatrix();
			GL11.glRotatef(rotationAngle, 0F, 1F, 0F);
			GL11.glTranslatef(s * m, -0.75F, 0F);
			GL11.glColor4f(1, 1, 1, 1);
			
			ModelHolder pos = getModel(stack, seg);
			mc.renderEngine.bindTexture(pos.isMale ? LibResourceLocations.male[seg] : LibResourceLocations.female[seg]);
			GL11.glScaled(0.75, 0.75, 0.75);
			GL11.glRotated(180, 1, 0, 0);
			GL11.glRotated(90 + pos.rotation, 0, 1, 0);
			GL11.glTranslated(0, -0.75, 0);
			RenderTileFlugelHead.model.render(0.0625F);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glRotatef(180F, 1F, 0F, 0F);
			float a = alpha;
			if(inside) {
				a += 0.3F;
				y0 = -y;
			}

			EnumRace.glColor(seg + 1);

			mc.renderEngine.bindTexture(LibResourceLocations.glow);
			tess.startDrawingQuads();
			for(int i = 0; i < segAngles; i++) {
				float ang = i + seg * segAngles + shift;
				double xp = Math.cos(ang * Math.PI / 180F) * s;
				double zp = Math.sin(ang * Math.PI / 180F) * s;

				tess.addVertexWithUV(xp * m, y, zp * m, u, v);
				tess.addVertexWithUV(xp, y0, zp, u, 0);

				xp = Math.cos((ang + 1) * Math.PI / 180F) * s;
				zp = Math.sin((ang + 1) * Math.PI / 180F) * s;

				tess.addVertexWithUV(xp, y0, zp, 0, 0);
				tess.addVertexWithUV(xp * m, y, zp * m, 0, v);
			}
			y0 = 0;
			tess.draw();

			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void renderHUD(ScaledResolution resolution, EntityPlayer player, ItemStack stack) {
		Minecraft.getMinecraft();
		int seg = getSegmentLookedAt(stack, player);

		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		String s = StatCollector.translateToLocal("race." + EnumRace.values()[seg+1].toString() + ".name");
		font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 65, EnumRace.getRGBColor(seg+1));

		s = StatCollector.translateToLocal("item.HoloProjector.select");
		font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 + 45, 0xFFFFFF);
		s = StatCollector.translateToLocal("item.HoloProjector.switch");
		font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 + 55, 0xFFFFFF);
		s = StatCollector.translateToLocal("item.HoloProjector.rotate");
		font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 + 65, 0xFFFFFF);
	}

	private static class ModelHolder {

		public final int rotation;
		public final boolean isMale;

		public ModelHolder(int rot, boolean male) {
			rotation = rot;
			isMale = male;
		}
	}
}