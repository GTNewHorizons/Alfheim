package alfheim.common.utils;

import java.util.Vector;

import alfheim.common.registry.AlfheimItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.item.ModItems;

public class ManaInfusionRecipies {
	
	public static Vector<IManaInfusionRecipe> recipes = new Vector<IManaInfusionRecipe>();

	static {
		recipes.add(new IManaInfusionRecipe(TilePool.MAX_MANA / 2, new ItemStack(ModItems.manaResource, 1, 4),
				new ItemStack[] {new ItemStack(ModItems.manaResource, 1, 0), new ItemStack(ModItems.manaResource, 1, 1), new ItemStack(ModItems.manaResource, 1, 2)}));
		recipes.add(new IManaInfusionRecipe(TilePool.MAX_MANA / 2, new ItemStack(AlfheimItems.elvenItems, 1, 1),
				new ItemStack[] {new ItemStack(ModItems.manaResource, 1, 7), new ItemStack(ModItems.manaResource, 1, 8), new ItemStack(ModItems.manaResource, 1, 9)}));
		recipes.add(new IManaInfusionRecipe(TilePool.MAX_MANA	 , new ItemStack(AlfheimItems.manaStone, 1, 1000),
				new ItemStack[] {new ItemStack(AlfheimItems.elvenItems, 4, 9), new ItemStack(ModItems.manaResource, 1, 9)}));
		recipes.add(new IManaInfusionRecipe(TilePool.MAX_MANA * 4, new ItemStack(AlfheimItems.manaStoneGreater, 1, 1000),
				new ItemStack[] {new ItemStack(AlfheimItems.elvenItems, 2, 7), new ItemStack(AlfheimItems.elvenItems, 2, 8), new ItemStack(AlfheimItems.manaStone, 1, OreDictionary.WILDCARD_VALUE)}));
	}
}
