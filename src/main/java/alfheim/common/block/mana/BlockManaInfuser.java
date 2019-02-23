package alfheim.common.block.mana;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.block.tile.TileManaInfuser;
import alfheim.common.core.registry.AlfheimAchievements;
import alfheim.common.lexicon.AlfheimLexiconData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;

public class BlockManaInfuser extends BlockContainer implements ILexiconable, IWandHUD, IWandable {

	public static IIcon[] textures = new IIcon[7];
	
	public BlockManaInfuser() {
		super(Material.rock);
		this.setBlockName("ManaInfuser");
		this.setBlockTextureName(ModInfo.MODID + ":ManaInfuser");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHardness(3);
		this.setHarvestLevel("pickaxe", 1);
		this.setResistance(60);
		this.setStepSound(soundTypeStone);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileManaInfuser();
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(this.getTextureName() + "Bottom");
		textures[1] = reg.registerIcon(this.getTextureName() + "Top");
		textures[2] = reg.registerIcon(this.getTextureName() + "Top_Active");
		textures[3] = reg.registerIcon(this.getTextureName() + "Side");
		textures[4] = reg.registerIcon(this.getTextureName() + "BottomDark");
		textures[5] = reg.registerIcon(this.getTextureName() + "TopDark");
		textures[6] = reg.registerIcon(this.getTextureName() + "SideDark");
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 0 ? (meta == 2 ? textures[4] : textures[0]) : side == 1 ? ((meta == 2 ? textures[5] : meta == 1 ? textures[2] : textures[1])) : (meta == 2 ? textures[6] : textures[3]);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (ModInfo.DEV && !world.isRemote && player.isSneaking()) {
			player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Mana: " + ((TileManaInfuser) world.getTileEntity(x, y, z)).getCurrentMana()));
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, placer, stack);
		if (placer instanceof EntityPlayer) {
			if (
					world.getBlock(x + 1, y, z	) == ModFluffBlocks.elfQuartz &&
					world.getBlock(x - 1, y, z	) == ModFluffBlocks.elfQuartz &&
					world.getBlock(x	, y, z+1) == ModFluffBlocks.elfQuartz &&
					world.getBlock(x	, y, z-1) == ModFluffBlocks.elfQuartz &&
					world.getBlock(x + 1, y, z+1) == ModBlocks.storage && world.getBlockMetadata(x+1, y, z+1) == 2 &&
					world.getBlock(x + 1, y, z-1) == ModBlocks.storage && world.getBlockMetadata(x+1, y, z-1) == 2 &&
					world.getBlock(x - 1, y, z+1) == ModBlocks.storage && world.getBlockMetadata(x-1, y, z+1) == 2 &&
					world.getBlock(x - 1, y, z-1) == ModBlocks.storage && world.getBlockMetadata(x-1, y, z-1) == 2
				)
				
			((EntityPlayer) placer).triggerAchievement(AlfheimAchievements.infuser);
		}
	}
	
	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.infuser;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TileManaInfuser) world.getTileEntity(x, y, z)).renderHUD(mc, res);
	}
	
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TileManaInfuser) world.getTileEntity(x, y, z)).onWanded(player, stack);
		return true;
	}

}