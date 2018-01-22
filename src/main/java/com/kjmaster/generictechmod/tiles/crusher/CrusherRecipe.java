package com.kjmaster.generictechmod.tiles.crusher;

import net.minecraft.item.ItemStack;

public class CrusherRecipe
{

    public ItemStack input, output;
    public int grindTicks;
    public int rfCost;

    public CrusherRecipe (ItemStack input, ItemStack output, int grindTicks, int rfCost)
    {
        this.input = input;
        this.output = output;
        this.grindTicks = grindTicks;
        this.rfCost = rfCost;
    }
}
