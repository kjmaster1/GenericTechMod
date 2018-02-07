package com.kjmaster.generictechmod.items;

import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.utils.IGTMModel;
import com.kjmaster.generictechmod.utils.RenderUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class ItemDust extends Item implements IGTMModel
{

    private static ArrayList<String> names = new ArrayList<>();

    public ItemDust() {
        this.setUnlocalizedName(GenericTechMod.MODID + ".dust");
        this.setRegistryName(new ResourceLocation(GenericTechMod.MODID, "dust"));
        this.setHasSubtypes(true);
        setCreativeTab(GenericTechMod.gtmTab);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
       for (int i = 0; i < DustTypes.values().length; i++)
       {
           if (stack.getItemDamage() == i)
           {
               return this.getUnlocalizedName() + "_" + DustTypes.values()[i].getName();
           }
       }
       return this.getUnlocalizedName() + "_" + DustTypes.IRON.getName();
    }

    @Override
    public void initModel() {
        RenderUtils.registerMetaRender(this, DustTypes.values().length, names);
    }

    public enum DustTypes implements IStringSerializable
    {

        IRON("iron", 0),
        GOLD("gold", 1),
        DIAMOND("diamond", 2),
        LAPIS("lapis", 3),
        EMERALD("emerald", 4),
        QUARTZ("quartz", 5),
        COAL("coal", 6);

        private int ID;
        private String name;

        DustTypes(String name, int ID)
        {
            this.ID = ID;
            this.name = name;
            names.add(name);
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
