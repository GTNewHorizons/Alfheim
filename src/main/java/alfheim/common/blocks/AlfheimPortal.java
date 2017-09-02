package alfheim.common.blocks;

import alfheim.AlfheimCore;
import alfheim.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import vazkii.botania.common.entity.EntityPixie;

public class AlfheimPortal extends Block {

	public IIcon[] textures = new IIcon[2];
	
	public AlfheimPortal() {
		super(Material.wood);
		this.setBlockName("AlfheimPortal");
		this.setBlockTextureName(Constants.MODID + ":AlfheimPortal");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHardness(1.0F);
		this.setLightLevel(0.3F);
		this.setResistance(600.0F);
		this.setStepSound(soundTypeWood);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(Constants.MODID + ":AlfheimPortal");
		textures[1] = reg.registerIcon(Constants.MODID + ":AlfheimPortalActive");
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return meta == 1 ? textures[1] : textures [0];
	}
}