package tallestred.numismaticoverhaul.network;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tallestred.numismaticoverhaul.NumismaticOverhaul;
import tallestred.numismaticoverhaul.block.ShopBlockEntity;
import tallestred.numismaticoverhaul.block.ShopOffer;
import tallestred.numismaticoverhaul.client.gui.ShopScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public record UpdateShopScreenS2CPacket(List<ShopOffer> offers, long storedCurrency,
                                        boolean transferEnabled) implements CustomPacketPayload {
    public static final Type<UpdateShopScreenS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(NumismaticOverhaul.MODID, "following"));

    public static final StreamCodec<FriendlyByteBuf, UpdateShopScreenS2CPacket> STREAM_CODEC = StreamCodec.ofMember(
            UpdateShopScreenS2CPacket::write,
            UpdateShopScreenS2CPacket::new
    );

    public UpdateShopScreenS2CPacket(FriendlyByteBuf packetBuf) {
        this(packetBuf.readList((buf) -> ShopOffer.fromNbt(VanillaRegistries.createLookup(), buf.readNbt())), packetBuf.readLong(), packetBuf.readBoolean());
    }

    public UpdateShopScreenS2CPacket(ShopBlockEntity shop) {
        this(shop.getOffers(), shop.getStoredCurrency(), shop.isTransferEnabled());
    }


    public static void handle(UpdateShopScreenS2CPacket packet, IPayloadContext ctx) {
        if (!(Minecraft.getInstance().screen instanceof ShopScreen screen)) return;
        ctx.enqueueWork(() -> {
            screen.update(packet);
        });
    }

    public static void write(UpdateShopScreenS2CPacket packet, FriendlyByteBuf packetBuf) {
        packetBuf.writeCollection(packet.offers, (buf, shopOffer) -> buf.writeNbt(shopOffer.toNbt(VanillaRegistries.createLookup())));
        packetBuf.writeLong(packet.storedCurrency);
        packetBuf.writeBoolean(packet.transferEnabled);
        ;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
