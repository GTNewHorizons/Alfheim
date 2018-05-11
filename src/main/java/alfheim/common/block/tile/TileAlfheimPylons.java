package alfheim.common.block.tile;

import java.util.Random;

import alfheim.common.core.registry.AlfheimBlocks;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;

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
				worldTime += new Random(xCoord ^ yCoord ^ zCoord).nextInt(1000);
				worldTime /= 5;
	
				float r = 0.75F + (float) Math.random() * 0.05F;
				double x = xCoord + 0.5 + Math.cos(worldTime) * r;
				double z = zCoord + 0.5 + Math.sin(worldTime) * r;
	
				Vector3 ourCoords = new Vector3(x, yCoord + 0.25, z);
				centerBlock.sub(new Vector3(0, 0.5, 0));
				Vector3 movementVector = centerBlock.sub(ourCoords).normalize().multiply(0.2);
	
				Botania.proxy.wispFX(worldObj, x, yCoord + 0.25, z, 0.75F + (float) Math.random() * 0.25F, 0.5F + (float) Math.random() * 0.25F, (meta == 0 ? 0.75F : 0) + (float) Math.random() * 0.25F, 0.25F + (float) Math.random() * 0.1F, -0.075F - (float) Math.random() * 0.015F);
				if(worldObj.rand.nextInt(3) == 0)
					Botania.proxy.wispFX(worldObj, x, yCoord + 0.25, z, 0.75F + (float) Math.random() * 0.25F, 0.5F + (float) Math.random() * 0.25F, (meta == 0 ? 0.75F : 0) + (float) Math.random() * 0.25F, 0.25F + (float) Math.random() * 0.1F, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
			}
		}

		if(worldObj.rand.nextBoolean() && worldObj.isRemote)
			Botania.proxy.sparkleFX(worldObj, xCoord + Math.random(), yCoord + Math.random() * 1.5, zCoord + Math.random(), 1.0F, 0.5F, meta == 0 ? 1.0F : 0F, (float) Math.random(), 2);
	}
}
