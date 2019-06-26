package alfheim.common.block.tile.sub;

import java.util.ArrayList;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.block.tile.SubTileEntity;
import alfheim.api.block.tile.SubTileEntity.EnumAnomalityRarity;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.lens.ItemLens;

public class SubTileManaTornado extends SubTileEntity {
	
	final Vector3 v = new Vector3();
	
	@Override
	public void update() {
		if (inWG()) return;
		
		int c = ASJUtilities.colorCode[worldObj().rand.nextInt(ASJUtilities.colorCode.length)];
		v.rand().sub(0.5).normalize().mul(Math.random()).add(superTile).add(0.5);
		Botania.proxy.wispFX(worldObj(), v.x, v.y, v.z, ((c >> 16) & 0xFF) / 255F, ((c >> 8) & 0xFF) / 255F, (c & 0xFF) / 255F, (float) (Math.random() * 0.25 + 0.25), 0, (float) (Math.random() * 2 + 1));
	}
	
	public EntityManaBurst spawnBurst() {
		EntityManaBurst burst = new EntityManaBurst(worldObj());
		float motionModifier = 0.5F;
		burst.setColor(ASJUtilities.colorCode[worldObj().rand.nextInt(ASJUtilities.colorCode.length)]);
		burst.setMana(120);
		burst.setStartingMana(340);
		burst.setMinManaLoss(50);
		burst.setManaLossPerTick(1F);
		burst.setGravity(0F);
		
		int meta = worldObj().rand.nextInt(ItemLens.SUBTYPES + 1);
		if (meta == ItemLens.SUBTYPES) meta = ItemLens.STORM;
		
		ItemStack lens = new ItemStack(ModItems.lens, 1, meta);
		burst.setSourceLens(lens);
		
		v.rand().sub(0.5).normalize().mul(motionModifier).add(0.5).add(superTile);
		burst.setPosition(v.x, v.y, v.z);
		v.sub(0.5).sub(superTile);
		burst.setMotion(v.x, v.y, v.z);
		
		return burst;
	}
	
	@Override
	public List<Object> getTargets() {
		if (worldObj().rand.nextInt(100) == 0) {
			List l = new ArrayList<Object>();
			l.add(spawnBurst());
			return l;
		}
		return EMPTY_LIST;
	}
	
	@Override
	public void performEffect(Object target) {
		if (target instanceof EntityManaBurst) worldObj().spawnEntityInWorld((EntityManaBurst) target);
	}
	
	@Override
	public int typeBits() {
		return ALL;
	}
	
	@Override
	public int getStrip() {
		return 2;
	}
	
	@Override
	public EnumAnomalityRarity getRarity() {
		return EnumAnomalityRarity.RARE;
	}
}