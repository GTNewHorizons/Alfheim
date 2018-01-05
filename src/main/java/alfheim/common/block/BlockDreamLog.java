package alfheim.common.block;

import alfheim.AlfheimCore;
import alfheim.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockDreamLog extends BlockLog {

	public IIcon[] textures = new IIcon[2]; 
	
	public BlockDreamLog() {
		super();
		this.setBlockName("DreamLog");
		this.setCreativeTab(AlfheimCore.alfheimTab);
	}

	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(Constants.MODID + ":DreamLogTop");
		textures[1] = reg.registerIcon(Constants.MODID + ":DreamLogSide");
    }
	
	@SideOnly(Side.CLIENT)
	protected IIcon getSideIcon(int meta) {
		return textures[1];
	}

	@SideOnly(Side.CLIENT)
	protected IIcon getTopIcon(int meta) {
		return textures[0];
	}
}
