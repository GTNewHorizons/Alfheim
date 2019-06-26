package alfheim.common.item.material

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import vazkii.botania.common.Botania

class ItemElvenResource: Item /*implements IElvenItem, ILensEffect BACK*/() {
	private val texture = arrayOfNulls<IIcon>(subItems.size)
	
	init {
		this.creativeTab = AlfheimCore.alfheimTab
		this.setHasSubtypes(true)
		this.unlocalizedName = "ElvenItems"
	}
	
	override fun registerIcons(reg: IIconRegister) {
		for (i in subItems.indices)
			texture[i] = reg.registerIcon(ModInfo.MODID + ":materials/" + subItems[i])
		
		harp = reg.registerIcon(ModInfo.MODID + ":misc/harp")
		mine = reg.registerIcon(ModInfo.MODID + ":misc/mine")
		wind = reg.registerIcon(ModInfo.MODID + ":misc/wind")
		wing = reg.registerIcon(ModInfo.MODID + ":misc/wing")
	}
	
	override fun getIconFromDamage(meta: Int): IIcon {
		return texture[Math.max(0, Math.min(meta, texture.size - 1))]
	}
	
	override fun getUnlocalizedName(stack: ItemStack): String {
		return "item." + subItems[Math.max(0, Math.min(stack.itemDamage, subItems.size - 1))]
	}
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<*>) {
		for (i in subItems.indices) {
			if (i == ElvenResourcesMetas.InfusedDreamwoodTwig && !Botania.thaumcraftLoaded) continue
			list.add(ItemStack(item, 1, i))
		}
	}
	
	companion object {
		
		val subItems = arrayOf("InterdimensionalGatewayCore", "ManaInfusionCore", "ElvoriumIngot", "MauftriumIngot", "MuspelheimPowerIngot", "NiflheimPowerIngot", "ElvoriumNugget", "MauftriumNugget", "MuspelheimEssence", "NiflheimEssence", "IffesalDust", "PrimalRune", "MuspelheimRune", "NiflheimRune", "InfusedDreamwoodTwig"/*, "Transferer" BACK*/)
		
		var harp: IIcon? = null
		var mine: IIcon? = null
		var wind: IIcon? = null
		var wing: IIcon? = null
	}
	/*
	@Override
	public boolean isElvenItem(ItemStack stack) {
		return stack.getItemDamage() == ElvenResourcesMetas.InterdimensionalGatewayCore;
	}
	
	@Override
	public void apply(ItemStack stack, BurstProperties props) {}
	
	@Override
	public boolean collideBurst(IManaBurst burst, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if (stack.getItemDamage() != ElvenResourcesMetas.Transferer) return true;
		
		EntityManaBurst entity = (EntityManaBurst) burst;
		if (!entity.worldObj.isRemote && pos.typeOfHit == MovingObjectType.BLOCK && entity.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ) == Blocks.sponge) {
			EntityItem item = new EntityItem(entity.worldObj, pos.blockX, pos.blockY, pos.blockZ, TileTransferer.getStack(entity).copy());
			entity.worldObj.spawnEntityInWorld(item);
			return true;
		}
		return false;
	}
	
	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		if (stack.getItemDamage() != ElvenResourcesMetas.Transferer) ((EntityThrowable) burst).setDead();
	}
	
	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		return stack.getItemDamage() == ElvenResourcesMetas.Transferer;
	}BACK*/
}
