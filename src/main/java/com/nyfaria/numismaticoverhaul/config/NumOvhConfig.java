package com.nyfaria.numismaticoverhaul.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;

public class NumOvhConfig {

    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final NumOvhConfig INSTANCE;

    static {
        Pair<NumOvhConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(NumOvhConfig::new);
        CONFIG_SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public ForgeConfigSpec.DoubleValue moneyDropChance;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> structuresToHaveCoins;


    private NumOvhConfig(ForgeConfigSpec.Builder builder) {
        moneyDropChance = builder.defineInRange("% chance for player to drop money", 10.0, -1000, 1000);
        structuresToHaveCoins = builder.comment("Structures that have coins, specific rates can be changed via datapacks.").defineListAllowEmpty("Structures",
                ImmutableList.of(BuiltInLootTables.STRONGHOLD_LIBRARY.toString(), BuiltInLootTables.BASTION_TREASURE.toString(), BuiltInLootTables.STRONGHOLD_CORRIDOR.toString(),
                        BuiltInLootTables.PILLAGER_OUTPOST.toString(), BuiltInLootTables.BURIED_TREASURE.toString(), BuiltInLootTables.SIMPLE_DUNGEON.toString(), BuiltInLootTables.ABANDONED_MINESHAFT.toString()), obj -> true);
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}