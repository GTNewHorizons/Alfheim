package alfheim.common.item;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.common.registry.AlfheimItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.mana.ManaItemHandler;

public class ElementalFireLeggings extends ElementalArmor {

	public ElementalFireLeggings() {
		super(2, "ElementalFireLeggings");
		this.setCreativeTab(AlfheimCore.alfheimTab);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.15F;
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if (armorType == 2 && player.getCurrentArmor(1) != null && player.getCurrentArmor(1).getItem() == AlfheimItems.elementalLeggings) {
			if ((player.motionX != 0 || player.motionZ != 0) && ManaItemHandler.requestManaExact(player.getCurrentArmor(1), player, 1, !world.isRemote)) player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 1, 1));
			if (player.isBurning()) player.extinguish();
		}
		
    }
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		list.add(StatCollector.translateToLocal("item.ElementalArmor.desc2"));
		super.addInformation(stack, player, list, b);
	}
}
