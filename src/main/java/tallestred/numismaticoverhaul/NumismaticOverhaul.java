package tallestred.numismaticoverhaul;


import com.mojang.serialization.MapCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import tallestred.numismaticoverhaul.config.NOClientConfig;
import tallestred.numismaticoverhaul.config.NOConfig;
import tallestred.numismaticoverhaul.init.*;
import tallestred.numismaticoverhaul.loot_stuff.AddItemModifier;
import tallestred.numismaticoverhaul.loot_stuff.MoneyBagLootModifier;
import tallestred.numismaticoverhaul.network.RequestPurseActionC2SPacket;
import tallestred.numismaticoverhaul.network.ShopScreenHandlerRequestC2SPacket;
import tallestred.numismaticoverhaul.network.UpdateShopScreenS2CPacket;
import tallestred.numismaticoverhaul.villagers.json.VillagerTradesHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(NumismaticOverhaul.MODID)
public class NumismaticOverhaul {
    public static final String MODID = "numismaticoverhaul";
    public static final Logger LOGGER = LogManager.getLogger();
    //public static final LootPoolEntryType MONEY_BAG_ENTRY = new LootPoolEntryType(new MoneyBagLootEntry.Serializer());
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", AddItemModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<? extends IGlobalLootModifier>> MONEY_BAG =
            LOOT_MODIFIER_SERIALIZERS.register("money_bag", MoneyBagLootModifier.CODEC);

    public NumismaticOverhaul(IEventBus bus, Dist dist, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, NOConfig.CONFIG_SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, NOClientConfig.CLIENT_SPEC);
        LOOT_MODIFIER_SERIALIZERS.register(bus);
        ItemInit.ITEMS.register(bus);
        EntityInit.ENTITIES.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ENTITIES.register(bus);
        CreativeTabInit.TABS.register(bus);
        MenuInit.MENU_TYPES.register(bus);
        DataAttachmentInit.ATTACHMENT_TYPES.register(bus);
        ItemComponentInit.DATA_COMPONENTS.register(bus);
        VillagerTradesHandler.registerDefaultAdapters();
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar reg = event.registrar(MODID).versioned("2.0.2");
        reg.playToServer(RequestPurseActionC2SPacket.TYPE, RequestPurseActionC2SPacket.STREAM_CODEC, RequestPurseActionC2SPacket::handle);
        reg.playToClient(UpdateShopScreenS2CPacket.TYPE, UpdateShopScreenS2CPacket.STREAM_CODEC, UpdateShopScreenS2CPacket::handle);
        reg.playToServer(ShopScreenHandlerRequestC2SPacket.TYPE, ShopScreenHandlerRequestC2SPacket.STREAM_CODEC, ShopScreenHandlerRequestC2SPacket::handle);
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        //   generator.addProvider(event.includeServer(), new ModRecipeProvider(generator));
        //generator.addProvider(event.includeServer(), new ModLootTableProvider(packOutput));
        //   generator.addProvider(event.includeServer(), new ModSoundProvider(generator, MODID, existingFileHelper));
        //    generator.addProvider(event.includeClient(), new ModItemModelProvider(generator, existingFileHelper));
        //  generator.addProvider(event.includeClient(), new ModBlockStateProvider(generator, existingFileHelper));
        //   generator.addProvider(event.includeClient(), new ModLangProvider(generator, MODID, "en_us"));
    }

    public static final Component PREFIX = Component.empty().withStyle(ChatFormatting.GRAY)
            .append(withColor("o", 0x3955e5))
            .append(withColor("ω", 0x13a6f0))
            .append(withColor("o", 0x3955e5))
            .append(Component.literal(" > ").withStyle(ChatFormatting.GRAY));

    public static MutableComponent withColor(String text, int color) {
        return Component.literal(text).setStyle(Style.EMPTY.withColor(color));
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}

