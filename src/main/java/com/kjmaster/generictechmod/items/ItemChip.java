package com.kjmaster.generictechmod.items;

import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.utils.IGTMModel;
import com.kjmaster.generictechmod.utils.RenderUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class ItemChip extends Item implements IGTMModel
{
    private static ArrayList<String> names = new ArrayList<>(3);

    public ItemChip()
    {
        this.setUnlocalizedName(GenericTechMod.MODID + ".chip");
        this.setRegistryName(new ResourceLocation(GenericTechMod.MODID, "chip"));
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        for (int i = 0; i < ChipTypes.values().length; i++)
        {
            if (stack.getItemDamage() == i)
            {
               return this.getUnlocalizedName() + "_" +  ChipTypes.values()[i].getName();
            }
        }
        return this.getUnlocalizedName() + "_" + ChipTypes.BASIC.getName();
    }

    @Override
    public void initModel()
    {
        RenderUtils.registerMetaRender(this, ChipTypes.values().length, names);
    }

    public enum ChipTypes implements IStringSerializable
    {
        BASIC("basic", 0),
        ADVANCED("advanced", 1),
        QUANTUM("quantum", 2);

        private int ID;
        private String name;

        ChipTypes(String name, int ID)
        {
            this.ID = ID;
            this.name = name;
            names.add(name);
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }
}
