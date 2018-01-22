package com.kjmaster.generictechmod.utils;

import com.kjmaster.generictechmod.GenericTechMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class RenderUtils
{

    @SideOnly(Side.CLIENT)
    public static void registerMetaRender(Item item, int length, ArrayList<String> names)
    {
        for (int i = 0; i < length; i++)
        {
            ModelLoader.setCustomModelResourceLocation(item, i,
                    new ModelResourceLocation(new ResourceLocation(GenericTechMod.MODID,
                            item.getRegistryName().getResourcePath() + "_" + names.get(i)),
                            "inventory"));
        }
    }
}
