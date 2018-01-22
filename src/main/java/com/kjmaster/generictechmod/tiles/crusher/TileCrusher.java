package com.kjmaster.generictechmod.tiles.crusher;

import com.kjmaster.generictechmod.utils.CustomEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileCrusher extends TileEntity implements ITickable
{
    private CustomEnergyStorage energyStorage = new CustomEnergyStorage(10000, 200, 0);
    private int processTime;
    private int recipeTime;
    private CrusherRecipe recipe;
    private ItemStackHandler handler = new ItemStackHandler(2)
    {
        @Override
        protected void onContentsChanged(int slot) {
            TileCrusher.this.markDirty();
        }
    };

    private CrusherItemHandler crusherItemHandler = new CrusherItemHandler(2, handler) {
        @Override
        protected void onContentsChanged(int slot) {
            TileCrusher.this.markDirty();
        }
    };

    @Override
    public void update()
    {
        if (world.isRemote)
        {
            return;
        }

        this.recipe = getValidRecipeForStack(handler.getStackInSlot(0));

        if (this.recipe != null)
        {
            recipeTime = recipe.grindTicks;
            ItemStack stack0 = handler.getStackInSlot(0);
            ItemStack stack1 = handler.getStackInSlot(1);
            if ((recipe.rfCost / recipe.grindTicks) <= energyStorage.getEnergyStored())
            {

                this.processTime++;
                boolean done = this.processTime >= recipe.grindTicks;

                energyStorage.setEnergy(energyStorage.getEnergyStored() - (recipe.rfCost / recipe.grindTicks));

                if (done)
                {
                    if (stack1.isItemEqual(recipe.output) && stack1.getCount() + recipe.output.getCount() < stack1.getMaxStackSize())
                    {
                        stack0.setCount(stack0.getCount() - 1);
                        stack1.setCount(stack1.getCount() + 1);
                    } else if (stack1.isEmpty())
                    {
                        stack0.setCount(stack0.getCount() - recipe.input.getCount());
                        handler.setStackInSlot(1, recipe.output.copy());
                    }
                    this.markDirty();
                    this.processTime = 0;
                    this.recipeTime = 0;
                }
            }
            else {
                this.processTime = 0;
            }
        } else
        {
            this.processTime = 0;
            this.recipeTime = 0;
        }
    }

    // Only one recipe per item, no item input should have multiple recipes
    private static CrusherRecipe getValidRecipeForStack(ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            for (CrusherRecipe recipe : CrusherRegistry.CRUSHER_RECIPES)
            {
                if (!recipe.input.isEmpty() && recipe.input.isItemEqual(stack))
                {
                    return recipe;
                }
            }
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("items"))
        {
            handler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }


        this.energyStorage.readFromNBT(compound);
        this.processTime = compound.getInteger("ProcessTime");
        this.recipeTime = compound.getInteger("RecipeTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        compound.setTag("items", handler.serializeNBT());

        this.energyStorage.writeToNBT(compound);
        compound.setInteger("ProcessTime", this.processTime);
        compound.setInteger("RecipeTime", this.recipeTime);

        return compound;
    }


    public int getFieldCount()
    {
        return 3;
    }


    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.processTime = value;
                break;
            case 1:
                energyStorage.setEnergy(value);
                break;
            case 2:
                this.recipeTime = value;
                break;
        }
    }


    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.processTime;
            case 1:
                return this.energyStorage.getEnergyStored();
            case 2:
                return this.recipeTime;
            default:
                return 0;
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return true;
        } else if (capability == CapabilityEnergy.ENERGY)
        {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(crusherItemHandler);
        if (capability == CapabilityEnergy.ENERGY)
        {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    public ItemStackHandler getHandler() {
        return handler;
    }

    public CrusherRecipe getRecipe() {
        return recipe;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);
        return new SPacketUpdateTileEntity(getPos(), 1, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }
}
