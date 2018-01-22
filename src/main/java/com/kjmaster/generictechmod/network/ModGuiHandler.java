package com.kjmaster.generictechmod.network;

import com.kjmaster.generictechmod.client.GuiCrusher;
import com.kjmaster.generictechmod.client.GuiFluxFurnace;
import com.kjmaster.generictechmod.tiles.crusher.ContainerCrusher;
import com.kjmaster.generictechmod.tiles.crusher.TileCrusher;
import com.kjmaster.generictechmod.tiles.fluxfurnace.ContainerFluxFurnace;
import com.kjmaster.generictechmod.tiles.fluxfurnace.TileFluxFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class ModGuiHandler implements IGuiHandler
{

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileCrusher && ID == 0)
        {
            return new GuiCrusher(player.inventory, (TileCrusher) te);
        }
        if (te instanceof TileFluxFurnace && ID == 1)
        {
            return new GuiFluxFurnace(player.inventory, (TileFluxFurnace) te);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileCrusher)
        {
            return new ContainerCrusher(player.inventory, (TileCrusher) te);
        }
        if (te instanceof TileFluxFurnace && ID == 1)
        {
            return new ContainerFluxFurnace(player.inventory, (TileFluxFurnace) te);
        }
        return null;
    }
}
