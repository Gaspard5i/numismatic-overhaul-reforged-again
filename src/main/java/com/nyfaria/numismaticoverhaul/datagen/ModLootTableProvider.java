package com.nyfaria.numismaticoverhaul.datagen;

import com.nyfaria.numismaticoverhaul.loot_stuff.AddItemModifier;
import com.nyfaria.numismaticoverhaul.NumismaticOverhaul;
import com.nyfaria.numismaticoverhaul.config.NumOvhConfig;
import com.nyfaria.numismaticoverhaul.init.ItemInit;
import com.nyfaria.numismaticoverhaul.loot_stuff.MoneyBagLootModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.packs.VanillaChestLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.util.List;

public class ModLootTableProvider extends GlobalLootModifierProvider {
    public ModLootTableProvider(PackOutput output) {
        super(output, NumismaticOverhaul.MODID);
    }

    @Override
    protected void start() {
        for (String structuresWithCoins : NumOvhConfig.INSTANCE.structuresToHaveCoins.get()) {
            ResourceLocation lootLocation = new ResourceLocation(structuresWithCoins);
            String structureName = lootLocation.getPath().replace("/", "_");
            this.add("generic_coin_loot_table_" + structureName, new AddItemModifier(new LootItemCondition[]{
                    LootItemRandomChanceCondition.randomChance(0.10F).build(),
                    new LootTableIdCondition.Builder(lootLocation).build()}, ItemInit.GOLD_COIN.get()) {
            });
        }
        this.add("money_bag_loot_table_pyramid", new MoneyBagLootModifier(new LootItemCondition[]{
                LootItemRandomChanceCondition.randomChance(0.45F).build(),
                new LootTableIdCondition.Builder(BuiltInLootTables.DESERT_PYRAMID).build()}, 300, 1200) {
        });
        for (ResourceLocation dungeonTypeLoot : List.of(BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.ABANDONED_MINESHAFT)) {
            this.add("money_bag_loot_table_" + dungeonTypeLoot.getPath().replace("/", "_"), new MoneyBagLootModifier(new LootItemCondition[]{
                    LootItemRandomChanceCondition.randomChance(0.75F).build(),
                    new LootTableIdCondition.Builder(dungeonTypeLoot).build()}, 500, 2000) {
            });
        }
        for (ResourceLocation specialTypeLoot : List.of(BuiltInLootTables.BASTION_TREASURE, BuiltInLootTables.STRONGHOLD_CORRIDOR, BuiltInLootTables.PILLAGER_OUTPOST, BuiltInLootTables.BURIED_TREASURE)) {
            this.add("money_bag_loot_table_" + specialTypeLoot.getPath().replace("/", "_"), new MoneyBagLootModifier(new LootItemCondition[]{
                    LootItemRandomChanceCondition.randomChance(0.75F).build(),
                    new LootTableIdCondition.Builder(specialTypeLoot).build()}, 1500, 4000) {
            });
        }
        this.add("money_bag_loot_table_stronghold_library", new MoneyBagLootModifier(new LootItemCondition[]{
                LootItemRandomChanceCondition.randomChance(0.85F).build(),
                new LootTableIdCondition.Builder(BuiltInLootTables.STRONGHOLD_LIBRARY).build()}, 2000, 6000) {
        });
    }
}
