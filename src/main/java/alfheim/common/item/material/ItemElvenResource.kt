package alfheim.common.item.material

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.item.AlfheimItems.ElvenResourcesMetas
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*
import net.minecraft.util.IIcon
import vazkii.botania.api.recipe.IElvenItem
import vazkii.botania.common.Botania
import kotlin.math.*

class ItemElvenResource: Item(), IElvenItem/*, ILensEffect*/ {
	private val texture = arrayOfNulls<IIcon>(subItems.size)
	
	init {
		creativeTab = AlfheimCore.alfheimTab
		setHasSubtypes(true)
		unlocalizedName = "ElvenItems"
	}
	
	override fun registerIcons(reg: IIconRegister) {
		for (i in subItems.indices)
			texture[i] = reg.registerIcon(ModInfo.MODID + ":materials/" + subItems[i])
		
		harp = reg.registerIcon(ModInfo.MODID + ":misc/harp")
		mine = reg.registerIcon(ModInfo.MODID + ":misc/mine")
		wind = reg.registerIcon(ModInfo.MODID + ":misc/wind")
		wing = reg.registerIcon(ModInfo.MODID + ":misc/wing")
		
		orn = reg.registerIcon(ModInfo.MODID + ":misc/focus_whatever_orn")
		dep = reg.registerIcon(ModInfo.MODID + ":misc/focus_warding_depth")
	}
	
	override fun getIconFromDamage(meta: Int) = texture[max(0, min(meta, texture.size - 1))]!!
	
	override fun getUnlocalizedName(stack: ItemStack) = "item.${subItems[max(0, min(stack.itemDamage, subItems.size - 1))]}"
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in subItems.indices) {
			if (i == ElvenResourcesMetas.InfusedDreamwoodTwig && !Botania.thaumcraftLoaded) continue
			list.add(ItemStack(item, 1, i))
		}
	}
	
	companion object {
		
		val subItems = arrayOf("InterdimensionalGatewayCore", "ManaInfusionCore", "ElvoriumIngot", "MauftriumIngot", "MuspelheimPowerIngot", "NiflheimPowerIngot", "ElvoriumNugget", "MauftriumNugget", "MuspelheimEssence", "NiflheimEssence", "IffesalDust", "PrimalRune", "MuspelheimRune", "NiflheimRune", "InfusedDreamwoodTwig"/*, "Transferer" BACK*/)
		
		lateinit var harp: IIcon
		lateinit var mine: IIcon
		lateinit var wind: IIcon
		lateinit var wing: IIcon
		
		lateinit var orn: IIcon
		lateinit var dep: IIcon
	}
	
	override fun isElvenItem(stack: ItemStack) = stack.itemDamage == ElvenResourcesMetas.InterdimensionalGatewayCore
	
	/*@Override
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
