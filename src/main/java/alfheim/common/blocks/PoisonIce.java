package alfheim.common.blocks;

import java.util.Random;

import alfheim.AlfheimCore;
import alfheim.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PoisonIce extends Block {

	public PoisonIce() {
		super(Material.packedIce);
		final float mod = 0.01F;
		this.setBlockBounds(0 + mod, 0 + mod, 0 + mod, 1 - mod, 1 - mod, 1 - mod);
		this.setBlockName("NiflheimIce");
        this.setBlockTextureName(Constants.MODID + ":NiflheimIce");
        this.setCreativeTab(AlfheimCore.alfheimTab);
        this.setLightOpacity(0);
        this.setHardness(5);
        this.setHarvestLevel("pickaxe", 3);
        this.setResistance(6000);
        this.setStepSound(soundTypeGlass);
        this.setTickRandomly(true);
		this.slipperiness = 0.98F;
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
    	if (!w.isRemote && w.getTotalWorldTime() % 20 == 0 && e instanceof EntityLivingBase) {
    		EntityLivingBase l = (EntityLivingBase) e;
    		l.addPotionEffect(new PotionEffect(Potion.poison.id, 100, 2));
    		l.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 25, 2));
    	}
    }
    
    @Override
    public void onEntityCollidedWithBlock(World w, int x, int y, int z, Entity e) {
    	if (!w.isRemote && w.getTotalWorldTime() % 20 == 0 && e instanceof EntityLivingBase) {
    		EntityLivingBase l = (EntityLivingBase) e;
    		l.addPotionEffect(new PotionEffect(Potion.poison.id, 100, 2));
    		l.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 25, 2));
    	}
    }
    
    @Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (world.getGameRules().getGameRuleBooleanValue("doFireTick")
			//&& rand.nextInt(100) == 0
			&& world.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(5, 5, 5)).isEmpty()) {
			world.setBlockToAir(x, y, z);
		}
	}
    
    @Override
    public int tickRate(World world) {
        return 1;
    }
}