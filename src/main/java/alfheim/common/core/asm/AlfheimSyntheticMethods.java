package alfheim.common.core.asm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.common.block.tile.mana.TilePool;

public class AlfheimSyntheticMethods {
	
	public static void onFinishedPotionEffect(EntityLivingBase e, PotionEffect pe) {
		// e.public_onFinishedPotionEffect(pe);
	}
	
	public static void onChangedPotionEffect(EntityLivingBase e, PotionEffect pe, boolean isNew) {
		// e.public_onChangedPotionEffect(pe, isNew);
	}
	
	public static boolean canAccept(TilePool te) {
		return te.alchemy;
		// return te.canAccept;
	}
	
	public static boolean canSpare(TilePool te) {
		return te.conjuration;
		// return te.canSpare;
	}
	
	public static void allowUpdate(Entity e) {
		e.isDead = false;
		// e.cantUpdateE = false;
	}
	
	public static void allowUpdate(TileEntity e) {
		e.blockMetadata = 0;
		// e.cantUpdateT = false;
	}
	
	public static void denyUpdate(Entity e) {
		e.isDead = true;
		// e.cantUpdateE = true;
	}
	
	public static void denyUpdate(TileEntity e) {
		e.blockMetadata = 1;
		// e.cantUpdateT = true;
	}
	
	public static boolean cantUpdate(Entity e) {
		return e.isDead;
		// return e.cantUpdateE;
	}
	
	public static boolean cantUpdate(TileEntity e) {
		return e.canUpdate();
		// return e.cantUpdateT;
	}
}
