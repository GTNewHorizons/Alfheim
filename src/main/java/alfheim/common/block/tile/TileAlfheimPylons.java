package alfheim.common.block.tile;

import java.util.Random;

import alexsocol.asjlib.math.Vector3;
import alfheim.common.core.registry.AlfheimBlocks;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;

public class TileAlfheimPylons extends TileEntity {

	boolean activated = false;
	int centerX, centerY, centerZ;
	int ticks = 0;

	@Override
	public void updateEntity() {
		++ticks;
		int meta = getBlockMetadata();

		if(activated && worldObj.isRemote) {
			if(worldObj.getBlock(centerX, centerY, centerZ) != AlfheimBlocks.tradePortal || worldObj.getBlockMetadata(centerX, centerY, centerZ) == 0) {
				activated = false;
				return;
			}

			Vector3 centerBlock = new Vector3(centerX + 0.5, centerY + 0.75 + (Math.random() - 0.5 * 0.25), centerZ + 0.5);

			if(ConfigHandler.elfPortalParticlesEnabled) {
				double worldTime = ticks;
				worldTime += Math.random() * 1000;
				worldTime /= 5;
	
				float r = 0.75F + (float) Math.random() * 0.05F;
				double x = xCoord + 0.5 + Math.cos(worldTime) * r;
				double z = zCoord + 0.5 + Math.sin(worldTime) * r;
	
				centerBlock.sub(0, 0.5, 0).sub(x, yCoord + 0.75, z).normalize().mul(0.2);
	
				Botania.proxy.wispFX(worldObj, x, yCoord + 0.25, z, 0.75F + (float) Math.random() * 0.25F, 0.5F + (float) Math.random() * 0.25F, (meta == 0 ? 0.75F : 0) + (float) Math.random() * 0.25F, 0.25F + (float) Math.random() * 0.1F, -0.075F - (float) Math.random() * 0.015F);
				if(worldObj.rand.nextInt(3) == 0)
					Botania.proxy.wispFX(worldObj, x, yCoord + 0.25, z, 0.75F + (float) Math.random() * 0.25F, 0.5F + (float) Math.random() * 0.25F, (meta == 0 ? 0.75F : 0) + (float) Math.random() * 0.25F, 0.25F + (float) Math.random() * 0.1F, (float) centerBlock.x, (float) centerBlock.y, (float) centerBlock.z);
			}
		}

		if(worldObj.rand.nextBoolean() && worldObj.isRemote)
			Botania.proxy.sparkleFX(worldObj, xCoord + Math.random(), yCoord + Math.random() * 1.5, zCoord + Math.random(), 1.0F, 0.5F, meta == 0 ? 1.0F : 0F, (float) Math.random(), 2);
	}
}
