package alfheim.common.potion;

import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.utils.AlfheimConfig;
import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import vazkii.botania.common.brew.potion.PotionMod;

public class PotionPossession extends PotionAlfheim {

	public PotionPossession() {
		super(AlfheimConfig.potionIDPossession, "possession", true, 0xCC0000, 0);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent e) {
		if (hasEffect(e.entityLiving) && e.entityLiving.getActivePotionEffect(this).getDuration() > 1200 && e.entityLiving.worldObj.getTotalWorldTime() % 20 == 0) e.entityLiving.attackEntityFrom(DamageSource.outOfWorld, (float) (e.entityLiving.getActivePotionEffect(this).getDuration() - 1200) / 400.0F);
	}
}
