package alfheim.common.spell.earth;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

public class SpellTitanHit extends SpellBase {

	public static final Material[] MATERIALS = new Material[] { Material.rock, Material.iron, Material.ice, Material.glass, Material.piston, Material.anvil, Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay };

	public SpellTitanHit() {
		super("titanhit", EnumRace.GNOME, 1, 1, 2);
	}

	/** temp value for counting total on block breaking */
	public int tcd = 0, tmana = 0;
	
	@Override
	public SpellCastResult performCast(EntityLivingBase caster) {
		if (!(caster instanceof EntityPlayer)) return SpellCastResult.WRONGTGT;
		SpellCastResult result;
		
		double dist = caster instanceof EntityPlayerMP ? ((EntityPlayerMP) caster).theItemInWorldManager.getBlockReachDistance() : 5.0;
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, dist, false);
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) return SpellCastResult.WRONGTGT;
		
		tmana = removeBlocksInIteration(caster.worldObj, (EntityPlayer) caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, false, false);
		
		result = checkCast(caster);
		if (result != SpellCastResult.OK) return result;
		
		removeBlocksInIteration(caster.worldObj, (EntityPlayer) caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, true, false);
		
		return result;
	}
	
	public int removeBlocksInIteration(World world, EntityPlayer player, int x, int y, int z, int side, boolean remove, boolean draw) {
		ForgeDirection direction = ForgeDirection.getOrientation(side);
		int xs = direction.offsetX == 0 ? -1 : 0, ys = direction.offsetY == 0 ? -1 : 0, zs = direction.offsetZ == 0 ? -1 : 0, xe = direction.offsetX == 0 ?  2 : 1, ye = direction.offsetY == 0 ?  2 : 1, ze = direction.offsetZ == 0 ?  2 : 1, mana = 0;
		for(int x1 = xs; x1 < xe; x1++)
			for(int y1 = ys; y1 < ye; y1++)
				for(int z1 = zs; z1 < ze; z1++)
					mana += removeBlockWithDrops(world, player, x1 + x, y1 + y, z1 + z, remove, draw, MATERIALS);
		return mana;
	}

	public int removeBlockWithDrops(World world, EntityPlayer player, int x, int y, int z, boolean remove, boolean draw, Material[] materialsListing) {
		if(!world.blockExists(x, y, z)) return 0;

		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z), mana = 0;
		
		Material mat = world.getBlock(x, y, z).getMaterial();
		if((!world.isRemote || (!remove && draw)) && block != null && !block.isAir(world, x, y, z) && block.getPlayerRelativeBlockHardness(player, world, x, y, z) > 0) {
			if(!block.canHarvestBlock(player, meta) || !ToolCommons.isRightMaterial(mat, materialsListing)) return 0;

			boolean flag = false;
			
			if(!player.capabilities.isCreativeMode) {
				int localMeta = world.getBlockMetadata(x, y, z);
				if (remove) block.onBlockHarvested(world, x, y, z, localMeta, player);

				if(remove && (flag = block.removedByPlayer(world, player, x, y, z, remove))) {
					block.onBlockDestroyedByPlayer(world, x, y, z, localMeta);

//					if(!ItemElementiumPick.isDisposable(block))
					block.harvestBlock(world, player, x, y, z, localMeta);
					
					mana += block.getBlockHardness(world, x, y, z) * 10;
					tcd += 2;
				}
				if (!remove) {
					flag = true;
					mana += block.getBlockHardness(world, x, y, z) * 10;
				}
			} else {
				if (remove) world.setBlockToAir(x, y, z);
				else {
					flag = x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000 && y >= 0 && y < 256;
				}
			}

			if (draw && flag) {
				Botania.proxy.setWispFXDepthTest(false);
				Botania.proxy.wispFX(player.worldObj, x + 0.5, y + 0.5, z + 0.5, 1, 0, 0, 0.2F, 0, 0.075F);
				Botania.proxy.setWispFXDepthTest(true);
			}
			
			if(!world.isRemote && remove && ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool) world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
		}
		
		return mana;
	}
	
	@Override
	public int getManaCost() {
		int temp = tmana;
		tmana = 0;
		return temp * mana;
	}

	@Override
	public int getCooldown() {
		int temp = tcd;
		tcd = 0;
		return temp * cldn;
	}

	@Override
	public void render(EntityLivingBase caster) {
		double dist = caster instanceof EntityPlayerMP ? ((EntityPlayerMP) caster).theItemInWorldManager.getBlockReachDistance() : 5.0;
		MovingObjectPosition mop = ASJUtilities.getSelectedBlock(caster, dist, false);
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK || mop.sideHit == -1) return;
		
		removeBlocksInIteration(caster.worldObj, (EntityPlayer) caster, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, false, true);
	}
}