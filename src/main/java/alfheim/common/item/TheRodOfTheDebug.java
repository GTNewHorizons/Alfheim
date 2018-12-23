package alfheim.common.item;

import java.util.List;
import java.util.UUID;

import aaa.alexsocol.glsltoimage.Main;
import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.render.ASJShaderHelper;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.entity.EnumRace;
import alfheim.client.core.handler.CardinalSystemClient;
import alfheim.client.core.handler.CardinalSystemClient.TargetingSystemClient;
import alfheim.client.gui.GUIParty;
import alfheim.client.render.world.SpellVisualizations;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem;
import alfheim.common.core.handler.CardinalSystem.TimeStopSystem;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimBotaniaModifiers;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.lexicon.AlfheimLexiconData;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

public class TheRodOfTheDebug extends Item {

	public TheRodOfTheDebug() {
		setCreativeTab(AlfheimCore.alfheimTab);
		setMaxStackSize(1);
		setTextureName(ModInfo.MODID + ":TheRodOfTheDebug");
		setUnlocalizedName("TheRodOfTheDebug");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		try {
			if (true/*!world.isRemote*/) {
				if (!player.isSneaking()) {
					if (!world.isRemote) {
//						PartySystem.setParty(player, new Party(player));
						PartySystem.getParty(player).add(TargetingSystem.getTarget(player).target);
					}
					
//					for (Object o : world.loadedEntityList) if (o instanceof Entity && !(o instanceof EntityPlayer)) ((Entity) o).setDead();
					
//					int r = 32;
//					for (int x = -r; x < r; x++) {
//						for (int z = -r; z < r; z++) {
//							for (int y = 1; y < 4; y++) {
//								world.setBlock(x, y, z, Blocks.grass);
//							}
//						}
//					}
					
//					ASJUtilities.sendToDimensionWithoutPortal(player, 0, player.posX, 228, player.posZ);
				} else {
					EnumRace.setRaceID(player, (EnumRace.getRace(player).ordinal() + 1) % 11);
//					ASJUtilities.chatLog(EnumRace.getRace(player).ordinal() + " - " + EnumRace.getRace(player).toString(), player);
				}
			}
			return stack;
		} catch (Throwable e) {
			ASJUtilities.log("Oops!");
			return stack;
		}
	}
}