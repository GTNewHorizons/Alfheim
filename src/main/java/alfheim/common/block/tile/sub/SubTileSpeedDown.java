package alfheim.common.block.tile.sub;

import java.util.List;

import alfheim.api.block.tile.SubTileEntity;
import alfheim.api.block.tile.SubTileEntity.EnumAnomalityRarity;
import alfheim.common.block.tile.TileAnomaly;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.common.Botania;

public class SubTileSpeedDown extends SubTileEntity {
	
	public static final int radius = 8;
	
	@Override
	protected void update() {
		if (inWG()) return;
		
		rand.setSeed(x() ^ y() ^ z());
		double worldTime = (worldObj().getTotalWorldTime() + rand.nextInt(1000)) / 10.0;
		float r = 0.75F + (float) Math.random() * 0.05F;
		
		Botania.proxy.wispFX(worldObj(), x() + 0.5, y() + 0.5 + Math.cos(worldTime) * r, z() + 0.5 + Math.sin(worldTime) * r,
				0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
		
		Botania.proxy.wispFX(worldObj(), x() + 0.5 + Math.sin(worldTime) * r, y() + 0.5, z() + 0.5 + Math.cos(worldTime) * r,
				0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
		
		Botania.proxy.wispFX(worldObj(), x() + 0.5 + Math.cos(worldTime) * r, y() + 0.5 + Math.sin(worldTime) * r, z() + 0.5,
				0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F,
				0.1F + (float) Math.random() * 0.1F);
	}
	
	@Override
	public List<Object> getTargets() {
		if (inWG() || worldObj().getTotalWorldTime() % 2 == 0) return EMPTY_LIST;
		List<Object> l = allAroundRaw(Entity.class, 8);
		for (int x = -radius; x <= radius; x++) 
			for (int z = -radius; z <= radius; z++) 
				for (int y = -radius; y <= radius; y++) {
					if (x == 0 && y == 0 && z == 0) continue;
					TileEntity t = worldObj().getTileEntity(x(x), y(y), z(z));
					if (t != null && t.canUpdate() && !t.isInvalid() && !(t instanceof TileAnomaly)) l.add(t);
				}
		
		return l;
	}
	
	@Override
	public void performEffect(Object target) {
		if (target instanceof Entity) ((Entity) target).canEntityUpdate = false;
		if (target instanceof TileEntity) ((TileEntity) target).canTileUpdate = false;
	}
	
	@Override
	public int typeBits() {
		return TIME;
	}
	
	@Override
	public int getStrip() {
		return 5;
	}
	
	@Override
	public EnumAnomalityRarity getRarity() {
		return EnumAnomalityRarity.EPIC;
	}
}
