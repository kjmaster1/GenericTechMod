package com.kjmaster.generictechmod.init;

import com.google.common.base.Preconditions;
import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.blocks.BlockCrusher;
import com.kjmaster.generictechmod.blocks.BlockFluxFurnace;
import com.kjmaster.generictechmod.blocks.BlockSolarPanel;
import com.kjmaster.generictechmod.utils.IGTMModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks
{

    public static Block crusher = new BlockCrusher();
    public static Block solarPanel = new BlockSolarPanel();
    public static Block fluxFurnace = new BlockFluxFurnace();

    @Mod.EventBusSubscriber(modid = GenericTechMod.MODID)
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event)
        {
            final IForgeRegistry<Block> registry = event.getRegistry();
            final Block[] blocks =
            {
                crusher,
                solarPanel,
                fluxFurnace,
            };

            registry.registerAll(blocks);
        }

        @SubscribeEvent
        public static void registerItemBlocks(final RegistryEvent.Register<Item> event)
        {
            final ItemBlock[] items =
            {
                new ItemBlock(crusher),
                new ItemBlock(solarPanel),
                new ItemBlock(fluxFurnace),
            };

            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final ItemBlock item : items)
            {
                final Block block = item.getBlock();
                final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName());
                registry.register(item.setRegistryName(registryName));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels()
    {
        final Block[] blocks =
        {
            crusher,
            solarPanel,
            fluxFurnace,
        };

        for (final Block block : blocks)
        {
            if (block instanceof IGTMModel)
            {
                ((IGTMModel) block).initModel();
            }
        }
    }
}
