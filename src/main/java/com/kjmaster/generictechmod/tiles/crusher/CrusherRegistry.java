package com.kjmaster.generictechmod.tiles.crusher;

import com.kjmaster.generictechmod.init.ModItems;
import com.kjmaster.generictechmod.items.ItemDust.DustTypes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class CrusherRegistry {

    public static List<CrusherRecipe> CRUSHER_RECIPES = new ArrayList<>();

    public static int NORMAL_GRIND_TICKS = 60;
    public static int NORMAL_RF_COST = 2400;


    public static void init()
    {
        addRecipe(new CrusherRecipe(new ItemStack(Blocks.COBBLESTONE, 1, 0),
                new ItemStack(Blocks.SAND, 1, 0),  NORMAL_GRIND_TICKS, NORMAL_RF_COST));

        // Ore Dict Dust Ores
        addOreDictDustRecipe("oreIron", new ItemStack(ModItems.dust, 2, DustTypes.IRON.ordinal()));
        addOreDictDustRecipe("oreGold", new ItemStack(ModItems.dust, 2, DustTypes.GOLD.ordinal()));
        addOreDictDustRecipe("oreDiamond", new ItemStack(ModItems.dust, 2, DustTypes.DIAMOND.ordinal()));
        addOreDictDustRecipe("oreLapis", new ItemStack(ModItems.dust, 2, DustTypes.LAPIS.ordinal()));
        addOreDictDustRecipe("oreEmerald", new ItemStack(ModItems.dust, 2, DustTypes.EMERALD.ordinal()));
        addOreDictDustRecipe("oreQuartz", new ItemStack(ModItems.dust, 2, DustTypes.QUARTZ.ordinal()));
        addOreDictDustRecipe("oreCoal", new ItemStack(ModItems.dust, 2, DustTypes.COAL.ordinal()));
        // Ore Dict Dust Ingots
        addOreDictDustRecipe("ingotIron", new ItemStack(ModItems.dust, 1, DustTypes.IRON.ordinal()));
        addOreDictDustRecipe("ingotGold", new ItemStack(ModItems.dust, 1, DustTypes.GOLD.ordinal()));
        // Ore Dict Dust Gems
        addOreDictDustRecipe("gemDiamond", new ItemStack(ModItems.dust, 1, DustTypes.DIAMOND.ordinal()));
        addOreDictDustRecipe("gemLapis", new ItemStack(ModItems.dust, 1, DustTypes.LAPIS.ordinal()));
        addOreDictDustRecipe("gemEmerald", new ItemStack(ModItems.dust, 1, DustTypes.EMERALD.ordinal()));
        addOreDictDustRecipe("gemQuartz", new ItemStack(ModItems.dust, 1, DustTypes.QUARTZ.ordinal()));
        // Coal Not Ore Dic
        addRecipe(new CrusherRecipe(new ItemStack(Items.COAL, 1, 0),
                new ItemStack(ModItems.dust, 1, DustTypes.COAL.ordinal()), NORMAL_GRIND_TICKS, NORMAL_RF_COST));

    }

    public static void addRecipe(CrusherRecipe recipe) {
        CRUSHER_RECIPES.add(recipe);
    }

    public static void addOreDictDustRecipe(String oreDictName, ItemStack output)
    {
        if (OreDictionary.doesOreNameExist(oreDictName))
        {
            NonNullList<ItemStack> stacks = OreDictionary.getOres(oreDictName);

            for (ItemStack stack : stacks)
            {
                addRecipe(new CrusherRecipe(stack, output, NORMAL_GRIND_TICKS, NORMAL_RF_COST));
            }
        }
    }
}
