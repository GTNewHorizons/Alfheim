package alfheim.common.block.tile.sub;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.block.tile.SubTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;

public class SubTileManaVoid extends SubTileEntity {

	public static final String TAG_MANA = "mana";
	public static int radius = 10;
	public int mana;
	Vector3 v = new Vector3();
	
	@Override
	public void update() {
		if (mana >= 120000) {
			for (EntityPlayer player : allAround(EntityPlayer.class, radius)) {
				radius = 50;
				for (int i = 0; i < 100; i++) performEffect(player);
			}
			
			superTile.getWorldObj().createExplosion(null, superTile.xCoord, superTile.yCoord, superTile.zCoord, radius = 10, false);
			
			for (int i = 0; i < 128; i++) {
				v.rand().sub(0.5).normalize().mul(Math.random() * 0.1);
				Botania.proxy.wispFX(superTile.getWorldObj(), superTile.xCoord + 0.5, superTile.yCoord + 0.5, superTile.zCoord + 0.5, 0.01F, 0.75F, 1, 0.25F, (float) v.x, (float) v.y, (float) v.z, 2);
			}
			
			mana = 0;
		}
	}
	
	@Override
	public List<Object> getTargets() {
		return allAroundRaw(EntityPlayer.class, radius);
	}

	@Override
	public void performEffect(Object target) {
		if (target == null || !(target instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) target;
		
		int m = ManaItemHandler.requestMana(new ItemStack(Blocks.stone), player, 100, true);
		if (m > 0) {
			mana += m;
			
			boolean flag = false;
			if (!ASJUtilities.isServer()) flag = Minecraft.getMinecraft().thePlayer != player;
			
			double l = v.set(superTile).add(0.5).sub(player.posX, player.posY + (flag ? 1 : -0.62), player.posZ).length();
			v.normalize().mul(l / 40);
			Botania.proxy.wispFX(superTile.getWorldObj(), player.posX, player.posY + (flag ? 1 : -0.62), player.posZ, 0.01F, 0.75F, 1, radius / 40F, (float) v.x, (float) v.y, (float) v.z, 2);
		}
	}

	@Override
	public int typeBits() {
		return MANA;
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setInteger(TAG_MANA, mana);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		mana = cmp.getInteger(TAG_MANA);
	}
}
