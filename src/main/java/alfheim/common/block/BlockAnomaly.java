package alfheim.common.block;

import alexsocol.asjlib.extendables.MaterialPublic;
import alfheim.api.*;
import alfheim.common.block.tile.TileAnomaly;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.item.block.ItemBlockAnomaly;
import alfheim.common.lexicon.AlfheimLexiconData;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.*;

import java.util.*;

import static vazkii.botania.common.core.helper.ItemNBTHelper.*;

public class BlockAnomaly extends BlockContainer implements ILexiconable {
	
	public static final Material anomaly = new MaterialPublic(MapColor.airColor).setGrass().setNotOpaque().setImmovableMobility();
	public static IIcon iconUndefined;
	
	public BlockAnomaly() {
		super(anomaly);
		setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
		setBlockName("Anomaly");
		setBlockTextureName(ModInfo.MODID + ":ManaInfuserTopDark"); // why not :)
		setBlockUnbreakable();
		setLightLevel(1);
		setLightOpacity(0);
		setResistance(Float.MAX_VALUE / 3.0F);
		setStepSound(soundTypeCloth);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return ((TileAnomaly) world.getTileEntity(x, y, z)).onActivated(player.getCurrentEquippedItem(), player, world, x, y, z);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}
	
	@Override
	public void getSubBlocks(Item block, CreativeTabs tab, List list) {
		for (String name : AlfheimAPI.anomalies.keySet()) 
			list.add(ItemBlockAnomaly.ofType(name));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		ItemStack anomaly = new ItemStack(AlfheimBlocks.anomaly);
		initNBT(anomaly);
		((TileAnomaly) world.getTileEntity(x, y, z)).writeCustomNBT(getNBT(anomaly));
		return anomaly;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAnomaly();
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		iconUndefined = reg.registerIcon(ModInfo.MODID + ":undefined");
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
	
	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.anomaly;
	}
}