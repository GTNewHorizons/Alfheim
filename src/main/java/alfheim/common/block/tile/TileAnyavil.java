package alfheim.common.block.tile;

import java.awt.Color;
import java.util.List;

import alexsocol.asjlib.extendables.ItemContainingTileEntity;
import alfheim.api.AlfheimAPI;
import alfheim.common.core.registry.AlfheimBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.mana.TilePool;

public class TileAnyavil extends ItemContainingTileEntity {

	public static final int MAX_PINK_CHARGE = TilePool.MAX_MANA_DILLUTED;
	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_CAP = "manaCap";
	private static final String TAG_METADATA = "metadata";

	public int pinkCharge = 0;
	
	public void onBurstCollision(IManaBurst burst, World world, int x, int y, int z) {
		if (burst.isFake()) return;
		if (this.item == null) return;
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
			pinkCharge = MAX_PINK_CHARGE;
			for (; extraPink > 0; extraPink--) {
				Botania.proxy.wispFX(world, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, col[0], col[1], col[2], 0.25F, worldObj.rand.nextFloat() / 5.0F - 0.1F, worldObj.rand.nextFloat() / 5.0F - 0.1F, worldObj.rand.nextFloat() / 5.0F - 0.1F);
			}
		}
		
		int needed = item.getItemDamage();
		int transfer = Math.max(0, Math.min(needed, pinkCharge));
		pinkCharge -= transfer;
		item.setItemDamage(item.getItemDamage() - transfer);
		
		for (int i = 0; i < 24; i++) Botania.proxy.wispFX(world, xCoord + 0.5 + (worldObj.rand.nextFloat() / 5.0F - 0.1F), yCoord + 1.5, zCoord + 0.5 + (worldObj.rand.nextFloat() / 5.0F - 0.1F), col[0], col[1], col[2], 0.25F, 0, worldObj.rand.nextFloat() / 5.0F - 0.1F, 0);
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
}