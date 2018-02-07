package com.kjmaster.generictechmod.blocks;

import com.google.common.base.Preconditions;
import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.tiles.crusher.TileCrusher;
import com.kjmaster.generictechmod.tiles.fluxfurnace.TileFluxFurnace;
import com.kjmaster.generictechmod.utils.IGTMModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFluxFurnace extends Block implements IGTMModel
{

    public static final PropertyBool IS_ON = PropertyBool.create("on");

    public BlockFluxFurnace()
    {
        super(Material.IRON);
        this.setUnlocalizedName(GenericTechMod.MODID + ".flux_furnace");
        this.setRegistryName(new ResourceLocation(GenericTechMod.MODID, "flux_furnace"));
        this.setTickRandomly(true);
        setCreativeTab(GenericTechMod.gtmTab);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
            return true;
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileFluxFurnace))
        {
            return false;
        }
        playerIn.openGui(GenericTechMod.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileFluxFurnace();
    }

    @Override
    public void initModel() {
        ResourceLocation registryName = Preconditions.checkNotNull(getRegistryName());
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(registryName, "inventory"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(IS_ON))
        {
            for (int i = 0; i < 5; i++)
            {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                         (double)pos.getX()+0.5F, (double)pos.getY()+1.0F,
                        (double)pos.getZ()+0.5F, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(IS_ON) ? 12 : 0;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite()), 2);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isOn = meta >= 4;
        EnumFacing facing = EnumFacing.getHorizontal(isOn ? meta - 4 : meta);
        return this.getDefaultState().withProperty(BlockHorizontal.FACING, facing).withProperty(IS_ON, isOn);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
        return state.getValue(IS_ON) ? meta + 4 : meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockHorizontal.FACING, IS_ON);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return this.withRotation(state, mirrorIn.toRotation(state.getValue(BlockHorizontal.FACING)));
    }
}
