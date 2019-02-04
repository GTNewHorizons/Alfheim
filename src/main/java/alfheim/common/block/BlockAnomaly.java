package alfheim.common.block;

import java.util.Arrays;
import java.util.List;

import alexsocol.asjlib.extendables.MaterialPublic;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.block.tile.TileAnomaly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.common.item.rod.ItemTerraformRod;

public class BlockAnomaly extends BlockContainer {

	public static final Material anomaly = new MaterialPublic(MapColor.airColor).setNotBlocker().setNoGrass().setNotOpaque().setNotSolid().setImmovableMobility();
	
	public static final List<String> validBlocks = Arrays.asList(new String[] { "stone", "dirt", "grass", "sand", "gravel", "hardenedClay", "snowLayer", "mycelium", "podzol", "sandstone", /* Mod support: */ "blockDiorite", "stoneDiorite", "blockGranite", "stoneGranite", "blockAndesite", "stoneAndesite", "marble", "blockMarble", "limestone", "blockLimestone" });
	
	public BlockAnomaly() {
		super(anomaly);
		setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
		setBlockName("Anomaly");
		setBlockTextureName(ModInfo.MODID + ":ManaInfuserTopDark"); // why not :)
		setBlockUnbreakable();
		setCreativeTab(AlfheimCore.alfheimTab);
		setLightLevel(1);
		setLightOpacity(0);
		setStepSound(soundTypeCloth);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAnomaly();
	}
}
