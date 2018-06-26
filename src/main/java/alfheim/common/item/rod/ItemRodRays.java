package alfheim.common.item.rod;

import java.awt.Color;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.ModItems;

public class ItemRodRays extends ItemRodBase implements IManaUsingItem {
	
	private static final String TAG_ATTACKER_USERNAME = "attackerUsername";
	private static final int MANA_PER_DAMAGE = 100;

	public ItemRodRays() {
		super("RodRays");
	}

	@Override
	public void cast(ItemStack stack, World world, EntityPlayer player) {
		for (int i = 0; i < 360; i += 15) {
			if (ManaItemHandler.requestManaExactForTool(stack, player, MANA_PER_DAMAGE, world.isRemote)) {
				if (!world.isRemote) player.worldObj.spawnEntityInWorld(getBurst(player, i));
				player.worldObj.playSoundAtEntity(player, "botania:terraBlade", 0.4F, 1.4F);
			}
		}
	}	
	
	public EntityManaBurst getBurst(EntityPlayer player, int i) {
		EntityManaBurst burst = new EntityManaBurst(player.worldObj);

		burst.setColor(new Color(179, 77, 179).getRGB());
		burst.setMana(1);
		burst.setStartingMana(1);
		burst.setMinManaLoss(600);
		burst.setManaLossPerTick(4F);
		burst.setGravity(0F);

		ItemStack lens = new ItemStack(ModItems.terraSword, 1, 0);
		ItemNBTHelper.setString(lens, TAG_ATTACKER_USERNAME, player.getCommandSenderName());
		burst.setSourceLens(lens);
		
		burst.setBurstSourceCoords(0, -1, 0);
		burst.setLocationAndAngles(player.posX, player.posY + player.getEyeHeight(), player.posZ, i, -player.rotationPitch);

		burst.posX -= MathHelper.cos((i) / 180.0F * (float) Math.PI) / 2.0;
		burst.posY -= 0.1;
		burst.posZ -= MathHelper.sin((i) / 180.0F * (float) Math.PI) / 2.0;

		burst.setPosition(burst.posX, burst.posY, burst.posZ);
		burst.yOffset = 0.0F;
		float f = 0.4F;
		double mx = MathHelper.sin(burst.rotationYaw / 180.0F * (float) Math.PI) * f / 2.0;
		double mz = -(MathHelper.cos(burst.rotationYaw / 180.0F * (float) Math.PI) * f) / 2.0;
		burst.setMotion(mx * 5, 0, mz * 5);
		
		return burst;
	}
}