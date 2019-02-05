package alfheim.common.block.tile.sub;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.block.tile.SubTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.MathHelper;

public class SubTileAntigrav extends SubTileEntity {

	public static final double power = 0.7, radius = 15;
	Vector3 v = new Vector3();
	
	@Override
	public void update() {
		List<Entity> list = superTile.getWorldObj().getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(superTile.xCoord, superTile.yCoord, superTile.zCoord, superTile.xCoord + 1, superTile.yCoord + 1, superTile.zCoord + 1).expand(radius, radius * 2, radius));
		if (list == null || list.isEmpty()) return;
		for (Entity e : list) {
			if (e instanceof EntityPlayer && ((EntityPlayer) e).capabilities.disableDamage) continue;
			if (MathHelper.pointDistancePlane(superTile.xCoord + 0.5, superTile.yCoord + 0.5, e.posX, e.posZ) > radius) continue;
			e.motionY += power * 0.125;
		}
		
		for (int i = 0; i < 4; i++) {
			v.set(Math.random() - 0.5, 0, Math.random() - 0.5).normalize().mul(Math.random() * radius).add(superTile.xCoord, superTile.yCoord + Math.random() * radius * 4 - radius * 2, superTile.zCoord);
			
			Botania.proxy.wispFX(superTile.getWorldObj(), v.x, v.y, v.z, 0.5F, 0.9F, 1, 0.1F, -0.1F, 10);
		}
	}
}