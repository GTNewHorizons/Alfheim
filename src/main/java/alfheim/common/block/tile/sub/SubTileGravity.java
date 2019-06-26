package alfheim.common.block.tile.sub;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.block.tile.SubTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.Botania;

import java.util.List;

public class SubTileGravity extends SubTileEntity {
	
	public static final String TAG_POWER = "power";
	public double power = 0.65;
	final Vector3 vt = new Vector3();
	final Vector3 ve = new Vector3();
	
	@Override
	public void update() {
		if (inWG()) return;
		
		if (ASJUtilities.isServer() && ticks % 100 == 0) {
			power = Math.random() * 0.65 + 0.35;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(superTile);
		}
		
		double radius = power * 10, dist = 0, str = 0;
		
		vt.rand().sub(0.5).normalize().mul(Math.random() * radius / 2).add(superTile).add(0.5);
		ve.set(superTile).add(0.5).sub(vt).mul(0.05);
		
		Botania.proxy.wispFX(worldObj(), vt.x, vt.y, vt.z,	1, 1, 1, 0.5F, (float) ve.x, (float) ve.y, (float) ve.z, 0.5F);
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
	
	@Override
	public List<Object> getTargets() {
		if (inWG()) return EMPTY_LIST;
		double radius = power * 10, dist = 0, str = 0;
		return allAroundRaw(Entity.class, radius);
	}
	
	@Override
	public void performEffect(Object target) {
		if (!(target instanceof Entity)) return;
		if (target instanceof EntityPlayer && ((EntityPlayer) target).capabilities.disableDamage) return;
		
		Entity entity = (Entity) target;
		double radius = power * 10, dist, str = 0;
		
		ve.set(entity);
		
		if (!ASJUtilities.isServer()) if (entity == Minecraft.getMinecraft().thePlayer) ve.add(0, -1.62, 0);
		
		if ((dist = Math.sqrt(Math.pow(ve.x - x() + 0.5, 2) + Math.pow(ve.y - y() + 0.5, 2) + Math.pow(ve.z - z() + 0.5, 2))) > radius) return;
		
		vt.set(superTile).add(0.5);
		vt.set(vt).sub(ve).normalize().mul(power * 0.5 * 1/dist);
		
		entity.motionX += vt.x;
		entity.motionY += vt.y * 1.25;
		entity.motionZ += vt.z;
	}
	
	@Override
	public int typeBits() {
		return MOTION;
	}
	
	@Override
	public int getStrip() {
		return 0;
	}
	
	@Override
	public EnumAnomalityRarity getRarity() {
		return EnumAnomalityRarity.COMMON;
	}
}