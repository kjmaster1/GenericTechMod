package com.kjmaster.generictechmod.tiles.fluxfurnace;

import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.blocks.BlockFluxFurnace;
import com.kjmaster.generictechmod.tiles.crusher.CrusherItemHandler;
import com.kjmaster.generictechmod.utils.CustomEnergyStorage;
import com.kjmaster.generictechmod.utils.container.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileFluxFurnace extends TileEntity implements ITickable
{

    public static final int ENERGY_USE = 40;
    public static final int SMELT_TIME = 60;

    private CustomEnergyStorage energyStorage = new CustomEnergyStorage(10000, 200, 0);
    private int smeltTime;
    private boolean lastSmelted;

    private ItemStackHandler handler = new ItemStackHandler(2);
    private CrusherItemHandler itemHandler = new CrusherItemHandler(2, handler);

    @Override
    public void update()
    {
        if (this.world.isRemote)
        {
            return;
        }

        boolean smelted = false;

        boolean canSmeltOn = this.canSmeltOn(handler.getStackInSlot(0), handler.getStackInSlot(1));

        if (canSmeltOn)
        {
            GenericTechMod.LOGGER.info("Stack Can Be Smelted");
            if (this.energyStorage.getEnergyStored() >= ENERGY_USE)
            {
                GenericTechMod.LOGGER.info("Sufficient Energy");
                this.smeltTime++;
                if (this.smeltTime >= SMELT_TIME)
                {
                    GenericTechMod.LOGGER.info("Finished Smelt Time");
                    this.finishBurning(handler.getStackInSlot(0), handler.getStackInSlot(1));
                    this.smeltTime = 0;
                }
                GenericTechMod.LOGGER.info("Going To Set Energy To: " + (this.energyStorage.getEnergyStored() - ENERGY_USE));
                this.energyStorage.setEnergy(this.energyStorage.getEnergyStored() - ENERGY_USE);
            }
            else
            {
                this.smeltTime = 0;
            }
            smelted = true;
        } else
        {
            GenericTechMod.LOGGER.info("Smelt Time Reset To 0");
            this.smeltTime = 0;
        }

        if (smelted != this.lastSmelted)
        {
            //GenericTechMod.LOGGER.info("Updating Blockstate");
            IBlockState currentState = this.world.getBlockState(this.pos);
            if (currentState.getValue(BlockFluxFurnace.IS_ON) != smelted)
            {
                //CustomEnergyStorage thisEnergyStorage = energyStorage;
                //int thisSmeltTime = smeltTime;
                //boolean thisLastSmelted = lastSmelted;
                //ItemStackHandler thisHandler = handler;
                //CrusherItemHandler thisCrusherHandler = itemHandler;
                //ItemStack stackInSlot0 = handler.getStackInSlot(0).copy();
                //ItemStack stackInSlot1 = handler.getStackInSlot(1).copy();

                this.world.setBlockState(this.pos, currentState.withProperty(BlockFluxFurnace.IS_ON, smelted));
                //TileEntity te = world.getTileEntity(this.pos);
                //if (te instanceof TileFluxFurnace)
                //{
                    //TileFluxFurnace tileFluxFurnace = (TileFluxFurnace) te;
                    //tileFluxFurnace.energyStorage = thisEnergyStorage;
                    //tileFluxFurnace.smeltTime = thisSmeltTime;
                    //tileFluxFurnace.lastSmelted = thisLastSmelted;
                    //tileFluxFurnace.handler = thisHandler;
                    //tileFluxFurnace.itemHandler = thisCrusherHandler;
                    //tileFluxFurnace.setField(1, thisSmeltTime);
                    //tileFluxFurnace.handler.setStackInSlot(0, stackInSlot0);
                    //tileFluxFurnace.handler.setStackInSlot(1, stackInSlot1);
                //}
            }
            this.lastSmelted = smelted;
        }
    }

    private boolean canSmeltOn(ItemStack theInput, ItemStack theOutput)
    {
        if (StackUtil.isValid(theInput))
        {
            GenericTechMod.LOGGER.info("Getting Output");
            ItemStack output = FurnaceRecipes.instance().getSmeltingResult(theInput);
            if (!StackUtil.isValid(theOutput) || (theOutput.isItemEqual(output)
                    && StackUtil.getStackSize(theOutput) <= theOutput.getMaxStackSize() - StackUtil.getStackSize(output)))
            {
                GenericTechMod.LOGGER.info("Returning True");
                return true;
            }
        }
        return false;
    }

    private void finishBurning(ItemStack theInput, ItemStack theOutput)
    {
        ItemStack smeltOutput = FurnaceRecipes.instance().getSmeltingResult(theInput);
        if (!StackUtil.isValid(theOutput))
        {
            GenericTechMod.LOGGER.info("Copying Stack Into Slot 1");
            this.handler.setStackInSlot(1, smeltOutput.copy());
        }
        else if (theOutput.isItemEqual(smeltOutput))
        {
            GenericTechMod.LOGGER.info("Adding Stack Into Slot 1");
            this.handler.setStackInSlot(1,
                    StackUtil.addStackSize(this.handler.getStackInSlot(1), StackUtil.getStackSize(smeltOutput)));
        }

        GenericTechMod.LOGGER.info("Subtracting 1 From Slot 0 Stack Count");
        this.handler.getStackInSlot(0).shrink(1);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("items"))
        {
            handler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }

        energyStorage.readFromNBT(compound);

        smeltTime = compound.getInteger("SmeltTime");
        lastSmelted = compound.getBoolean("LastSmelted");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        energyStorage.writeToNBT(compound);

        compound.setTag("items", handler.serializeNBT());

        compound.setInteger("SmeltTime", smeltTime);
        compound.setBoolean("LastSmelted", lastSmelted);

        return compound;
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

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return (T) itemHandler;
        } else if (capability == CapabilityEnergy.ENERGY)
        {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    public ItemStackHandler getHandler() {
        return handler;
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.energyStorage.setEnergy(value);
                break;
            case 1:
                this.smeltTime = value;
                break;
        }
    }

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.energyStorage.getEnergyStored();
            case 1:
                return this.smeltTime;
            default:
                return 0;
        }
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

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
