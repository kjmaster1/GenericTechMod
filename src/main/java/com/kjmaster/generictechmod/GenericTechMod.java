package com.kjmaster.generictechmod;

import com.kjmaster.generictechmod.init.ModItems;
import com.kjmaster.generictechmod.network.ModGuiHandler;
import com.kjmaster.generictechmod.proxy.CommonProxy;
import com.kjmaster.generictechmod.tiles.crusher.CrusherRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = GenericTechMod.MODID, version = GenericTechMod.VERSION)
public class GenericTechMod
{
    @SidedProxy(clientSide = "com.kjmaster.generictechmod.proxy.ClientProxy",
            serverSide = "com.kjmaster.generictechmod.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static GenericTechMod instance;

    public static final String MODID = "generictechmod";
    public static final String VERSION = "1.0";

    public static CreativeTabs gtmTab = new CreativeTabs(GenericTechMod.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.chip);
        }
    };

    public static Logger LOGGER;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerTiles();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        CrusherRegistry.init();
    }
}
