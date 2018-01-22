package com.kjmaster.generictechmod.proxy;

import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.init.ModBlocks;
import com.kjmaster.generictechmod.init.ModItems;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = GenericTechMod.MODID)
public class ClientProxy extends CommonProxy
{

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        ModItems.registerModels();
        ModBlocks.registerModels();
    }
}
