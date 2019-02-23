package alfheim.common.block;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.lexicon.AlfheimLexiconData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockPowerPylon extends Block implements ILexiconable {
	
	public BlockPowerPylon() {
		super(Material.rock);
		setBlockName("PowerPylon");
		setBlockTextureName(ModInfo.MODID + ":ManaInfuserBottomDark");
		setCreativeTab(AlfheimCore.alfheimTab);
		setHardness(2);
		setResistance(6000);
		setStepSound(soundTypeStone);
	}
	
	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.powerPys;
	}
}
