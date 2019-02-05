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

public class SubTileGravity extends SubTileEntity {

	public static final String TAG_POWER = "power";
	public double power = 0.65;
	Vector3 vt = new Vector3();
	Vector3 ve = new Vector3();
	
	@Override
	public void update() {
		if (ASJUtilities.isServer() && ticks % 100 == 0) {
			power = Math.random() * 0.65 + 0.35;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(superTile);
		}
		
		double radius = power * 10, dist = 0, str = 0;
		
		vacuum: {
			List<Entity> list = superTile.getWorldObj().getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(superTile.xCoord, superTile.yCoord, superTile.zCoord, superTile.xCoord + 1, superTile.yCoord + 1, superTile.zCoord + 1).expand(radius, radius, radius));
			if (list == null || list.isEmpty()) break vacuum;
			
			for (Entity e : list) {
				if (e instanceof EntityPlayer && ((EntityPlayer) e).capabilities.disableDamage) continue;
				
				ve.set(e.posX, e.posY, e.posZ);
				
				if (!ASJUtilities.isServer()) if (e == Minecraft.getMinecraft().thePlayer) ve.add(0, -1.62, 0);
				
				if ((dist = Math.sqrt(Math.pow(ve.x - superTile.xCoord + 0.5, 2) + Math.pow(ve.y - superTile.yCoord + 0.5, 2) + Math.pow(ve.z - superTile.zCoord + 0.5, 2))) > radius) continue;
					
				vt.set(superTile.xCoord + 0.5, superTile.yCoord + 0.5, superTile.zCoord + 0.5);
				vt.set(vt).sub(ve).normalize().mul(str = power * 0.5 * 1/dist);
				
				e.motionX += vt.x;
				e.motionY += vt.y * 1.25;
				e.motionZ += vt.z;
			}
		}
		
		vt.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(Math.random() * radius / 2).add(superTile.xCoord + 0.5, superTile.yCoord + 0.5, superTile.zCoord + 0.5);
		ve.set(superTile.xCoord + 0.5, superTile.yCoord + 0.5, superTile.zCoord + 0.5).sub(vt).mul(0.05);
		
		Botania.proxy.wispFX(superTile.getWorldObj(), vt.x, vt.y, vt.z,	1, 1, 1, 0.5F, (float) ve.x, (float) ve.y, (float) ve.z, 0.5F);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setDouble(TAG_POWER, power);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		power = cmp.getDouble(TAG_POWER);
	}
}