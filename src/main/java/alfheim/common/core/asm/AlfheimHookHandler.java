package alfheim.common.core.asm;

import org.lwjgl.opengl.GL11;

import alfheim.api.event.NetherPortalActivationEvent;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.entity.boss.EntityFlugel;
import alfheim.common.potion.PotionSoulburn;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockPylon;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.item.relic.ItemFlugelEye;

public class AlfheimHookHandler {

	private static boolean updatingEntity = false;
	private static final String TAG_TRANSFER_STACK = "transferStack";
	
	@Hook
	public static void updateEntity(TilePylon entity) {
		if (entity.getWorldObj().isRemote) updatingEntity = true;
	}

	@Hook(injectOnExit = true, targetMethod = "updateEntity")
	public static void updateEntityPost(TilePylon entity) {
		if (entity.getWorldObj().isRemote) {
			updatingEntity = false;
			if (entity.getWorldObj().rand.nextBoolean()) {
				int meta = entity.getBlockMetadata();
				Botania.proxy.sparkleFX(entity.getWorldObj(), entity.xCoord + Math.random(), entity.yCoord + Math.random() * 1.5, entity.zCoord + Math.random(), meta == 2 ? 0F : 0.5F, meta == 0 ? 0.5F : 1F, meta == 1 ? 0.5F : 1F, (float) Math.random(), 2);
			}
		}
	}

	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	public static boolean sparkleFX(ClientProxy proxy, World world, double x, double y, double z, float r, float g, float b, float size, int m, boolean fake) {
		return updatingEntity;
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	public static IIcon getIcon(BlockPylon pylon, int side, int meta) {
		return meta == 0 || meta == 1 ? ModBlocks.storage.getIcon(side, meta) : Blocks.diamond_block.getIcon(side, 0);
	}
	
	@Hook(returnCondition = ReturnCondition.ON_TRUE, targetMethod = "func_150000_e", isMandatory = true)
	public static boolean onNetherPortalActivation(BlockPortal portal, World world, int x, int y, int z) {
		return MinecraftForge.EVENT_BUS.post(new NetherPortalActivationEvent(world, x, y, z));
	}
	
	@Hook(returnCondition = ReturnCondition.ON_TRUE, booleanReturnConstant = false, isMandatory = true)
	public static boolean matches(RecipePureDaisy recipe, World world, int x, int y, int z, SubTileEntity pureDaisy, Block block, int meta) {
		return recipe.getOutput().equals(ModBlocks.livingwood) && world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim;
	}
	
	@Hook(returnCondition = ReturnCondition.ON_TRUE, isMandatory = true)
	public static boolean onItemUse(ItemFlugelEye eye, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(player.isSneaking() && world.getBlock(x, y, z) == Blocks.beacon) {
			return EntityFlugel.spawn(player, stack, world, x, y, z);
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Hook(injectOnExit = true)
	public static void renderManaBar(HUDHandler hh, int x, int y, int color, float alpha, int mana, int maxMana) {
		if (mana < 0) return;
		GL11.glPushMatrix();
		String text = mana + "/" + maxMana;
		x = x + 51 - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2;
		y -= 19;

		Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, color, true);
		GL11.glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Hook
	public static void doRenderShadowAndFire(Render render, Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getActivePotionEffect(AlfheimRegistry.soulburn) != null) PotionSoulburn.renderEntityOnFire(render, entity, x, y, z, partialTicks);
	}

	@SideOnly(Side.CLIENT)
	@Hook
	public static void renderOverlays(ItemRenderer renderer, float partialTicks) {
		if (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(AlfheimRegistry.soulburn) != null) {
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			PotionSoulburn.renderFireInFirstPerson(partialTicks);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}
}