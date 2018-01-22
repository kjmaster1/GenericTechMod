package com.kjmaster.generictechmod.tiles.solarpanel;

import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.utils.CustomEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileSolarPanel extends TileEntity implements ITickable
{

    private CustomEnergyStorage energyStorage = new CustomEnergyStorage(10000, 0, 1000);

    private int currentEnergyPerTick;

    private float sunIntensity;

    private int ticks;

    @Override
    public void update()
    {

        if (world.isRemote)
        {
            return;
        }

        handleSendingEnergy();

        ticks++;

        generateEnergy();

        if (ticks >= 20)
        {
            updateCurrentEnergyPerTick();
            GenericTechMod.LOGGER.info("Sun Intensity Is: " + sunIntensity);
            GenericTechMod.LOGGER.info("Current Energy Per Tick Is: " + currentEnergyPerTick);
            ticks = 0;
        }
    }

    private void handleSendingEnergy()
    {
        int energyStored = energyStorage.getEnergyStored();

        for (EnumFacing facing : EnumFacing.VALUES)
        {
            BlockPos pos = getPos().offset(facing);
            TileEntity te = getWorld().getTileEntity(pos);
            EnumFacing opposite = facing.getOpposite();
            if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, opposite))
            {
                int energyToGive = 1000 <= energyStored ? 1000 : energyStored;
                int received = 0;

                IEnergyStorage teStorage = te.getCapability(CapabilityEnergy.ENERGY, opposite);

                if (teStorage != null)
                {
                    received = teStorage.receiveEnergy(energyToGive, false);
                }
                energyStored -= energyStorage.extractEnergy(received, false);
                if (energyStored <= 0) {
                    break;
                }
            }
        }
    }

    private void updateCurrentEnergyPerTick()
    {
        calculateSunIntensity();

        double energyGen = 80 * sunIntensity;

        currentEnergyPerTick = (int) Math.round(energyGen);
    }

    private void calculateSunIntensity()
    {
        if (world.canBlockSeeSky(pos.offset(EnumFacing.UP)))
        {
            float multiplicator = 1.5F;
            float displacement = 1.2F;

            float celestialAngleRadians = world.getCelestialAngleRadians(1.0F);
            if (celestialAngleRadians > Math.PI)
            {
                celestialAngleRadians = (((float) (2 * (Math.PI))) - celestialAngleRadians);
            }

            sunIntensity = multiplicator * MathHelper.cos(celestialAngleRadians / displacement);
            sunIntensity = Math.max(0, sunIntensity);
            sunIntensity = Math.min(1, sunIntensity);

            if (sunIntensity > 0)
            {
                if (world.isRaining())
                {
                    sunIntensity *= 0.8F;
                }
                if (world.isThundering())
                {
                    sunIntensity *= 0.8F;
                }
            }
        } else
        {
            sunIntensity = 0;
        }
    }

    private void generateEnergy()
    {
        if (currentEnergyPerTick > 0)
        {
            energyStorage.setEnergy(energyStorage.getEnergyStored() + currentEnergyPerTick);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        energyStorage.readFromNBT(compound);

        ticks = compound.getInteger("Ticks");
        currentEnergyPerTick = compound.getInteger("CurrentEnergyPerTick");
        sunIntensity = compound.getFloat("SunIntensity");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        energyStorage.writeToNBT(compound);

        compound.setInteger("Ticks", ticks);
        compound.setInteger("CurrentEnergyPerTick", currentEnergyPerTick);
        compound.setFloat("SunIntensity", sunIntensity);

        return compound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == null)
        {
            return true;
        }
        if (capability == CapabilityEnergy.ENERGY && facing != null && !facing.equals(EnumFacing.UP))
        {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == null)
        {
            return (T) energyStorage;
        }
        if (capability == CapabilityEnergy.ENERGY && facing != null && !facing.equals(EnumFacing.UP))
        {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    public CustomEnergyStorage getEnergyStorage()
    {
        return energyStorage;
    }

    public float getSunIntensity()
    {
        return sunIntensity;
    }

    public int getCurrentEnergyPerTick()
    {
        return currentEnergyPerTick;
    }

    public void setCurrentEnergyPerTick(int currentEnergyPerTick)
    {
        this.currentEnergyPerTick = currentEnergyPerTick;
    }

    public void setSunIntensity(float sunIntensity)
    {
        this.sunIntensity = sunIntensity;
    }
}
