package com.kjmaster.generictechmod.tiles.crusher;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CrusherItemHandler extends ItemStackHandler
{

    private ItemStackHandler handler;

    public CrusherItemHandler(int size, ItemStackHandler handler)
    {
        super(size);
        this.handler = handler;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (slot == 0)
        {
            return ItemStack.EMPTY;
        } else {
            return handler.extractItem(1, amount, simulate);
        }
    }

    @Override
    public void setSize(int size) {
        handler.setSize(size);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        handler.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        return handler.insertItem(slot, stack, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return handler.getSlotLimit(slot);
    }
}
