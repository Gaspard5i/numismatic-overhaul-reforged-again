package tallestred.numismaticoverhaul.config;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class NOConfig {

    public static final ModConfigSpec CONFIG_SPEC;
    public static final NOConfig INSTANCE;

    static {
        Pair<NOConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(NOConfig::new);
        CONFIG_SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public ModConfigSpec.DoubleValue moneyDropChance;
    public ModConfigSpec.ConfigValue<List<? extends String>> structuresToHaveCoins;


    private NOConfig(ModConfigSpec.Builder builder) {
        moneyDropChance = builder.defineInRange("% chance for player to drop money", 10.0, -1000, 1000);
        structuresToHaveCoins = builder.comment("Structures that have coins, specific rates can be changed via datapacks.").defineListAllowEmpty("Structures",
                ImmutableList.of(BuiltInLootTables.STRONGHOLD_LIBRARY.toString(), BuiltInLootTables.BASTION_TREASURE.toString(), BuiltInLootTables.STRONGHOLD_CORRIDOR.toString(),
                        BuiltInLootTables.PILLAGER_OUTPOST.toString(), BuiltInLootTables.BURIED_TREASURE.toString(), BuiltInLootTables.SIMPLE_DUNGEON.toString(), BuiltInLootTables.ABANDONED_MINESHAFT.toString()), obj -> true);
    }
}