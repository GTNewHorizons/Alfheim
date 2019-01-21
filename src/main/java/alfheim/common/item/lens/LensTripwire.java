package alfheim.common.item.lens;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.lens.Lens;

public class LensTripwire extends Lens {
	
	private static final String TAG_TRIPPED = "botania:triwireLensTripped";

	@Override
	public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		if (spreader instanceof TileSpreader) {
			IManaBurst burst = runBurstSimulation((TileSpreader) spreader);
			Entity e = (Entity) burst;
			return e.getEntityData().getBoolean(TAG_TRIPPED);
		}
		return false;
	}

	@Override
	public void updateBurst(IManaBurst burst, EntityThrowable entity, ItemStack stack) {
		if(burst.isFake()) {
			if(entity.worldObj.isRemote) return;

			AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(0.25, 0.25, 0.25);
			List<EntityLivingBase> entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axis);
			if(!entities.isEmpty()) {
				Entity e = (Entity) burst;
				e.getEntityData().setBoolean(TAG_TRIPPED, true);
			}
		}
	}
	
	public IManaBurst runBurstSimulation(TileSpreader spreader) {
		EntityManaBurst fakeBurst = spreader.getBurst(true);
		fakeBurst.setScanBeam();
		fakeBurst.getCollidedTile(true);
		return fakeBurst;
	}
}