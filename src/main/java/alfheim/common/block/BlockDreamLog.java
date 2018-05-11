package alfheim.common.block;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.lexicon.AlfheimLexiconData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockDreamLog extends BlockLog implements ILexiconable {

	public IIcon[] textures = new IIcon[2]; 
	
	public BlockDreamLog() {
		super();
		this.setBlockName("DreamLog");
		this.setCreativeTab(AlfheimCore.alfheimTab);
	}

	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(ModInfo.MODID + ":DreamLogTop");
		textures[1] = reg.registerIcon(ModInfo.MODID + ":DreamLogSide");
    }
	
	@SideOnly(Side.CLIENT)
	protected IIcon getSideIcon(int meta) {
		return textures[1];
	}

	@SideOnly(Side.CLIENT)
	protected IIcon getTopIcon(int meta) {
		return textures[0];
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.worldgen;
	}
}
