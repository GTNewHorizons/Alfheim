package alfheim.common.core.asm;

import java.util.Map;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
	private static String TAG_TRANSFER_STACK = "transferStack";

	@Hook
	public static void updateEntity(TilePylon entity) {
		updatingEntity = entity.getWorldObj().isRemote;
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

	@Hook(returnCondition = ReturnCondition.ON_TRUE, targetMethod = "public static voidc_150000_e", isMandatory = true)
	public static boolean onNetherPortalActivation(BlockPortal portal, World world, int x, int y, int z) {
		return MinecraftForge.EVENT_BUS.post(new NetherPortalActivationEvent(world, x, y, z));
	}

	@Hook(returnCondition = ReturnCondition.ON_TRUE, booleanReturnConstant = false, isMandatory = true)
	public static boolean matches(RecipePureDaisy recipe, World world, int x, int y, int z, SubTileEntity pureDaisy, Block block, int meta) {
		return recipe.getOutput().equals(ModBlocks.livingwood) && world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim;
	}

	@Hook(returnCondition = ReturnCondition.ON_TRUE, isMandatory = true)
	public static boolean onItemUse(ItemFlugelEye eye, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (player.isSneaking() && world.getBlock(x, y, z) == Blocks.beacon) return EntityFlugel.spawn(player, stack, world, x, y, z, false);
		return false;
	}

	@Hook(returnCondition = ReturnCondition.ALWAYS, targetMethod = "toString")
	public static String NBTTagCompound_toString(NBTTagCompound nbt) {
		StringBuilder sb = new StringBuilder("{\n");
		for (Object o : nbt.tagMap.entrySet()) {
			Map.Entry e = (Map.Entry) o;
			if (e.getValue() instanceof NBTTagList || e.getValue() instanceof NBTTagCompound) {
				String[] arr = e.getValue().toString().split("\n");
				sb.append(" $key = ${arr.get(0)}\n");
				for (int i = 1; i < arr.length; i++) sb.append(" ${arr.get(i)}\n");
			} else sb.append("    ${key} = ${value}\n");
		}
		sb.append("}");
		return sb.toString();
	}

	@Hook(returnCondition = ReturnCondition.ALWAYS, targetMethod = "toString")
	public static String NBTTagList_toString(NBTTagList nbt) {
		StringBuilder sb = new StringBuilder("list [\n");
		for (Object obj : nbt.tagList) if (obj instanceof NBTTagList || obj instanceof NBTTagCompound) for (String s : obj.toString().split("\n")) sb.append("    $s\n"); else sb.append("$obj\n");
		sb.append("]");
		return sb.toString();
	}

	@SideOnly(Side.CLIENT)
	@Hook(injectOnExit = true)
	public static void renderManaBar(HUDHandler hh, int x, int y, int color, float alpha, int mana, int maxMana) {
		if(mana < 0) return;
		GL11.glPushMatrix();
		String text = mana + "/" + maxMana;
		int X = x + 51 - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2;
		int Y = y - 19;
		Minecraft.getMinecraft().fontRenderer.drawString(text, X, Y, color, true);
		GL11.glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Hook
	public static void doRenderShadowAndFire(Render render, Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
		if(entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getActivePotionEffect(AlfheimRegistry.soulburn) != null)  PotionSoulburn.renderEntityOnFire(render, entity, x, y, z, partialTickTime);
	}

	@SideOnly(Side.CLIENT)
	@Hook
	public static void renderOverlays(ItemRenderer renderer, float partialTicks) {
		if(Minecraft.getMinecraft().thePlayer.getActivePotionEffect(AlfheimRegistry.soulburn) != null) {
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			PotionSoulburn.renderFireInFirstPerson(partialTicks);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}
}