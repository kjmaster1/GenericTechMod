package com.kjmaster.generictechmod.blocks;

import com.google.common.base.Preconditions;
import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.network.ModGuiHandler;
import com.kjmaster.generictechmod.proxy.CommonProxy;
import com.kjmaster.generictechmod.tiles.crusher.TileCrusher;
import com.kjmaster.generictechmod.utils.IGTMModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nullable;

public class BlockCrusher extends Block implements IGTMModel
{

    public BlockCrusher()
    {
        super(Material.IRON);
        this.setUnlocalizedName(GenericTechMod.MODID + ".crusher");
        this.setRegistryName(new ResourceLocation(GenericTechMod.MODID, "crusher"));
        setCreativeTab(GenericTechMod.gtmTab);
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
        return new TileCrusher();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
            return true;
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileCrusher))
        {
            return false;
        }
        playerIn.openGui(GenericTechMod.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void initModel() {
        ResourceLocation registryName = Preconditions.checkNotNull(getRegistryName());
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(registryName, "inventory"));
    }
}
