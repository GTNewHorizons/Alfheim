package alfheim.common.block;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.lexicon.AlfheimLexiconData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockPowerStone extends Block implements ILexiconable {
	
	public static IIcon[] icons = new IIcon[5];
	
	public BlockPowerStone() {
		super(Material.rock);
		setBlockName("PowerStone");
		setBlockTextureName(ModInfo.MODID + ":ManaInfuserBottomDark");
		setHardness(2);
		setResistance(6000);
		setStepSound(soundTypeStone);
	}
	
	public int damageDropped(int meta) {
        return meta;
    }
	
	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta < 0 || icons.length <= meta) meta = 0;
		
		return side == 1 ? icons[meta] : icons[0];
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		super.registerBlockIcons(reg);
		icons[0] = blockIcon;
		for (int i = 1; i < icons.length; i++)
			icons[i] = reg.registerIcon(ModInfo.MODID + ":PowerStone" + i);
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List subs) {
		subs.add(new ItemStack(item, 1, 1)); // berserk
		subs.add(new ItemStack(item, 1, 2)); // overmage
		subs.add(new ItemStack(item, 1, 3)); // tank
		subs.add(new ItemStack(item, 1, 4)); // ninja
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		switch (world.getBlockMetadata(x, y, z)) {
			case 1: return makePlayerBerserk(player);
			case 2: return makePlayerOvermage(player);
			case 3: return makePlayerTank(player);
			case 4: return makePlayerNinja(player);
			default: return false;
		}
	}	
	
	// +20% DMG, -20% HP
	private boolean makePlayerBerserk(EntityPlayer player) {
		if (!player.isPotionActive(AlfheimRegistry.overmage) && !player.isPotionActive(AlfheimRegistry.tank) && !player.isPotionActive(AlfheimRegistry.ninja)) {
			player.addPotionEffect(new PotionEffect(AlfheimRegistry.berserk.id, 72000, 0));
			return true;
		}
		return false;
	}
	
	// +20% Spell DMG, +20% Spell Cost
	private boolean makePlayerOvermage(EntityPlayer player) {
		if (!player.isPotionActive(AlfheimRegistry.berserk) && !player.isPotionActive(AlfheimRegistry.tank) && !player.isPotionActive(AlfheimRegistry.ninja)) {
			player.addPotionEffect(new PotionEffect(AlfheimRegistry.overmage.id, 72000, 0));
			return true;
		}
		return false;
	}
	
	// +20% Resistance, -20% Speed
	private boolean makePlayerTank(EntityPlayer player) {
		if (!player.isPotionActive(AlfheimRegistry.overmage) && !player.isPotionActive(AlfheimRegistry.berserk) && !player.isPotionActive(AlfheimRegistry.ninja)) {
			player.addPotionEffect(new PotionEffect(AlfheimRegistry.tank.id, 72000, 0));
			return true;
		}
		return false;
	}
	
	// +20% Speed, -20% DMG
	private boolean makePlayerNinja(EntityPlayer player) {
		if (!player.isPotionActive(AlfheimRegistry.overmage) && !player.isPotionActive(AlfheimRegistry.tank) && !player.isPotionActive(AlfheimRegistry.berserk)) {
			player.addPotionEffect(new PotionEffect(AlfheimRegistry.ninja.id, 72000, 0));
			return true;
		}
		return false;
	}
	
	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.shrines;
	}
}