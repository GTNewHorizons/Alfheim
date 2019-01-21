// This is here since ItemHolyCloak#getRenderTexture is package-private
package vazkii.botania.common.item.equipment.bauble;

import alfheim.AlfheimCore;
import alfheim.api.lib.LibResourceLocations;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.botania.common.Botania;

public class ItemBalanceCloak extends ItemHolyCloak {
	
	public ItemBalanceCloak() {
		super("BalanceCloak");
		setCreativeTab(AlfheimCore.alfheimTab);
	}
	
	@Override
	public boolean effectOnDamage(LivingHurtEvent event, EntityPlayer player, ItemStack stack) {
		if(!event.source.isMagicDamage()) {
			event.ammount /= 2;
			
			if(event.source.getEntity() != null)
				event.source.getEntity().attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, player), event.ammount);
			
			if(event.ammount > player.getHealth())
				event.ammount = player.getHealth() - 1;
			
			player.worldObj.playSoundAtEntity(player, "botania:holyCloak", 1F, 1F);
			
			for(int i = 0; i < 30; i++) {
				double x = player.posX + Math.random() * player.width * 2 - player.width;
				double y = player.posY + Math.random() * player.height;
				double z = player.posZ + Math.random() * player.width * 2 - player.width;
				boolean green = Math.random() > 0.5;
				Botania.proxy.sparkleFX(player.worldObj, x, y, z, 0.3F, green ? 1F : 0.3F, green ? 0.3F : 1F, 0.8F + (float) Math.random() * 0.4F, 3);
			}
			return true;
		}

		return false;
	}
	
	@Override
	public ResourceLocation getRenderTexture() {
		return LibResourceLocations.balanceCloak;
	}
}