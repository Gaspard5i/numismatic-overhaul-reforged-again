package tallestred.numismaticoverhaul;


import com.mojang.serialization.MapCodec;
import io.wispforest.endec.impl.ReflectiveEndecBuilder;
import io.wispforest.owo.network.OwoNetChannel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import tallestred.numismaticoverhaul.block.ShopOffer;
import tallestred.numismaticoverhaul.config.NOClientConfig;
import tallestred.numismaticoverhaul.config.NOConfig;
import tallestred.numismaticoverhaul.init.*;
import tallestred.numismaticoverhaul.loot_stuff.AddItemModifier;
import tallestred.numismaticoverhaul.loot_stuff.MoneyBagLootModifier;
import tallestred.numismaticoverhaul.network.RequestPurseActionC2SPacket;
import tallestred.numismaticoverhaul.network.ShopScreenHandlerRequestC2SPacket;
import tallestred.numismaticoverhaul.network.UpdatePlayerCurrencyPacket;
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
    public static final OwoNetChannel MY_CHANNEL = OwoNetChannel.create(ResourceLocation.fromNamespaceAndPath(MODID, "main"));

    public NumismaticOverhaul(IEventBus bus, Dist dist, ModContainer container) {
        container.registerConfig(ModConfig.Type.STARTUP, NOConfig.CONFIG_SPEC);
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
        bus.addListener(this::onCommonSetup);
        NeoForge.EVENT_BUS.register(this);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        ReflectiveEndecBuilder.SHARED_INSTANCE.register(ShopOffer.ENDEC, ShopOffer.class);
        MY_CHANNEL.registerClientbound(UpdateShopScreenS2CPacket.class, UpdateShopScreenS2CPacket::handle);
        MY_CHANNEL.registerClientbound(UpdatePlayerCurrencyPacket.class, UpdatePlayerCurrencyPacket::handle);
        MY_CHANNEL.registerServerbound(RequestPurseActionC2SPacket.class, RequestPurseActionC2SPacket::handle);
        MY_CHANNEL.registerServerbound(ShopScreenHandlerRequestC2SPacket.class, ShopScreenHandlerRequestC2SPacket::handle);
    }

    @SubscribeEvent
    public void playerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            NumismaticOverhaul.MY_CHANNEL.serverHandle(player).send(new UpdatePlayerCurrencyPacket(player.getData(DataAttachmentInit.VALUE.get())));
        }
    }


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

