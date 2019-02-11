package alfheim.common.block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import alexsocol.asjlib.extendables.MaterialPublic;
import alfheim.AlfheimCore;
import alfheim.api.AlfheimAPI;
import alfheim.api.ModInfo;
import alfheim.common.block.tile.TileAnomaly;
import alfheim.common.item.block.ItemBlockAnomaly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockAnomaly extends BlockContainer {
	
	public static final Material anomaly = new MaterialPublic(MapColor.airColor).setGrass().setNotOpaque().setImmovableMobility();
	public static final List<String> validBlocks = Arrays.asList(new String[] { "stone", "dirt", "grass", "sand", "gravel", "hardenedClay", "snowLayer", "mycelium", "podzol", "sandstone", /* Mod support: */ "blockDiorite", "stoneDiorite", "blockGranite", "stoneGranite", "blockAndesite", "stoneAndesite", "marble", "blockMarble", "limestone", "blockLimestone" });
	public static final HashMap<String, IIcon> icons = new HashMap<String, IIcon>();
	
	public BlockAnomaly() {
		super(anomaly);
		setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
		setBlockName("Anomaly");
		setBlockTextureName(ModInfo.MODID + ":ManaInfuserTopDark"); // why not :)
		setBlockUnbreakable();
		setCreativeTab(AlfheimCore.alfheimTab);
		setLightLevel(1);
		setLightOpacity(0);
		setResistance(Float.MAX_VALUE / 3.0F);
		setStepSound(soundTypeCloth);
	}
	
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox(0.25 + x, 0.25 + y, 0.25 + z, 0.75 + x, 0.75 + y, 0.75 + z);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}
	
	@Override
	public void getSubBlocks(Item block, CreativeTabs tab, List list) { // BlockSpecialFlower
		for (String name : AlfheimAPI.anomalies.keySet()) 
			list.add(ItemBlockAnomaly.ofType(name));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return ItemBlockAnomaly.ofType(((TileAnomaly) world.getTileEntity(x, y, z)).mainSubTile);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAnomaly();
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (String name : AlfheimAPI.anomalies.keySet())
			icons.put(name, reg.registerIcon(ModInfo.MODID + ":anomalies/" + name));
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}
	
	@Override
	public Item getItemDropped(int meta, Random rand, int luck) {
		return null;
	}
}
