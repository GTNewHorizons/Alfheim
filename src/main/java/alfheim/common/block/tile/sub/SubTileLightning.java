package alfheim.common.block.tile.sub;

import java.util.ArrayList;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.block.tile.SubTileEntity;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
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
		
		if (AlfheimConfig.lightningsSpeed < 1) return;
		
		if (ticks % AlfheimConfig.lightningsSpeed == 0) {
			ve.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize();
			vt.add(ve.x / 2.25, ve.y / 2.25, ve.z / 2.25);
			ve.multiply(1.5).add(superTile.xCoord + 0.5, superTile.yCoord + 0.5, superTile.zCoord + 0.5);
			Botania.proxy.lightningFX(superTile.getWorldObj(), vt, ve, 50, superTile.getWorldObj().rand.nextLong(), 0, 0xFF0000);
		}
	}
	
	@Override
	public List<Object> getTargets() {
		List l = new ArrayList<Object>();
		if (ticks % 50 == 0) l.add(findNearestEntity(radius));
		return l;
	}

	@Override
	public void performEffect(Object target) {
		if (ticks % 25 != 0) return;
		if (target == null || !(target instanceof EntityLivingBase)) return;
    	if (target instanceof EntityPlayer && ((EntityPlayer) target).capabilities.disableDamage) return;

		EntityLivingBase entity = (EntityLivingBase) target;
		
		entity.attackEntityFrom(DamageSourceSpell.corruption, (float) Math.min((Math.random() * 2 + 3) / vt.set(superTile.xCoord + 0.5, superTile.yCoord + 0.5, superTile.zCoord + 0.5).add(-entity.posX, -entity.posY, -entity.posZ).mag() / 2, 2.5));
		
		vt.set(superTile.xCoord + 0.5, superTile.yCoord + 0.5, superTile.zCoord + 0.5);
		ve.set(entity.posX, entity.posY, entity.posZ).normalize();
		vt.add(ve.x / 2, ve.y / 2, ve.z / 2);
		ve.set(entity.posX, entity.posY, entity.posZ);
		
		Botania.proxy.lightningFX(superTile.getWorldObj(), vt, ve, 1, superTile.getWorldObj().rand.nextLong(), 0, 0xFF0000);
	}

	@Override
	public int typeBits() {
		return HEALTH;
	}
}