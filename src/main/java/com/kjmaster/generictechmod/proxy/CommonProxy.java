package com.kjmaster.generictechmod.proxy;

import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.tiles.crusher.TileCrusher;
import com.kjmaster.generictechmod.tiles.fluxfurnace.TileFluxFurnace;
import com.kjmaster.generictechmod.tiles.solarpanel.TileSolarPanel;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{

    public void registerTiles()
    {
        GameRegistry.registerTileEntity(TileCrusher.class, GenericTechMod.MODID + ":tile_crusher");
        GameRegistry.registerTileEntity(TileSolarPanel.class, GenericTechMod.MODID + "tile_solar_panel");
        GameRegistry.registerTileEntity(TileFluxFurnace.class, GenericTechMod.MODID + "tile_flux_furnace");
    }
}
