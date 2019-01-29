package alfheim.common.core.asm;

import static gloomyfolken.hooklib.asm.ReturnCondition.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.block.IHourglassTrigger;
import alfheim.api.event.LivingPotionEvent;
import alfheim.api.event.NetherPortalActivationEvent;
import alfheim.client.render.entity.RenderButterflies;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.entity.boss.EntityFlugel;
import alfheim.common.item.lens.LensMessanger;
import alfheim.common.item.lens.LensTripwire;
import alfheim.common.potion.PotionSoulburn;
import codechicken.nei.recipe.GuiRecipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gloomyfolken.hooklib.asm.Hook;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockPylon;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom;
import vazkii.botania.common.block.tile.TileHourglass;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.proxy.CommonProxy;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ItemGaiaHead;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.item.relic.ItemFlugelEye;
import vazkii.botania.common.lib.LibBlockNames;

public class AlfheimHookHandler {

	private static boolean updatingTile = false, updatingEntity = false;
	private static String TAG_TRANSFER_STACK = "transferStack";
	public static boolean numMana = true;
	
	@Hook(injectOnExit = true, isMandatory = true)
	public static void onNewPotionEffect(EntityLivingBase e, PotionEffect pe) {
		MinecraftForge.EVENT_BUS.post(new LivingPotionEvent.Add.Post(e, pe));
	}
	
	@Hook(injectOnExit = true, isMandatory = true)
	public static void onChangedPotionEffect(EntityLivingBase e, PotionEffect pe, boolean was) {
		MinecraftForge.EVENT_BUS.post(new LivingPotionEvent.Change.Post(e, pe));
	}
	
	@Hook(injectOnExit = true, isMandatory = true)
	public static void onFinishedPotionEffect(EntityLivingBase e, PotionEffect pe) {
		MinecraftForge.EVENT_BUS.post(new LivingPotionEvent.Remove.Post(e, pe));
	}
	
	@Hook(returnCondition = ON_TRUE)
	public static boolean requestManaExact(ManaItemHandler handler, ItemStack stack, EntityPlayer player, int manaToGet, boolean remove) {
		return player.capabilities.isCreativeMode;
	}
	
	@Hook(returnCondition = ON_TRUE, returnType = "int", returnAnotherMethod = "requestManaChecked")
	public static boolean requestMana(ManaItemHandler handler, ItemStack stack, EntityPlayer player, int manaToGet, boolean remove) {
		return player.capabilities.isCreativeMode;
	}
	
	public static int requestManaChecked(ManaItemHandler handler, ItemStack stack, EntityPlayer player, int manaToGet, boolean remove) {
		return manaToGet;
	}
	
	@Hook(injectOnExit = true, isMandatory = true)
	public static void moveFlying(Entity e, float x, float y, float z) {
		if (AlfheimCore.enableMMO && e instanceof EntityLivingBase && ((EntityLivingBase) e).isPotionActive(AlfheimRegistry.leftFlame)) {
			e.motionX = e.motionY = e.motionZ = 0.0D;
		}
	}
	
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	public static void updatePotionEffects(EntityLivingBase e) {
		try {
	        Iterator iterator = e.activePotionsMap.keySet().iterator();
	
	        while (iterator.hasNext()) {
	            Integer integer = (Integer)iterator.next();
	            PotionEffect potioneffect = (PotionEffect) e.activePotionsMap.get(integer);
	
	            if (!potioneffect.onUpdate(e)) {
	                //if (!e.worldObj.isRemote) {
	                    iterator.remove();
	                    AlfheimSyntheticMethods.onFinishedPotionEffect(e, potioneffect);
	                //}
	            } else if (potioneffect.getDuration() % 600 == 0) {
	                AlfheimSyntheticMethods.onChangedPotionEffect(e, potioneffect, false);
	            }
	        }
	
	        int i;
	
	        if (e.potionsNeedUpdate) {
	            if (!e.worldObj.isRemote) {
	                if (e.activePotionsMap.isEmpty()) {
	                    e.getDataWatcher().updateObject(8, Byte.valueOf((byte)0));
	                    e.getDataWatcher().updateObject(7, Integer.valueOf(0));
	                    e.setInvisible(false);
	                } else {
	                    i = PotionHelper.calcPotionLiquidColor(e.activePotionsMap.values());
	                    e.getDataWatcher().updateObject(8, Byte.valueOf((byte)(PotionHelper.func_82817_b(e.activePotionsMap.values()) ? 1 : 0)));
	                    e.getDataWatcher().updateObject(7, Integer.valueOf(i));
	                    e.setInvisible(e.isPotionActive(Potion.invisibility.id));
	                }
	            }
	
	            e.potionsNeedUpdate = false;
	        }
	
	        i = e.getDataWatcher().getWatchableObjectInt(7);
	        boolean flag1 = e.getDataWatcher().getWatchableObjectByte(8) > 0;
	
	        if (i > 0) {
	            boolean flag = false;
	
	            if (!e.isInvisible()) {
	                flag = e.worldObj.rand.nextBoolean();
	            } else {
	                flag = e.worldObj.rand.nextInt(15) == 0;
	            }
	
	            if (flag1) {
	                flag &= e.worldObj.rand.nextInt(5) == 0;
	            }
	
	            if (flag && i > 0) {
	                double d0 = (double)(i >> 16 & 255) / 255.0D;
	                double d1 = (double)(i >> 8 & 255) / 255.0D;
	                double d2 = (double)(i >> 0 & 255) / 255.0D;
	                e.worldObj.spawnParticle(flag1 ? "mobSpellAmbient" : "mobSpell", e.posX + (e.worldObj.rand.nextDouble() - 0.5D) * (double)e.width, e.posY + e.worldObj.rand.nextDouble() * (double)e.height - (double)e.yOffset, e.posZ + (e.worldObj.rand.nextDouble() - 0.5D) * (double)e.width, d0, d1, d2);
	            }
	        }
		} catch (ConcurrentModificationException ex) {
			ASJUtilities.log("Well, that was expected. Ignore.");
			ex.printStackTrace();
		}
    }
	
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	public static float getHealth(EntityLivingBase e) {
		if (AlfheimCore.enableMMO && AlfheimRegistry.leftFlame != null && e.isPotionActive(AlfheimRegistry.leftFlame)) return 0.000000000000000000000000000000000000000000001F;
		else return e.getDataWatcher().getWatchableObjectFloat(6);
	}
	
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	public static float getMaxHealth(EntityLivingBase e) {
		if (AlfheimCore.enableMMO && AlfheimRegistry.leftFlame != null && e.isPotionActive(AlfheimRegistry.leftFlame)) return 0.0F;
		else return (float) e.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
	}
	
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	public static void setHealth(EntityLivingBase e, float hp) {
		if (!AlfheimCore.enableMMO) {
			e.getDataWatcher().updateObject(6, Float.valueOf(MathHelper.clamp_float(hp, 0.0F, e.getMaxHealth())));
			return;
		}
		
		if (AlfheimRegistry.leftFlame != null && e.isPotionActive(AlfheimRegistry.leftFlame)) {
			hp = 0.000000000000000000000000000000000000000000001F;
		}
		
		if (AlfheimRegistry.sharedHP != null && !e.isPotionActive(AlfheimRegistry.sharedHP)) {
			e.getDataWatcher().updateObject(6, Float.valueOf(MathHelper.clamp_float(hp, 0.0F, e.getMaxHealth())));
			return;
		}
		
		Party pt = PartySystem.getMobParty(e);
		if (pt == null) {
			e.getDataWatcher().updateObject(6, Float.valueOf(MathHelper.clamp_float(hp, 0.0F, e.getMaxHealth())));
			return;
		}
		
		EntityLivingBase[] mr = new EntityLivingBase[pt.count];
		for (int i = 0; i < pt.count; i++) mr[i] = pt.get(i);
		
		for (int i = 0; i < mr.length; i++) {
			if (mr[i] != null) {
				mr[i].getDataWatcher().updateObject(6, Float.valueOf(MathHelper.clamp_float(hp, 0.0F, mr[i].getMaxHealth())));
				if (hp < 0.0F) mr[i].onDeath(DamageSource.outOfWorld);
			}
		}
	}
	
	@Hook
	public static void onLivingUpdate(EntityDoppleganger e) {
		updatingEntity = true;
	}
	
	@Hook(injectOnExit = true, targetMethod = "onLivingUpdate")
	public static void onLivingUpdatePost(EntityDoppleganger e) {
		updatingEntity = false;
	}
	
	public static float rt = 0, gt = 0, bt = 0;
	
	@Hook(returnCondition = ALWAYS)
	public static void wispFX(CommonProxy proxy, World world, double x, double y, double z, float r, float g, float b, float size, float gravity) {
		if (updatingEntity) {
			r = rt = (float) Math.random() * 0.3F;
			g = gt = 0.7F + (float) Math.random() * 0.3F;
			b = bt = 0.7F + (float) Math.random() * 0.3F;
		}
		Botania.proxy.wispFX(world, x, y, z, r, g, b, size, gravity, 1F);
	}
	
	@Hook(returnCondition = ALWAYS)
	public static void wispFX(CommonProxy proxy, World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz) {
		if (updatingEntity && size == 0.4F) {
			r = rt;
			g = gt;
			b = bt;
		}
		Botania.proxy.wispFX(world, x, y, z, r, g, b, size, motionx, motiony, motionz, 1F);
	}
	
	@Hook(injectOnExit = true, targetMethod = "updateEntity")
	public static void TileHourglass$updateEntity(TileHourglass hourglass) {
		if (hourglass.blockMetadata == 1 && hourglass.flipTicks == 3) {
			Block block;
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				block = hourglass.getWorldObj().getBlock(hourglass.xCoord + dir.offsetX, hourglass.yCoord + dir.offsetY, hourglass.zCoord + dir.offsetZ);
				if (block instanceof IHourglassTrigger)
					((IHourglassTrigger) block).onTriggeredByHourglass(hourglass.getWorldObj(), hourglass.xCoord + dir.offsetX, hourglass.yCoord + dir.offsetY, hourglass.zCoord + dir.offsetZ, hourglass);
			}
		}
	}
	
	@Hook(targetMethod = "updateEntity")
	public static void TilePylon$updateEntity(TilePylon entity) {
		updatingTile = entity.getWorldObj().isRemote;
	}
	
	@Hook(injectOnExit = true, targetMethod = "updateEntity")
	public static void TilePylon$updateEntityPost(TilePylon entity) {
		if (entity.getWorldObj().isRemote) {
			updatingTile = false;
			if (entity.getWorldObj().rand.nextBoolean()) {
				int meta = entity.getBlockMetadata();
				Botania.proxy.sparkleFX(entity.getWorldObj(), entity.xCoord + Math.random(), entity.yCoord + Math.random() * 1.5, entity.zCoord + Math.random(), meta == 2 ? 0F : 0.5F, meta == 0 ? 0.5F : 1F, meta == 1 ? 0.5F : 1F, (float) Math.random(), 2);
			}
		}
	}
	
	@Hook(returnCondition = ON_TRUE)
	public static boolean sparkleFX(ClientProxy proxy, World world, double x, double y, double z, float r, float g, float b, float size, int m, boolean fake) {
		return updatingTile;
	}

	@Hook(returnCondition = ALWAYS)
	public static void getSubBlocks(BlockSpecialFlower flower, Item item, CreativeTabs tab, List list) {
		for(String s : BotaniaAPI.subtilesForCreativeMenu) {
			list.add(ItemBlockSpecialFlower.ofType(s));
			if(BotaniaAPI.miniFlowers.containsKey(s))
				list.add(ItemBlockSpecialFlower.ofType(BotaniaAPI.miniFlowers.get(s)));
			if (s.equals(LibBlockNames.SUBTILE_DAYBLOOM))
				list.add(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DAYBLOOM_PRIME));
			if (s.equals(LibBlockNames.SUBTILE_NIGHTSHADE))
				list.add(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_NIGHTSHADE_PRIME));
		}
	}
	
	public static final int MESSANGER = 22, TRIPWIRE = 23;
	
	@Hook(injectOnExit = true, isMandatory = true, targetMethod = "<clinit>")
	public static void ItemLens$clinit(ItemLens lens) {
		lens.setProps(MESSANGER, 1);
		lens.setProps(TRIPWIRE, 1 << 5);

		lens.setLens(MESSANGER, new LensMessanger());
		lens.setLens(TRIPWIRE, new LensTripwire());
	}
	
	@Hook(injectOnExit = true)
	public static void displayAllReleventItems(BotaniaCreativeTab tab, List list) {
		AlfheimItems.thinkingHand.getSubItems(AlfheimItems.thinkingHand, tab, list);
	}
	
	@Hook
	public static void onBlockPlacedBy(SubTileEntity subtile, World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		if (subtile instanceof SubTileDaybloom && ((SubTileDaybloom) subtile).isPrime()) ((SubTileDaybloom) subtile).setPrimusPosition();
	}
	
	@Hook
	public static void onBlockAdded(SubTileEntity subtile, World world, int x, int y, int z) {
		if (subtile instanceof SubTileDaybloom && ((SubTileDaybloom) subtile).isPrime()) ((SubTileDaybloom) subtile).setPrimusPosition();
	}

	@Hook(returnCondition = ALWAYS)
	public static IIcon getIcon(BlockPylon pylon, int side, int meta) {
		return meta == 0 || meta == 1 ? ModBlocks.storage.getIcon(side, meta) : Blocks.diamond_block.getIcon(side, 0);
	}

	@Hook(returnCondition = ON_TRUE, targetMethod = "func_150000_e", isMandatory = true)
	public static boolean onNetherPortalActivation(BlockPortal portal, World world, int x, int y, int z) {
		return MinecraftForge.EVENT_BUS.post(new NetherPortalActivationEvent(world, x, y, z));
	}

	@Hook(returnCondition = ON_TRUE, booleanReturnConstant = false, isMandatory = true)
	public static boolean matches(RecipePureDaisy recipe, World world, int x, int y, int z, SubTileEntity pureDaisy, Block block, int meta) {
		return recipe.getOutput().equals(ModBlocks.livingwood) && world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim;
	}

	@Hook(returnCondition = ON_TRUE, isMandatory = true)
	public static boolean onItemUse(ItemFlugelEye eye, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (player.isSneaking() && world.getBlock(x, y, z) == Blocks.beacon) return EntityFlugel.spawn(player, stack, world, x, y, z, false);
		return false;
	}
	
	@Hook(returnCondition = ON_TRUE, booleanReturnConstant = false)
	public static boolean spawn(EntityDoppleganger gaia, EntityPlayer player, ItemStack stack, World world, int x, int y, int z, boolean hard) {
		for (int i = -1; i < 2; i++) 
			for (int k = -1; k < 2; k++) 
				if (!world.getBlock(x + i, y - 1, z + k).isBeaconBase(world, x + i, y - 1, z + k, x, y, z)) {
					if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.inactive");
					return true;
				}
		return false;
	}

	@Hook(createMethod = true)
	public static ItemStack onItemRightClick(ItemGaiaHead item, ItemStack stack, World world, EntityPlayer player) {
		if (player.getCurrentArmor(3) == null) player.setCurrentItemOrArmor(4, stack.splitStack(1)); 
		return stack;
	}
	
	@Hook(isMandatory = true, returnCondition = ALWAYS)
	public static int getFortuneModifier(EnchantmentHelper h, EntityLivingBase e) {
		return EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, e.getHeldItem()) + (AlfheimCore.enableMMO && e.isPotionActive(AlfheimRegistry.goldRush) ? 2 : 0);
	}
	
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	public static boolean extinguishFire(World world, EntityPlayer player, int x, int y, int z, int side) {
        if (side == 0) --y;
        if (side == 1) ++y;
        if (side == 2) --z;
        if (side == 3) ++z;
        if (side == 4) --x;
        if (side == 5) ++x;
        Block b = world.getBlock(x, y, z);
        
        boolean f = b.getPlayerRelativeBlockHardness(player, world, x, y, z) > 0.0F;
        
        if (player != null) f = f || player.capabilities.isCreativeMode;
        if (b.getMaterial() == Material.fire && f) {
        	world.playAuxSFXAtEntity(player, 1004, x, y, z, 0);
        	world.setBlockToAir(x, y, z);
            return true;
        }
        return false;
    }

	@SideOnly(Side.CLIENT)
	@Hook(injectOnExit = true, isMandatory = true)
	public static void renderManaBar(HUDHandler hh, int x, int y, int color, float alpha, int mana, int maxMana) {
		if (mana < 0 || !AlfheimConfig.numericalMana || !numMana) return;
		glPushMatrix();
		boolean f = Minecraft.getMinecraft().currentScreen == null;
		boolean f1 = !f && Minecraft.getMinecraft().currentScreen instanceof GuiRecipe;
		String text = mana + "/" + maxMana;
		int X = x + 51 - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2;
		int Y = f1 ? y - 9 : y - 19;
		Minecraft.getMinecraft().fontRenderer.drawString(text, X, Y, color, f);
		glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Hook(isMandatory = true)
	public static void doRenderShadowAndFire(Render render, Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
		if (AlfheimCore.enableMMO) if(entity instanceof EntityLivingBase) if (((EntityLivingBase) entity).isPotionActive(AlfheimRegistry.butterShield)) RenderButterflies.render(render, entity, x, y, z, Minecraft.getMinecraft().timer.renderPartialTicks);
	}

	@SideOnly(Side.CLIENT)
	@Hook(isMandatory = true)
	public static void renderOverlays(ItemRenderer renderer, float partialTicks) {
		if(Minecraft.getMinecraft().thePlayer.isPotionActive(AlfheimRegistry.soulburn)) {
			glDisable(GL_ALPHA_TEST);
			PotionSoulburn.renderFireInFirstPerson(partialTicks);
			glEnable(GL_ALPHA_TEST);
		}
	}
}