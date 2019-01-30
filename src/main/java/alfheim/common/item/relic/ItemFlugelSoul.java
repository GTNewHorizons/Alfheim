package alfheim.common.item.relic;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.lib.LibResourceLocations;
import alfheim.common.entity.boss.EntityFlugel;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileBrewery;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemRelic;

public class ItemFlugelSoul extends ItemRelic {
	
	private static final int SEGMENTS = 12;
	private static final MultiversePosition FALLBACK_POSITION = new MultiversePosition(0, -1, 0, 0);

	private static final String TAG_EQUIPPED = "equipped";
	private static final String TAG_ROTATION_BASE = "rotationBase";
	private static final String TAG_WARP_PREFIX = "warp";
	private static final String TAG_POS_X = "posX";
	private static final String TAG_POS_Y = "posY";
	private static final String TAG_POS_Z = "posZ";
	private static final String TAG_DIMENSION = "dim";
	private static final String TAG_FIRST_TICK = "firstTick";
	private static final String TAG_DISABLED = "disabled";
	private static final String TAG_BLOCKED = "blocked";
	
	// ItemFlightTiara
	private static final String TAG_TIME_LEFT = "timeLeft";
	private static final int MAX_FLY_TIME = 1200;

	
	IIcon[] signs;

	public ItemFlugelSoul() {
		super("FlugelSoul");
		setCreativeTab(AlfheimCore.alfheimTab);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		return Color.HSBtoRGB(Botania.proxy.getWorldElapsedTicks() * 2 % 360 / 360F, 0.75F, 1F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		signs = new IIcon[12];
		for(int i = 0; i < 12; i++) signs[i] = IconHelper.forName(par1IconRegister, "unused/sign" + i);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Block block = world.getBlock(x, y, z);
		if (block == Blocks.beacon) {
			if (player.isSneaking() && getBlocked(stack) < SEGMENTS) {
				boolean success = EntityFlugel.spawn(player, stack, world, x, y, z, true);
				if (success) setDisabled(stack, getBlocked(stack), true);
				return success;
			}
		} else if (block == ModBlocks.brewery) {
			TileBrewery brew = (TileBrewery) world.getTileEntity(x, y, z);
			brew.setInventorySlotContents(0, stack.splitStack(1));
		}
		return false;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(isRightPlayer(player, stack) && !player.isSneaking()) {
			int segment = getSegmentLookedAt(stack, player);
			MultiversePosition pos = getWarpPoint(stack, segment);
			if(pos.isValid()) {
				if (!world.isRemote && player instanceof EntityPlayerMP && (player.capabilities.isCreativeMode || ManaItemHandler.requestManaExact(stack, player, pos.mana(player), true))) {
					world.playSoundAtEntity(player, "mob.endermen.portal", 1F, 1F);
					ASJUtilities.sendToDimensionWithoutPortal(player, pos.dim, pos.x, pos.y, pos.z);
				}
			} else setWarpPoint(stack, segment, player.posX, player.posY, player.posZ, world.provider.dimensionId);
		}

		return stack;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase player, ItemStack stack) {
		if(player.isSneaking() && player instanceof EntityPlayer && isRightPlayer((EntityPlayer) player, stack)) {
			int segment = getSegmentLookedAt(stack, player);
			MultiversePosition pos = getWarpPoint(stack, segment);
			if(pos.isValid()) {
				setWarpPoint(stack, segment, 0, -1, 0, 0);
				return false;
			}
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
			int segAngles = angles / SEGMENTS;
			float shift = segAngles / 2;
			setRotationBase(stack, getCheckingAngle((EntityLivingBase) entity) - shift);
			if(firstTick) tickFirst(stack);
		}
		
		if (entity instanceof EntityPlayer) {
			ItemStack tiara = PlayerHandler.getPlayerBaubles((EntityPlayer) entity).getStackInSlot(0);
			if (tiara != null && tiara.getItem() instanceof ItemFlightTiara) 
				ItemNBTHelper.setInt(tiara, TAG_TIME_LEFT, MAX_FLY_TIME);
		}
	}

	private static int getSegmentLookedAt(ItemStack stack, EntityLivingBase player) {
		float yaw = getCheckingAngle(player, getRotationBase(stack));

		int angles = 360;
		int segAngles = angles / SEGMENTS;
		for(int seg = 0; seg < SEGMENTS; seg++) {
			float calcAngle = (float) seg * segAngles;
			if(yaw >= calcAngle && yaw < calcAngle + segAngles) return seg;
		}
		return 0;
	}

	public static float getCheckingAngle(EntityLivingBase player) {
		return getCheckingAngle(player, 0F);
	}

	// Screw the way minecraft handles rotation
	// Really...
	private static float getCheckingAngle(EntityLivingBase player, float base) {
		float yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw) + 90F;
		int angles = 360;
		int segAngles = angles / SEGMENTS;
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

	public static float getRotationBase(ItemStack stack) {
		return ItemNBTHelper.getFloat(stack, TAG_ROTATION_BASE, 0F);
	}

	public static void setRotationBase(ItemStack stack, float rotation) {
		ItemNBTHelper.setFloat(stack, TAG_ROTATION_BASE, rotation);
	}

	public static boolean isDisabled(ItemStack stack, int warp) {
		return ItemNBTHelper.getBoolean(stack, TAG_DISABLED + warp, false);
	}

	public static void setDisabled(ItemStack stack, int warp, boolean disable) {
		ItemNBTHelper.setBoolean(stack, TAG_DISABLED + (warp - (disable ? 0 : 1)), disable);
		stack.getTagCompound().tagMap.remove(TAG_WARP_PREFIX + warp);
		setBlocked(stack, getBlocked(stack) + (disable ? 1 : -1));
	}
	
	public static int getBlocked(ItemStack item) {
		return ItemNBTHelper.getInt(item, TAG_BLOCKED, 0);
	}
	
	public static void setBlocked(ItemStack item, int blocked) {
		ItemNBTHelper.setInt(item, TAG_BLOCKED, blocked);
	}

	public static void setWarpPoint(ItemStack stack, int warp, double x, double y, double z, int dim) {
		if (isDisabled(stack, warp)) return;
		NBTTagCompound cmp = new NBTTagCompound();
		cmp.setDouble(TAG_POS_X, x);
		cmp.setDouble(TAG_POS_Y, y);
		cmp.setDouble(TAG_POS_Z, z);
		cmp.setInteger(TAG_DIMENSION, dim);
		ItemNBTHelper.setCompound(stack, TAG_WARP_PREFIX + warp, cmp);
	}

	public static MultiversePosition getWarpPoint(ItemStack stack, int warp) {
		if (isDisabled(stack, warp)) return FALLBACK_POSITION; 
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_WARP_PREFIX + warp, true);
		if(cmp == null) return FALLBACK_POSITION;

		double x = cmp.getDouble(TAG_POS_X);
		double y = cmp.getDouble(TAG_POS_Y);
		double z = cmp.getDouble(TAG_POS_Z);
		int dim = cmp.getInteger(TAG_DIMENSION);
		return new MultiversePosition(x, y, z, dim);
	}

	public static ChunkCoordinates getFirstCoords(ItemStack stack) {
		MultiversePosition pos = getWarpPoint(stack, getBlocked(stack));
		return new ChunkCoordinates((int) pos.x, (int) pos.y, (int) pos.z);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null && stack.getItem() == this)
			render(stack, player, event.partialTicks);
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

		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		float alpha = ((float) Math.sin((ClientTickHandler.ticksInGame + partialTicks) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;

		double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		glTranslated(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);

		float base = getRotationBase(stack);
		int angles = 360;
		int segAngles = angles / SEGMENTS;
		float shift = base - segAngles / 2;

		float u = 1F;
		float v = 0.25F;

		float s = 3F;
		float m = 0.8F;
		float y = v * s * 2;
		float y0 = 0;

		int segmentLookedAt = getSegmentLookedAt(stack, player);

		for(int seg = 0; seg < SEGMENTS; seg++) {
			boolean inside = false;
			float rotationAngle = (seg + 0.5F) * segAngles + shift;
			if(segmentLookedAt == seg) inside = true;

			glPushMatrix();
			glRotatef(rotationAngle, 0F, 1F, 0F);
			glTranslatef(s * m, -0.75F, 0F);

			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
			glScalef(0.75F, 0.75F, 0.75F);
			glTranslatef(0F, 0F, 0.5F);
			IIcon icon = signs[seg];
			glRotatef(90F, 0F, 1F, 0F);
			glColor4f(1, !isDisabled(stack, seg) ? 1 : 0, !isDisabled(stack, seg) ? 1 : 0, getWarpPoint(stack, seg).isValid() && !isDisabled(stack, seg) ? 1 : 0.2F);
			float f = icon.getMinU();
			float f1 = icon.getMaxU();
			float f2 = icon.getMinV();
			float f3 = icon.getMaxV();
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);

			glColor3f(1F, 1F, 1F);
			glPopMatrix();

			glPushMatrix();
			glRotatef(180F, 1F, 0F, 0F);
			float a = alpha;
			if(inside) {
				a += 0.3F;
				y0 = -y;
			}

			float c = seg % 2 == 0 ? 0.6F : 1F;
			if(isDisabled(stack, seg)) glColor4f(c, 0, 0, a);
			else glColor4f(c, c, c, a);

			mc.renderEngine.bindTexture(isDisabled(stack, seg) ? LibResourceLocations.glow : LibResourceLocations.glowCyan);
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

			glPopMatrix();
		}
		glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void renderHUD(ScaledResolution resolution, EntityPlayer player, ItemStack stack) {
		Minecraft.getMinecraft();
		int slot = getSegmentLookedAt(stack, player);
		MultiversePosition pos = getWarpPoint(stack, slot);

		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		String s = StatCollector.translateToLocal("item.FlugelSoul.sign" + slot);
		font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 65, isDisabled(stack, slot) ? 0xAAAAAA : 0xFFD409);

		if(pos.isValid()) {
			int dist = MathHelper.floor_double(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(pos.x, pos.y, pos.z, player.posX, player.posY - 1.6, player.posZ));

			if (pos.dim != player.dimension) {
				s = String.format(StatCollector.translateToLocal("item.FlugelSoul.anotherDim"), pos.dim);
				font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 50, 0x9999FF);
			}
			s = dist == 1 ? StatCollector.translateToLocal("item.FlugelSoul.blockAway") : String.format(StatCollector.translateToLocal("item.FlugelSoul.blocksAway"), dist);
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 40, 0x9999FF);
			s = StatCollector.translateToLocal("item.FlugelSoul.clickToTeleport");
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 30, 0xFFFFFF);
			s = StatCollector.translateToLocal("item.FlugelSoul.clickToRemoveWarp");
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 20, 0xFFFFFF);
		} else if (isDisabled(stack, slot)) {
			s = StatCollector.translateToLocal("item.FlugelSoul.blockedWarp");
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 40, 0xAAAAAA);
		} else {
			s = StatCollector.translateToLocal("item.FlugelSoul.unboundWarp");
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 40, 0xFFFFFF);
			s = StatCollector.translateToLocal("item.FlugelSoul.clickToAddWarp");
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 30, 0xFFFFFF);
		}
	}

	private static class MultiversePosition {

		public final double x, y, z;
		public final int dim;

		public MultiversePosition(double x, double y, double z, int dim) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.dim = dim;
		}

		boolean isValid() {
			return y > 0 && (ASJUtilities.isServer() ? MinecraftServer.getServer().worldServerForDimension(dim) != null : true);
		}

		int mana(EntityPlayer player) {
			double mod = player.dimension != dim ? (player.worldObj.provider.getMovementFactor() / MinecraftServer.getServer().worldServerForDimension(dim).provider.getMovementFactor()) * 4.0 : 1.0;
			return MathHelper.floor_double(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(x, y, z, player.posX, player.posY - 1.6, player.posZ) * mod) * 10;
		}
	}
}