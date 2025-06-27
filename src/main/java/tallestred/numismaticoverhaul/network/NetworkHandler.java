package tallestred.numismaticoverhaul.network;

import com.google.common.collect.ImmutableList;
import tallestred.numismaticoverhaul.NumismaticOverhaul;
import tallestred.numismaticoverhaul.cap.CurrencyHolderAttacher;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.function.BiConsumer;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(NumismaticOverhaul.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int nextId = 0;

    public static void register() {
        List<BiConsumer<SimpleChannel, Integer>> packets = ImmutableList.<BiConsumer<SimpleChannel, Integer>>builder()
                .add((channel, id) -> SimpleEntityCapabilityStatusPacket.register(CurrencyHolderAttacher.EXAMPLE_RL, CurrencyHolderAttacher::getExampleHolderUnwrap, channel, id))
                .add(RequestPurseActionC2SPacket::register)
                .add(UpdateShopScreenS2CPacket::register)
                .add(ShopScreenHandlerRequestC2SPacket::register)
                .build();

        packets.forEach(consumer -> consumer.accept(INSTANCE, getNextId()));
    }

    private static int getNextId() {
        return nextId++;
    }
}