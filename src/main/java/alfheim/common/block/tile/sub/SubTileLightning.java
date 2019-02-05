package alfheim.common.block.tile.sub;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.block.tile.SubTileEntity;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;

public class SubTileLightning extends SubTileEntity {

	public static final double radius = 12;
	Vector3 vt = new Vector3();
	Vector3 ve = new Vector3();
	
	@Override
	public void update() {
		vt.set(superTile.xCoord + 0.5, superTile.yCoord + 0.5, superTile.zCoord + 0.5);
		
		if (superTile.getWorldObj().rand.nextInt(6000) == 0) {
			double x = superTile.xCoord + Math.random() * 10 - 5, z = superTile.zCoord + Math.random() * 10 - 5;
			superTile.getWorldObj().addWeatherEffect(new EntityLightningBolt(superTile.getWorldObj(), x, superTile.getWorldObj().getTopSolidOrLiquidBlock(MathHelper.floor_double(x), MathHelper.floor_double(z)), z));
			return;
		}
		
		if (ticks % 100 == 0) {
			EntityLivingBase entity = findNearestEntity();
			if (entity == null) return;
			
			entity.attackEntityFrom(DamageSourceSpell.corruption, (float) (Math.random() * 2 + 3));
			
			if (entity instanceof EntityPlayer)
				ManaItemHandler.requestMana(new ItemStack(Blocks.stone), (EntityPlayer) entity, (int) (Math.random() * 10000 + 2000), true);
			
			ve.set(entity.posX, entity.posY, entity.posZ).normalize();
			vt.add(ve.x / 2, ve.y / 2, ve.z / 2);
			ve.set(entity.posX, entity.posY, entity.posZ);
			
			Botania.proxy.lightningFX(superTile.getWorldObj(), vt, ve, 1, superTile.getWorldObj().rand.nextLong(), 0, 0xFF0000);
		}
		
		if (ticks % 2 == 0) {
			ve.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize();
			vt.add(ve.x / 2.25, ve.y / 2.25, ve.z / 2.25);
			ve.multiply(1.5).add(superTile.xCoord + 0.5, superTile.yCoord + 0.5, superTile.zCoord + 0.5);
			Botania.proxy.lightningFX(superTile.getWorldObj(), vt, ve, 50, superTile.getWorldObj().rand.nextLong(), 0, 0xFF0000);
		}
	}
	
	public EntityLivingBase findNearestEntity() {
        List<EntityLivingBase> list = superTile.getWorldObj().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(superTile.xCoord, superTile.yCoord, superTile.zCoord, superTile.xCoord, superTile.yCoord, superTile.zCoord).expand(radius, radius, radius));
        EntityLivingBase entity1 = null;
        double d0 = Double.MAX_VALUE;

        for (EntityLivingBase entity2 : list) {
            if (entity2 != null) {
                double d1 = getDistanceSqToEntity(entity2);

                if (d1 <= d0) {
                    entity1 = entity2;
                    d0 = d1;
                }
            }
        }

        return entity1;
    }
	
	public double getDistanceSqToEntity(EntityLivingBase entity) {
        double d0 = superTile.xCoord - entity.posX;
        double d1 = superTile.yCoord - entity.posY;
        double d2 = superTile.zCoord - entity.posZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }
}