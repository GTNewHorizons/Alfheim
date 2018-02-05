package alfheim.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.ModInfo;
import alfheim.api.crafting.recipe.IManaInfusionRecipe;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.core.utils.AlfheimConfig;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

public class AlfheimAPI {
	public static final ArmorMaterial ELVORIUM = EnumHelper.addArmorMaterial("ELVORIUM", 50, new int[] {5, 10, 8, 5}, 30);
	public static final ArmorMaterial ELEMENTAL = EnumHelper.addArmorMaterial("ELEMENTAL", 20, new int[] {2, 9, 5, 2}, 20);
	public static final ToolMaterial REALITY = EnumHelper.addToolMaterial("REALITY", 10, 9000, 3, 8, 30);

	public static final IAttribute RACE = new BaseAttribute(ModInfo.MODID.toUpperCase() + ":RACE", 0) {
		@Override
		public double clampValue(double d) {
			return d;
		}
	}.setShouldWatch(true);
	
	public static final IAttribute FLIGHT = new BaseAttribute(ModInfo.MODID.toUpperCase() + ":FLIGHT", AlfheimConfig.flightTime) { 
		
		@Override
		public double clampValue(double d) {
			return Math.max(0, Math.min(AlfheimConfig.flightTime, d));
		}
	}.setShouldWatch(true);
	
	/** List of {@link RecipeElvenTrade} outputs forbidden for re'trading from Alfheim trade portal */
	public static List<ItemStack> forbiddenRetrades = new ArrayList();
	/** List of recipies for mana infuser */
	public static List<IManaInfusionRecipe> manaInfusionRecipes = new ArrayList<IManaInfusionRecipe>();
	/** List of all pink items with their relative pinkness */
	public static HashMap<ItemStack, Integer> pinkness = new HashMap<ItemStack, Integer>();
	
	public static IManaInfusionRecipe addInfusionRecipe(IManaInfusionRecipe rec) {
		manaInfusionRecipes.add(rec);
		return rec;
	}
	
	public static IManaInfusionRecipe addInfusionRecipe(ItemStack result, int mana, ItemStack... ingredients) {
		IManaInfusionRecipe rec = new IManaInfusionRecipe(mana, result, ingredients);
		manaInfusionRecipes.add(rec);
		return rec;
	}
	
	public static boolean removeInfusionRecipeByResult(ItemStack result) {
		for (int i = 0; i < manaInfusionRecipes.size(); i++) if (ItemStack.areItemStacksEqual(manaInfusionRecipes.get(i).getOutput(), result)) {
			manaInfusionRecipes.remove(i);
			return true;
		}
		return false;
	}
	
	public static void addForbiddenRetrade(ItemStack output) {
		forbiddenRetrades.add(output);
	}
	
	public static boolean isRetradeForbidden(ItemStack output) {
		for (ItemStack out : forbiddenRetrades) if (ItemStack.areItemStacksEqual(output, out)) return true;
		return false;
	}
	
	public static void addPink(ItemStack pink, int weight) {
		pinkness.put(pink, Integer.valueOf(weight));
	}
	
	public static int getPinkness(ItemStack item) {
		for (int i = 0; i < pinkness.size(); i++) {
			ItemStack pink = ASJUtilities.mapGetKeyId(pinkness, i);
			if (pink.getItem().equals(item.getItem()) && pink.getItemDamage() == item.getItemDamage()) return ASJUtilities.mapGetValueId(pinkness, i);
		}
		return 0;
	}
}
