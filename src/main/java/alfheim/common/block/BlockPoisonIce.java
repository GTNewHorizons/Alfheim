package alfheim.common.block;

import java.util.Random;

import alfheim.api.ModInfo;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.lexicon.AlfheimLexiconData;
import baubles.api.BaublesApi;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ManaItemHandler;

public class BlockPoisonIce extends Block implements ILexiconable {

	public BlockPoisonIce() {
		super(Material.packedIce);
		float mod = 0.001F;
		setBlockBounds(0 + mod, 0 + mod, 0 + mod, 1 - mod, 1 - mod, 1 - mod);
		setBlockName("NiflheimIce");
		setBlockTextureName(ModInfo.MODID + ":NiflheimIce");
		setBlockUnbreakable();
		setHarvestLevel("pick", 2);
		setLightOpacity(0);
		setResistance(Float.MAX_VALUE);
		setStepSound(soundTypeGlass);
		setTickRandomly(true);
		slipperiness = 0.98F;
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
        float hardness = getBlockHardness(world, x, y, z);
        
        IInventory bbls = BaublesApi.getBaubles(player);
		if (bbls.getStackInSlot(0) != null && bbls.getStackInSlot(0).getItem() == AlfheimItems.elfIcePendant && ManaItemHandler.requestManaExact(bbls.getStackInSlot(0), player, 5, true)) hardness = 2;

		if (hardness < 0.0F) return 0.0F;
		
        if (!ForgeHooks.canHarvestBlock(this, player, metadata)) return player.getBreakSpeed(this, true, metadata, x, y, z) / hardness / 100F;
        else return player.getBreakSpeed(this, false, metadata, x, y, z) / hardness / 30F;
	}
	
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return world.getBlock(x, y, z) != this && !world.getBlock(x, y, z).isOpaqueCube();
	}

	@Override
	public int quantityDropped(Random r) {
		return 0;
	}
	
	@Override
	public void dropBlockAsItem(World w, int x, int y, int z, ItemStack s) {
		return;
	}

	@Override
	public void onEntityWalking(World w, int x, int y, int z, Entity e) {
		if (e instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e;
			if (BaublesApi.getBaubles(player).getStackInSlot(0) != null && BaublesApi.getBaubles(player).getStackInSlot(0).getItem() == AlfheimItems.elfIcePendant) return;
		}
		e.setInWeb();
		if (!w.isRemote && e instanceof EntityLivingBase) {
			EntityLivingBase l = (EntityLivingBase) e;
			if (!l.isPotionActive(Potion.poison)) l.addPotionEffect(new PotionEffect(Potion.poison.id, 100, 2));
			l.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 25, 2));
		}
	}
	
	@Override
	public void onEntityCollidedWithBlock(World w, int x, int y, int z, Entity e) {
		this.onEntityWalking(w, x, y, z, e);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (world.getGameRules().getGameRuleBooleanValue("doFireTick")
			&& rand.nextInt(10) == 0
			&& world.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(5, 5, 5)).isEmpty()) {
			world.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public int tickRate(World world) {
		return 1;
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.ruling;
	}
}