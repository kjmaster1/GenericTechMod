package com.kjmaster.generictechmod.blocks;

import com.google.common.base.Preconditions;
import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.tiles.solarpanel.TileSolarPanel;
import com.kjmaster.generictechmod.utils.IGTMModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nullable;

public class BlockSolarPanel extends Block implements IGTMModel
{

    public BlockSolarPanel()
    {
        super(Material.IRON);
        this.setUnlocalizedName(GenericTechMod.MODID + ".solar_panel");
        this.setRegistryName(new ResourceLocation(GenericTechMod.MODID, "solar_panel"));
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileSolarPanel();
    }

    @Override
    public void initModel()
    {
        ResourceLocation registryName = Preconditions.checkNotNull(getRegistryName());
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(registryName, "inventory"));
    }
}
