package com.kjmaster.generictechmod.init;

import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.items.ItemChip;
import com.kjmaster.generictechmod.items.ItemDust;
import com.kjmaster.generictechmod.utils.IGTMModel;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems
{

    public static final Item chip = new ItemChip();
    public static final Item dust = new ItemDust();

    @Mod.EventBusSubscriber(modid = GenericTechMod.MODID)
    public static class RegistrationHandler
    {

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event)
        {
            final Item[] items =
            {
                chip,
                dust,

            };

            final IForgeRegistry<Item> registry = event.getRegistry();

            registry.registerAll(items);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels()
    {
        final Item[] items =
        {
            chip,
            dust,
        };

        for (final Item item : items)
        {
            if (item instanceof IGTMModel)
            {
                ((IGTMModel) item).initModel();
            }
        }
    }
}
