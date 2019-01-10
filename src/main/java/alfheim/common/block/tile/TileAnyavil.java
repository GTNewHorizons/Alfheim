package alfheim.common.block.tile;

import java.awt.Color;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.extendables.ItemContainingTileEntity;
import alexsocol.asjlib.math.Vector3;
import alfheim.AlfheimCore;
import alfheim.api.AlfheimAPI;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.network.MessageTileItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.mana.TilePool;

public class TileAnyavil extends ItemContainingTileEntity implements ISidedInventory {

	public static final int MAX_PINK_CHARGE = TilePool.MAX_MANA_DILLUTED;
	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_CAP = "manaCap";
	private static final String TAG_METADATA = "metadata";

	public int pinkCharge = 0;
	
	public void onBurstCollision(IManaBurst burst, World world, int x, int y, int z) {
		if (burst.isFake()) return;
		if (getItem() == null) return;
		if (burst.getColor() != 0xFFF280A6) return;
		List<EntityItem> eitems = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord + 2, zCoord + 2).expand(5, 3, 5));
		for (EntityItem eitem : eitems) {
			if (eitem.isDead) continue;
			ItemStack item = eitem.getEntityItem();
			int pinkness = AlfheimAPI.getPinkness(item);
			if (pinkness > 0) {
				pinkCharge += (pinkness * item.stackSize);
				eitem.setDead();
			}
		}
		
		int extraPink = Math.max(0, pinkCharge - MAX_PINK_CHARGE);
		float[] col = EntitySheep.fleeceColorTable[6];
		if (extraPink > 0) {
			extraPink = Math.min(extraPink, 4000);
			pinkCharge = MAX_PINK_CHARGE;
			Vector3 m = new Vector3();
			for (; extraPink > 0; extraPink--) {
				m.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(0.1);
				Botania.proxy.wispFX(world, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, col[0], col[1], col[2], 0.25F, (float) m.x, (float) m.y, (float) m.z);
			}
		}
		
		int needed = getItem().getItemDamage();
		int transfer = Math.max(0, Math.min(needed, pinkCharge));
		pinkCharge -= transfer;
		getItem().setItemDamage(getItem().getItemDamage() - transfer);
		
		for (int i = 0; i < 24; i++) Botania.proxy.wispFX(world, xCoord + 0.5 + (worldObj.rand.nextFloat() / 5.0F - 0.1F), yCoord + 1.5, zCoord + 0.5 + (worldObj.rand.nextFloat() / 5.0F - 0.1F), col[0], col[1], col[2], 0.25F, 0, worldObj.rand.nextFloat() * 0.2F - 0.1F, 0);
	}

	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		String name = AlfheimBlocks.anyavil.getLocalizedName();
		float[] col = EntitySheep.fleeceColorTable[6];
		int color = new Color(col[0], col[1], col[2]).getRGB();
		BotaniaAPI.internalHandler.drawSimpleManaHUD(color, pinkCharge, MAX_PINK_CHARGE, name, res);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt) {
		super.writeCustomNBT(nbt);
		nbt.setInteger(TAG_MANA, pinkCharge);
		nbt.setInteger(TAG_MANA_CAP, MAX_PINK_CHARGE);
		nbt.setInteger(TAG_METADATA, blockMetadata);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt) {
		super.readCustomNBT(nbt);
		pinkCharge = nbt.getInteger(TAG_MANA);
		this.blockMetadata = nbt.getInteger(TAG_METADATA);
	}
	
	@Override
	public void setItem(ItemStack stack) {
		super.setItem(stack);
		if (ASJUtilities.isServer() && worldObj != null) {
			AlfheimCore.network.sendToDimension(new MessageTileItem(xCoord, yCoord, zCoord, getItem()), worldObj.provider.dimensionId);
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return getItem();
	}

	@Override
	public ItemStack decrStackSize(int slot, int ammount) {
		if (getStackInSlot(slot) != null) {
            ItemStack itemstack;

            if (getStackInSlot(slot).stackSize <= ammount){
                itemstack = getStackInSlot(slot);
                setInventorySlotContents(slot, null);
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(slot).splitStack(ammount);
                if (this.getStackInSlot(slot).stackSize == 0) setInventorySlotContents(slot, null);
                return itemstack;
            }
        }
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (getStackInSlot(slot) != null) {
            ItemStack itemstack = getStackInSlot(slot);
            setInventorySlotContents(slot, null);
            return itemstack;
        } 
        return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		setItem(stack);
        if (stack != null && stack.stackSize > getInventoryStackLimit())  stack.stackSize = getInventoryStackLimit();
	}

	@Override
	public String getInventoryName() {
		return "container.anyavil";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64.0;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack != null && stack.stackSize == 1 && stack.getItem().isDamageable();
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return side == 1 ? isItemValidForSlot(slot, stack) : false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return side == 0 && slot == 0 ? !stack.isItemDamaged() : false;
	}
}