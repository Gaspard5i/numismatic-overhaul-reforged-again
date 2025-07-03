package tallestred.numismaticoverhaul.network;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tallestred.numismaticoverhaul.NumismaticOverhaul;
import tallestred.numismaticoverhaul.block.ShopScreenHandler;
import net.minecraft.network.FriendlyByteBuf;

public record ShopScreenHandlerRequestC2SPacket(ShopScreenHandlerAction action, long value) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ShopScreenHandlerRequestC2SPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(NumismaticOverhaul.MODID, "following"));
    public static final StreamCodec<FriendlyByteBuf, ShopScreenHandlerRequestC2SPacket> STREAM_CODEC = StreamCodec.composite(new EnumStreamCodec<>(ShopScreenHandlerRequestC2SPacket.ShopScreenHandlerAction.class), ShopScreenHandlerRequestC2SPacket::action,
            ByteBufCodecs.VAR_LONG, ShopScreenHandlerRequestC2SPacket::value,
            ShopScreenHandlerRequestC2SPacket::new
    );

    public ShopScreenHandlerRequestC2SPacket(ShopScreenHandlerAction action) {
        this(action, 0);
    }

    public static void handle(ShopScreenHandlerRequestC2SPacket packet, IPayloadContext context) {
        final var player = context.player();

        if (!(player.containerMenu instanceof ShopScreenHandler shopHandler)) return;

        switch (packet.action()) {
            case ShopScreenHandlerAction.LOAD_OFFER -> shopHandler.loadOffer(packet.value());
            case ShopScreenHandlerAction.CREATE_OFFER -> shopHandler.createOffer(packet.value());
            case ShopScreenHandlerAction.DELETE_OFFER -> shopHandler.deleteOffer();
            case ShopScreenHandlerAction.EXTRACT_CURRENCY -> shopHandler.extractCurrency();
            case ShopScreenHandlerAction.TOGGLE_TRANSFER -> shopHandler.toggleTransfer();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum ShopScreenHandlerAction {
        CREATE_OFFER, DELETE_OFFER, LOAD_OFFER, EXTRACT_CURRENCY, TOGGLE_TRANSFER
    }

}
