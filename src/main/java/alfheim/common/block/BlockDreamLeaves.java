package alfheim.common.block;

import java.util.Random;

import alexsocol.asjlib.render.IGlowingLayerBlock;
import alexsocol.asjlib.render.RenderGlowingLayerBlock;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.handler.CardinalSystem;
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem;
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.lexicon.AlfheimLexiconData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.ModItems;

public class BlockDreamLeaves extends BlockLeaves implements IGlowingLayerBlock, ILexiconable {

	public IIcon[] textures = new IIcon[3];
	
	public BlockDreamLeaves() {
		super();
		this.setBlockName("DreamLeaves");
		this.setBlockTextureName(ModInfo.MODID + ":DreamLeaves");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setLightOpacity(0);
	}

	@Override
	// IDK whether this is good source of glowstone or not
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ModItems.manaResource && player.getCurrentEquippedItem().getItemDamage() == 9) {
			int eat = 2;
			final int c = eat;
			boolean[] sides = new boolean[6];
			
			for (ForgeDirection dir : ForgeDirection.values()) {
				if (dir == ForgeDirection.UNKNOWN || eat <= 0) continue;
				if (world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == this) {
					--eat;
					sides[dir.ordinal()] = true;
				}
			}
			
			if (eat > 0) return false;
			for (ForgeDirection dir : ForgeDirection.values()) {
				if (dir == ForgeDirection.UNKNOWN) continue;
				if (sides[dir.ordinal()]) world.setBlockToAir(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
			}
			
			world.setBlockToAir(x, y, z);
				
			player.getCurrentEquippedItem().stackSize--;
			if (!player.inventory.addItemStackToInventory(new ItemStack(Items.glowstone_dust))) {
				player.dropPlayerItemWithRandomChoice(new ItemStack(Items.glowstone_dust), true);
			}
			
			if (!world.isRemote && player instanceof EntityPlayerMP) KnowledgeSystem.learn((EntityPlayerMP) player, Knowledge.GLOWSTONE);
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isLeaves(IBlockAccess world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return Blocks.leaves.isOpaqueCube();
	}
	
	@Override
	public Item getItemDropped(int meta, Random rand, int p_149650_3_) {
		return Item.getItemFromBlock(AlfheimBlocks.dreamSapling);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta) {
		return Integer.parseInt("E5FFF9", 16);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		return Integer.parseInt("E5FFF9", 16);
	}
	
	@Override
	public void func_150124_c(World world, int x, int y, int z, int meta, int chance) {}

	@Override
	protected int func_150123_b(int meta) {
		return 100;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return textures[Blocks.leaves.isOpaqueCube() ? 1 : 0];
	}

	@Override
	public IIcon getGlowIcon(int side, int meta) {
		return textures[2];
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(ModInfo.MODID + ":DreamLeaves");
		textures[1] = reg.registerIcon(ModInfo.MODID + ":DreamLeavesOpaque");
		textures[2] = reg.registerIcon(ModInfo.MODID + ":DreamSparks");
	}
	
	public int getRenderType() {
		return RenderGlowingLayerBlock.glowBlockID;
	}
	
	@Override
	public String[] func_150125_e() {
		return new String[] { "dream" };
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.worldgen;
	}
}