package alfheim.common.item.material;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.block.tile.TileTransferer;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.entity.EnumRace;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.common.entity.EntityManaBurst;

public class ItemElvenResource extends Item implements IElvenItem/*, ILensEffect FIXME*/ {
	
	public static final String[] subItems = new String[] { "InterdimensionalGatewayCore", "ManaInfusionCore", "ElvoriumIngot", "MauftriumIngot", "MuspelheimPowerIngot", "NiflheimPowerIngot", "ElvoriumNugget", "MauftriumNugget", "MuspelheimEssence", "NiflheimEssence", "IffesalDust", "PrimalRune", "MuspelheimRune", "NiflheimRune" /*"Transferer"*/ /*"InfusedDreamwoodTwig"*/ /*"TheRodOfTheDebug"*/ };
	private IIcon[] texture = new IIcon[subItems.length];
	
	public ItemElvenResource() {
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("ElvenItems");
	}
	
	public void registerIcons(IIconRegister iconRegister){
		for (int i = 0; i < subItems.length; i++){
			texture[i] = iconRegister.registerIcon(ModInfo.MODID + ":materials/" + subItems[i]);
		}
	}

	public IIcon getIconFromDamage(int i) {
		if (i < texture.length) {
			return texture[i];
		} else {
			return texture[0];
		}
	}

	public String getUnlocalizedName(ItemStack stack) {
		if (stack.getItemDamage() < subItems.length) {
			return "item." + subItems[stack.getItemDamage()];
		} else {
			return subItems[0];
		}
	}

	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < subItems.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (/*!world.isRemote && */stack.getItemDamage() == subItems.length - 1) {
			if (!player.isSneaking()) {
				EnumRace.setRaceID(player, (EnumRace.getRace(player).ordinal() + 1) % 11);
				ASJUtilities.chatLog(EnumRace.getRace(player).ordinal() + " - " + EnumRace.getRace(player).toString(), player);
				//ASJUtilities.sendToDimensionWithoutPortal(player, 0, player.posX, 228, player.posZ);
			} else {
				player.addChatComponentMessage(new ChatComponentText("Current dimension id: " + player.dimension));
			}
		}
		return stack;
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return stack.getItemDamage() == ElvenResourcesMetas.InterdimensionalGatewayCore;
	}

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
	} FIXME*/
}
