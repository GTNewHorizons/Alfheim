package alfheim.common.item.equipment.bauble;

import alfheim.AlfheimCore;
import baubles.api.BaubleType;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class CreativeReachPendant extends ItemPendant {

	public CreativeReachPendant() {
		super("CreativeReachPendant");
	}

	@Override
	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
		Botania.proxy.setExtraReach(player, 100F);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		Botania.proxy.setExtraReach(player, -100F);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack stack) {
		String s = this.getUnlocalizedName(stack);
		return s == null ? "" : StatCollector.translateToLocal(s);
	}
	
	@Override
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		// NO-OP
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		itemIcon = IconHelper.forItem(reg, this);
	}
}
