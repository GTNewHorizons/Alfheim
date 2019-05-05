package alfheim.common.block.tile;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.extendables.ItemContainingTileEntity;
import alfheim.AlfheimCore;
import alfheim.common.core.asm.AlfheimSyntheticMethods;
import alfheim.common.network.MessageTileItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.common.block.tile.mana.TileBellows;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibMisc;

public class TileItemHolder extends ItemContainingTileEntity {
	
	// for some reason it updates as many times per tick as many times you right-click on it
	private long lastTick = 0;
	
	@Override
	public void updateEntity() {
		long tick = worldObj.getTotalWorldTime();
		if (lastTick == tick) return;
			
		TileEntity te = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
		if (te == null || !(te instanceof TilePool)) {
			if (worldObj.isRemote) return;
			worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, new ItemStack(worldObj.getBlock(xCoord, yCoord, zCoord), 1, worldObj.getBlockMetadata(xCoord, yCoord, zCoord))));
			if (getItem() != null) worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, getItem().copy()));
			setItem(null);
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			return;
		}
		
		TilePool pool = (TilePool) te;
		
		ItemStack stack = getItem();
		if (getItem() == null) return;
		
		if (getItem().getItem() instanceof IManaItem) {
			IManaItem mana = (IManaItem) getItem().getItem();
			
			if(pool.isOutputtingPower() && mana.canReceiveManaFromPool(stack, pool) || !pool.isOutputtingPower() && mana.canExportManaToPool(stack, pool)) {
				boolean didSomething = false;
				
				int bellowCount = 0;
				if(pool.isOutputtingPower())
					for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) {
						TileEntity tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord - 1, zCoord + dir.offsetZ);
						if(tile != null && tile instanceof TileBellows && ((TileBellows) tile).getLinkedTile() == pool)
							bellowCount++;
					}
				int transfRate = 1000 * (bellowCount + 2);
				
				if(pool.isOutputtingPower()) {
					if(pool.canSpare) {
						if(pool.getCurrentMana() > 0 && mana.getMana(stack) < mana.getMaxMana(stack))
							didSomething = true;
						
						int manaVal = Math.min(transfRate, Math.min(pool.getCurrentMana(), mana.getMaxMana(stack) - mana.getMana(stack)));
						if(!worldObj.isRemote)
							mana.addMana(stack, manaVal);
						pool.recieveMana(-manaVal);
					}
				} else {
					if(pool.canAccept) {
						if(mana.getMana(stack) > 0 && !pool.isFull())
							didSomething = true;
						
						int manaVal = Math.min(transfRate, Math.min(pool.manaCap - pool.getCurrentMana(), mana.getMana(stack)));
						if(!worldObj.isRemote)
							mana.addMana(stack, -manaVal);
						pool.recieveMana(manaVal);
					}
				}
				
				if(didSomething) {
					if(worldObj.isRemote && ConfigHandler.chargingAnimationEnabled && worldObj.rand.nextInt(100) == 0) {
						Vector3 itemVec = Vector3.fromTileEntity(pool).add(0.5, 0.5 + Math.random() * 0.3, 0.5);
						Vector3 tileVec = Vector3.fromTileEntity(pool).add(0.2 + Math.random() * 0.6, 0, 0.2 + Math.random() * 0.6);
						LightningHandler.spawnLightningBolt(worldObj, pool.isOutputtingPower() ? tileVec : itemVec, pool.isOutputtingPower() ? itemVec : tileVec, 80, worldObj.rand.nextLong(), 0x4400799c, 0x4400C6FF);
					}
					pool.isDoingTransfer = pool.isOutputtingPower();
					if (worldObj.getTotalWorldTime() % 20 == 0) {
						worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
						if (ASJUtilities.isServer()) AlfheimCore.network.sendToDimension(new MessageTileItem(xCoord, yCoord, zCoord, getItem()), worldObj.provider.dimensionId);
					}
				}
			}
		}
		
		lastTick = worldObj.getTotalWorldTime();
	}
}