package alfheim.common.block.tile.sub;

import java.awt.Color;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.block.tile.SubTileEntity;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.entity.EntityManaStorm;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.lens.ItemLens;

public class SubTileManaTornado extends SubTileEntity {

	Vector3 v = new Vector3();
	
	@Override
	public void update() {
		if (superTile.getWorldObj().rand.nextInt(100) == 0) spawnBurst();
		
		int c = ASJUtilities.colorCode[superTile.getWorldObj().rand.nextInt(ASJUtilities.colorCode.length)];
		v.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(Math.random()).add(superTile.xCoord + 0.5, superTile.yCoord + 0.5, superTile.zCoord + 0.5);
		Botania.proxy.wispFX(superTile.getWorldObj(), v.x, v.y, v.z, ((c >> 16) & 0xFF) / 255F, ((c >> 8) & 0xFF) / 255F, (c & 0xFF) / 255F, (float) (Math.random() * 0.25 + 0.25), 0, (float) (Math.random() * 2 + 1));
	}
	
	public void spawnBurst() {
		EntityManaBurst burst = new EntityManaBurst(superTile.getWorldObj());
		float motionModifier = 0.5F;
		burst.setColor(ASJUtilities.colorCode[superTile.getWorldObj().rand.nextInt(ASJUtilities.colorCode.length)]);
		burst.setMana(120);
		burst.setStartingMana(340);
		burst.setMinManaLoss(50);
		burst.setManaLossPerTick(1F);
		burst.setGravity(0F);

		int meta = superTile.getWorldObj().rand.nextInt(ItemLens.SUBTYPES + 1);
		if (meta == ItemLens.SUBTYPES) meta = ItemLens.STORM;
		
		ItemStack lens = new ItemStack(ModItems.lens, 1, meta);
		burst.setSourceLens(lens);

		v.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(motionModifier);
		burst.setPosition(superTile.xCoord + 0.5 + v.x, superTile.yCoord + 0.5 + v.y, superTile.zCoord + 0.5 + v.z);
		burst.setMotion(v.x, v.y, v.z);
		superTile.getWorldObj().spawnEntityInWorld(burst);
	}
}