package alfheim.common.block;

import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.Random;

import alfheim.api.ModInfo;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.lexicon.AlfheimLexiconData;
import baubles.api.BaublesApi;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFire;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ManaItemHandler;

public class BlockRedFlame extends BlockFire implements ILexiconable {

	public IIcon[] icons;

	public BlockRedFlame() {
		setBlockName("MuspelheimFire");
		setBlockUnbreakable();
		setLightLevel(1.0F);
		setLightOpacity(0);
		setResistance(Float.MAX_VALUE);
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
        float hardness = getBlockHardness(world, x, y, z);
        
        IInventory bbls = BaublesApi.getBaubles(player);
		if (bbls.getStackInSlot(0) != null && bbls.getStackInSlot(0).getItem() == AlfheimItems.elfFirePendant && ManaItemHandler.requestManaExact(bbls.getStackInSlot(0), player, 300, true)) hardness = 2;

		if (hardness < 0.0F) return 0.0F;
		
        if (!ForgeHooks.canHarvestBlock(this, player, metadata)) return player.getBreakSpeed(this, true, metadata, x, y, z) / hardness / 100F;
        else return player.getBreakSpeed(this, false, metadata, x, y, z) / hardness / 30F;
	}
	
	@Override
	public boolean isCollidable() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.icons = new IIcon[] { reg.registerIcon(ModInfo.MODID + ":MuspelheimFire0"), reg.registerIcon(ModInfo.MODID + ":MuspelheimFire1") };
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getFireIcon(int i) {
		return this.icons[i];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return this.icons[0];
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if (entity instanceof EntityPlayer && BaublesApi.getBaubles((EntityPlayer) entity).getStackInSlot(0) != null && BaublesApi.getBaubles((EntityPlayer) entity).getStackInSlot(0).getItem() == AlfheimItems.elfFirePendant && ManaItemHandler.requestManaExact(BaublesApi.getBaubles((EntityPlayer) entity).getStackInSlot(0), (EntityPlayer) entity, 50, true)) return;
		if (entity instanceof EntityLivingBase) {
			PotionEffect soulburn = new PotionEffect(AlfheimRegistry.soulburn.id, 200);
			soulburn.getCurativeItems().clear();
			((EntityLivingBase) entity).addPotionEffect(soulburn);
		}
		entity.setInWeb();
	}

	public void tryCatchFire(World world, int x, int y, int z, int side, Random rand, int meta, ForgeDirection face) {
		int j1 = world.getBlock(x, y, z).getFlammability(world, x, y, z, face);
		if (rand.nextInt(side) < j1) {
			boolean flag = world.getBlock(x, y, z) == Blocks.tnt;

			int k1 = meta + rand.nextInt(5) / 4;
			if (k1 > 15) {
				k1 = 15;
			}
			world.setBlock(x, y, z, this, k1, 3);
			if (flag) {
				Blocks.tnt.onBlockDestroyedByPlayer(world, x, y, z, 1);
			}
		}
	}

	public boolean canNeighborBurn(World world, int x, int y, int z) {
		return this.canCatchFire(world, x + 1, y, z, WEST) || this.canCatchFire(world, x - 1, y, z, EAST)
				|| this.canCatchFire(world, x, y - 1, z, UP) || this.canCatchFire(world, x, y + 1, z, DOWN)
				|| this.canCatchFire(world, x, y, z - 1, SOUTH) || this.canCatchFire(world, x, y, z + 1, NORTH);
	}

	public int getChanceOfNeighborsEncouragingFire(World world, int x, int y, int z) {
		byte b0 = 0;

		if (!world.isAirBlock(x, y, z)) {
			return 0;
		} else {
			int l = b0;
			l = this.getChanceToEncourageFire(world, x + 1, y, z, l, WEST);
			l = this.getChanceToEncourageFire(world, x - 1, y, z, l, EAST);
			l = this.getChanceToEncourageFire(world, x, y - 1, z, l, UP);
			l = this.getChanceToEncourageFire(world, x, y + 1, z, l, DOWN);
			l = this.getChanceToEncourageFire(world, x, y, z - 1, l, SOUTH);
			l = this.getChanceToEncourageFire(world, x, y, z + 1, l, NORTH);
			return l;
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
			if (!canPlaceBlockAt(world, x, y, z)) {
				world.setBlockToAir(x, y, z);
			}

			if (world.rand.nextInt(100) == 0) world.setBlockToAir(x, y, z);
			
			{
				int l = world.getBlockMetadata(x, y, z);
				if (l < 15) {
					world.setBlockMetadataWithNotify(x, y, z, l + rand.nextInt(3) / 2, 4);
				}
				world.scheduleBlockUpdate(x, y, z, this, tickRate(world) + rand.nextInt(15));

				{
					boolean flag1 = world.isBlockHighHumidity(x, y, z);
					byte b0 = 0;
					if (flag1) {
						b0 = -50;
					}
					tryCatchFire(world, x + 1, y, z, 300 + b0, rand, l, WEST);
					tryCatchFire(world, x - 1, y, z, 300 + b0, rand, l, EAST);
					tryCatchFire(world, x, y - 1, z, 250 + b0, rand, l, UP);
					tryCatchFire(world, x, y + 1, z, 250 + b0, rand, l, DOWN);
					tryCatchFire(world, x, y, z - 1, 300 + b0, rand, l, SOUTH);
					tryCatchFire(world, x, y, z + 1, 300 + b0, rand, l, NORTH);
					for (int i1 = x - 1; i1 <= x + 1; i1++) {
						for (int j1 = z - 1; j1 <= z + 1; j1++) {
							for (int k1 = y - 1; k1 <= y + 4; k1++) {
								if ((i1 != x) || (k1 != y) || (j1 != z)) {
									int l1 = 100;
									if (k1 > y + 1) {
										l1 += (k1 - (y + 1)) * 100;
									}
									int i2 = getChanceOfNeighborsEncouragingFire(world, i1, k1, j1);
									if (i2 > 0) {
										int j2 = (i2 + 40 + world.difficultySetting.getDifficultyId() * 7) / (l + 30);
										if (flag1) {
											j2 /= 2;
										}
										if ((j2 > 0) && (rand.nextInt(l1) <= j2)
												&& ((!world.isRaining()) || (!world.canLightningStrikeAt(i1, k1, j1)))
												&& (!world.canLightningStrikeAt(i1 - 1, k1, z))
												&& (!world.canLightningStrikeAt(i1 + 1, k1, j1))
												&& (!world.canLightningStrikeAt(i1, k1, j1 - 1))
												&& (!world.canLightningStrikeAt(i1, k1, j1 + 1))) {
											int k2 = l + rand.nextInt(5) / 4;
											if (k2 > 15) {
												k2 = 15;
											}
											world.setBlock(i1, k1, j1, this, k2, 3);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.ruling;
	}
}
