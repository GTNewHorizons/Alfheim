package alfheim.common.core.asm;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import vazkii.botania.common.block.tile.mana.TilePool;

public class AlfheimSyntheticMethods {

	public static void onFinishedPotionEffect(EntityLivingBase e, PotionEffect pe) {
		// e.public_onFinishedPotionEffect(pe);
	}

	public static void onChangedPotionEffect(EntityLivingBase e, PotionEffect pe, boolean isNew) {
		// e.public_onChangedPotionEffect(pe, isNew);
	}
	
	public static boolean canAccept(TilePool te) {
		return te.alchemy; // just because of respect for the compiler
		// return te.canAccept;
	}
	
	public static boolean canSpare(TilePool te) {
		return te.conjuration; // just because of respect for the compiler
		// return te.canSpare;
	}
}
