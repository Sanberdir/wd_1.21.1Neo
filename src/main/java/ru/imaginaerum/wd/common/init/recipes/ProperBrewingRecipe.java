package ru.imaginaerum.wd.common.init.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

import javax.annotation.Nonnull;

public class ProperBrewingRecipe implements IBrewingRecipe {

    private final Ingredient input;
    private final Ingredient ingredient;
    private final ItemStack output;

    public ProperBrewingRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (ItemStack match : input.getItems()) {
            if (ItemStack.isSameItemSameComponents(match, stack)) return true;
        }
        return false;
    }

    @Override
    public boolean isIngredient(@Nonnull ItemStack stack) {
        return ingredient.test(stack);
    }

    @Override
    public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
        if (!isInput(input) || !isIngredient(ingredient)) return ItemStack.EMPTY;
        return output.copy();
    }
}