package com.kjmaster.generictechmod.tiles.fluxfurnace;

import com.kjmaster.generictechmod.utils.container.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFluxFurnace extends Container
{

    private final TileFluxFurnace fluxFurnace;
    private int smeltTime;
    private int lastEnergy;

    private IItemHandler iItemHandler;


    public ContainerFluxFurnace(InventoryPlayer playerInv, TileFluxFurnace fluxFurnace)
    {
        this.fluxFurnace = fluxFurnace;
        this.iItemHandler = this.fluxFurnace.getHandler();
        this.addSlotToContainer(new SlotItemHandler(iItemHandler, 0, 56, 17));
        this.addSlotToContainer(new SlotOutput(iItemHandler, 1, 56, 53));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); i++)
        {
            IContainerListener iContainerListener = this.listeners.get(i);

            if (this.lastEnergy != this.fluxFurnace.getField(0))
            {
                iContainerListener.sendWindowProperty(this, 0, this.fluxFurnace.getField(0));
            }
            if (this.smeltTime != this.fluxFurnace.getField(1))
            {
                iContainerListener.sendWindowProperty(this, 1, this.fluxFurnace.getField(1));
            }

            this.lastEnergy = this.fluxFurnace.getField(0);
            this.smeltTime = this.fluxFurnace.getField(1);
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        this.fluxFurnace.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return !playerIn.isSpectator();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (index < this.iItemHandler.getSlots()) {
                if (!this.mergeItemStack(current, this.iItemHandler.getSlots(),
                        this.iItemHandler.getSlots() + 36, true))
                    return ItemStack.EMPTY;
            } else {
                if (!this.mergeItemStack(current, 0, this.iItemHandler.getSlots(), false))
                    return ItemStack.EMPTY;
            }

            if (current.getCount() == 0) //Use func_190916_E() instead of stackSize 1.11 only 1.11.2 use getCount()
                slot.putStack(ItemStack.EMPTY); //Use ItemStack.field_190927_a instead of (ItemStack)null for a blank item stack. In 1.11.2 use ItemStack.EMPTY
            else
                slot.onSlotChanged();

            if (current.getCount() == previous.getCount())
                return null;
            slot.onTake(playerIn, current);
        }
        return previous;
    }
}
