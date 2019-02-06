package alfheim.common.block.tile.sub;

import java.util.List;

import alexsocol.asjlib.math.Vector3;
import alfheim.api.block.tile.SubTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.MathHelper;

public class SubTileAntigrav extends SubTileEntity {

	public static final double power = 0.7, radius = 15;
	Vector3 v = new Vector3();
	
	@Override
	public void update() {
		for (int i = 0; i < 4; i++) {
			v.rand().sub(0.5).set(v.x, 0, v.z).normalize().mul(Math.random() * radius).add(superTile).add(0, Math.random() * radius * 4 - radius * 2, 0);
			Botania.proxy.wispFX(superTile.getWorldObj(), v.x, v.y, v.z, 0.5F, 0.9F, 1, 0.1F, -0.1F, 10);
		}
	}

	@Override
	public List<Object> getTargets() {
		return superTile.getWorldObj().getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(superTile.xCoord, superTile.yCoord, superTile.zCoord, superTile.xCoord + 1, superTile.yCoord + 1, superTile.zCoord + 1).expand(radius, radius * 2, radius));
	}

	@Override
	public void performEffect(Object target) {
		if (target == null || !(target instanceof Entity)) return;
		if (target instanceof EntityPlayer && ((EntityPlayer) target).capabilities.disableDamage) return;
		
		Entity entity = (Entity) target;
		if (MathHelper.pointDistancePlane(superTile.xCoord + 0.5, superTile.yCoord + 0.5, entity.posX, entity.posZ) > radius) return;
		
		entity.motionY += power * 0.125;
	}

	@Override
	public int typeBits() {
		return MOTION;
	}
}