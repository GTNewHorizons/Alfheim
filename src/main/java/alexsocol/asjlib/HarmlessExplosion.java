package alexsocol.asjlib;

import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;

public class HarmlessExplosion {
	private final World worldObj;
	public final double explosionX;
	public final double explosionY;
	public final double explosionZ;
	public final EntityPlayer exploder;
	public float explosionSize;
	
	public HarmlessExplosion(World world, EntityPlayer exploder, double x, double y, double z, float size) {
		worldObj = world;
		this.exploder = exploder;
		explosionX = x;
		explosionY = y;
		explosionZ = z;
		explosionSize = size;
		doExplosionA();
	}
	
	/**
	 * Does the first part of the explosion (destroy blocks)
	 */
	public void doExplosionA() {
		float f = explosionSize;
		HashSet hashset = new HashSet();
		int i;
		int j;
		int k;
		double d5;
		double d6;
		double d7;
		
		worldObj.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
		worldObj.spawnParticle("hugeexplosion", explosionX, explosionY, explosionZ, 1.0, 0.0, 0.0);
	
		explosionSize *= 2.0F;
		i = MathHelper.floor_double(explosionX - (double)explosionSize - 1.0);
		j = MathHelper.floor_double(explosionX + (double)explosionSize + 1.0);
		k = MathHelper.floor_double(explosionY - (double)explosionSize - 1.0);
		int i2 = MathHelper.floor_double(explosionY + (double)explosionSize + 1.0);
		int l = MathHelper.floor_double(explosionZ - (double)explosionSize - 1.0);
		int j2 = MathHelper.floor_double(explosionZ + (double)explosionSize + 1.0);
		
		List list = exploder != null ? worldObj.getEntitiesWithinAABBExcludingEntity(exploder, AxisAlignedBB.getBoundingBox((double)i, (double)k, (double)l, (double)j, (double)i2, (double)j2)) : worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox((double)i, (double)k, (double)l, (double)j, (double)i2, (double)j2));
		Vec3 vec3 = Vec3.createVectorHelper(explosionX, explosionY, explosionZ);
		
		for (Object o : list) {
			Entity entity = (Entity) o;
			double d4 = entity.getDistance(explosionX, explosionY, explosionZ) / (double) explosionSize;
			
			if (d4 <= 1.0) {
				d5 = entity.posX - explosionX;
				d6 = entity.posY + (double) entity.getEyeHeight() - explosionY;
				d7 = entity.posZ - explosionZ;
				double d9 = (double) MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7);
				
				if (d9 != 0.0) {
					d5 /= d9;
					d6 /= d9;
					d7 /= d9;
					double d10 = (double) worldObj.getBlockDensity(vec3, entity.boundingBox);
					double d11 = (1.0 - d4) * d10;
					entity.attackEntityFrom(DamageSource.setExplosionSource(null), (float) ((int) ((d11 * d11 + d11) / 2.0 * 8.0 * (double) explosionSize + 1.0)));
					double d8 = EnchantmentProtection.func_92092_a(entity, d11);
					entity.motionX += d5 * d8;
					entity.motionY += d6 * d8;
					entity.motionZ += d7 * d8;
				}
			}
		}
		explosionSize = f;
	}
}