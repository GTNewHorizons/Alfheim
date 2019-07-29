package alfheim.api.crafting.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.recipe.RecipePetals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A recipe for the Dendric Suffuser.
 */
public class RecipeTreeCrafting extends RecipePetals {

    private Block outputBlock;
    private List<Object> inputs;
    private int meta;
    private int throttle = -1;
    private int mana;

    public RecipeTreeCrafting(int mana, Block outputBlock, int meta, Object... inputs) {
        super(new ItemStack(outputBlock, 1, meta), inputs);
        this.mana = mana;
        this.outputBlock = outputBlock;
        this.meta = meta;
        this.inputs = new ArrayList<>(Arrays.asList(inputs));
    }

    public RecipeTreeCrafting(int mana, Block outputBlock, int meta, int throttle, Object... inputs) {
        this(mana, outputBlock, meta, inputs);
        this.throttle = throttle;
    }

    public boolean matches(List<ItemStack> items) {
        List<Object> inputsMissing = new ArrayList<>(inputs);

        for (ItemStack i : items) {
            for (int j = 0; j < inputsMissing.size(); j++) {
                Object inp = inputsMissing.get(j);
                if (inp instanceof ItemStack && ((ItemStack) inp).getItemDamage() == 32767)
                    ((ItemStack) inp).setItemDamage(i.getItemDamage());
                if (itemEquals(i, inp)) {
                    inputsMissing.remove(j);
                    break;
                }
            }
        }
        return inputsMissing.isEmpty();
    }

    public Block getOutputBlock() {
        return this.outputBlock;
    }

    public List getInputs() {
        return this.inputs;
    }

    public int getMeta() {
        return this.meta;
    }

    public int getManaUsage() {
        return this.mana;
    }

    public int getThrottle() {
        return this.throttle;
    }

    private boolean itemEquals(ItemStack stack, Object stack2) {
        if (stack2 instanceof String) {

            for (ItemStack orestack : OreDictionary.getOres((String) stack2)) {
                ItemStack cstack = orestack.copy();

                if (cstack.getItemDamage() == 32767) cstack.setItemDamage(stack.getItemDamage());
                if (stack.isItemEqual(cstack)) return true;
            }

        } else return stack2 instanceof ItemStack && simpleAreStacksEqual(stack, (ItemStack) stack2);
        return false;
    }

    private boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }
}
