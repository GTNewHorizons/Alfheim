package alfheim.common.block.tile.sub;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.block.tile.SubTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.Botania;

import java.util.*;

public class SubTileWarp extends SubTileEntity {
	
	// public static final List<String> validBlocks = Arrays.asList(new String[] { "stone", "dirt", "grass", "sand", "gravel", "hardenedClay", "snowLayer", "mycelium", "podzol", "sandstone", /* Mod support: */ "blockDiorite", "stoneDiorite", "blockGranite", "stoneGranite", "blockAndesite", "stoneAndesite", "marble", "blockMarble", "limestone", "blockLimestone" });
	// maybe will change warp's behavior to swap only blocks from list above ^
	public static final String TAG_RADIUS = "radius";
	public int radius = 20;
	
	@Override
	public void update() {
		if (inWG()) return;
		
		if (ASJUtilities.isServer() && ticks % 600 == 0) {
			radius = worldObj().rand.nextInt(8) + 16;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(superTile);
		}
		
		rand.setSeed(x() ^ y() ^ z());
		double worldTime = (worldObj().getTotalWorldTime() + rand.nextInt(1000)) / 5.0;
		float r = 0.75F + (float) Math.random() * 0.05F;
		
		double x = x() + 0.5 + Math.sin(worldTime) * r;
		double y = y() + 0.5 + Math.sin(worldTime) * r;
		double z = z() + 0.5 + Math.sin(worldTime) * r;
		double X = x() + 0.5 + Math.cos(worldTime) * r;
		double Y = y() + 0.5 + Math.cos(worldTime) * r;
		double Z = z() + 0.5 + Math.cos(worldTime) * r;
		
		double _x = x() + 0.5 + Math.sin(-worldTime) * r;
		double _y = y() + 0.5 + Math.sin(-worldTime) * r;
		double _z = z() + 0.5 + Math.sin(-worldTime) * r;
		double _X = x() + 0.5 + Math.cos(-worldTime) * r;
		double _Y = y() + 0.5 + Math.cos(-worldTime) * r;
		double _Z = z() + 0.5 + Math.cos(-worldTime) * r;
		
		Botania.proxy.wispFX(worldObj(), x() + 0.5, Y, Z,
				0.25F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
		
		Botania.proxy.wispFX(worldObj(), x() + 0.5, _y, z,
				0.25F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
		
		Botania.proxy.wispFX(worldObj(), X, y() + 0.5, Z,
				0.25F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
		
		Botania.proxy.wispFX(worldObj(), x, y() + 0.5, _z,
				0.25F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
		
		Botania.proxy.wispFX(worldObj(), X, Y, z() + 0.5,
				0.25F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
		
		Botania.proxy.wispFX(worldObj(), _x, y, z() + 0.5,
				0.25F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
		
		Botania.proxy.wispFX(worldObj(), x() + 0.5, y() + 0.5, Z,
				0.25F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
		
		Botania.proxy.wispFX(worldObj(), x() + 0.5, y, z() + 0.5,
				0.25F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
		
		Botania.proxy.wispFX(worldObj(), X, y() + 0.5, z() + 0.5,
				0.25F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setInteger(TAG_RADIUS, radius);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		radius = cmp.getInteger(TAG_RADIUS);
	}
	
	@Override
	public List<Object> getTargets() {
		if (!ASJUtilities.isServer()) return EMPTY_LIST;
		
		List<Object> l = null;
		
		if (ticks % 100 == 0 && !inWG()) {
			l = allAroundRaw(EntityLivingBase.class, radius);
			if (l != null && l.size() > 0) {
				if (l.size() == 1) {
					l.add(new LivingCoords((EntityLivingBase) l.remove(0), x(), y(), z(), radius));
				} else {
					l.add(new LivingPair(rand(l), rand(l)));
				}
				return l;
			}
		} else if (ticks % 25 == 1) {
			Block b1, b2;
			Vector8i v = new Vector8i();
			int tries = 50;
			
			while (tries-->0) {
				v.set(x(), y(), z(), radius, worldObj().rand);
				
				b1 = worldObj().getBlock(v.x1, v.y1, v.z1);
				b2 = worldObj().getBlock(v.x2, v.y2, v.z2);
				
				if ((worldObj().isAirBlock(v.x1, v.y1, v.z1)				&&
					 worldObj().isAirBlock(v.x2, v.y2, v.z2))				||
					 b1.getBlockHardness(worldObj(), v.x1, v.y1, v.z1) < 0	||
					 b2.getBlockHardness(worldObj(), v.x2, v.y2, v.z2) < 0	||
					 worldObj().getTileEntity(v.x1, v.y1, v.z1) != null 	||
					 worldObj().getTileEntity(v.x2, v.y2, v.z2) != null
				) continue;
				
				v.m1 = worldObj().getBlockMetadata(v.x1, v.y1, v.z1);
				v.m2 = worldObj().getBlockMetadata(v.x2, v.y2, v.z2);
				l = new ArrayList<Object>();
				l.add(v);
				break;
			}
		}
		
		if (l == null) l = EMPTY_LIST;
		return l;
	}
	
	private EntityLivingBase rand(List<Object> l) {
		return (EntityLivingBase) l.remove(worldObj().rand.nextInt(l.size()));
	}
	
	@Override
	public void performEffect(Object target) {
		if (!ASJUtilities.isServer()) return;
		
		if (!inWG()) {
			if (target instanceof LivingPair) {
				LivingPair pair = (LivingPair) target;
				if ((pair.e1 instanceof EntityPlayer && ((EntityPlayer) pair.e1).capabilities.isCreativeMode) || (pair.e2 instanceof EntityPlayer && ((EntityPlayer) pair.e2).capabilities.isCreativeMode)) return;
				double x = pair.e1.posX, y = pair.e1.posY, z = pair.e1.posZ;
				
				pair.e1.setPositionAndUpdate(pair.e2.posX, pair.e2.posY, pair.e2.posZ);
				pair.e2.setPositionAndUpdate(x, y, z);
				return;
			} else if (target instanceof LivingCoords) {
				LivingCoords crds = (LivingCoords) target;
				if (crds.e instanceof EntityPlayer && ((EntityPlayer) crds.e).capabilities.isCreativeMode) return;
				crds.e.setPositionAndUpdate(crds.x, crds.y, crds.z);
				return;
			}
		}
		
		if (target instanceof Vector8i) {
			Vector8i v = (Vector8i) target;
			Block block = worldObj().getBlock(v.x1, v.y1, v.z1);
			worldObj().setBlock(v.x1, v.y1, v.z1, worldObj().getBlock(v.x2, v.y2, v.z2), v.m2, 3);
			worldObj().setBlock(v.x2, v.y2, v.z2, block, v.m1, 3);
		}
	}
	
	@Override
	public int typeBits() {
		return SPACE;
	}
	
	@Override
	public int getStrip() {
		return 6;
	}
	
	private static class Vector8i {
		
		public static final Vector3 v = new Vector3();
		public int x1, y1, z1, m1, x2, y2, z2, m2;
		
		public void set(int x, int y, int z, int radius, Random rand) {
			x1 = rand.nextInt(radius * 2) - radius;
			y1 = rand.nextInt(radius * 2) - radius;
			z1 = rand.nextInt(radius * 2) - radius;
			
			x2 = rand.nextInt(radius * 2) - radius;
			y2 = rand.nextInt(radius * 2) - radius;
			z2 = rand.nextInt(radius * 2) - radius;
			
			v.set(x1, y1, z1);
			if (v.length() > radius) v.shrink(v.length() - radius);
			x1 = MathHelper.floor_double(v.x);
			y1 = MathHelper.floor_double(v.y);
			z1 = MathHelper.floor_double(v.z);
			
			v.set(x2, y2, z2);
			if (v.length() > radius) v.shrink(v.length() - radius);
			x2 = MathHelper.floor_double(v.x);
			y2 = MathHelper.floor_double(v.y);
			z2 = MathHelper.floor_double(v.z);
			
			x1 = Math.max(-30000000,	Math.min(x + x1, 30000000));
			y1 = Math.max(0,			Math.min(y + y1, 255	 ));
			z1 = Math.max(-30000000,	Math.min(z + z1, 30000000));
			
			x2 = Math.max(-30000000,	Math.min(x + x2, 30000000));
			y2 = Math.max(0,			Math.min(y + y2, 255	 ));
			z2 = Math.max(-30000000,	Math.min(z + z2, 30000000));
		}
	}
	
	private static class LivingCoords {
		
		public final EntityLivingBase e;
		public final double x, y, z;
		
		public LivingCoords(EntityLivingBase entity, double posX, double posY, double posZ, int radius) {
			e = entity;
			x = Math.max(-30000000,	Math.min(posX + Math.random() * radius * 2 - radius, 30000000));
			y = Math.max(1,			Math.min(posY + Math.random() * radius * 2 - radius, 255	 ));
			z = Math.max(-30000000,	Math.min(posZ + Math.random() * radius * 2 - radius, 30000000));
		}
	}
	
	private static class LivingPair {
		
		public final EntityLivingBase e1, e2;
		
		public LivingPair(EntityLivingBase en1, EntityLivingBase en2) {
			e1 = en1;
			e2 = en2;
		}
	}
	
	@Override
	public EnumAnomalityRarity getRarity() {
		return EnumAnomalityRarity.RARE;
	}
}
